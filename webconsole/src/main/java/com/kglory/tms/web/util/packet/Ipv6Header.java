/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.NumberUtil;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author leecjong
 */
public class Ipv6Header {

    private byte[] binary;
    short version;					// 버전					(4bit - 앞 4자리)
    String strTrafficClass;
    String strFlowLabel;
    String strNextHeader;
    int flowLabel;
    long payloadLength;
    int nextHeader;
    int hopLimit;
    String strSrcIp;					// 소스 IP
    String strDstIp;					// 대상 IP

    public Ipv6Header() {
    }

    public Ipv6Header(byte[] binary) throws BaseException {
        this.binary = binary;
        
        if (binary == null) {
            return;
        }

        short offset = 14;
        version = (short) ((0xff & binary[offset]) >>> 4);
        byte[] traffic = PacketAnalyzer.byteArrayCopy(binary, offset, 2);
        traffic[0] = (byte) (traffic[0] & 0x0f);
        traffic[1] = (byte) (traffic[1] & 0xf0);
        strTrafficClass = String.format("0x%02x", (byte) (traffic[0] | traffic[1]) & 0xff);
        byte[] flow = PacketAnalyzer.byteArrayCopy(binary, offset + 1, 3);
        flow[0] = (byte) (flow[0] & 0x0f);
        flowLabel = PacketAnalyzer.byteToInt(flow, ByteOrder.LITTLE_ENDIAN);
        payloadLength = NumberUtil.bytesToLong(PacketAnalyzer.byteArrayCopy(binary, offset + 4, 2));
        nextHeader = (0xff & binary[offset + 6]);
        hopLimit = (0xff & binary[offset + 7]);
        strSrcIp = IpUtil.byteToIpv6(PacketAnalyzer.byteArrayCopy(binary, offset + 8, 16));
        strDstIp = IpUtil.byteToIpv6(PacketAnalyzer.byteArrayCopy(binary, offset + 24, 16));
    }
    
    

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public String getStrTrafficClass() {
        return strTrafficClass;
    }

    public void setStrTrafficClass(String strTrafficClass) {
        this.strTrafficClass = strTrafficClass;
    }

    public String getStrFlowLabel() {
        return strFlowLabel;
    }

    public void setStrFlowLabel(String strFlowLabel) {
        this.strFlowLabel = strFlowLabel;
    }

    public String getStrNextHeader() {
        return strNextHeader;
    }

    public void setStrNextHeader(String strNextHeader) {
        this.strNextHeader = strNextHeader;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(int flowLabel) {
        this.flowLabel = flowLabel;
    }

    public long getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(long payloadLength) {
        this.payloadLength = payloadLength;
    }

    public int getNextHeader() {
        return nextHeader;
    }

    public void setNextHeader(int nextHeader) {
        this.nextHeader = nextHeader;
    }

    public int getHopLimit() {
        return hopLimit;
    }

    public void setHopLimit(int hopLimit) {
        this.hopLimit = hopLimit;
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
    
    public String getString() {
        return "IpPacket [" + "version=" + version + ", strTrafficClass=" + strTrafficClass 
                + ", strFlowLabel=" + strFlowLabel + ", strNextHeader=" + strNextHeader 
                + ", flowLabel=" + flowLabel + ", payloadLength=" + payloadLength + ", nextHeader=" + nextHeader 
                + ", hopLimit=" + hopLimit + ", strSrcIp=" + strSrcIp + ", strDstIp=" + strDstIp + ']';
    }
}
