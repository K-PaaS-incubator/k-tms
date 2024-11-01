package com.kglory.tms.web.services;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.CreateTableMapper;
import com.kglory.tms.web.mapper.systemSettings.ManagerMapper;
import com.kglory.tms.web.model.vo.ManagerVO;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.TableFinder;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.transaction.annotation.Transactional;

@Service("oracleSvc")
public class OracleService {

    private static Logger logger = LoggerFactory.getLogger(OracleService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    AuditLogService auditLogSvc;
    @Autowired
    CreateTableMapper createMapper;

    public List<String> selectTables(List<String> tableName) {
        return oracleMapper.selectTables(tableName);
    }

    public List<String> findTables(String prefix, Calendar startDate, Calendar endDate, Long ipType) {
        return selectTables(TableFinder.getStatisticsTables(prefix, startDate, endDate, ipType));
    }

    public String selectNowDbDateTime() {
        return oracleMapper.selectNowDbDateTime();
    }

    public String isTable(String tableName) {
        return oracleMapper.isTable(tableName);
    }

    public List<String> selectTableNames(HashMap<String, String> map) throws BaseException{
        List<String> rtnList = new ArrayList<>();
        rtnList = oracleMapper.selectTableNames(map);
        return rtnList;
    }
    
    public List<String> selectTableDeleteNames(HashMap<String, String> map) throws BaseException{
        List<String> rtnList = new ArrayList<>();
        rtnList = oracleMapper.selectTableDeleteNames(map);
        return rtnList;
    }

    @Transactional
    public void dropTables(List<String> list) throws BaseException{
        try {
            if (list != null && list.size() > 0) {
                oracleMapper.dropTables(list);
                HashMap<String, String> map;
                for (String tableName : list) {
                    map = new HashMap<>();
                    map.put("tableIndex", tableName + "_IDX");
                    map.put("tableName", tableName);
//                    oracleMapper.dropTableIndex(map);
                }
                auditLogSvc.insertAuditLogMsg(1L, MessageUtil.getbuilMessage("audit.db.tabledelete.success", list.toString()), "administrator");
            }
        } catch (BaseException ex) {
            logger.error(ex.getLocalizedMessage(), ex);
            auditLogSvc.insertAuditLogMsg(2L, MessageUtil.getbuilMessage("audit.db.tabledelete.fail", list.toString()), "administrator");
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
            auditLogSvc.insertAuditLogMsg(2L, MessageUtil.getbuilMessage("audit.db.tabledelete.fail", list.toString()), "administrator");
            throw new BaseException(ex);
        }
    }

    public void deleteDaysTable() throws BaseException{
        String v6 = "V6_";
        String log = "LOG_";
        String rowData = "RAWDATA_";
        String session = "SESSION_";
        String applayer = "APPLAYER_";
        String fileMeta = "FILEMETA_";
        String audit = "AUDIT_";
        String auditResult = "AUDIT_RESULT_";
        String sensorAlive = "SENSOR_ALIVE_";
        String sensorSession = "SENSOR_SESSION_";
        String systemLog = "SYSTEMLOG_SENSOR_";
        String ipTraffic = "IP_TRAFFIC_";
        String protocol = "PROTOCOL_";
        String service = "SERVICE_";
        String trafficIp = "TRAFFIC_IP_";
        String trafficLog = "TRAFFIC_DETECTION_";

        ManagerVO dbConfig = managerMapper.selectDbManagement();
        String eventStartDay = DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yy_MM_dd"), (dbConfig.getnRawPeriodic().intValue() * -1), "yy_MM_dd");
        String auditStartDay = DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yy_MM_dd"), (dbConfig.getnAuditPeriodic().intValue() * -1), "yy_MM_dd");
        List<String> deleteTables = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        //------ log -----------------------------
        map.put("tableName", log);
        map.put("tableDate", eventStartDay);
        map.put("excludTable", log + v6);
        map.put("num", String.valueOf(log.length() + 1));
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ log v6 -----------------------------
        map.put("tableName", log + v6);
        map.put("num", String.valueOf(log.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ rowData -----------------------------
        map.put("tableName", rowData);
        map.put("num", String.valueOf(rowData.length() + 1));
        map.put("excludTable", log + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ rowData v6 -----------------------------
        map.put("tableName", rowData + v6);
        map.put("num", String.valueOf(rowData.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ session -----------------------------
        map.put("tableName", session);
        map.put("num", String.valueOf(session.length() + 1));
        map.put("excludTable", session + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ session v6 -----------------------------
        map.put("tableName", session + v6);
        map.put("num", String.valueOf(session.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ applayer -----------------------------
        map.put("tableName", applayer);
        map.put("num", String.valueOf(applayer.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ fileMeta -----------------------------
        map.put("tableName", fileMeta);
        map.put("num", String.valueOf(fileMeta.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================

        //------ sensorAlive -----------------------------
        map.put("tableName", sensorAlive);
        map.put("num", String.valueOf(sensorAlive.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ sensorSession -----------------------------
        map.put("tableName", sensorSession);
        map.put("num", String.valueOf(sensorSession.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ systemLog -----------------------------
        map.put("tableName", systemLog);
        map.put("num", String.valueOf(systemLog.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ ipTraffic -----------------------------
        map.put("tableName", ipTraffic);
        map.put("num", String.valueOf(ipTraffic.length() + 1));
        map.put("excludTable", ipTraffic + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ ipTraffic v6 -----------------------------
        map.put("tableName", ipTraffic + v6);
        map.put("num", String.valueOf(ipTraffic.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ protocol -----------------------------
        map.put("tableName", protocol);
        map.put("num", String.valueOf(protocol.length() + 1));
        map.put("excludTable", protocol + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ protocol v6 -----------------------------
        map.put("tableName", protocol + v6);
        map.put("num", String.valueOf(protocol.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ service -----------------------------
        map.put("tableName", service);
        map.put("num", String.valueOf(service.length() + 1));
        map.put("excludTable", service + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ service v6 -----------------------------
        map.put("tableName", service + v6);
        map.put("num", String.valueOf(service.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ trafficIp -----------------------------
        map.put("tableName", trafficIp);
        map.put("num", String.valueOf(trafficIp.length() + 1));
        map.put("excludTable", trafficIp + v6);
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ trafficIp v6 -----------------------------
        map.put("tableName", trafficIp + v6);
        map.put("num", String.valueOf(trafficIp.length() + v6.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================

        //------ audit -----------------------------
        map.put("tableName", audit);
        map.put("tableDate", auditStartDay);
        map.put("num", String.valueOf(audit.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
        //------ auditResult -----------------------------
        map.put("tableName", auditResult);
        map.put("num", String.valueOf(auditResult.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================

        //------ trafficLog -----------------------------
        map.put("tableName", trafficLog);
        map.put("num", String.valueOf(trafficLog.length() + 1));
        map.put("excludTable", "");
        deleteTables = oracleMapper.selectTableNames(map);
        dropTables(deleteTables);
        //========================================
    }

    public List<String> selectTableDateList() throws BaseException{
        List<String> rtnList = new ArrayList<>();
        rtnList = oracleMapper.selectTableDateList();
        return rtnList;
    }

    public List<String> selectTableNameList(String tableDate) throws BaseException{
        List<String> rtnList = new ArrayList<>();
        rtnList = oracleMapper.selectTableNameList(tableDate);
        return rtnList;
    }
}
