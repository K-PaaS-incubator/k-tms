/*
 *---------------------------------------------------------------------------
 *
 * SECUREWORKS V4.0
 * Copyright (c) 1997-2004 Oullim Information Technology Inc.
 * All rights reserved.
 *
 *---------------------------------------------------------------------------
 * $Id: mkhmac.c,v 1.1.2.1 2006/07/25 11:07:27 gonswing Exp $
 *---------------------------------------------------------------------------
 * $Log: mkhmac.c,v $
 * Revision 1.1.2.1  2006/07/25 11:07:27  gonswing
 * [T060700119] [4.0 R3 1��]�̹��� ���׷��̵�� �̹��� ���󿩺� Ȯ�� ��� �߰� ��û
 * �־��� �̹��� ���Ϸκ��� HMAC���� ���ؼ� ���Ϸ� ����� ���α׷��� �ۼ� �߰���
 *
 *
 *---------------------------------------------------------------------------
 */
   
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/shm.h>
#include <sys/vfs.h>
#include <sys/ioctl.h>
#include <sys/fcntl.h>
#include <netinet/in.h>
#include <unistd.h>
#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <ftw.h>
#include <fcntl.h>

#include "sha.h"
#include "sha_locl.h"


#define KHMACSTR	"KHMAC : "
#define RHMACSTR	"RHMAC : "
#define HMACSIZE	40
#define SHA_DIGEST_LENGTH		20
#define BUFSIZE			2048


char *fileintegrity(char *fname);
static char *pt(unsigned char *md);

int main(int argc, char *argv[])
{
	int					c;
	char				kernelimg_file[64];
	char				ramimg_file[64];
	char				hmacimg_file[64];
	FILE				*hmacfp;
	char				kernelhmac[64];
	char				ramhmac[64];

	if(argc < 3) {
		goto used;
	}
	
	while ((c = getopt(argc, argv, "k:r:o:h")) != EOF) {
		switch (c) {

        case 'k':
			strcpy(kernelimg_file, optarg);
            break;

        case 'r':
			strcpy(ramimg_file, optarg);
            break;

        case 'o':
			strcpy(hmacimg_file, optarg);
            break;

		case 'h':
		default:
used:			
			printf("NOTICE: MKHMAC - Make Image file HMAC\n");
				printf("EX] ./mkhmac -k [kernel image] -r [ramdrive image] -o [output hmac file]\n");
				printf("\t-k              Kernel Image file\n");
				printf("\t-r              Ramdrive Image file\n");
				printf("\t-o              Output HMAC file\n");
				printf("\t-h              Print help (this screen)\n");
				printf("\n");
			exit(0);
		}
	}


	memset(kernelhmac, '\0', sizeof(kernelhmac));
	memset(ramhmac, '\0', sizeof(ramhmac));
	strcpy(kernelhmac, (char *)fileintegrity(kernelimg_file));
	strcpy(ramhmac, (char *)fileintegrity(ramimg_file));

	printf(" >> Make Kernel image hmac.....   : [%s]\r\n", kernelhmac);
	printf(" >> Make Ramdrive image hmac..... : [%s]\r\n", ramhmac);

	if((hmacfp = fopen(hmacimg_file, "w+")) != NULL) {
		fprintf(hmacfp, "%s%s\n", KHMACSTR, kernelhmac);
		fprintf(hmacfp, "%s%s\n", RHMACSTR, ramhmac);
		fclose(hmacfp);
	}

	return 0;
}

static char *pt(unsigned char *md)
{
    int				i;
    static char		buf[SHA_DIGEST_LENGTH*2+1];

	/* SHA1 ������� HEX�� ��� */

    for (i = 0; i < SHA_DIGEST_LENGTH; i++) {
        sprintf(&(buf[i*2]), "%02x", md[i]);
    }

    return buf;
}

char *fileintegrity(char *fname)
{   
	SHA_CTX					c;
	unsigned char			sha1[SHA_DIGEST_LENGTH];
	char					*sha1_magic = "\xd3\x99\xf4\x12";
	struct stat				st;
    char					*bogusmd = "";

	/* ���Ἲ���� ��� ������Ʈ������ ���¸� ��´�. ���⿡ �����ϸ� bogus md��
       �����Ѵ� */

	if (lstat(fname, &st) == -1) return bogusmd;

	/// ���Ͽ����� �⺻�Ӽ����� ���Ἲ���ô���̵Ǵ� �׸���� �����Ѵ� 

	/// SHA1 �ʱ�ȭ 

	SHA1_Init(&c);

	/// ������Ʈ�� �����̸� ���ϳ����� �������� ���Ἲ���� �����Ѵ�.
	/// ������ �ƴѰ�� �Ӽ������� ������ HASH���� �����Ѵ�. 

	if (st.st_mode & S_IFREG) {
            int						fd; // LECAH (2019.06.24) : Fixing a vulnerability

		/* ����Ÿ���̽��� ȭ���� ����. �����׺��̽� ���⿡ �����ϸ� ���Ἲ���� ��Ȳ���� �����ϰ�
		   ���Ἲ���� �Ұ����� ������ �����Ͽ� �����߻��ϵ��� �Ѵ� */

		if ((fd = open(fname, O_RDONLY)) == -1) {

			// ������Ʈ�� �ʿ��� ���� �� link�� �ְ� ����� ���� ��쵵 �ִ�.
			if (st.st_mode & S_IFLNK) {
				return ("LINK_NO_TARGET");
			}

			return bogusmd;
		}

            ssize_t                 nread;
            unsigned char           buf[BUFSIZE];

		for (;;) {
			if ((nread = read(fd, buf, BUFSIZE)) < 0) {
				close(fd);

				// ���͸��� ���� ��ũ�� ���� �ִ�.
				if (st.st_mode & S_IFLNK) {
					return ("LINK_DIRECTORY");
				}

				return bogusmd;
			}
			if (nread == 0) break;
			SHA1_Update(&c, buf, (unsigned int)nread);
		}

		close(fd);
	}

	SHA1_Update(&c, (u_char *)sha1_magic, strlen(sha1_magic));

	/* SHA1 ��� ���� */

    SHA1_Final(&(sha1[0]), &c);

    return pt(sha1);
}
