/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.services.common;

import com.ibatis.common.resources.Resources;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.common.CreateTableMapper;
import com.kglory.tms.web.services.OracleService;
import com.kglory.tms.web.util.DateTimeUtil;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author leecjong
 */
@Service("createTableSvc")
public class CreateTableService {

    private static Logger logger = LoggerFactory.getLogger(CreateTableService.class);

    @Autowired
    OracleService oracleSvc;
    @Autowired
    CreateTableMapper createMapper;
    @Autowired
    SqlSessionTemplate sqlSession;
    /**
     * 시스템 시작시 로그 테이블 확인 생성
     */
    public void initCreateSystemTable() throws BaseException{
        String resource = "sql/initCreateTable.sql";
        execSqlFile(resource);
    }

    public void execSqlFile(String resource) throws BaseException{
        try {
            SqlSession session = sqlSession.getSqlSessionFactory().openSession();
            ScriptRunner runner = new ScriptRunner(session.getConnection());
            Reader reader = Resources.getResourceAsReader(resource);

            runner.setLogWriter(null);
            runner.runScript(reader);
            session.commit();
            reader.close();
            session.close();
        } catch(IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public void initCreateLogTable() {
        try {
            String nowTableDate = DateTimeUtil.getNowTableDate();
            String nextDayTableDate = DateTimeUtil.getNextDayTableDate();
            // TEMP 시간 테이블
            //            createTempDateTimeTable(nowTableDate);
            //            createTempDateTimeTable(nextDayTableDate);
            //침입탐지로그
            createLogTable(nowTableDate);
            createLogTable(nextDayTableDate);
            createRawdataTable(nowTableDate);
            createRawdataTable(nextDayTableDate);
            //어플리케이션로그
            createApplayLogTable(nowTableDate);
            createApplayLogTable(nextDayTableDate);
            //감사로그
            createAuditLogTable(nowTableDate);
            createAuditLogTable(nextDayTableDate);
            createAuditResultLogTable(nowTableDate);
            createAuditResultLogTable(nextDayTableDate);
            //파일메타로그
            createFilemetaTable(nowTableDate);
            createFilemetaTable(nextDayTableDate);
            //센서 현황
            createSensorAliveTable(nowTableDate);
            createSensorAliveTable(nextDayTableDate);

            createSensorSessionTable(nowTableDate);
            createSensorSessionTable(nextDayTableDate);
            createSystemLogSensorTable(nowTableDate);
            createSystemLogSensorTable(nextDayTableDate);
            //트래픽 로그
            createIpTrafficTable(nowTableDate);
            createIpTrafficTable(nextDayTableDate);
            createTrafficIpTable(nowTableDate);
            createTrafficIpTable(nextDayTableDate);
            //프로토콜 로그
            createProtocolTable(nowTableDate);
            createProtocolTable(nextDayTableDate);
            //서비스 로그
            createServiceTable(nowTableDate);
            createServiceTable(nextDayTableDate);
            //세션 로그
            createSessionTable(nowTableDate);
            createSessionTable(nextDayTableDate);
            //트랙픽 탐지로그
            createTrafficLogTable(nowTableDate);
            createTrafficLogTable(nextDayTableDate);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 명일 날짜 로그 테이블 생성(스케쥴)
     */
    public void nextDayCreateLogTable() {
        try {
            String nextDayTableDate = DateTimeUtil.getNextDayTableDate();
            // TEMP 시간 테이블
            //            createTempDateTimeTable(nextDayTableDate);
            //침입탐지로그
            createLogTable(nextDayTableDate);
            createRawdataTable(nextDayTableDate);
            //어플리케이션로그
            createApplayLogTable(nextDayTableDate);
            //감사로그
            createAuditLogTable(nextDayTableDate);
            createAuditResultLogTable(nextDayTableDate);
            //파일메타로그
            createFilemetaTable(nextDayTableDate);
            //센서 현황
            createSensorAliveTable(nextDayTableDate);

            createSensorSessionTable(nextDayTableDate);
            createSystemLogSensorTable(nextDayTableDate);
            //트래픽 로그
            createIpTrafficTable(nextDayTableDate);
            createTrafficIpTable(nextDayTableDate);
            //프로토콜 로그
            createProtocolTable(nextDayTableDate);
            //서비스 로그
            createServiceTable(nextDayTableDate);
            //세션 로그
            createSessionTable(nextDayTableDate);
            //트랙픽 탐지로그
            createTrafficLogTable(nextDayTableDate);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public void createTempDateTimeTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "TEMP_DATETIME_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getDateTempTable(tableName + tableDate));
            createMapper.createTable(map);
            insertTempDateTime(tableName + tableDate);
        }
    }

    public void createApplayLogTable(String tableDate) {
        String applayLog = "APPLAYER_";
        boolean chk = isTables(applayLog + tableDate);
        HashMap map = new HashMap();
        if (!chk) {
            map.put("sql", getApplayTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createAuditLogTable(String tableDate) {
        String tableName = "AUDIT_";
        boolean chk = isTables(tableName + tableDate);
        HashMap map = new HashMap();
        if (!chk) {
            map.put("sql", getAuditLogTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createAuditResultLogTable(String tableDate) {
        String tableName = "AUDIT_RESULT_";
        boolean chk = isTables(tableName + tableDate);
        HashMap map = new HashMap();
        if (!chk) {
            map.put("sql", getAuditResultTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createFilemetaTable(String tableDate) {
        String tableName = "FILEMETA_";
        boolean chk = isTables(tableName + tableDate);
        HashMap map = new HashMap();
        if (!chk) {
            map.put("sql", getFilemetaTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createLogTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "LOG_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getLogTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getLogTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createRawdataTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "RAWDATA_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getRawdataTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getRawdataTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createSensorAliveTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "SENSOR_ALIVE_";
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getSensorAliveTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createSensorSessionTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "SENSOR_SESSION_";
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getSensorSessionTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createSystemLogSensorTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "SYSTEMLOG_SENSOR_";
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getSystemLogSensorTable(tableDate));
            createMapper.createTable(map);
        }
    }

    public void createSessionTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "SESSION_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getSessionTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getSessionTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createIpTrafficTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "IP_TRAFFIC_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getIpTrafficTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getIpTrafficTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createProtocolTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "PROTOCOL_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getProtocolTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getProtocolTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createServiceTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "SERVICE_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getServiceTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getServiceTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createTrafficIpTable(String tableDate) {
        HashMap map = new HashMap();
        String tableName = "TRAFFIC_IP_";
        String v6 = "V6_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getTrafficIpTable(tableDate, 4));
            createMapper.createTable(map);
        }

        //V6 table
        chk = isTables(tableName + v6 + tableDate);
        if (!chk) {
            map.put("sql", getTrafficIpTable(tableDate, 6));
            createMapper.createTable(map);
        }
    }

    public void createTrafficLogTable(String tableDate) throws BaseException{
        HashMap map = new HashMap();
        String tableName = "TRAFFIC_DETECTION_";
        // V4 table
        boolean chk = isTables(tableName + tableDate);
        if (!chk) {
            map.put("sql", getTrafficLogTable(tableName, tableDate));
            createMapper.createTable(map);
        }
    }

    public String getApplayTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("APPLAYER_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("TMLOGTIME	DATETIME,");
        sb.append("NPROTOCOL	INT,");
        sb.append("DWSOURCEIP	INT UNSIGNED,");
        sb.append("STRSOURCEIP	VARCHAR(40),");
        sb.append("NSOURCEPORT	INT,");
        sb.append("DWDESTINATIONIP	INT UNSIGNED,");
        sb.append("STRDESTINATIONIP	VARCHAR(40),");
        sb.append("NDESTINATIONPORT	INT,");
        sb.append("BTYPE	INT,");
        sb.append("BIPTYPE	INT,");
        sb.append("WDATASIZE	INT,");
        sb.append("LSRCNETINDEX	INT,");
        sb.append("LDSTNETINDEX	INT,");
        sb.append("STRSESSIONINDEX	VARCHAR(50),");
        sb.append("TMSESSION	VARCHAR(50),");
        sb.append("SDATA	TEXT,");
        sb.append("TMDBTIME	DATETIME,");
        sb.append("CONSTRAINT APPLAYER_" + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX APPLAYER_" + tableDate + "_IDX (TMLOGTIME, TMDBTIME),");
        sb.append("INDEX APPLAYER_SESSION_" + tableDate + "_IDX (STRSESSIONINDEX, TMSESSION)");
        sb.append(")");
        return sb.toString();
    }

    public String getAuditLogTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("AUDIT_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LAUDITLOGINDEX	INT UNSIGNED AUTO_INCREMENT,");
        sb.append("TMOCCUR	DATETIME,");
        sb.append("STRCONTENT	TEXT,");
        sb.append("STROPERATOR	VARCHAR(512),");
        sb.append("LAUDITSETINDEX	INT,");
        sb.append("LTYPE1	INT,");
        sb.append("LTYPE2	INT,");
        sb.append("STRCOMMENT	TEXT,");
        sb.append("LOGTARGET	INT,");
        sb.append("CONSTRAINT AUDIT_" + tableDate + "_PK PRIMARY KEY (LAUDITLOGINDEX),");
        sb.append("INDEX AUDIT_" + tableDate + "_IDX (LTYPE1, LTYPE2, TMOCCUR)");
        sb.append(")");
        return sb.toString();
    }

    public String getAuditResultTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("AUDIT_RESULT_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LOGINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("LAUDITLOGINDEX	INT,");
        sb.append("STRMESSAGE	VARCHAR(512),");
        sb.append("USERID	VARCHAR(20),");
        sb.append("SENDTYPE	INT,");
        sb.append("RESULT	INT,");
        sb.append("REGDATE	DATETIME,");
        sb.append("CONSTRAINT AUDIT_RESULT_" + tableDate + "_PK PRIMARY KEY (LOGINDEX),");
        sb.append("INDEX AUDIT_RESULT_" + tableDate + "_IDX (REGDATE)");
        sb.append(")");
        return sb.toString();
    }

    public String getFilemetaTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("FILEMETA_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("TMLOGTIME	DATETIME,");
        sb.append("NPROTOCOL	INT,");
        sb.append("DWSOURCEIP	INT UNSIGNED,");
        sb.append("STRSOURCEIP	VARCHAR(40),");
        sb.append("NSOURCEPORT	INT,");
        sb.append("DWDESTINATIONIP	INT UNSIGNED,");
        sb.append("STRDESTINATIONIP	VARCHAR(40),");
        sb.append("NDESTINATIONPORT	INT,");
        sb.append("BIPTYPE	INT,");
        sb.append("LSRCNETINDEX	INT,");
        sb.append("LDSTNETINDEX	INT,");
        sb.append("STRURI	VARCHAR(256),");
        sb.append("STRREFERER	VARCHAR(256),");
        sb.append("STRHOST	VARCHAR(256),");
        sb.append("STRUSERAGENT	VARCHAR(256),");
        sb.append("STRMAGIC	VARCHAR(50),");
        sb.append("NSTATE	INT,");
        sb.append("NPKTNUM	INT,");
        sb.append("DWFILESIZE	INT,");
        sb.append("STRFILENAME	VARCHAR(256),");
        sb.append("STRFILEHASH	VARCHAR(256),");
        sb.append("STRSTOREFILENAME	VARCHAR(256),");
        sb.append("STRSESSIONINDEX	VARCHAR(50),");
        sb.append("TMSESSION	VARCHAR(50),");
        sb.append("LCODE	INT UNSIGNED,");
        sb.append("NGRPINDEX	INT,");
        sb.append("BSEVERITY	INT,");
        sb.append("STRRULENAME	VARCHAR(256),");
        sb.append("STRGRPNAME	VARCHAR(256),");
        sb.append("TMDBTIME	DATETIME,");
        sb.append("NAPPPROTO	INT,");
        sb.append("CONSTRAINT FILEMETA_" + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX FILEMETA_" + tableDate + "_IDX (TMDBTIME, TMLOGTIME)");
        sb.append(")");
        return sb.toString();
    }

    public String getLogTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String type = "INT UNSIGNED,";
        String tableName = "LOG_V6_";
        if (nType == 4) {
            tableName = "LOG_";
        } else {
            type = "VARCHAR(40),";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED,");
        sb.append("LCODE	INT UNSIGNED,");
        sb.append("TMSTART	DATETIME,");
        sb.append("TMEND	DATETIME,");
        sb.append("DWSOURCEIP	").append(type);
        sb.append("DWDESTINATIONIP	").append(type);
        sb.append("NPROTOCOL	INT,");
        sb.append("BTYPE	INT,");
        sb.append("STRTITLE	VARCHAR(256),");
        sb.append("STRSOURCEMAC	VARCHAR(20),");
        sb.append("STRDESTINATIONMAC	VARCHAR(20),");
        sb.append("NSOURCEPORT	INT,");
        sb.append("NDESTINATIONPORT	INT,");
        sb.append("DWPACKETCOUNTER	INT,");
        sb.append("DWDSTPORTCOUNTER	INT,");
        sb.append("DWSRCIPCOUNTER	INT,");
        sb.append("BSEVERITY	INT,");
        sb.append("NTTL	INT,");
        sb.append("DWDSTIPCOUNTER	INT,");
        sb.append("DWSRCPORTCOUNTER	INT,");
        sb.append("DWEVENTNUM	INT,");
        sb.append("WINBOUND	INT,");
        sb.append("UCCREATELOGTYPE	INT,");
        sb.append("WVLANINFO	INT,");
        sb.append("DWPKTSIZE	INT,");
        sb.append("DWMALICIOUSSRVFRAME	INT,");
        sb.append("DWMALICIOUSCLIFRAME	INT,");
        sb.append("DWMALICIOUSSRVBYTE	INT,");
        sb.append("DWMALICIOUSCLIBYTE	INT,");
        sb.append("UCINTRUSIONDIR	INT,");
        sb.append("UCACCESSDIR	INT,");
        sb.append("LSRCNETINDEX	INT,");
        sb.append("LDSTNETINDEX	INT,");
        sb.append("LSRCUSERINDEX	INT,");
        sb.append("LDSTUSERINDEX	INT,");
        sb.append("LURLINDEX	INT,");
        sb.append("STRSRCNATIONISO	VARCHAR(256),");
        sb.append("STRDESTNATIONISO	VARCHAR(256),");
        sb.append("TMDBTIME	DATETIME,");
        sb.append("CONSTRAINT " + tableName + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMDBTIME, TMSTART, TMEND)");
        sb.append(")");
        return sb.toString();
    }

    public String getRawdataTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String tableName = "RAWDATA_V6_";
        if (nType == 4) {
            tableName = "RAWDATA_";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED,");
        sb.append("LMODE	INT,");
        sb.append("WDATASIZE	INT,");
        sb.append("SDATA	TEXT,");
        sb.append("CONSTRAINT " + tableName + tableDate + "_PK PRIMARY KEY (LINDEX)");
        sb.append(")");
        return sb.toString();
    }

    public String getSensorAliveTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("SENSOR_ALIVE_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("DBLTOTALPACKETFRAME	INT,");
        sb.append("DBLTOTALPACKETBYTES	INT,");
        sb.append("DBLPPS	INT,");
        sb.append("DBLBPS	INT,");
        sb.append("DBLDROPPACKET	INT,");
        sb.append("DBLDPPS1000	INT,");
        sb.append("DBLSESSION	INT,");
        sb.append("DBLMALICIOUSSESSION	INT,");
        sb.append("DBLLPS	INT,");
        sb.append("DBLDLPS	INT,");
        sb.append("TMCUR	DATETIME,");
        sb.append("DBLMALICIOUSFRAME	INT,");
        sb.append("DBLMALICIOUSBYTES	INT,");
        sb.append("DBLMALCIOUSBPS	INT,");
        sb.append("DBLMALCIOUSPPS	INT,");
        sb.append("LCPUSPEED	INT,");
        sb.append("NCPUNUM	INT,");
        sb.append("NCPUUSAGE	INT,");
        sb.append("DBLMEMTOTAL	INT,");
        sb.append("DBLMEMUSED	INT,");
        sb.append("LPROCESSNUM	INT,");
        sb.append("TMSENSORUPTIME	VARCHAR(256),");
        sb.append("LTIMESYNCNUM	INT,");
        sb.append("STRDRIVENAME	VARCHAR(256),");
        sb.append("DBLHDDTOTAL	INT,");
        sb.append("DBLHDDUSED	INT,");
        sb.append("UCCONNECT	INT,");
        sb.append("INDEX SENSOR_ALIVE_" + tableDate + "_IDX (TMCUR)");
        sb.append(")");
        return sb.toString();
    }

    public String getSensorSessionTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("SENSOR_SESSION_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("DBLTCPSYNFRAME	INT,");
        sb.append("DBLTCPSYNBYTES	INT,");
        sb.append("DBLTCPSYNACKFRAME	INT,");
        sb.append("DBLTCPSYNACKBYTES	INT,");
        sb.append("DBLTCPRSTFRAME	INT,");
        sb.append("DBLTCPRSTBYTES	INT,");
        sb.append("DBLTCPFINFRAME	INT,");
        sb.append("DBLTCPFINBYTES	INT,");
        sb.append("TMCUR	DATETIME,");
        sb.append("INDEX SENSOR_SESSION_" + tableDate + "_IDX (TMCUR)");
        sb.append(")");
        return sb.toString();
    }

    public String getSystemLogSensorTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append("SYSTEMLOG_SENSOR_");
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LSYSTEMLOGINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("TMOCCUR	DATETIME,");
        sb.append("TMSENSORRUP	VARCHAR(256),");
        sb.append("DWTIMESYNCNUM	INT,");
        sb.append("DWCPUSPEED	INT,");
        sb.append("DWCPUNUM	INT,");
        sb.append("DWMEMTOTAL	INT,");
        sb.append("DBLMAXCPUUSAGE	INT,");
        sb.append("DBLMINCPUUSAGE	INT,");
        sb.append("DBLAVGCPUUSAGE	INT,");
        sb.append("DBLCURCPUUSAGE	INT,");
        sb.append("DBLMAXMEMUSED	INT,");
        sb.append("DBLMINMEMUSED	INT,");
        sb.append("DBLAVGMEMUSED	INT,");
        sb.append("DBLCURMEMUSED	INT,");
        sb.append("DWPROCESSNUM	INT,");
        sb.append("STRHDDNAME	VARCHAR(256),");
        sb.append("DWHDDTOTAL	INT,");
        sb.append("DWHDDUSED	INT,");
        sb.append("CONSTRAINT SYSTEMLOG_SENSOR_" + tableDate + "_PK PRIMARY KEY (LSYSTEMLOGINDEX),");
        sb.append("INDEX SYSTEMLOG_SENSOR_" + tableDate + "_IDX (TMOCCUR)");
        sb.append(")");
        return sb.toString();
    }

    public String getSessionTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String type = "INT UNSIGNED,";
        String tableName = "SESSION_V6_";
        if (nType == 4) {
            tableName = "SESSION_";
        } else {
            type = "VARCHAR(40),";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("STRSESSIONINDEX  VARCHAR(15),");
        sb.append("DWSERVERIP	").append(type);
        sb.append("DWCLIENTIP	").append(type);
        sb.append("WSERVERPORT	INT,");
        sb.append("WCLIENTPORT	INT,");
        sb.append("TMSTART	DATETIME,");
        sb.append("TMEND	DATETIME,");
        sb.append("STRUSER	VARCHAR(20),");
        sb.append("STRINFORMATION	VARCHAR(256),");
        sb.append("DWSERVERBYTES	INT,");
        sb.append("DWCLIENTBYTES	INT,");
        sb.append("STRFILENAME	VARCHAR(256),");
        sb.append("WSESSIONSTATUS	INT,");
        sb.append("DWVIOLATIONCODE	INT,");
        sb.append("DWRESERVED	INT,");
        sb.append("LSRCNETINDEX	INT,");
        sb.append("LDSTNETINDEX	INT,");
        sb.append("TMDBTIME	DATETIME,");
        sb.append("INDEX " + tableName + tableDate + "_IDX (STRSESSIONINDEX, TMSTART, TMEND)");
        sb.append(")");
        return sb.toString();
    }

    public String getIpTrafficTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String type = "INT UNSIGNED,";
        String tableName = "IP_TRAFFIC_V6_";
        if (nType == 4) {
            tableName = "IP_TRAFFIC_";
        } else {
            type = "VARCHAR(40),";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("TMSTART	DATETIME,");
        sb.append("DWSOURCEIP	").append(type);
        sb.append("DWDESTINATIONIP	").append(type);
        sb.append("WSRCPORT	INT,");
        sb.append("WDSTPORT	INT,");
        sb.append("NPROTOCOL	INT,");
        sb.append("DBLSRCPPS	INT,");
        sb.append("DBLSRCBPS	INT,");
        sb.append("DBLDSTPPS	INT,");
        sb.append("DBLDSTBPS	INT,");
        sb.append("LNETWORKINDEX	INT,");
        sb.append("LUSERINDEX	INT,");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMSTART)");
        sb.append(")");
        return sb.toString();
    }

    public String getProtocolTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String tableName = "PROTOCOL_V6_";
        if (nType == 4) {
            tableName = "PROTOCOL_";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("TMSTART	DATETIME,");
        sb.append("TMEND	DATETIME,");
        sb.append("UCTYPE	INT,");
        sb.append("NPROTOCOL	INT,");
        sb.append("STRNAME	VARCHAR(256),");
        sb.append("DBLPPS	INT,");
        sb.append("DBLBPS	INT,");
        sb.append("DBLATTEMPTCPS	INT,");
        sb.append("DBLMADECPS	INT,");
        sb.append("DBLCURRENTCPS	INT,");
        sb.append("DBLCLOSECPS	INT,");
        sb.append("DBLKILLCPS	INT,");
        sb.append("LNETWORKINDEX	INT,");
        sb.append("LUSERINDEX	INT,");
        sb.append("DBLPPSININ	INT,");
        sb.append("DBLPPSINOUT	INT,");
        sb.append("DBLPPSOUTIN	INT,");
        sb.append("DBLPPSOUTOUT	INT,");
        sb.append("DBLPPSOTHERS	INT,");
        sb.append("DBLBPSININ	INT,");
        sb.append("DBLBPSINOUT	INT,");
        sb.append("DBLBPSOUTIN	INT,");
        sb.append("DBLBPSOUTOUT	INT,");
        sb.append("DBLBPSOTHERS	INT,");
        sb.append("DBLATTEMPTCPSININ	INT,");
        sb.append("DBLATTEMPTCPSINOUT	INT,");
        sb.append("DBLATTEMPTCPSOUTIN	INT,");
        sb.append("DBLATTEMPTCPSOUTOUT	INT,");
        sb.append("DBLMADECPSININ	INT,");
        sb.append("DBLMADECPSINOUT	INT,");
        sb.append("DBLMADECPSOUTIN	INT,");
        sb.append("DBLMADECPSOUTOUT	INT,");
        sb.append("DBLCURRENTCPSININ	INT,");
        sb.append("DBLCURRENTCPSINOUT	INT,");
        sb.append("DBLCURRENTCPSOUTIN	INT,");
        sb.append("DBLCURRENTCPSOUTOUT	INT,");
        sb.append("DBLCLOSECPSININ	INT,");
        sb.append("DBLCLOSECPSINOUT	INT,");
        sb.append("DBLCLOSECPSOUTIN	INT,");
        sb.append("DBLCLOSECPSOUTOUT	INT,");
        sb.append("DBLKILLCPSININ	INT,");
        sb.append("DBLKILLCPSINOUT	INT,");
        sb.append("DBLKILLCPSOUTIN	INT,");
        sb.append("DBLKILLCPSOUTOUT	INT,");
        sb.append("CONSTRAINT " + tableName + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMSTART, TMEND)");
        sb.append(")");
        return sb.toString();
    }

    public String getServiceTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String tableName = "SERVICE_V6_";
        if (nType == 4) {
            tableName = "SERVICE_";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("TMSTART	DATETIME,");
        sb.append("TMEND	DATETIME,");
        sb.append("NPROTOCOL	INT,");
        sb.append("WSERVICE	INT,");
        sb.append("DBLSRCPPS	INT,");
        sb.append("DBLSRCBPS	INT,");
        sb.append("DBLDSTPPS	INT,");
        sb.append("DBLDSTBPS	INT,");
        sb.append("DBLATTEMPTCPS	INT,");
        sb.append("DBLMADECPS	INT,");
        sb.append("DBLCURRENTCPS	INT,");
        sb.append("DBLCLOSECPS	INT,");
        sb.append("DBLKILLCPS	INT,");
        sb.append("LNETWORKINDEX	INT,");
        sb.append("LUSERINDEX	INT,");
        sb.append("DBLSRCPPSININ	INT,");
        sb.append("DBLSRCPPSINOUT	INT,");
        sb.append("DBLSRCPPSOUTIN	INT,");
        sb.append("DBLSRCPPSOUTOUT	INT,");
        sb.append("DBLSRCBPSININ	INT,");
        sb.append("DBLSRCBPSINOUT	INT,");
        sb.append("DBLSRCBPSOUTIN	INT,");
        sb.append("DBLSRCBPSOUTOUT	INT,");
        sb.append("DBLDSTPPSININ	INT,");
        sb.append("DBLDSTPPSINOUT	INT,");
        sb.append("DBLDSTPPSOUTIN	INT,");
        sb.append("DBLDSTPPSOUTOUT	INT,");
        sb.append("DBLDSTBPSININ	INT,");
        sb.append("DBLDSTBPSINOUT	INT,");
        sb.append("DBLDSTBPSOUTIN	INT,");
        sb.append("DBLDSTBPSOUTOUT	INT,");
        sb.append("DBLATTEMPTCPSININ	INT,");
        sb.append("DBLATTEMPTCPSINOUT	INT,");
        sb.append("DBLATTEMPTCPSOUTIN	INT,");
        sb.append("DBLATTEMPTCPSOUTOUT	INT,");
        sb.append("DBLMADECPSININ	INT,");
        sb.append("DBLMADECPSINOUT	INT,");
        sb.append("DBLMADECPSOUTIN	INT,");
        sb.append("DBLMADECPSOUTOUT	INT,");
        sb.append("DBLCURRENTCPSININ	INT,");
        sb.append("DBLCURRENTCPSINOUT	INT,");
        sb.append("DBLCURRENTCPSOUTIN	INT,");
        sb.append("DBLCURRENTCPSOUTOUT	INT,");
        sb.append("DBLCLOSECPSININ	INT,");
        sb.append("DBLCLOSECPSINOUT	INT,");
        sb.append("DBLCLOSECPSOUTIN	INT,");
        sb.append("DBLCLOSECPSOUTOUT	INT,");
        sb.append("DBLKILLCPSININ	INT,");
        sb.append("DBLKILLCPSINOUT	INT,");
        sb.append("DBLKILLCPSOUTIN	INT,");
        sb.append("DBLKILLCPSOUTOUT	INT,");
        sb.append("CONSTRAINT " + tableName + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMSTART, TMEND)");
        sb.append(")");
        return sb.toString();
    }

    public String getTrafficIpTable(String tableDate, int nType) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        String type = "INT UNSIGNED,";
        String tableName = "TRAFFIC_IP_V6_";
        if (nType == 4) {
            tableName = "TRAFFIC_IP_";
        } else {
            type = "VARCHAR(40),";
        }
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED,");
        sb.append("TMSTART	DATETIME,");
        sb.append("TMEND	DATETIME,");
        sb.append("DWIPADDR	").append(type);
        sb.append("DBLSRCPPS	INT,");
        sb.append("DBLSRCBPS	INT,");
        sb.append("DBLDSTPPS	INT,");
        sb.append("DBLDSTBPS	INT,");
        sb.append("DBLATTEMPTCPS	INT,");
        sb.append("DBLMADECPS	INT,");
        sb.append("DBLCURRENTCPS	INT,");
        sb.append("DBLCLOSECPS	INT,");
        sb.append("DBLKILLCPS	INT,");
        sb.append("LNETWORKINDEX	INT,");
        sb.append("LUSERINDEX	INT,");
        sb.append("DBLSRCPPSININ	INT,");
        sb.append("DBLSRCPPSINOUT	INT,");
        sb.append("DBLSRCPPSOUTIN	INT,");
        sb.append("DBLSRCPPSOUTOUT	INT,");
        sb.append("DBLSRCBPSININ	INT,");
        sb.append("DBLSRCBPSINOUT	INT,");
        sb.append("DBLSRCBPSOUTIN	INT,");
        sb.append("DBLSRCBPSOUTOUT	INT,");
        sb.append("DBLDSTPPSININ	INT,");
        sb.append("DBLDSTPPSINOUT	INT,");
        sb.append("DBLDSTPPSOUTIN	INT,");
        sb.append("DBLDSTPPSOUTOUT	INT,");
        sb.append("DBLDSTBPSININ	INT,");
        sb.append("DBLDSTBPSINOUT	INT,");
        sb.append("DBLDSTBPSOUTIN	INT,");
        sb.append("DBLDSTBPSOUTOUT	INT,");
        sb.append("DBLATTEMPTCPSINOUT	INT,");
        sb.append("DBLATTEMPTCPSOUTIN	INT,");
        sb.append("DBLMADECPSINOUT	INT,");
        sb.append("DBLMADECPSOUTIN	INT,");
        sb.append("DBLCURRENTCPSINOUT	INT,");
        sb.append("DBLCURRENTCPSOUTIN	INT,");
        sb.append("DBLCLOSECPSINOUT	INT,");
        sb.append("DBLCLOSECPSOUTIN	INT,");
        sb.append("DBLKILLCPSINOUT	INT,");
        sb.append("DBLKILLCPSOUTIN	INT,");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMSTART, TMEND)");
        sb.append(")");
        return sb.toString();
    }

    public String getTrafficLogTable(String tableName, String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append(tableDate);
        sb.append(" (");
        sb.append("LINDEX	INT UNSIGNED NOT NULL AUTO_INCREMENT,");
        sb.append("LMODE	INT,");
        if(tableName.toUpperCase().indexOf("TRAFFIC_DETECTION") > -1 ) {
            sb.append("LCODE	INT,");
        }
        else {
            sb.append("LCODE	INT UNSIGNED,");
        }
        sb.append("STRTITLE	VARCHAR(256),");
        sb.append("SOURCEIP	VARCHAR(40),");
        sb.append("DESTNATIONIP	VARCHAR(40),");
        sb.append("SOURCEPORT	INT,");
        sb.append("DESTNATIONPORT	INT,");
        sb.append("PROTOCOL	INT,");
        sb.append("IPTYPE	INT,");
        sb.append("TMCUR	DATETIME,");
        sb.append("CONSTRAINT " + tableName + tableDate + "_PK PRIMARY KEY (LINDEX),");
        sb.append("INDEX " + tableName + tableDate + "_IDX (TMCUR)");
        sb.append(")");
        return sb.toString();
    }

    private String getDateTempTable(String tableDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableDate);
        sb.append(" (TEMPDATE	VARCHAR(20))");
        return sb.toString();
    }

    private void insertTempDateTime(String tableName) {
        HashMap map = new HashMap();
        map.put("tableName", tableName);
        for(int i = 0 ; i < 24 ; i++) {
            for(int min = 0 ; min < 60 ; min ++) {
                map.put("dateTime", DateTimeUtil.getNowTempDateHourMin(i, min));
                createMapper.insertTempDateTime(map);
            }
        }
    }

    public boolean isTables(String tableName) {
        boolean rtn = false;
        String result = oracleSvc.isTable(tableName);
        if (result != null && !result.isEmpty()) {
            rtn = true;
        }
        return rtn;
    }
}
