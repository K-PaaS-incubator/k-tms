package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.logging.Log;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.util.NumberUtil;
import java.util.Objects;

public class DetectionPolicyDto extends CommonBean {

    private long lCode;
    private String strTitle;
    private String strAttacKType;
    private long bSeverity;
    private String strSummary;
    private String strDescription;
    private String strFalsePositive;
    private String strSolution;

    private String attackNameInput;
    private BigInteger attackTypeSelect;
    private BigInteger severityLevel;
    private String protocol;
    private Long thresholdNum;
    private Long thresholdTime;
    private long lResponseSelect;
    private long lThresholdNum;
    private long lThresholdTime;
    private long lThresholdNumValue;
    private long lThresholdTimeValue;

    private String cveId;
    private String strCveId;
    private String strbId;
    private String strReference;
    private String strVul;
    private String strNotVul;
    private String strhelpDescription;
    private String strAddrsPoof;
    private long lUsed;
    private long lUsedValue;
    private Boolean useSignature;
    private Boolean unUseSignature;
    private Boolean resultInsearch;

    private long sAlive;
    private long sClassType;
    private String sClassName;
    private long sSeverity;
    private String strSigRule;
    private long lResponse;
    private long lResponseValue;
    private long lBlock;
    private long lBlockValue;
    private String strResponse;
    private long lvsensorIndex;

    private String strAtkType;
    private String strDetect;
    private String helpYN;

    private List<Integer> sClassTypeList;

    private List<Long> vsensorList;
    private List<DeployPolicyDto> deployUserPolicyList;
    private List<DeployPolicyDto> deployPolicyList;

    private Integer startRowSize;
    private Integer endRowSize;

    private long nClassType;
    private String strName;
    private String isDownload;
    private Boolean lUsedCheck;
    private Boolean lResponseBool;
    private Boolean lBlockBool;
    private Boolean lBlockCheck;

    public long getlCode() {
        return lCode;
    }

    public void setlCode(long lCode) {
        this.lCode = lCode;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrAttacKType() {
		return strAttacKType;
	}

	public void setStrAttacKType(String strAttacKType) {
		this.strAttacKType = strAttacKType;
	}

	public long getbSeverity() {
        return bSeverity;
    }

    public void setbSeverity(long bSeverity) {
        this.bSeverity = bSeverity;
    }

    public String getStrSummary() {
        return strSummary;
    }

    public void setStrSummary(String strSummary) {
        this.strSummary = strSummary;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrFalsePositive() {
        return strFalsePositive;
    }

    public void setStrFalsePositive(String strFalsePositive) {
        this.strFalsePositive = strFalsePositive;
    }

    public String getStrSolution() {
        return strSolution;
    }

    public void setStrSolution(String strSolution) {
        this.strSolution = strSolution;
    }

    public String getAttackNameInput() {
        return attackNameInput;
    }

    public void setAttackNameInput(String attackNameInput) {
        this.attackNameInput = attackNameInput;
    }

    public BigInteger getAttackTypeSelect() {
        return attackTypeSelect;
    }

    public void setAttackTypeSelect(BigInteger attackTypeSelect) {
        this.attackTypeSelect = attackTypeSelect;
    }

    public BigInteger getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(BigInteger severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getThresholdNum() {
        return thresholdNum;
    }

    public void setThresholdNum(Long thresholdNum) {
        this.thresholdNum = thresholdNum;
    }

    public Long getThresholdTime() {
        return thresholdTime;
    }

    public void setThresholdTime(Long thresholdTime) {
        this.thresholdTime = thresholdTime;
    }

    public long getlResponseSelect() {
        return lResponseSelect;
    }

    public void setlResponseSelect(long lResponseSelect) {
        this.lResponseSelect = lResponseSelect;
    }

    public long getlThresholdNum() {
        return lThresholdNum;
    }

    public void setlThresholdNum(long lThresholdNum) {
        this.lThresholdNum = lThresholdNum;
    }

    public long getlThresholdTime() {
        return lThresholdTime;
    }

    public void setlThresholdTime(long lThresholdTime) {
        this.lThresholdTime = lThresholdTime;
    }

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public String getStrCveId() {
        return strCveId;
    }

    public void setStrCveId(String strCveId) {
        this.strCveId = strCveId;
    }

    public String getStrbId() {
        return strbId;
    }

    public void setStrbId(String strbId) {
        this.strbId = strbId;
    }

    public String getStrReference() {
        return strReference;
    }

    public void setStrReference(String strReference) {
        this.strReference = strReference;
    }

    public String getStrVul() {
        return strVul;
    }

    public void setStrVul(String strVul) {
        this.strVul = strVul;
    }

    public String getStrNotVul() {
        return strNotVul;
    }

    public void setStrNotVul(String strNotVul) {
        this.strNotVul = strNotVul;
    }

    public String getStrhelpDescription() {
        return strhelpDescription;
    }

    public void setStrhelpDescription(String strhelpDescription) {
        this.strhelpDescription = strhelpDescription;
    }

    public String getStrAddrsPoof() {
        return strAddrsPoof;
    }

    public void setStrAddrsPoof(String strAddrsPoof) {
        this.strAddrsPoof = strAddrsPoof;
    }

    public long getlUsed() {
        return lUsed;
    }

    public void setlUsed(long lUsed) {
        this.lUsed = lUsed;
    }

    public Boolean getUseSignature() {
        return useSignature;
    }

    public void setUseSignature(Boolean useSignature) {
        this.useSignature = useSignature;
    }

    public Boolean getUnUseSignature() {
        return unUseSignature;
    }

    public void setUnUseSignature(Boolean unUseSignature) {
        this.unUseSignature = unUseSignature;
    }

    public Boolean getResultInsearch() {
        return resultInsearch;
    }

    public void setResultInsearch(Boolean resultInsearch) {
        this.resultInsearch = resultInsearch;
    }

    public long getsAlive() {
        return sAlive;
    }

    public void setsAlive(long sAlive) {
        this.sAlive = sAlive;
    }

    public long getsClassType() {
        return sClassType;
    }

    public void setsClassType(long sClassType) {
        this.sClassType = sClassType;
    }

    public String getsClassName() {
        return sClassName;
    }

    public void setsClassName(String sClassName) {
        this.sClassName = sClassName;
    }

    public long getsSeverity() {
        return sSeverity;
    }

    public void setsSeverity(long sSeverity) {
        this.sSeverity = sSeverity;
    }

    public String getStrSigRule() {
        return strSigRule;
    }

    public void setStrSigRule(String strSigRule) {
        this.strSigRule = strSigRule;
    }

    public long getlResponse() {
        return lResponse;
    }

    public void setlResponse(long lResponse) {
        this.lResponse = lResponse;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public String getStrAtkType() {
        return strAtkType;
    }

    public void setStrAtkType(String strAtkType) {
        this.strAtkType = strAtkType;
    }

    public String getHelpYN() {
        return helpYN;
    }

    public void setHelpYN(String helpYN) {
        this.helpYN = helpYN;
    }

    public String getStrDetect() {
        return strDetect;
    }

    public void setStrDetect(String strDetect) {
        this.strDetect = strDetect;
    }

    public List<Integer> getsClassTypeList() {
        return sClassTypeList;
    }

    public void setsClassTypeList(List<Integer> sClassTypeList) {
        this.sClassTypeList = sClassTypeList;
    }

    public List<Long> getVsensorList() {
        return vsensorList;
    }

    public void setVsensorList(List<Long> vsensorList) {
        this.vsensorList = vsensorList;
    }

    public List<DeployPolicyDto> getDeployUserPolicyList() {
        return deployUserPolicyList;
    }

    public void setDeployUserPolicyList(List<DeployPolicyDto> deployUserPolicyList) {
        this.deployUserPolicyList = deployUserPolicyList;
    }

    public List<DeployPolicyDto> getDeployPolicyList() {
        return deployPolicyList;
    }

    public void setDeployPolicyList(List<DeployPolicyDto> deployPolicyList) {
        this.deployPolicyList = deployPolicyList;
    }

    public Integer getStartRowSize() {
        return startRowSize;
    }

    public void setStartRowSize(Integer startRowSize) {
        this.startRowSize = startRowSize;
    }

    public Integer getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(Integer endRowSize) {
        this.endRowSize = endRowSize;
    }

    public long getlThresholdNumValue() {
        return lThresholdNumValue;
    }

    public void setlThresholdNumValue(long lThresholdNumValue) {
        this.lThresholdNumValue = lThresholdNumValue;
    }

    public long getlThresholdTimeValue() {
        return lThresholdTimeValue;
    }

    public void setlThresholdTimeValue(long lThresholdTimeValue) {
        this.lThresholdTimeValue = lThresholdTimeValue;
    }

    public long getlUsedValue() {
        return lUsedValue;
    }

    public void setlUsedValue(long lUsedValue) {
        this.lUsedValue = lUsedValue;
    }

    public long getlResponseValue() {
        return lResponseValue;
    }

    public void setlResponseValue(long lResponseValue) {
        this.lResponseValue = lResponseValue;
    }

    public long getlBlock() {
        return lBlock;
    }

    public void setlBlock(long lBlock) {
        this.lBlock = lBlock;
    }

    public long getlBlockValue() {
        return lBlockValue;
    }

    public void setlBlockValue(long lBlockValue) {
        this.lBlockValue = lBlockValue;
    }

    public long getnClassType() {
        return nClassType;
    }

    public void setnClassType(long nClassType) {
        this.nClassType = nClassType;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public Boolean getlUsedCheck() {
        return lUsedCheck;
    }

    public void setlUsedCheck(Boolean lUsedCheck) {
        this.lUsedCheck = lUsedCheck;
    }

    public Boolean getlResponseBool() {
        return lResponseBool;
    }

    public void setlResponseBool(Boolean lResponseBool) {
        this.lResponseBool = lResponseBool;
    }

    public Boolean getlBlockBool() {
        return false;
    }

    public void setlBlockBool(Boolean lBlockBool) {
        this.lBlockBool = lBlockBool;
    }

    public Boolean getlBlockCheck() {
        return lBlockCheck;
    }

    public void setlBlockCheck(Boolean lBlockCheck) {
        this.lBlockCheck = lBlockCheck;
    }

    public String getStrResponse() {
        return strResponse;
    }

    public void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    public boolean equalsSig(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetectionPolicyDto other = (DetectionPolicyDto) obj;
        if (this.lThresholdNumValue != other.lThresholdNum) {
            return false;
        }
        if (this.lThresholdTimeValue != other.lThresholdTime) {
            return false;
        }
        if (this.lUsed != other.lUsed) {
            return false;
        }
        if (this.lResponse != other.lResponse) {
            return false;
        }
        return true;
    }
    
    public boolean equalsUserSig(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetectionPolicyDto other = (DetectionPolicyDto) obj;
        if (!Objects.equals(this.strTitle, other.strTitle)) {
            return false;
        }
        if (!Objects.equals(this.strDescription, other.strDescription)) {
            return false;
        }
        if (this.lThresholdNumValue != other.lThresholdNum) {
            return false;
        }
        if (this.lThresholdTimeValue != other.lThresholdTime) {
            return false;
        }
        if (this.lUsed != other.lUsed) {
            return false;
        }
        if (this.sClassType != other.sClassType) {
            return false;
        }
        if (this.sSeverity != other.sSeverity) {
            return false;
        }
        if (this.lResponse != other.lResponse) {
            return false;
        }
        return true;
    }

    public boolean equalsSigHelp(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetectionPolicyDto other = (DetectionPolicyDto) obj;
        if (!Objects.equals(this.strSummary, other.strSummary)) {
            return false;
        }
        if (!Objects.equals(this.strDescription, other.strDescription)) {
            return false;
        }
        if (!Objects.equals(this.strFalsePositive, other.strFalsePositive)) {
            return false;
        }
        if (!Objects.equals(this.strSolution, other.strSolution)) {
            return false;
        }
        if (!Objects.equals(this.strCveId, other.strCveId)) {
            return false;
        }
        if (!Objects.equals(this.strbId, other.strbId)) {
            return false;
        }
        if (!Objects.equals(this.strReference, other.strReference)) {
            return false;
        }
        if (!Objects.equals(this.strVul, other.strVul)) {
            return false;
        }
        if (!Objects.equals(this.strNotVul, other.strNotVul)) {
            return false;
        }
        if (!Objects.equals(this.strAddrsPoof, other.strAddrsPoof)) {
            return false;
        }
        return true;
    }


    public static DetectionPolicyDto detectionResponseVoToDto(DetectionPolicyVO vo) {

        DetectionPolicyDto rtn = new DetectionPolicyDto();
        rtn.setStrDescription(vo.getStrDescription());
        rtn.setsClassType(vo.getsClassType());
        rtn.setsSeverity(vo.getsSeverity());
        rtn.setStrTitle(vo.getStrSigRule());
        rtn.setlUsed(vo.getlUsed());
        if (NumberUtil.longEquals(vo.getlResponse(), 196610L)) {
            rtn.setlResponse(131074L);
        } else if (NumberUtil.longEquals(vo.getlResponse(), 196609L)) {
            rtn.setlResponse(131073L);
        } else {
            rtn.setlResponse(vo.getlResponse());
        }
        rtn.setlThresholdTime(vo.getlThresholdTime());
        rtn.setlThresholdNum(vo.getlThresholdNum());
        
        rtn.setStrFalsePositive(vo.getStrFalsePositive());
        rtn.setStrSolution(vo.getStrSolution());
        rtn.setStrReference(vo.getStrReference());
        rtn.setStrCveId(vo.getStrCveId());
        rtn.setStrbId(vo.getStrbId());
        rtn.setStrVul(vo.getStrVul());
        rtn.setStrSummary(vo.getStrSummary());
        rtn.setStrAddrsPoof(vo.getStrAddrsPoof());
        rtn.setStrNotVul(vo.getStrNotVul());
        return rtn;
    }

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
    
}
