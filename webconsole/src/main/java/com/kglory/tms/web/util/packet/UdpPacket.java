package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;

/**
 * UDP 패킷
 * 
 * @author idess
 * @since 2012. 10. 23.
 * @version 1.0
 * 
 */
public class UdpPacket extends IpPacket {
	int		udpSrcPort;	// 소스 포트
	int		udpDstPort;	// 대상 포트
	int		udpLength;		// 크기
	short	udpChecksum;	// 체크썸
							
	/**
	 * 생성자
	 * 
	 * @param binary
	 *            패킷 바이너리
	 * @throws BaseException 
	 */
	public UdpPacket(byte[] binary) throws BaseException {
		super(binary);
		this.packetType = PT_UDP;
		parse();
	}
	
	private void parse() {
		super.offset	= (short)ipHdrLength + 14;
		udpSrcPort 		= ((0xff & binary[offset]) << 8) | (0xff & binary[offset + 1]);
		udpDstPort 		= ((0xff & binary[offset + 2]) << 8) | (0xff & binary[offset + 3]);
		udpLength 		= ((0xff & binary[offset + 4]) << 8) | (0xff & binary[offset + 5]);
		udpChecksum 	= (short) (((0xff & binary[offset + 6]) << 8) | (0xff & binary[offset + 7]));
	}
	
	public int getUdpSrcPort() {
		return udpSrcPort;
	}
	
	public int getUdpDstPort() {
		return udpDstPort;
	}
	
	public int getUdpLength() {
		return udpLength;
	}
	
	public short getUdpChecksum() {
		return udpChecksum;
	}
	
	@Override
	public String toString() {
		return "UdpPacket [udpSrcPort=" + udpSrcPort + ", udpDstPort=" + udpDstPort 
					+ ", udpLength=" + udpLength + ", udpChecksum=" + udpChecksum + "]";
	}
}
