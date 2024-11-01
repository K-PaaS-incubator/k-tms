#include <stdio.h>
#include <string.h>
#include <pthread.h>

#include "common/TSEnvman.h"

int main(int argc, char *argv[])
{
	STSEnvman Env;

	if (TSGetSensorEnv(&Env) != 0)
	{
		exit(0);
	}

	TsDebugString("sensor.driver	     : %d\n", Env.Sensor.Driver);
	TsDebugString("sensor.driver.dist    : %d\n", Env.Sensor.Dist);
	TsDebugString("sensor.pcap.file      : %s\n", Env.Sensor.Path);
	TsDebugString("sensor.transport.mode : %d\n", Env.Sensor.Ssl);
	TsDebugString("sensor.transport.unit : %d\n", Env.Sensor.Unit);
	TsDebugString("sensor.ipaddr         : %u\n", Env.Sensor.IP.s_addr);
	TsDebugString("sensor.event          : %d\n", Env.Sensor.Event);
	
	for (int i=0; i<ENV_MAX_BACKUP; i++)
	{
		TsDebugString("sensor.log : %d\n", Env.Sensor.Backup[i]);
	}

	for (int i=0; i<ENV_MAX_NETMON_CONTROL; i++)
	{
		TsDebugString("sensor.netmon : %d\n", Env.Sensor.Netmon[i]);
	}

	TsDebugString("sensor.driver.count   : %d\n", Env.Driver.Count);
	TsDebugString("sensor.driver.infosec.eth0 : %d\n", Env.Driver.Core[0]);
	TsDebugString("sensor.driver.infosec.eth1 : %d\n", Env.Driver.Core[1]);
	TsDebugString("sensor.driver.infosec.eth2 : %d\n", Env.Driver.Core[2]);
	TsDebugString("sensor.driver.infosec.eth3 : %d\n", Env.Driver.Core[3]);

	TsDebugString("manager ip  ; %u\n", Env.Manager.IP);

	TsDebugString("event       ; %d\n", Env.Hscan.Event);
	TsDebugString("capture.q   : %d\n", Env.Queue.Capture);
	TsDebugString("autit.q     : %d\n", Env.Queue.Auditlog);
	TsDebugString("brute.q     : %d\n", Env.Queue.Bruteforce);
	TsDebugString("traffic.q   : %d\n", Env.Queue.Trafficdump);
	TsDebugString("netmon.q    : %d\n", Env.Queue.Netmon);
	TsDebugString("session.q   : %d\n", Env.Queue.SessionMan);
	TsDebugString("session.q   : %d\n", Env.Queue.SessionDos);
	TsDebugString("statanal.q  : %d\n", Env.Queue.Statanalysis);
	
	TsDebugString("autilog.h  : %d/%d\n", Env.Hash.AuditThreshold.Table
			                            , Env.Hash.AuditThreshold.Element);

	TsDebugString("autilog.h  : %d/%d\n", Env.Hash.AuditDigest.Table
			                            , Env.Hash.AuditDigest.Element);

	TsDebugString("brute.h    : %d/%d\n", Env.Hash.Bruteforce.Table 
                                        , Env.Hash.Bruteforce.Element);

	TsDebugString("fragment.h : %d/%d\n", Env.Hash.Fragment.Table 
										, Env.Hash.Fragment.Element);

	TsDebugString("vnetwork.h : %d/%d\n", Env.Hash.Vnetwork.Table 
                                        , Env.Hash.Vnetwork.Element);

	TsDebugString("netmon.h   : %d/%d\n", Env.Hash.NetmonServer.Table 
                                        , Env.Hash.NetmonServer.Element);

	TsDebugString("netmon.h   : %d/%d\n", Env.Hash.NetmonSession.Table 
                                        , Env.Hash.NetmonSession.Element);

	TsDebugString("netmon.h   : %d/%d\n", Env.Hash.NetmonHost.Table 
                                        , Env.Hash.NetmonHost.Element);

	TsDebugString("netmon.h   : %d/%d\n", Env.Hash.NetmonService.Table 
                                        , Env.Hash.NetmonService.Element);

	TsDebugString("session.h  : %d/%d\n", Env.Hash.SessionManHalf.Table 
                                        , Env.Hash.SessionManHalf.Element);

	TsDebugString("session.h  : %d/%d\n", Env.Hash.SessionManFull.Table 
                                        , Env.Hash.SessionManFull.Element);
	
	TsDebugString("session.h  : %d/%d\n", Env.Hash.SessionDos.Table 
										, Env.Hash.SessionDos.Element);

	TsDebugString("statanal.h : %d/%d\n", Env.Hash.StatanalyTcp.Table 
                                        , Env.Hash.StatanalyTcp.Element);
	
	TsDebugString("statanal.h : %d/%d\n", Env.Hash.StatanalyUdp.Table 
										, Env.Hash.StatanalyUdp.Element);

	TsDebugString("statanal.h : %d/%d\n", Env.Hash.StatanalyIcmp.Table 
										, Env.Hash.StatanalyIcmp.Element);
	
	TsDebugString("hscan.h    : %d/%d\n", Env.Hash.Hscan.Table 
										, Env.Hash.Hscan.Element);

	TsDebugString("core.count : %d\n", Env.Process.Count);

	for (int i=0; i<ENV_MAX_PROCESS; i++) {
		TsDebugString("core.%d     : %d\n", i, Env.Process.Core[i]);
	}

	TsDebugString("netmon.count : %d\n", Env.Netmon.Count);

	for (int i=0; i<3; i++) {
		TsDebugString("netmon.%d     : %d\n", i, Env.Netmon.Core[i]);
	}
	
	TsDebugString("common.count : %d\n", Env.Common.Count);

	for (int i=0; i<10; i++) {
		TsDebugString("common .%d     : %d\n", i, Env.Common.Core[i]);
	}

	TsDebugString("Integrity.se : %d\n", Env.Integrity.Setup);
	TsDebugString("Integrity.au : %d\n", Env.Integrity.Automatic);
	TsDebugString("Integrity.in : %d\n", Env.Integrity.Interval);

	return 0;
}
