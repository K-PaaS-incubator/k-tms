package com.kglory.tms.web.model.vo;

import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class FileMetaDetectionEventVO extends CommonBean {

	private BigInteger lIndex;
	private String tmDbTime;
	private Integer nProtocol;
	private String tmLogTime;
	private String dwSourceIp;
	private Integer nSourcePort;
	private Integer	nDestinationPort;
	private String destinationIp;
	private String strDestinationIp;
	private Integer nDesctinationPort;
	private String strSourceIp;
	private Integer bType;
	private Integer bIpType;
	private BigInteger lSrcNetIndex;
	private BigInteger lDstNetIndex;
	private BigInteger lvsensorIndex;
	private BigInteger lsensorIndex;
	private String strUri;
	private String strHost;
	private String strUserAgent;
	private String strMagic;
	private Integer nState;
	private Integer nPktNum;
	private BigInteger dwFileSize;
	private String strFileName;
	private String strFileHash;
	private String strStoreFileName;
	private String vsensorName;
	private String sensorName;
	private String strReferer;
        private Long lCode; //yara rule index code
        private Long nGrpIndex; //yara rule group index
        private Integer bSeverity; //yara rule serverity
        private String strRuleName; //yara rule name
        private String strGrpName; //yara rule group name
	
	public Integer getnDestinationPort() {
		return nDestinationPort;
	}
	public void setnDestinationPort(Integer nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}
	public BigInteger getlIndex() {
		return lIndex;
	}
	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}
	public String getTmLogTime() {
		return tmLogTime;
	}
	public void setTmLogTime(String tmLogTime) {
		this.tmLogTime = tmLogTime;
	}
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	public String getStrSourceIp() {
		return strSourceIp;
	}
	public void setStrSourceIp(String strSourceIp) {
		this.strSourceIp = strSourceIp;
	}
	public String getDestinationIp() {
		return destinationIp;
	}
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}
        public String getStrDestinationIp() {
                return strDestinationIp;
        }
        public void setStrDestinationIp(String strDestinationIp) {
                this.strDestinationIp = strDestinationIp;
        }
	public Integer getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	public Integer getnSourcePort() {
		return nSourcePort;
	}
	public void setnSourcePort(Integer nSourcePort) {
		this.nSourcePort = nSourcePort;
	}
	public Integer getnDesctinationPort() {
		return nDesctinationPort;
	}
	public void setnDesctinationPort(Integer nDesctinationPort) {
		this.nDesctinationPort = nDesctinationPort;
	}
	public Integer getbType() {
		return bType;
	}
	public void setbType(Integer bType) {
		this.bType = bType;
	}
	public Integer getbIpType() {
		return bIpType;
	}
	public void setbIpType(Integer bIpType) {
		this.bIpType = bIpType;
	}
	public BigInteger getlSrcNetIndex() {
		return lSrcNetIndex;
	}
	public void setlSrcNetIndex(BigInteger lSrcNetIndex) {
		this.lSrcNetIndex = lSrcNetIndex;
	}
	public BigInteger getlDstNetIndex() {
		return lDstNetIndex;
	}
	public void setlDstNetIndex(BigInteger lDstNetIndex) {
		this.lDstNetIndex = lDstNetIndex;
	}
	public BigInteger getLvsensorIndex() {
		return lvsensorIndex;
	}
	public void setLvsensorIndex(BigInteger lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}
	public BigInteger getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(BigInteger lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	public String getTmDbTime() {
		return tmDbTime;
	}
	public void setTmDbTime(String tmDbTime) {
		this.tmDbTime = tmDbTime;
	}
	public String getStrUri() {
		return strUri;
	}
	public void setStrUri(String strUri) {
		this.strUri = strUri;
	}
	public String getStrHost() {
		return strHost;
	}
	public void setStrHost(String strHost) {
		this.strHost = strHost;
	}
	public String getStrUserAgent() {
		return strUserAgent;
	}
	public void setStrUserAgent(String strUserAgent) {
		this.strUserAgent = strUserAgent;
	}
	public String getStrMagic() {
		return strMagic;
	}
	public void setStrMagic(String strMagic) {
		this.strMagic = strMagic;
	}
	public Integer getnState() {
		return nState;
	}
	public void setnState(Integer nState) {
		this.nState = nState;
	}
	public Integer getnPktNum() {
		return nPktNum;
	}
	public void setnPktNum(Integer nPktNum) {
		this.nPktNum = nPktNum;
	}
	public BigInteger getDwFileSize() {
		return dwFileSize;
	}
	public void setDwFileSize(BigInteger dwFileSize) {
		this.dwFileSize = dwFileSize;
	}
	public String getStrFileName() {
		return strFileName;
	}
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}
	public String getStrFileHash() {
		return strFileHash;
	}
	public void setStrFileHash(String strFileHash) {
		this.strFileHash = strFileHash;
	}
	public String getStrStoreFileName() {
		return strStoreFileName;
	}
	public void setStrStoreFileName(String strStoreFileName) {
		this.strStoreFileName = strStoreFileName;
	}
	public String getVsensorName() {
		return vsensorName;
	}
	public void setVsensorName(String vsensorName) {
		this.vsensorName = vsensorName;
	}
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	public String getStrReferer() {
		return strReferer;
	}
	public void setStrReferer(String strReferer) {
		this.strReferer = strReferer;
	}

        public Long getlCode() {
            return lCode;
        }

        public void setlCode(Long lCode) {
            this.lCode = lCode;
        }

        public Long getnGrpIndex() {
            return nGrpIndex;
        }

        public void setnGrpIndex(Long nGrpIndex) {
            this.nGrpIndex = nGrpIndex;
        }

        public Integer getbSeverity() {
            return bSeverity;
        }

        public void setbSeverity(Integer bSeverity) {
            this.bSeverity = bSeverity;
        }

        public String getStrRuleName() {
            return strRuleName;
        }

        public void setStrRuleName(String strRuleName) {
            this.strRuleName = strRuleName;
        }

        public String getStrGrpName() {
            return strGrpName;
        }

        public void setStrGrpName(String strGrpName) {
            this.strGrpName = strGrpName;
        }
}
