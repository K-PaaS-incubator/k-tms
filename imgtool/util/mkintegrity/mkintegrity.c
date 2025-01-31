#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#include "sha2.h"

#define FILENAME_INTEGRRITY "integrity.cfg"
#define SHA256_HASHSIZE 64
#define SHA256_HASHSTRSIZE  SHA256_HASHSIZE*2+1
#define DEFAULT_DBHASH "d65237000cbd22927c5f4c11af1f91db1f5c85a5e3ca6b4508229a393b498e54677ffb943c8444b82e5813c5e1d70c61086a65c6b80b2892d8f4ba7987b2761d"

static int HexToStr(uint8_t *pbSrc, int iLen, char *pszDst)
{
    int i = 0;
    char *pszTarget=pszDst;
    for(i=0;i<iLen;i++)
    {
        sprintf(pszTarget, "%02x", pbSrc[i]);
        pszTarget+=2;
    }
    return 0;
}

int get_filehash(char * file, uint8_t *out_value)
{
    FILE *fp = NULL;

    unsigned char buf[16384];
    unsigned long read = 0;

    memset(out_value, 0 , SHA256_HASHSIZE);
    sha512_ctx sha512;

    fp = fopen(file, "rb");
    if(fp == NULL) return -1;


    sha512_begin(&sha512);

    while(1)
    {
        read = fread(buf, 1, 16384, fp);

        if(read != 0)
        {
            sha512_hash(buf, read, &sha512);
        }

        if(read != 16384) break;
    }

    sha512_end(out_value, &sha512);

    fclose(fp); fp = NULL;

    return 0;
}


/* XXX dbfile hash는 기본 해시가 디폴트로 해야될 것으로 보임 TODO */ 
static int update_DBfile(char *file_name)
{
    char cmd[256];
    char dst[256];

    FILE  *fpsrc = NULL;
    FILE  *fpdst = NULL;

    uint8_t hash[SHA256_HASHSIZE + 1 ];
    char    hash_str[SHA256_HASHSTRSIZE];

    char one_line[4096];

    if (get_filehash(file_name, hash)  != 0) return -1; 
    HexToStr(hash, SHA256_HASHSIZE, hash_str);

    memset(dst, 0, sizeof(dst));
    snprintf(dst, sizeof(dst), "%s.bak",file_name);

    fpsrc = fopen(file_name, "rt");
    if (fpsrc == NULL) return -2;

    fpdst = fopen(dst, "wt");
    if (fpdst == NULL)
    {
        fclose(fpsrc);
        return -2;
    }

    fgets(one_line, 4095, fpsrc);
    fprintf(fpdst, "%s\n", hash_str);

    while( fgets(one_line, sizeof(one_line), fpsrc) )
    {
        fprintf(fpdst, "%s", one_line);
    }

    fclose(fpsrc);
    fclose(fpdst);

    snprintf(cmd, sizeof(cmd), "mv %s %s",dst,file_name);
    system(cmd);
    return 0;   

}

#define BINARY_ITEM 1
#define CFG_ITEM 2
#define RULE_ITEM 3
#define CERTKEY_ITEM 4
static int add_item(char *item_path, char *item, char *integrity_file, int flag)
{
    FILE *fp = NULL;
    uint8_t hash[SHA256_HASHSIZE + 1 ];
    char    hash_str[SHA256_HASHSTRSIZE];

    time_t time_now;

	char file_name[256];

    memset(file_name, 0, sizeof(file_name));
    snprintf(file_name, sizeof(file_name),"%s/%s",item_path, item);

    fp = fopen(integrity_file , "at");
    if (fp == NULL) return -2;
    fseek(fp, 0L, SEEK_END);

    if (get_filehash(file_name, hash)  != 0) {
		fclose(fp);
		return -1;
	}

    HexToStr(hash, SHA256_HASHSIZE, hash_str);

    time(&time_now);

    if (flag == BINARY_ITEM) {
        fprintf(fp, "/home/sensor/%s:%u:%u:%d:%s\n", item, (uint32_t)time_now, (uint32_t)time_now, 1, hash_str);
    }
    else if (flag == CFG_ITEM) {
        fprintf(fp, "/home/sensor/cfg/%s:%u:%u:%d:%s\n", item, (uint32_t)time_now, (uint32_t)time_now, 1, hash_str);
    }
    else if (flag == RULE_ITEM) {
        fprintf(fp, "/home/sensor/rule/%s:%u:%u:%d:%s\n", item, (uint32_t)time_now, (uint32_t)time_now, 1, hash_str);
    }
    else if (flag == CERTKEY_ITEM) {
        fprintf(fp, "/home/sensor/CertKeys/%s:%u:%u:%d:%s\n", item, (uint32_t)time_now, (uint32_t)time_now, 1, hash_str);
    }
    else {
        fprintf(fp, "%s:%u:%u:%d:%s\n", item, (uint32_t)time_now, (uint32_t)time_now, 1, hash_str);
    }

    fclose(fp);
    return 0;
}

#define INTEGRITY1_FILENAME "integrity1.cfg"
#define INTEGRITY2_FILENAME "integrity2.cfg"
int main(int argc, char **argv)
{
    int c;
	char bin_path[128];
	char cfg_path[128];
	char certkey_path[128];
	char rule_path[128];
	char integrity_filename[256];

    FILE *fp = NULL;

	if(argc < 2) {
        goto used;
    }

	memset(bin_path, '\0', sizeof(bin_path));
	memset(cfg_path, '\0', sizeof(cfg_path));
	memset(certkey_path, '\0', sizeof(certkey_path));
	memset(rule_path, '\0', sizeof(rule_path));
	memset(integrity_filename, '\0', sizeof(integrity_filename));

	while ((c = getopt(argc, argv, "c:p:a:u:k:s:t:h")) != EOF) {
		switch (c) {
        case 'c':
			strcpy(integrity_filename, optarg);
			fp = fopen(integrity_filename, "wt");
			fprintf(fp, "%s\n",DEFAULT_DBHASH);
			fclose(fp);
			break;
        case 'p':
			strcpy(bin_path, optarg);
			add_item(bin_path, "sensor", INTEGRITY1_FILENAME, BINARY_ITEM);
			add_item(bin_path, "monitor", INTEGRITY1_FILENAME, BINARY_ITEM);
			add_item(bin_path, "sysmon", INTEGRITY1_FILENAME, BINARY_ITEM);
			add_item(bin_path, "config", INTEGRITY1_FILENAME, BINARY_ITEM);
			update_DBfile(INTEGRITY1_FILENAME);
            break;
        case 'a':
			strcpy(cfg_path, optarg);
			add_item(cfg_path, "sensor.env", INTEGRITY2_FILENAME, CFG_ITEM);
			add_item(cfg_path, "sensor.yaml", INTEGRITY2_FILENAME, CFG_ITEM);
			add_item(cfg_path, "classification.config", INTEGRITY2_FILENAME, CFG_ITEM);
			add_item(cfg_path, "hardcode_mode.cfg", INTEGRITY2_FILENAME, CFG_ITEM);
			add_item(cfg_path, "reference.config", INTEGRITY2_FILENAME, CFG_ITEM);
			add_item(cfg_path, "threshold.config", INTEGRITY2_FILENAME, CFG_ITEM);
    		update_DBfile(INTEGRITY2_FILENAME);

            break;
        case 'u':
			strcpy(integrity_filename, optarg);
            break;
        case 'k':
			if (strlen(integrity_filename) < 1) {
				snprintf(integrity_filename, sizeof(integrity_filename),
						"/etc/install/integrity.cfg");
			}
			strcpy(certkey_path, optarg);
			add_item(certkey_path, "infosec.crt", integrity_filename, CERTKEY_ITEM);
			add_item(certkey_path, "infosec.pri", integrity_filename, CERTKEY_ITEM);
			add_item(certkey_path, "rootcert.crt", integrity_filename, CERTKEY_ITEM);
			add_item(certkey_path, "sensor.crt", integrity_filename, CERTKEY_ITEM);
			add_item(certkey_path, "sensor.pri", integrity_filename, CERTKEY_ITEM);
    		update_DBfile(integrity_filename);
            break;
        case 's':
			if (strlen(integrity_filename) < 1) {
				snprintf(integrity_filename, sizeof(integrity_filename),
						"/etc/install/integrity.cfg");
			}
			strcpy(cfg_path, optarg);
			add_item(cfg_path, "sensor.env", integrity_filename, CFG_ITEM);
			add_item(cfg_path, "sensor.yaml", integrity_filename, CFG_ITEM);
            break;
        case 't':
			strcpy(rule_path, optarg);
			add_item(rule_path, "Exception.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Inbound.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "NetworkEx.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Sensor.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "SessionMonitor.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "SigResponse.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Signature.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserRule.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserRuleResp.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "YaraRule.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserYaraRule.pol", INTEGRITY2_FILENAME, RULE_ITEM);

			add_item(rule_path, "Exception_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Inbound_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Sftp_web.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "NetworkEx_web.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Sensor_web.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "SessionMonitor_web.cfg", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "SigResponse_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "Signature_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserRule_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserRuleResp_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "YaraRule_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
			add_item(rule_path, "UserYaraRule_web.pol", INTEGRITY2_FILENAME, RULE_ITEM);
    		update_DBfile(INTEGRITY2_FILENAME);
			break;
		case 'h':
		default:
used:			
			printf("NOTICE: say HO!! - Make Integrity.cfg file \n");
				printf("EX] ./mkintegrity -n -p [binary path] -t [cfg path]\n");
				printf("\t-c              create file (ex: integrity.cfg)\n");
				printf("\t-u              update file (ex: /etc/install/integrity.cfg)\n");
				printf("\t-p              file path (ex: /home/sensor/)\n");
				printf("\t-a              file path (ex: /home/sensor/cfg)\n");
				printf("\t-k              file path (ex: /home/sensor/CertKeys)\n");
				printf("\t-s              file path (ex: /home/sensor/cfg for TASUnitedSC)\n");
				printf("\t-t              file path (ex: /home/sensor/rule)\n");
				printf("\t-h              Print help (this screen)\n");
				printf("\n");
			exit(0);
		}
	}

    return 0;
}

