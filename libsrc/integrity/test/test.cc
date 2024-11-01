#include <stdio.h>
#include <string.h>
#include <pthread.h>

#include "common/TSIntegrity.h"

int main(int argc, char *argv[])
{
	BYTE abHash[66]={0,};
	
	if(TSIntegrityGetHashFile("/home/sensor/monitor", abHash, 0)!=0)
		TsDebugString("failed to get hash value\n");
	
	printf("abhash:");
	for(int i=0;i<66;i++)
	{
		printf("%02x", abHash[i]);
	}
	printf("\n");
	
	int iError=TSIntegrityAddItem("/home/sensor/", "/home/sensor/monitor");
	if(iError!=0)
		TsDebugString("failed to add item:%d\n", iError);
	iError=TSIntegrityAddItem("/home/sensor/", "/home/sensor/sysmon");
	if(iError!=0)
		TsDebugString("failed to add item:%d\n", iError);
	return 0;
}
