package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class FileMetaVO extends CommonBean implements Serializable {
	private static final long serialVersionUID = -8786900484836972882L;

	private long 		rNum = 0;
	private long 		lIndex;
	private String 		tmLogTime;
	private String 		strMagic = "";
	private String 		deDestinationIp = "";
	private String      strDestinationIp;
	private String 		dwSourceIp = "";
	private String      strSourceIp;
	private long 		nSourcePort = 0;
	private long 		nDestinationPort = 0;
	private	long 		nProtocol;
	private Integer     bIpType;
	private long 		totalRowSize = 0;
	private BigInteger 	lvsensorIndex;
	private BigInteger 	lsensorIndex;
	private BigInteger 	lSrcNetIndex;
	private BigInteger 	lDstNetIndex;
	private String		sensorName;
	private String 		vsensorName;
	private String 		strFileName;
	private long 		dwFileSize;
	private String		strFileHash;
	private String		strHost = "";
	private String      strStoreFileName;
	private Long        lCode; //yara rule index code
	private Long        nGrpIndex; //yara rule group index
	private Integer     bSeverity; //yara rule serverity
	private String      strRuleName; //yara rule name
	private String      strGrpName; //yara rule group name
	
	public long getrNum() {
		return rNum;
	}
	public void setrNum(long rNum) {
		this.rNum = rNum;
	}
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public String getTmLogTime() {
		return tmLogTime;
	}
	public void setTmLogTime(String tmLogTime) {
		this.tmLogTime = tmLogTime;
	}
	public String getStrMagic() {
		return strMagic;
	}
	public void setStrMagic(String strMagic) {
		this.strMagic = strMagic;
	}
	public String getDeDestinationIp() {
		return deDestinationIp;
	}
	public void setDeDestinationIp(String deDestinationIp) {
		this.deDestinationIp = deDestinationIp;
	}
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	public long getnSourcePort() {
		return nSourcePort;
	}
	public void setnSourcePort(long nSourcePort) {
		this.nSourcePort = nSourcePort;
	}
	public long getnDestinationPort() {
		return nDestinationPort;
	}
	public void setnDestinationPort(long nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}
	public long getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(long nProtocol) {
		this.nProtocol = nProtocol;
	}
	public long getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(long totalRowSize) {
		this.totalRowSize = totalRowSize;
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
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	public String getVsensorName() {
		return vsensorName;
	}
	public void setVsensorName(String vsensorName) {
		this.vsensorName = vsensorName;
	}
	public String getStrFileName() {
		return strFileName;
	}
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}
	public long getDwFileSize() {
		return dwFileSize;
	}
	public void setDwFileSize(long dwFileSize) {
		this.dwFileSize = dwFileSize;
	}
	public String getStrFileHash() {
		return strFileHash;
	}
	public void setStrFileHash(String strFileHash) {
		this.strFileHash = strFileHash;
	}
	public String getStrHost() {
		return strHost;
	}
	public void setStrHost(String strHost) {
		this.strHost = strHost;
	}

        public String getStrStoreFileName() {
                return strStoreFileName;
        }

        public void setStrStoreFileName(String strStoreFileName) {
                this.strStoreFileName = strStoreFileName;
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

        public String getStrDestinationIp() {
            return strDestinationIp;
        }

        public void setStrDestinationIp(String strDestinationIp) {
            this.strDestinationIp = strDestinationIp;
        }

        public String getStrSourceIp() {
            return strSourceIp;
        }

        public void setStrSourceIp(String strSourceIp) {
            this.strSourceIp = strSourceIp;
        }

        public Integer getbIpType() {
            return bIpType;
        }

        public void setbIpType(Integer bIpType) {
            this.bIpType = bIpType;
        }
}
