package com.kglory.tms.web.util.packet;


import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.IpUtil;

public class Ipv4Header {

    short version;					// 버전					(4bit - 앞 4자리)
    short ipHdrLength;				// 헤더 크기				(4bit - 뒷 4자리)
    short typeOfService;				// 서비스 유형				(8bit - 8자리)
    short typeOfServicePrecedence;	// 0: Normal Delay / 1: Low Delay
    short typeOfServiceDelay;			// 0: Normal Delay / 1: Low Delay
    short typeOfServiceThrt;			// 0: Normal Throughput / 1: High Throughput
    short typeOfServiceRelty;			// 0: Normal Relibility / 1: High Relibility
    short typeOfServiceFuture;		// Reserved for Future Use
    int ipLength;					// 크기					(16bit - 16자리)
    int identification; 			// 식별자					(16bit - 16자리)
    short ipFlags;					// 플래그					(3bit - 3자리)
    boolean moreFragFlag;				// More Fragments 플래그
    boolean dontFragFlag;				// Don't Fragment 플래그
    short fragOffset;					// Fragment Offset		(13bit - 13자리)
    short ipTtl;						// TTL					(8bit - 8자리)
    short ipProtocol;					// 프로토콜				(8bit - 8자리)
    short ipChecksum;					// 체크썸					(16bit - 16자리)
    long srcIp;						// 소스 IP				(32bit - 32자리)
    long dstIp;						// 대상 IP				(32bit - 32자리)
    String strSrcIp;					// 소스 IP
    String strDstIp;					// 대상 IP

    public Ipv4Header(byte[] binary) throws BaseException {
        if (binary == null) {
            return;
        }

        short offset = 14;
        version = (short) ((0xff & binary[offset]) >>> 4);
        ipHdrLength = (short) ((binary[offset] & 0x0f) * 4);
        typeOfService = (short) (0xff & binary[offset + 1]);
        typeOfServicePrecedence = (short) (0xff & (binary[offset + 1]) >>> 5);				// idx : 0-2
        typeOfServiceDelay = (short) ((binary[offset + 1] & 0x10) >>> 4);					// 0001 0000
        typeOfServiceThrt = (short) ((binary[offset + 1] & 0x08) >>> 3);					// 0000 1000
        typeOfServiceRelty = (short) ((binary[offset + 1] & 0x04) >> 2);					// 0000 0100
        typeOfServiceFuture = (short) (binary[offset + 1] & 0x03);							// 0000 0011
        ipLength = (0xff & binary[offset + 2]) << 8 | (0xff & binary[offset + 3]);
        identification = ((0xff & binary[offset + 4]) << 8) | (0xff & binary[offset + 5]);
        ipFlags = (short) ((binary[offset + 6] & 0x60) >>> 4);								// 0x60 - 0110 0000 
        dontFragFlag = (binary[offset + 6] & 0x40) != 0;									// 0x40 - 0100 0000 ( 0이 아니면 단편화 아님 )
        moreFragFlag = (binary[offset + 6] & 0x20) != 0;									// 0x20 - 0010 0000 ( 0 마지막 조각/ 1 전송될 조각 더 있음 )
        fragOffset = (short) ((binary[offset + 6] & 0x1f) << 8 | (0xff & binary[offset + 7]));
        ipTtl = (short) (0xff & binary[offset + 8]);
        ipProtocol = (short) (0xff & binary[offset + 9]);
        ipChecksum = (short) ((0xff & binary[offset + 10]) << 8 | (0xff & binary[offset + 11]));
        srcIp = ((0xffL & binary[offset + 12]) << 24) | ((0xffL & binary[offset + 13]) << 16)
                | ((0xffL & binary[offset + 14]) << 8) | (0xffL & binary[offset + 15]);
        strSrcIp = IpUtil.getHostByteOrderIpToString(srcIp);
        dstIp = ((0xffL & binary[offset + 16]) << 24) | ((0xffL & binary[offset + 17]) << 16)
                | ((0xffL & binary[offset + 18]) << 8) | (0xffL & binary[offset + 19]);
        strDstIp = IpUtil.getHostByteOrderIpToString(dstIp);
    }

    public String getString() {
        return "IpPacket [version=" + version + ", ipHdrLength=" + ipHdrLength
                + ", typeOfService=" + typeOfService + ", typeOfServicePrecedence=" + typeOfServicePrecedence
                + ", typeOfServiceDelay=" + typeOfServiceDelay + ", typeOfServiceThrt=" + typeOfServiceThrt
                + ", typeOfServiceRelty=" + typeOfServiceRelty + ", typeOfServiceFuture=" + typeOfServiceFuture
                + ", ipLength=" + ipLength + ", identification=" + identification
                + ", ipFlags=" + ipFlags + ", dontFragFlag=" + dontFragFlag + ", moreFragFlag=" + moreFragFlag
                + ", fragOffset=" + fragOffset + ", ipTtl=" + ipTtl + ", ipProtocol=" + ipProtocol
                + ", ipChecksum=" + ipChecksum + ", srcIp=" + srcIp + ", strSrcIp=" + strSrcIp
                + ", dstIp=" + dstIp + ", strDstIp=" + strDstIp + "]";
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public short getIpHdrLength() {
        return ipHdrLength;
    }

    public void setIpHdrLength(short ipHdrLength) {
        this.ipHdrLength = ipHdrLength;
    }

    public short getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(short typeOfService) {
        this.typeOfService = typeOfService;
    }

    public int getIpLength() {
        return ipLength;
    }

    public void setIpLength(int ipLength) {
        this.ipLength = ipLength;
    }

    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    public short getIpFlags() {
        return ipFlags;
    }

    public void setIpFlags(short ipFlags) {
        this.ipFlags = ipFlags;
    }

    public boolean isMoreFragFlag() {
        return moreFragFlag;
    }

    public void setMoreFragFlag(boolean moreFragFlag) {
        this.moreFragFlag = moreFragFlag;
    }

    public boolean isDontFragFlag() {
        return dontFragFlag;
    }

    public void setDontFragFlag(boolean dontFragFlag) {
        this.dontFragFlag = dontFragFlag;
    }

    public short getFragOffset() {
        return fragOffset;
    }

    public void setFragOffset(short fragOffset) {
        this.fragOffset = fragOffset;
    }

    public short getIpTtl() {
        return ipTtl;
    }

    public void setIpTtl(short ipTtl) {
        this.ipTtl = ipTtl;
    }

    public short getIpProtocol() {
        return ipProtocol;
    }

    public void setIpProtocol(short ipProtocol) {
        this.ipProtocol = ipProtocol;
    }

    public short getIpChecksum() {
        return ipChecksum;
    }

    public void setIpChecksum(short ipChecksum) {
        this.ipChecksum = ipChecksum;
    }

    public long getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(long srcIp) throws BaseException {
        this.strSrcIp = IpUtil.getHostByteOrderIpToString(srcIp);
        this.srcIp = srcIp;
    }

    public long getDstIp() {
        return dstIp;
    }

    public void setDstIp(long dstIp) throws BaseException {
        this.strDstIp = IpUtil.getHostByteOrderIpToString(dstIp);
        this.dstIp = dstIp;
    }

    public String getStrSrcIp() {
        return strSrcIp;
    }

    public void setStrSrcIp(String strSrcIp) {
        this.strSrcIp = strSrcIp;
    }

    public String getStrDstIp() {
        return strDstIp;
    }

    public void setStrDstIp(String strDstIp) {
        this.strDstIp = strDstIp;
    }

    public short getTypeOfServicePrecedence() {
        return typeOfServicePrecedence;
    }

    public void setTypeOfServicePrecedence(short typeOfServicePrecedence) {
        this.typeOfServicePrecedence = typeOfServicePrecedence;
    }

    public short getTypeOfServiceDelay() {
        return typeOfServiceDelay;
    }

    public void setTypeOfServiceDelay(short typeOfServiceDelay) {
        this.typeOfServiceDelay = typeOfServiceDelay;
    }

    public short getTypeOfServiceThrt() {
        return typeOfServiceThrt;
    }

    public void setTypeOfServiceThrt(short typeOfServiceThrt) {
        this.typeOfServiceThrt = typeOfServiceThrt;
    }

    public short getTypeOfServiceRelty() {
        return typeOfServiceRelty;
    }

    public void setTypeOfServiceRelty(short typeOfServiceRelty) {
        this.typeOfServiceRelty = typeOfServiceRelty;
    }

    public short getTypeOfServiceFuture() {
        return typeOfServiceFuture;
    }

    public void setTypeOfServiceFuture(short typeOfServiceFuture) {
        this.typeOfServiceFuture = typeOfServiceFuture;
    }
}
