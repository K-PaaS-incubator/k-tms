package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.ManagerDto;
import com.kglory.tms.web.model.dto.ManagerIntegrityFileDto;
import java.util.List;
import java.util.Objects;

public class ManagerVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = -9130271204339869004L;

    private Long nEventPort;

    private String strDbtsn;
    private String strDbuid;
    private String strDbpwd;

    private String strManagerVersion;
    private String strServer;
    private Long nPort;
    private Long nAutoUpdate;
    private Long nDate;
    private Long nTime;
    private Integer nUpdateState;
    private String tmUpdateState;

    private String strEmailServer;

    private String strAlertProgramPathName;

    private Long nRawPeriodic;
    private Long nAuditPeriodic;

    private Long nDiskUsage;
    private Long nDiskWarn;
    private Integer nDirectIntegrity;
    private Long nStartIntegrity;
    private Long nAutoIntegrity;
    private Long nAutoIntegrityMin;

    private List<SystemConfVO> systemConfList;
    private List<String> loginAuthIpList;

    private Integer sessionIdx;
    private String sessionValue;
    private Integer lockIdx;
    private String lockValue;
    private Integer timeIdx;
    private String timeValue;

    public Long getnEventPort() {
        return nEventPort;
    }

    public void setnEventPort(Long nEventPort) {
        this.nEventPort = nEventPort;
    }

    public String getStrDbtsn() {
        return strDbtsn;
    }

    public void setStrDbtsn(String strDbtsn) {
        this.strDbtsn = strDbtsn;
    }

    public String getStrDbuid() {
        return strDbuid;
    }

    public void setStrDbuid(String strDbuid) {
        this.strDbuid = strDbuid;
    }

    public String getStrDbpwd() {
        return strDbpwd;
    }

    public void setStrDbpwd(String strDbpwd) {
        this.strDbpwd = strDbpwd;
    }

    public String getStrManagerVersion() {
        return strManagerVersion;
    }

    public void setStrManagerVersion(String strManagerVersion) {
        this.strManagerVersion = strManagerVersion;
    }

    public String getStrServer() {
        return strServer;
    }

    public void setStrServer(String strServer) {
        this.strServer = strServer;
    }

    public Long getnPort() {
        return nPort;
    }

    public void setnPort(Long nPort) {
        this.nPort = nPort;
    }

    public Long getnAutoUpdate() {
        return nAutoUpdate;
    }

    public void setnAutoUpdate(Long nAutoUpdate) {
        this.nAutoUpdate = nAutoUpdate;
    }

    public Long getnDate() {
        return nDate;
    }

    public void setnDate(Long nDate) {
        this.nDate = nDate;
    }

    public Long getnTime() {
        return nTime;
    }

    public void setnTime(Long nTime) {
        this.nTime = nTime;
    }

    public Integer getnUpdateState() {
        return nUpdateState;
    }

    public void setnUpdateState(Integer nUpdateState) {
        this.nUpdateState = nUpdateState;
    }

    public String getTmUpdateState() {
        return tmUpdateState;
    }

    public void setTmUpdateState(String tmUpdateState) {
        this.tmUpdateState = tmUpdateState;
    }

    public String getStrEmailServer() {
        return strEmailServer;
    }

    public void setStrEmailServer(String strEmailServer) {
        this.strEmailServer = strEmailServer;
    }

    public String getStrAlertProgramPathName() {
        return strAlertProgramPathName;
    }

    public void setStrAlertProgramPathName(String strAlertProgramPathName) {
        this.strAlertProgramPathName = strAlertProgramPathName;
    }

    public Long getnRawPeriodic() {
        return nRawPeriodic;
    }

    public void setnRawPeriodic(Long nRawPeriodic) {
        this.nRawPeriodic = nRawPeriodic;
    }

    public Long getnAuditPeriodic() {
        return nAuditPeriodic;
    }

    public void setnAuditPeriodic(Long nAuditPeriodic) {
        this.nAuditPeriodic = nAuditPeriodic;
    }

    public Long getnDiskUsage() {
        return nDiskUsage;
    }

    public void setnDiskUsage(Long nDiskUsage) {
        this.nDiskUsage = nDiskUsage;
    }

    public Long getnDiskWarn() {
        return nDiskWarn;
    }

    public void setnDiskWarn(Long nDiskWarn) {
        this.nDiskWarn = nDiskWarn;
    }

    public Integer getnDirectIntegrity() {
        return nDirectIntegrity;
    }

    public void setnDirectIntegrity(Integer nDirectIntegrity) {
        this.nDirectIntegrity = nDirectIntegrity;
    }

    public Long getnStartIntegrity() {
        return nStartIntegrity;
    }

    public void setnStartIntegrity(Long nStartIntegrity) {
        this.nStartIntegrity = nStartIntegrity;
    }

    public Long getnAutoIntegrity() {
        return nAutoIntegrity;
    }

    public void setnAutoIntegrity(Long nAutoIntegrity) {
        this.nAutoIntegrity = nAutoIntegrity;
    }

    public Long getnAutoIntegrityMin() {
        return nAutoIntegrityMin;
    }

    public void setnAutoIntegrityMin(Long nAutoIntegrityMin) {
        this.nAutoIntegrityMin = nAutoIntegrityMin;
    }

    public List<SystemConfVO> getSystemConfList() {
        return systemConfList;
    }

    public void setSystemConfList(List<SystemConfVO> systemConfList) {
        this.systemConfList = systemConfList;
    }

    public List<String> getLoginAuthIpList() {
        return loginAuthIpList;
    }

    public void setLoginAuthIpList(List<String> loginAuthIpList) {
        this.loginAuthIpList = loginAuthIpList;
    }

    public Integer getSessionIdx() {
        return sessionIdx;
    }

    public void setSessionIdx(Integer sessionIdx) {
        this.sessionIdx = sessionIdx;
    }

    public String getSessionValue() {
        return sessionValue;
    }

    public void setSessionValue(String sessionValue) {
        this.sessionValue = sessionValue;
    }

    public Integer getLockIdx() {
        return lockIdx;
    }

    public void setLockIdx(Integer lockIdx) {
        this.lockIdx = lockIdx;
    }

    public String getLockValue() {
        return lockValue;
    }

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }

    public Integer getTimeIdx() {
        return timeIdx;
    }

    public void setTimeIdx(Integer timeIdx) {
        this.timeIdx = timeIdx;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public boolean equalsDbInfo(ManagerDto dto) {
        if (nRawPeriodic.equals(dto.getnRawPeriodicInput())
                && nAuditPeriodic.equals(dto.getnAuditPeriodicInput())
                && nDiskUsage.equals(dto.getnDiskUsage())
                && nDiskWarn.equals(dto.getnDiskWarn())) {
            return true;
        }
        return false;
    }
    
    public boolean equalsIntegrityInfo(ManagerIntegrityFileDto dto) {
        if (nStartIntegrity.equals(dto.getnStartIntegrity()) && nAutoIntegrity.equals(dto.getnAutoIntegrity()) && nAutoIntegrityMin.equals(dto.getnAutoIntegrityMin())) {
            return true;
        }
        return false;
    }
    
    public boolean equalsUpdateInfo(ManagerDto dto) {
        if (nAutoUpdate.equals(dto.getnAutoUpdateCheck()) && nDate.equals(dto.getnDateSelect()) && nTime.equals(dto.getnTimeSelect())) {
            return true;
        }

        return false;
    }

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
    
}
