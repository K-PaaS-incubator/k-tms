package com.kglory.tms.web.model.vo;

import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class DetectionEventVO extends CommonBean {
	private BigInteger	lIndex;
	private BigInteger	lCode;
	private String		tmStart;
	private String		tmEnd;
	private String		strStartTime;
	private String		dwSourceIp;
	private String		deDestinationIp;
	private Integer		nProtocol;
	private String		bType;
	private String		strTitle;
	private BigInteger	lvsensorIndex;
	private String		vsensorName;
	private String		strSourceMac;
	private String		strDestinationMac;
	private Integer		nSourcePort;
	private Integer		nDestinationPort;
	private Long		dwPacketCounter;
	private Long		deSrcPortCounter;
	private Long		dwDstPortCounter;
	private Long		dwSrcIpCounter;
	private Long		dwDstIpCounter;
	private Byte		bSeverity;
	private Integer		nTtl;
	private Long		lAlertResponse1;
	private Long		lResetResponse1;
	private Long		lEmailResponse1;
	private Long		lSnmpResponse1;
	private Long		lIwResponse1;
	private Long		lIwResponse2;
	private Long		lFwResponse1;
	private Long		lFwResponse2;
	private Long		dwEventNum;
	private Integer		wInbound;
	private Integer		ucCreateLogType;
	private Integer		wVlanInfo;
	private Long		dwPktSize;
	private Long		dwMaliciousSrvFrame;
	private Long		dwMaliciousCliFrame;
	private Long		dwMaliciousSrvByte;
	private Long		dwMaliciousCliByte;
	private Integer		ucIntrusionDir;
	private Integer		ucAccessDir;
	private BigInteger	lSrcNetIndex;
	private String		srcNetworkName;
	private BigInteger	lDstNetIndex;
    private Integer         lNetGroupIndex;
    private Integer         srclNetGroupIndex;
    private Integer         desclNetGroupIndex;
	private String		dstNetworkName;
	private BigInteger	lSrcUserIndex;
	private BigInteger	lDstUserIndex;
	private BigInteger	lUrlIndex;
	private String		strSrcNationIso;
	private String		strDestNationIso;
	private BigInteger	lSensorIndex;
	private String		sensorName;
	private String		tmDbTime;
	private String		srcService;
	private String		dstService;
	private String		rawdata;
    private String          strSrcCategory;
    private Integer         nSrcScore;
    private String          strDestCategory;
    private Integer         nDestScore;
	
	public BigInteger getlIndex() {
		return lIndex;
	}
	
	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}
	
	public BigInteger getlCode() {
		return lCode;
	}
	
	public void setlCode(BigInteger lCode) {
		this.lCode = lCode;
	}
	
	public String getTmStart() {
		return tmStart;
	}
	
	public void setTmStart(String tmStart) {
		this.tmStart = tmStart;
	}
	
	public String getTmEnd() {
		return tmEnd;
	}
	
	public void setTmEnd(String tmEnd) {
		this.tmEnd = tmEnd;
	}
	
	public String getStrStartTime() {
		return strStartTime;
	}
	
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	
	public String getDeDestinationIp() {
		return deDestinationIp;
	}
	
	public void setDeDestinationIp(String deDestinationIp) {
		this.deDestinationIp = deDestinationIp;
	}
	
	public Integer getnProtocol() {
		return nProtocol;
	}
	
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	
	public String getbType() {
		return bType;
	}
	
	public void setbType(String bType) {
		this.bType = bType;
	}
	
	public String getStrTitle() {
		return strTitle;
	}
	
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	
	public BigInteger getLvsensorIndex() {
		return lvsensorIndex;
	}

	public void setLvsensorIndex(BigInteger lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}

	public String getVsensorName() {
		return vsensorName;
	}
	
	public void setVsensorName(String vsensorName) {
		this.vsensorName = vsensorName;
	}
	
	public String getStrSourceMac() {
		return strSourceMac;
	}
	
	public void setStrSourceMac(String strSourceMac) {
		this.strSourceMac = strSourceMac;
	}
	
	public String getStrDestinationMac() {
		return strDestinationMac;
	}
	
	public void setStrDestinationMac(String strDestinationMac) {
		this.strDestinationMac = strDestinationMac;
	}
	
	public Integer getnSourcePort() {
		return nSourcePort;
	}
	
	public void setnSourcePort(Integer nSourcePort) {
		this.nSourcePort = nSourcePort;
	}
	
	public Integer getnDestinationPort() {
		return nDestinationPort;
	}
	
	public void setnDestinationPort(Integer nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}
	
	public Long getDwPacketCounter() {
		return dwPacketCounter;
	}
	
	public void setDwPacketCounter(Long dwPacketCounter) {
		this.dwPacketCounter = dwPacketCounter;
	}
	
	public Long getDeSrcPortCounter() {
		return deSrcPortCounter;
	}
	
	public void setDeSrcPortCounter(Long deSrcPortCounter) {
		this.deSrcPortCounter = deSrcPortCounter;
	}
	
	public Long getDwDstPortCounter() {
		return dwDstPortCounter;
	}
	
	public void setDwDstPortCounter(Long dwDstPortCounter) {
		this.dwDstPortCounter = dwDstPortCounter;
	}
	
	public Long getDwSrcIpCounter() {
		return dwSrcIpCounter;
	}
	
	public void setDwSrcIpCounter(Long dwSrcIpCounter) {
		this.dwSrcIpCounter = dwSrcIpCounter;
	}
	
	public Long getDwDstIpCounter() {
		return dwDstIpCounter;
	}
	
	public void setDwDstIpCounter(Long dwDstIpCounter) {
		this.dwDstIpCounter = dwDstIpCounter;
	}
	
	public Byte getbSeverity() {
		return bSeverity;
	}
	
	public void setbSeverity(Byte bSeverity) {
		this.bSeverity = bSeverity;
	}
	
	public Integer getnTtl() {
		return nTtl;
	}
	
	public void setnTtl(Integer nTtl) {
		this.nTtl = nTtl;
	}
	
	public Long getlAlertResponse1() {
		return lAlertResponse1;
	}
	
	public void setlAlertResponse1(Long lAlertResponse1) {
		this.lAlertResponse1 = lAlertResponse1;
	}
	
	public Long getlResetResponse1() {
		return lResetResponse1;
	}
	
	public void setlResetResponse1(Long lResetResponse1) {
		this.lResetResponse1 = lResetResponse1;
	}
	
	public Long getlEmailResponse1() {
		return lEmailResponse1;
	}
	
	public void setlEmailResponse1(Long lEmailResponse1) {
		this.lEmailResponse1 = lEmailResponse1;
	}
	
	public Long getlSnmpResponse1() {
		return lSnmpResponse1;
	}
	
	public void setlSnmpResponse1(Long lSnmpResponse1) {
		this.lSnmpResponse1 = lSnmpResponse1;
	}
	
	public Long getlIwResponse1() {
		return lIwResponse1;
	}
	
	public void setlIwResponse1(Long lIwResponse1) {
		this.lIwResponse1 = lIwResponse1;
	}
	
	public Long getlIwResponse2() {
		return lIwResponse2;
	}

	public void setlIwResponse2(Long lIwResponse2) {
		this.lIwResponse2 = lIwResponse2;
	}

	public Long getlFwResponse1() {
		return lFwResponse1;
	}
	
	public void setlFwResponse1(Long lFwResponse1) {
		this.lFwResponse1 = lFwResponse1;
	}
	
	public Long getlFwResponse2() {
		return lFwResponse2;
	}
	
	public void setlFwResponse2(Long lFwResponse2) {
		this.lFwResponse2 = lFwResponse2;
	}
	
	public Long getDwEventNum() {
		return dwEventNum;
	}
	
	public void setDwEventNum(Long dwEventNum) {
		this.dwEventNum = dwEventNum;
	}
	
	public Integer getwInbound() {
		return wInbound;
	}
	
	public void setwInbound(Integer wInbound) {
		this.wInbound = wInbound;
	}
	
	public Integer getUcCreateLogType() {
		return ucCreateLogType;
	}
	
	public void setUcCreateLogType(Integer ucCreateLogType) {
		this.ucCreateLogType = ucCreateLogType;
	}
	
	public Integer getwVlanInfo() {
		return wVlanInfo;
	}
	
	public void setwVlanInfo(Integer wVlanInfo) {
		this.wVlanInfo = wVlanInfo;
	}
	
	public Long getDwPktSize() {
		return dwPktSize;
	}
	
	public void setDwPktSize(Long dwPktSize) {
		this.dwPktSize = dwPktSize;
	}
	
	public Long getDwMaliciousSrvFrame() {
		return dwMaliciousSrvFrame;
	}
	
	public void setDwMaliciousSrvFrame(Long dwMaliciousSrvFrame) {
		this.dwMaliciousSrvFrame = dwMaliciousSrvFrame;
	}
	
	public Long getDwMaliciousCliFrame() {
		return dwMaliciousCliFrame;
	}
	
	public void setDwMaliciousCliFrame(Long dwMaliciousCliFrame) {
		this.dwMaliciousCliFrame = dwMaliciousCliFrame;
	}
	
	public Long getDwMaliciousSrvByte() {
		return dwMaliciousSrvByte;
	}
	
	public void setDwMaliciousSrvByte(Long dwMaliciousSrvByte) {
		this.dwMaliciousSrvByte = dwMaliciousSrvByte;
	}
	
	public Long getDwMaliciousCliByte() {
		return dwMaliciousCliByte;
	}
	
	public void setDwMaliciousCliByte(Long dwMaliciousCliByte) {
		this.dwMaliciousCliByte = dwMaliciousCliByte;
	}
	
	public Integer getUcIntrusionDir() {
		return ucIntrusionDir;
	}
	
	public void setUcIntrusionDir(Integer ucIntrusionDir) {
		this.ucIntrusionDir = ucIntrusionDir;
	}
	
	public Integer getUcAccessDir() {
		return ucAccessDir;
	}
	
	public void setUcAccessDir(Integer ucAccessDir) {
		this.ucAccessDir = ucAccessDir;
	}
	
	public BigInteger getlSrcNetIndex() {
		return lSrcNetIndex;
	}
	
	public void setlSrcNetIndex(BigInteger lSrcNetIndex) {
		this.lSrcNetIndex = lSrcNetIndex;
	}
	
	public String getSrcNetworkName() {
		return srcNetworkName;
	}
	
	public void setSrcNetworkName(String srcNetworkName) {
		this.srcNetworkName = srcNetworkName;
	}
	
	public BigInteger getlDstNetIndex() {
		return lDstNetIndex;
	}
	
	public void setlDstNetIndex(BigInteger lDstNetIndex) {
		this.lDstNetIndex = lDstNetIndex;
	}
	
	public String getDstNetworkName() {
		return dstNetworkName;
	}
	
	public void setDstNetworkName(String dstNetworkName) {
		this.dstNetworkName = dstNetworkName;
	}
	
	public BigInteger getlSrcUserIndex() {
		return lSrcUserIndex;
	}
	
	public void setlSrcUserIndex(BigInteger lSrcUserIndex) {
		this.lSrcUserIndex = lSrcUserIndex;
	}
	
	public BigInteger getlDstUserIndex() {
		return lDstUserIndex;
	}
	
	public void setlDstUserIndex(BigInteger lDstUserIndex) {
		this.lDstUserIndex = lDstUserIndex;
	}
	
	public BigInteger getlUrlIndex() {
		return lUrlIndex;
	}
	
	public void setlUrlIndex(BigInteger lUrlIndex) {
		this.lUrlIndex = lUrlIndex;
	}
	
	public String getStrSrcNationIso() {
		return strSrcNationIso;
	}
	
	public void setStrSrcNationIso(String strSrcNationIso) {
		this.strSrcNationIso = strSrcNationIso;
	}
	
	public String getStrDestNationIso() {
		return strDestNationIso;
	}
	
	public void setStrDestNationIso(String strDestNationIso) {
		this.strDestNationIso = strDestNationIso;
	}
	
	public BigInteger getlSensorIndex() {
		return lSensorIndex;
	}
	
	public void setlSensorIndex(BigInteger lSensorIndex) {
		this.lSensorIndex = lSensorIndex;
	}
	
	public String getSensorName() {
		return sensorName;
	}
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	public String getTmDbTime() {
		return tmDbTime;
	}
	
	public void setTmDbTime(String tmDbTime) {
		this.tmDbTime = tmDbTime;
	}
	
	public String getSrcService() {
		return srcService;
	}

	public void setSrcService(String srcService) {
		this.srcService = srcService;
	}

	public String getDstService() {
		return dstService;
	}

	public void setDstService(String dstService) {
		this.dstService = dstService;
	}

        public String getRawdata() {
            return rawdata;
        }

        public void setRawdata(String rawdata) {
            this.rawdata = rawdata;
        }

        public Integer getlNetGroupIndex() {
            return lNetGroupIndex;
        }

        public void setlNetGroupIndex(Integer lNetGroupIndex) {
            this.lNetGroupIndex = lNetGroupIndex;
        }

        public Integer getSrclNetGroupIndex() {
            return srclNetGroupIndex;
        }

        public void setSrclNetGroupIndex(Integer srclNetGroupIndex) {
            this.srclNetGroupIndex = srclNetGroupIndex;
        }

        public Integer getDesclNetGroupIndex() {
            return desclNetGroupIndex;
        }

        public void setDesclNetGroupIndex(Integer desclNetGroupIndex) {
            this.desclNetGroupIndex = desclNetGroupIndex;
        }

    public String getStrSrcCategory() {
        return strSrcCategory;
    }

    public void setStrSrcCategory(String strSrcCategory) {
        this.strSrcCategory = strSrcCategory;
    }

    public Integer getnSrcScore() {
        return nSrcScore;
    }

    public void setnSrcScore(Integer nSrcScore) {
        this.nSrcScore = nSrcScore;
    }

    public String getStrDestCategory() {
        return strDestCategory;
    }

    public void setStrDestCategory(String strDestCategory) {
        this.strDestCategory = strDestCategory;
    }

    public Integer getnDestScore() {
        return nDestScore;
    }

    public void setnDestScore(Integer nDestScore) {
        this.nDestScore = nDestScore;
    }

	@Override
	public String toString() {
		return "DetectionEventVO [lIndex=" + lIndex + ", lCode=" + lCode
				+ ", tmStart=" + tmStart + ", tmEnd=" + tmEnd
				+ ", strStartTime=" + strStartTime + ", dwSourceIp="
				+ dwSourceIp + ", deDestinationIp=" + deDestinationIp
				+ ", nProtocol=" + nProtocol + ", bType=" + bType
				+ ", strTitle=" + strTitle + ", lvsensorIndex=" + lvsensorIndex
				+ ", vsensorName=" + vsensorName + ", strSourceMac="
				+ strSourceMac + ", strDestinationMac=" + strDestinationMac
				+ ", nSourcePort=" + nSourcePort + ", nDestinationPort="
				+ nDestinationPort + ", dwPacketCounter=" + dwPacketCounter
				+ ", deSrcPortCounter=" + deSrcPortCounter
				+ ", dwDstPortCounter=" + dwDstPortCounter
				+ ", dwSrcIpCounter=" + dwSrcIpCounter + ", dwDstIpCounter="
				+ dwDstIpCounter + ", bSeverity=" + bSeverity + ", nTtl="
				+ nTtl + ", lAlertResponse1=" + lAlertResponse1
				+ ", lResetResponse1=" + lResetResponse1 + ", lEmailResponse1="
				+ lEmailResponse1 + ", lSnmpResponse1=" + lSnmpResponse1
				+ ", lIwResponse1=" + lIwResponse1 + ", LIwResponse2="
				+ lIwResponse2 + ", lFwResponse1=" + lFwResponse1
				+ ", lFwResponse2=" + lFwResponse2 + ", dwEventNum="
				+ dwEventNum + ", wInbound=" + wInbound + ", ucCreateLogType="
				+ ucCreateLogType + ", wVlanInfo=" + wVlanInfo + ", dwPktSize="
				+ dwPktSize + ", dwMaliciousSrvFrame=" + dwMaliciousSrvFrame
				+ ", dwMaliciousCliFrame=" + dwMaliciousCliFrame
				+ ", dwMaliciousSrvByte=" + dwMaliciousSrvByte
				+ ", dwMaliciousCliByte=" + dwMaliciousCliByte
				+ ", ucIntrusionDir=" + ucIntrusionDir + ", ucAccessDir="
				+ ucAccessDir + ", lSrcNetIndex=" + lSrcNetIndex
				+ ", srcNetworkName=" + srcNetworkName + ", lDstNetIndex="
				+ lDstNetIndex + ", dstNetworkName=" + dstNetworkName
				+ ", lSrcUserIndex=" + lSrcUserIndex + ", lDstUserIndex="
				+ lDstUserIndex + ", lUrlIndex=" + lUrlIndex
				+ ", strSrcNationIso=" + strSrcNationIso
				+ ", strDestNationIso=" + strDestNationIso + ", lSensorIndex="
				+ lSensorIndex + ", sensorName=" + sensorName + ", tmDbTime="
				+ tmDbTime + ", srcService=" + srcService + ", dstService="
				+ dstService + "]";
	}
	
}
