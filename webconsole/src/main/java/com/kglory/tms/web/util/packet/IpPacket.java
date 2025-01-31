package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;

/**
 * IP 패킷
 *
 * @author idess
 * @since 2012. 10. 23.
 * @version 1.0
 *
 */
public class IpPacket extends Packet {

    // offset = MAC DESC - 6byte / MAC SOURCE - 6byte / ethertype - 2byte

    short version;					// 버전					(4bit - 앞 4자리)
    short ipHdrLength;				// 헤더 크기				(4bit - 뒷 4자리)
    Ipv4Header ipv4Header;
    Ipv6Header ipv6Header;

    /**
     * 생성자
     *
     * @param binary 패킷 바이너리
     * @throws BaseException 
     */
    public IpPacket(byte[] binary) throws BaseException {
        super(binary);

        if (binary == null) {
            return;
        }

        short offset = 14;
        version = (short) ((0xff & binary[offset]) >>> 4);
        if (version == 4) {
            ipHdrLength = (short) ((binary[offset] & 0x0f) * 4);
            ipv4Header = new Ipv4Header(binary);
        } else {
            ipHdrLength = (short) 40;
            ipv6Header = new Ipv6Header(binary);
        }
    }

    @Override
    public String toString() {
        if (version == 4) {
            return this.getIpv4Header().toString();
        } else {
            return this.getIpv6Header().getString();
        }
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

    public Ipv4Header getIpv4Header() {
        return ipv4Header;
    }

    public void setIpv4Header(Ipv4Header ipv4Header) {
        this.ipv4Header = ipv4Header;
    }

    public Ipv6Header getIpv6Header() {
        return ipv6Header;
    }

    public void setIpv6Header(Ipv6Header ipv6Header) {
        this.ipv6Header = ipv6Header;
    }

}
