package com.kglory.tms.web.util.packet;

import java.util.Arrays;

/**
 * 이더넷 패킷
 * 
 * @author idess
 * @since 2012. 10. 23.
 * @version 1.0
 * 
 */
public class EthernetPacket {
	private byte[]	binary;
	
	byte[]			srcMac;	// 소스 MAC 주소
	byte[]			dstMac;	// 대상 MAC 주소
	String			strSrcMac; // 소스 MAC 주소
	String			strDstMac; // 대상 MAC 주소
	short			frameType;	// 프레임 유형
								
	public EthernetPacket(byte[] binary) {
		this.binary = binary;
		parse();
	}
	
	private void parse() {
		dstMac = new byte[6];
		for (int i = 0; i < 6; i++) {
			dstMac[i] = binary[i];
		}
		strDstMac = String.format("%02X-%02X-%02X-%02X-%02X-%02X", dstMac[0], dstMac[1], dstMac[2], dstMac[3], dstMac[4], dstMac[5]);
		
		srcMac = new byte[6];
		for (int i = 0; i < 6; i++) {
			srcMac[i] = binary[i + 6];
		}
		strSrcMac = String.format("%02X-%02X-%02X-%02X-%02X-%02X", srcMac[0], srcMac[1], srcMac[2], srcMac[3], srcMac[4], srcMac[5]);
		
		frameType = (short) (((0xff & binary[12]) << 8) | binary[13]);
	}

	public byte[] getSrcMac() {
		return srcMac;
	}

	public void setSrcMac(byte[] srcMac) {
		this.srcMac = srcMac;
	}

	public byte[] getDstMac() {
		return dstMac;
	}

	public void setDstMac(byte[] dstMac) {
		this.dstMac = dstMac;
	}

	public String getStrSrcMac() {
		return strSrcMac;
	}

	public void setStrSrcMac(String strSrcMac) {
		this.strSrcMac = strSrcMac;
	}

	public String getStrDstMac() {
		return strDstMac;
	}

	public void setStrDstMac(String strDstMac) {
		this.strDstMac = strDstMac;
	}

	public short getFrameType() {
		return frameType;
	}

	public void setFrameType(short frameType) {
		this.frameType = frameType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EthernetPacket [binary=");
		builder.append(Arrays.toString(binary));
		builder.append(", srcMac=");
		builder.append(Arrays.toString(srcMac));
		builder.append(", dstMac=");
		builder.append(Arrays.toString(dstMac));
		builder.append(", strSrcMac=");
		builder.append(strSrcMac);
		builder.append(", strDstMac=");
		builder.append(strDstMac);
		builder.append(", frameType=");
		builder.append(frameType);
		builder.append("]");
		return builder.toString();
	}
	
	
}
