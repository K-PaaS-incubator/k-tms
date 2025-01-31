package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.util.packet.idx.DataIndex;
import com.kglory.tms.web.util.packet.idx.IcmpHeaderIndex;
import com.kglory.tms.web.util.packet.idx.Ipv4HeaderIndex;
import com.kglory.tms.web.util.packet.idx.Ipv6HeaderIndex;
import com.kglory.tms.web.util.packet.idx.MacHeaderIndex;
import com.kglory.tms.web.util.packet.idx.PatternDetectedIndex;
import com.kglory.tms.web.util.packet.idx.TcpHeaderIndex;
import com.kglory.tms.web.util.packet.idx.UdpHeaderIndex;

public class Packet {
	
	public static final short		PT_ETC				= 0;
	public static final short		PT_ICMP				= 1;
	public static final short		PT_TCP				= 2;
	public static final short		PT_UDP				= 3;
	public static final short		PT_IGMP				= 4;
	public static final short		PT_IPV6				= 5;
	
	private EthernetPacket			datalink;					// 데이터 링크
	protected byte[]				binary;						// 패킷 바이너리
	protected short					packetType			= 0;	// 패킷 유형
	private String					strData;					// 패킷을 스트링으로 파싱
	
	private MacHeaderIndex 			macHeaderIndex;
	private Ipv4HeaderIndex 		ipv4HeaderIndex;
        private Ipv6HeaderIndex                 ipv6HeaderIndex;
	private IcmpHeaderIndex			icmpHeaderIndex;
	private TcpHeaderIndex 			tcpHeaderIndex;
	private UdpHeaderIndex 			udpHeaderIndex;
	private DataIndex 				dataIndex;
	private PatternDetectedIndex	patternDetectedIndex;
	
	protected int		 			offset 				= 0;
	protected int		 			endOffset 			= 0;
	
	/**
	 * 생성자
	 * 
	 * @param binary
	 *            패킷 바이너리
	 */
	public Packet(byte[] binary) {
		this.binary = binary;
	}
	
	public EthernetPacket getDatalink() {
		return datalink;
	}
	
	public void setDatalink(EthernetPacket datalink) {
		this.datalink = datalink;
	}
	
	public byte[] getBinary() {
		return binary;
	}
	
	public short getPacketType() {
		return packetType;
	}
	
	public String getBinaryString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < binary.length; i++) {
			sb.append(String.format("%02x", 0xff & binary[i]));
		}
		
		return sb.toString();
	}
	
	public String getStrData() {
		return strData;
	}
	
	public void setStrData(String strData) {
		this.strData = strData;
	}

	public Ipv4HeaderIndex getIpv4HeaderIndex() {
		return ipv4HeaderIndex;
	}

	public void setIpv4HeaderIndex(Ipv4HeaderIndex ipv4HeaderIndex) {
		this.ipv4HeaderIndex = ipv4HeaderIndex;
	}

        public Ipv6HeaderIndex getIpv6HeaderIndex() {
            return ipv6HeaderIndex;
        }

        public void setIpv6HeaderIndex(Ipv6HeaderIndex ipv6HeaderIndex) {
            this.ipv6HeaderIndex = ipv6HeaderIndex;
        }

	public MacHeaderIndex getMacHeaderIndex() {
		return macHeaderIndex;
	}

	public void setMacHeaderIndex(MacHeaderIndex macHeaderIndex) {
		this.macHeaderIndex = macHeaderIndex;
	}

	public IcmpHeaderIndex getIcmpHeaderIndex() {
		return icmpHeaderIndex;
	}

	public void setIcmpHeaderIndex(IcmpHeaderIndex icmpHeaderIndex) {
		this.icmpHeaderIndex = icmpHeaderIndex;
	}

	public TcpHeaderIndex getTcpHeaderIndex() {
		return tcpHeaderIndex;
	}

	public void setTcpHeaderIndex(TcpHeaderIndex tcpHeaderIndex) {
		this.tcpHeaderIndex = tcpHeaderIndex;
	}

	public UdpHeaderIndex getUdpHeaderIndex() {
		return udpHeaderIndex;
	}

	public void setUdpHeaderIndex(UdpHeaderIndex udpHeaderIndex) {
		this.udpHeaderIndex = udpHeaderIndex;
	}

	public DataIndex getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(DataIndex dataIndex) {
		this.dataIndex = dataIndex;
	}

	public PatternDetectedIndex getPatternDetectedIndex() {
		return patternDetectedIndex;
	}
	public void setPatternDetectedIndex(PatternDetectedIndex patternDetectedIndex) {
		this.patternDetectedIndex = patternDetectedIndex;
	}

	public void setPacketType(short packetType) {
		this.packetType = packetType;
	}
	
}