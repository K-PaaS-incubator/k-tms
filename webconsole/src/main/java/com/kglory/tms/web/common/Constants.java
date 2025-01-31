package com.kglory.tms.web.common;

import java.math.BigInteger;

/**
 * Constants
 */
public class Constants {

    public static final long AUTH_NONE = 0x00000000;	// 아무 권한 없음
    public static final long AUTH_CONTROLVIEWER = 0x00000001;	// Console의 View 제어 권한
    public static final long AUTH_CONTROLCONTROLLER = 0x00000002;	// Console의 컨트롤러 제어 권한
    public static final long AUTH_CONTROLREPORT = 0x00000004;	// Console의 리포터 제어 권한
    public static final long AUTH_VIEWAUDIT = 0x00000008;	// 감사로그를 조회 권한
    public static final long AUTH_MANAGEACCOUNT = 0x00000010;	// 계정관리 권한
    public static final long AUTH_MANAGESESSION = 0x00000020;	// Session decoding 권한(아직 정확한 의미 모르겠음)
    public static final long AUTH_SUPERADMIN = 0x10000000;	// Super Admin 권한(시스템에 유일)
    public static final long AUTH_LOGINTOCONSOLE = 0x01000000;	// Console에 로그인 권한
    public static final long AUTH_LOGINTOWEB = 0x02000000;	// Web에 로그인 권한

    public static final int SEVERITY_INFO = 0;			// 위험도 Info
    public static final int SEVERITY_LOW = 1;			// 위험도 Low
    public static final int SEVERITY_MEDIUM = 3;			// 위험도 Medium
    public static final int SEVERITY_HIGH = 5;			// 위험도 Hign

    public static final String AUDITLOG_SPLITTER = "%\\d";		// 감사로그 스트링 분할
    // 정규표현식
    public static final String WEBCONSOLE = "웹콘솔";

    public static final BigInteger LOGIN_SUCCESS = BigInteger.valueOf(20001);
    public static final BigInteger LOGOUT_SUCCESS = BigInteger.valueOf(20002);
    public static final BigInteger LOGIN_SESSION_OUT = BigInteger.valueOf(20003);
    public static final BigInteger ACCOUNT_ADD_SUCCESS = BigInteger.valueOf(20004);
    public static final BigInteger ACCOUNT_MOD_SUCCESS = BigInteger.valueOf(20005);
    public static final BigInteger ACCOUNT_DEL_SUCCESS = BigInteger.valueOf(20006);
    public static final BigInteger ACCOUNT_GROUP_ADD_SUCCESS = BigInteger.valueOf(20007);
    public static final BigInteger ACCOUNT_GROUP_MOD_SUCCESS = BigInteger.valueOf(20008);
    public static final BigInteger ACCOUNT_GROUP_DEL_SUCCESS = BigInteger.valueOf(20009);
    public static final BigInteger SYSTEM_LOGIN_MOD_SUCCESS = BigInteger.valueOf(20010);
    public static final BigInteger SYSTEM_IPMONITOR_MOD_SUCCESS = BigInteger.valueOf(20011);
    public static final BigInteger SYSTEM_EMAIL_MOD_SUCCESS = BigInteger.valueOf(20012);
    public static final BigInteger SYSTEM_HITCOUNT_MOD_SUCCESS = BigInteger.valueOf(20013);
    public static final BigInteger SYSTEM_DB_MANAGE_MOD_SUCCESS = BigInteger.valueOf(20014);
    public static final BigInteger SYSTEM_TIMESYC_MOD_SUCCESS = BigInteger.valueOf(20015);
    public static final BigInteger SYSTEM_DB_BACKUP_MOD_SUCCESS = BigInteger.valueOf(20016);
    public static final BigInteger SYSTEM_DB_BACKUP_EXEC_MOD_SUCCESS = BigInteger.valueOf(20017);
    public static final BigInteger SYSTEM_INTEGRITY_MOD_SUCCESS = BigInteger.valueOf(20018);
    public static final BigInteger SENSOR_INBOUND_MOD_SUCCESS = BigInteger.valueOf(20019);
    public static final BigInteger NETWORK_ADD_SUCCESS = BigInteger.valueOf(20020);
    public static final BigInteger NETWORK_MOD_SUCCESS = BigInteger.valueOf(20021);
    public static final BigInteger NETWORK_DEL_SUCCESS = BigInteger.valueOf(20022);
    public static final BigInteger FILTERVIEW_ADD_SUCCESS = BigInteger.valueOf(20023);
    public static final BigInteger FILTERVIEW_MOD_SUCCESS = BigInteger.valueOf(20024);
    public static final BigInteger FILTERVIEW_DEL_SUCCESS = BigInteger.valueOf(20025);
    public static final BigInteger POLICY_DETECTION_MOD_SUCCESS = BigInteger.valueOf(20026);
    public static final BigInteger POLICY_DETECTION_USER_ADD_SUCCESS = BigInteger.valueOf(20027);
    public static final BigInteger POLICY_DETECTION_USER_HELP_ADD_SUCCESS = BigInteger.valueOf(20028);
    public static final BigInteger POLICY_DETECTION_USER_MOD_SUCCESS = BigInteger.valueOf(20029);
    public static final BigInteger POLICY_DETECTION_USER_HELP_MOD_SUCCESS = BigInteger.valueOf(20030);
    public static final BigInteger POLICY_DETECTION_USER_DEL_SUCCESS = BigInteger.valueOf(20031);
    public static final BigInteger POLICY_DETECTION_GROUP_ADD_SUCCESS = BigInteger.valueOf(20032);
    public static final BigInteger POLICY_DETECTION_GROUP_DEL_SUCCESS = BigInteger.valueOf(20033);
    public static final BigInteger POLICY_DETECTION_IMPORT_SUCCESS = BigInteger.valueOf(20034);
    public static final BigInteger POLICY_DETECTION_EXPORT_SUCCESS = BigInteger.valueOf(20035);
    public static final BigInteger POLICY_YARA_MOD_SUCCESS = BigInteger.valueOf(20036);
    public static final BigInteger POLICY_YARA_USER_ADD_SUCCESS = BigInteger.valueOf(20037);
    public static final BigInteger POLICY_YARA_USER_MOD_SUCCESS = BigInteger.valueOf(20038);
    public static final BigInteger POLICY_YARA_USER_DEL_SUCCESS = BigInteger.valueOf(20039);
    public static final BigInteger POLICY_YARA_GROUP_ADD_SUCCESS = BigInteger.valueOf(20040);
    public static final BigInteger POLICY_YARA_GROUP_DEL_SUCCESS = BigInteger.valueOf(20041);
    public static final BigInteger PROTOCOL_ADD_SUCCESS = BigInteger.valueOf(20042);
    public static final BigInteger PROTOCOL_MOD_SUCCESS = BigInteger.valueOf(20043);
    public static final BigInteger PROTOCOL_DEL_SUCCESS = BigInteger.valueOf(20044);
    public static final BigInteger AUDITLOG_MOD_SUCCESS = BigInteger.valueOf(20045);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_SUCCESS = BigInteger.valueOf(20046);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_MOD_SUCCESS = BigInteger.valueOf(20047);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_DEL_SUCCESS = BigInteger.valueOf(20048);
    public static final BigInteger INTEGRITY_SUCCESS = BigInteger.valueOf(20049);
    public static final BigInteger DUPLE_LOGIN = BigInteger.valueOf(20050);
    public static final BigInteger TIME_SYNC_SUCCESS = BigInteger.valueOf(20051);
    public static final BigInteger DB_BACKUP_SUCCESS = BigInteger.valueOf(20052);
    public static final BigInteger TABLE_DEL_SUCCESS = BigInteger.valueOf(20053);
    public static final BigInteger SYSTEM_SFTP_ADD_SUCCESS = BigInteger.valueOf(20054);
    public static final BigInteger SYSTEM_SFTP_MOD_SUCCESS = BigInteger.valueOf(20055);

    public static final BigInteger LOGIN_FAIL = BigInteger.valueOf(20001);
    public static final BigInteger ACCOUNT_ADD_FAIL = BigInteger.valueOf(20002);
    public static final BigInteger ACCOUNT_MOD_FAIL = BigInteger.valueOf(20003);
    public static final BigInteger ACCOUNT_DEL_FAIL = BigInteger.valueOf(20004);
    public static final BigInteger ACCOUNT_GROUP_ADD_FAIL = BigInteger.valueOf(20005);
    public static final BigInteger ACCOUNT_GROUP_MOD_FAIL = BigInteger.valueOf(20006);
    public static final BigInteger ACCOUNT_GROUP_DEL_FAIL = BigInteger.valueOf(20007);
    public static final BigInteger SYSTEM_LOGIN_MOD_FAIL = BigInteger.valueOf(20008);
    public static final BigInteger SYSTEM_IPMONITOR_MOD_FAIL = BigInteger.valueOf(20009);
    public static final BigInteger SYSTEM_EMAIL_MOD_FAIL = BigInteger.valueOf(20010);
    public static final BigInteger SYSTEM_HITCOUNT_MOD_FAIL = BigInteger.valueOf(20011);
    public static final BigInteger SYSTEM_DB_MANAGE_MOD_FAIL = BigInteger.valueOf(20012);
    public static final BigInteger SYSTEM_TIMESYC_MOD_FAIL = BigInteger.valueOf(20013);
    public static final BigInteger SYSTEM_DB_BACKUP_MOD_FAIL = BigInteger.valueOf(20014);
    public static final BigInteger SYSTEM_DB_BACKUP_EXEC_MOD_FAIL = BigInteger.valueOf(20015);
    public static final BigInteger SYSTEM_INTEGRITY_MOD_FAIL = BigInteger.valueOf(20016);
    public static final BigInteger SENSOR_INBOUND_MOD_FAIL = BigInteger.valueOf(20017);
    public static final BigInteger NETWORK_ADD_FAIL = BigInteger.valueOf(20018);
    public static final BigInteger NETWORK_MOD_FAIL = BigInteger.valueOf(20019);
    public static final BigInteger NETWORK_DEL_FAIL = BigInteger.valueOf(20020);
    public static final BigInteger FILTERVIEW_ADD_FAIL = BigInteger.valueOf(20021);
    public static final BigInteger FILTERVIEW_MOD_FAIL = BigInteger.valueOf(20022);
    public static final BigInteger FILTERVIEW_DEL_FAIL = BigInteger.valueOf(20023);
    public static final BigInteger POLICY_DETECTION_MOD_FAIL = BigInteger.valueOf(20024);
    public static final BigInteger POLICY_DETECTION_USER_ADD_FAIL = BigInteger.valueOf(20025);
    public static final BigInteger POLICY_DETECTION_USER_MOD_FAIL = BigInteger.valueOf(20026);
    public static final BigInteger POLICY_DETECTION_USER_DEL_FAIL = BigInteger.valueOf(20027);
    public static final BigInteger POLICY_DETECTION_GROUP_ADD_FAIL = BigInteger.valueOf(20028);
    public static final BigInteger POLICY_DETECTION_GROUP_DEL_FAIL = BigInteger.valueOf(20029);
    public static final BigInteger POLICY_DETECTION_IMPORT_FAIL = BigInteger.valueOf(20030);
    public static final BigInteger POLICY_DETECTION_EXPORT_FAIL = BigInteger.valueOf(20031);
    public static final BigInteger POLICY_YARA_MOD_FAIL = BigInteger.valueOf(20032);
    public static final BigInteger POLICY_YARA_USER_ADD_FAIL = BigInteger.valueOf(20033);
    public static final BigInteger POLICY_YARA_USER_MOD_FAIL = BigInteger.valueOf(20034);
    public static final BigInteger POLICY_YARA_USER_DEL_FAIL = BigInteger.valueOf(20035);
    public static final BigInteger POLICY_YARA_GROUP_ADD_FAIL = BigInteger.valueOf(20036);
    public static final BigInteger POLICY_YARA_GROUP_DEL_FAIL = BigInteger.valueOf(20037);
    public static final BigInteger PROTOCOL_ADD_FAIL = BigInteger.valueOf(20038);
    public static final BigInteger PROTOCOL_MOD_FAIL = BigInteger.valueOf(20039);
    public static final BigInteger PROTOCOL_DEL_FAIL = BigInteger.valueOf(20040);
    public static final BigInteger AUDITLOG_MOD_FAIL = BigInteger.valueOf(20041);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_FAIL = BigInteger.valueOf(20042);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_MOD_FAIL = BigInteger.valueOf(20043);
    public static final BigInteger POLICY_DETECTION_EXCEPTION_DEL_FAIL = BigInteger.valueOf(20044);
    public static final BigInteger TIME_SYNC_FAIL = BigInteger.valueOf(20045);
    public static final BigInteger DB_BACKUP_FAIL = BigInteger.valueOf(20046);
    public static final BigInteger TABLE_DEL_FAIL = BigInteger.valueOf(20047);
    public static final BigInteger SYSTEM_SFTP_ADD_FAIL = BigInteger.valueOf(20048);
    public static final BigInteger SYSTEM_SFTP_MOD_FAIL = BigInteger.valueOf(20049);
    public static final BigInteger UNAUTHORIZED_IP = BigInteger.valueOf(20050);

    public static final BigInteger INTEGRITY_FAIL = BigInteger.valueOf(20001);

    
    private static Integer systemMode = 1;  // 1 : 일반, 2 : CC, 3 : KERIS 
    private static Integer loginMode = 0;  // 0 : 일반, 1 : EPKI

    private static Integer loginIpCount = 3;  //사용자 별 로그인 허용 IP 수

    private static Integer writeMode = 1;  //1 : write on, 0 : write off
    private static Integer dualSystem = 0;  //0 : normal, 1 : dual system
    private static Integer currentMode = 1;  //1 : master, 0 : slave
    private static Integer masterStat = 1;  //0 : off, 1 : on
    private static Integer slaveStat = 1;  //0 : off, 1 : on
    private static long aliveStartTime = 0;
    
	public static enum SYSTEM {

        DEFAULT(1), SECURITY(2), KERIS(3);

        private int value;

        private SYSTEM(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum LOGIN {

        DEFAULT(0), EPKI(1);

        private int value;

        private LOGIN(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum AUDIT_LTYPE1 {

        ACTION(BigInteger.valueOf(1)), ERROR(BigInteger.valueOf(2)), WARNNING(BigInteger.valueOf(3));

        private BigInteger value;

        private AUDIT_LTYPE1(BigInteger value) {
            this.value = value;
        }

        public BigInteger getValue() {
            return this.value;
        }
    }
    
    public static Integer getSystemMode() {
		return systemMode;
	}

	public static void setSystemMode(Integer systemMode) {
		Constants.systemMode = systemMode;
	}

	public static Integer getLoginMode() {
		return loginMode;
	}

	public static void setLoginMode(Integer loginMode) {
		Constants.loginMode = loginMode;
	}

	public static Integer getLoginIpCount() {
		return loginIpCount;
	}

	public static void setLoginIpCount(Integer loginIpCount) {
		Constants.loginIpCount = loginIpCount;
	}

	public static Integer getWriteMode() {
		return writeMode;
	}

	public static void setWriteMode(Integer writeMode) {
		Constants.writeMode = writeMode;
	}

	public static Integer getDualSystem() {
		return dualSystem;
	}

	public static void setDualSystem(Integer dualSystem) {
		Constants.dualSystem = dualSystem;
	}

	public static Integer getCurrentMode() {
		return currentMode;
	}

	public static void setCurrentMode(Integer currentMode) {
		Constants.currentMode = currentMode;
	}

	public static Integer getSlaveStat() {
		return slaveStat;
	}

	public static void setSlaveStat(Integer slaveStat) {
		Constants.slaveStat = slaveStat;
	}

	public static long getAliveStartTime() {
		return aliveStartTime;
	}

	public static void setAliveStartTime(long aliveStartTime) {
		Constants.aliveStartTime = aliveStartTime;
	}

	public static Integer getMasterStat() {
		return masterStat;
	}

	public static void setMasterStat(Integer masterStat) {
		Constants.masterStat = masterStat;
	}
	
}
