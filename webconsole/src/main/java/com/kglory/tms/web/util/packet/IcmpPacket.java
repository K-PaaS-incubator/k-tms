package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;

/**
 * ICMP 패킷
 *
 * @author idess
 * @since 2012. 10. 23.
 * @version 1.0
 *
 */
public class IcmpPacket extends IpPacket {

    private short icmpType;		// ICMP 유형
    private String icmpTypeDesc;	// ICMP 유형 설명
    private String icmpCodeDesc;	// ICMP 코드 설명
    short icmpCode;		// ICMP 코드
    short icmpChecksum;	// ICMP 체크썸
    short icmpIdentifier;	// ICMP 식별자
    short icmpSequence;	// ICMP 시퀀스
    // short id;
    // short seqNo;

    /**
     * 생성자
     *
     * @param binary 패킷 바이너리
     * @throws BaseException 
     */
    public IcmpPacket(byte[] binary) throws BaseException {
        super(binary);
        this.packetType = PT_ICMP;
        super.offset = (short) (ipHdrLength + 14);
        parse();
    }

    private void parse() {
        icmpType = (short) (0xff & binary[offset]);
        icmpCode = (short) (0xff & binary[offset + 1]);
        if (version == 4) {
            setType(icmpType, icmpCode);
        } else if (version == 6) {
            setIpv6Description(icmpType, icmpCode);
        }
        icmpChecksum = (short) (((0xff & binary[offset + 2]) << 8) | (0xff & binary[offset + 3]));
        icmpIdentifier = (short) (((0xff & binary[offset + 4]) << 8) | (0xff & binary[offset + 5]));
        icmpSequence = (short) (((0xff & binary[offset + 6]) << 8) | (0xff & binary[offset + 7]));
    }

    void setType(short type, short code) {
        switch (type) {
            case 0:
                icmpTypeDesc = "Echo reply";
                icmpCodeDesc = "used to ping";
                break;
            case 3:
                icmpTypeDesc = "Destination unreachable";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "Destination network unreachable";
                        break;
                    case 1:
                        icmpCodeDesc = "Destination host unreachable";
                        break;
                    case 2:
                        icmpCodeDesc = "Destination protocol unreachable";
                        break;
                    case 3:
                        icmpCodeDesc = "Destination port unreachable";
                        break;
                    case 4:
                        icmpCodeDesc = "Fragmentation required, and DF flag set";
                        break;
                    case 5:
                        icmpCodeDesc = "Source route failed";
                        break;
                    case 6:
                        icmpCodeDesc = "Destination network unknown";
                        break;
                    case 7:
                        icmpCodeDesc = "Destination host unknown";
                        break;
                    case 8:
                        icmpCodeDesc = "Source host isolated";
                        break;
                    case 9:
                        icmpCodeDesc = "Network administratively prohibited";
                        break;
                    case 10:
                        icmpCodeDesc = "Host administratively prohibited";
                        break;
                    case 11:
                        icmpCodeDesc = "Network unreachable for ToS";
                        break;
                    case 12:
                        icmpCodeDesc = "Host unreachable for ToS";
                        break;
                    case 13:
                        icmpCodeDesc = "Communication administratively prohibited";
                        break;
                    case 14:
                        icmpCodeDesc = "Host Precedence Violation";
                        break;
                    case 15:
                        icmpCodeDesc = "Precedence cutoff in effect";
                        break;
                    default:
                    	icmpCodeDesc = "";
                    	break;
                }
                break;
            case 4:
                icmpTypeDesc = "Source quench";
                icmpCodeDesc = "congestion control";
                break;
            case 5:
                icmpTypeDesc = "Redirect";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "Redirect Datagram for the Network";
                        break;
                    case 1:
                        icmpCodeDesc = "Redirect Datagram for the Host";
                        break;
                    case 2:
                        icmpCodeDesc = "Redirect Datagram for the ToS & network";
                        break;
                    case 3:
                        icmpCodeDesc = "Redirect Datagram for the ToS & host";
                        break;
                    default:
                    	icmpCodeDesc = "";
                    	
                }
                break;
            case 6:
                icmpTypeDesc = "";
                icmpCodeDesc = "Alternate Host Address";
                break;
            case 8:
                icmpTypeDesc = "Echo request";
                icmpCodeDesc = "used to ping";
                break;
            case 9:
                icmpTypeDesc = "Router Advertisement";
                break;
            case 10:
                icmpTypeDesc = "Router selection";
                icmpCodeDesc = "Router discovery/selection/solicitation";
                break;
            case 11:
                icmpTypeDesc = "Time exceeded";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "TTL expired in transit";
                        break;
                    case 1:
                        icmpCodeDesc = "Fragment reassembly time exceeded";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 12:
                icmpTypeDesc = "Parameter problem";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "Pointer indicates the error";
                        break;
                    case 1:
                        icmpCodeDesc = "Missing a required option";
                        break;
                    case 2:
                        icmpCodeDesc = "Bad length";
                        break;
                }
                break;
            case 13:
                icmpTypeDesc = "Timestamp";
                break;
            case 14:
                icmpTypeDesc = "Timestamp reply";
                break;
            case 15:
                icmpTypeDesc = "Information Request";
                break;
            case 16:
                icmpTypeDesc = "Information Reply";
                break;
            case 17:
                icmpTypeDesc = "Address Mask Request";
                break;
            case 18:
                icmpTypeDesc = "Address Mask Reply";
                break;
            case 19:
                icmpCodeDesc = "Reserved for security";
                break;
            case 30:
                icmpTypeDesc = "Traceroute";
                icmpCodeDesc = "Information Request";
                break;
            case 31:
                icmpCodeDesc = "Datagram Conversion Error";
                break;
            case 32:
                icmpCodeDesc = "Mobile Host Redirect";
                break;
            case 33:
                icmpCodeDesc = "Where-Are-You (originally meant for IPv6)";
                break;
            case 34:
                icmpCodeDesc = "Here-I-Am (originally meant for IPv6)";
                break;
            case 35:
                icmpCodeDesc = "Mobile Registration Request";
                break;
            case 36:
                icmpCodeDesc = "Mobile Registration Reply";
                break;
            case 37:
                icmpCodeDesc = "Domain Name Request";
                break;
            case 38:
                icmpCodeDesc = "Domain Name Reply";
                break;
            case 39:
                icmpCodeDesc = "SKIP Algorithm Discovery Protocol, Simple Key-Management for Internet Protocol";
                break;
            case 40:
                icmpCodeDesc = "Photuris, Security failures";
                break;
            case 41:
                icmpCodeDesc = "ICMP for experimental mobility protocols such as Seamoby";
                break;
            case 253:
                icmpCodeDesc = "RFC3692-style Experiment 1";
                break;
            case 254:
                icmpCodeDesc = "RFC3692-style Experiment 2";
                break;
            default:
                icmpTypeDesc = "";
        }
        if (type >= 20 && type <= 29) {
            icmpCodeDesc = "Reserved for robustness experiment";
        }
        if ((type >= 1 && type <= 2) || type == 7 || (type >= 42 && type <= 252) || type == 255) {
            icmpCodeDesc = "Reserved";
        }
    }
    
    public void setIpv6Description(short type, short code) {
        switch (type) {
            case 1:
                icmpTypeDesc = "Destination Unreachable";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "no route to destination";
                        break;
                    case 1:
                        icmpCodeDesc = "communication with destination administratively prohibited";
                        break;
                    case 2:
                        icmpCodeDesc = "beyond scope of source address";
                        break;
                    case 3:
                        icmpCodeDesc = "address unreachable";
                        break;
                    case 4:
                        icmpCodeDesc = "port unreachable";
                        break;
                    case 5:
                        icmpCodeDesc = "source address failed ingress/egress policy";
                        break;
                    case 6:
                        icmpCodeDesc = "reject route to destination";
                        break;
                    case 7:
                        icmpCodeDesc = "Error in Source Routing Header";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 2:
                icmpTypeDesc = "Packet Too Big";
                break;
            case 3:
                icmpTypeDesc = "Time Exceeded";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "hop limit exceeded in transit";
                        break;
                    case 1:
                        icmpCodeDesc = "fragment reassembly time exceeded";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 4:
                icmpTypeDesc = "Parameter Problem";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "erroneous header field encountered";
                        break;
                    case 1:
                        icmpCodeDesc = "unrecognized Next Header type encountered";
                        break;
                    case 2:
                        icmpCodeDesc = "unrecognized IPv6 option encountered";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 100:
                icmpTypeDesc = "Private experimentation";
                break;
            case 101:
                icmpTypeDesc = "Private experimentation";
                break;
            case 127:
                icmpTypeDesc = "Reserved for expansion of ICMPv6 error messages";
                break;
            case 128:
                icmpTypeDesc = "Echo Request";
                break;
            case 129:
                icmpTypeDesc = "Echo Reply";
                break;
            case 130:
                icmpTypeDesc = "Multicast Listener Query (MLD)";
                icmpCodeDesc = "There are two subtypes of Multicast Listener Query messages:\n" +
                                "General Query, used to learn which multicast addresses have listeners on an attached link.\n" +
                                "Multicast-Address-Specific Query, used to learn if a particular multicast address has any listeners on an attached link.\n" +
                                "These two subtypes are differentiated by the contents of the Multicast Address field, as described in section 3.6 of RFC 2710";
                break;
            case 131:
                icmpTypeDesc = "Multicast Listener Report (MLD)";
                break;
            case 132:
                icmpTypeDesc = "Multicast Listener Done (MLD)";
                break;
            case 133:
                icmpTypeDesc = "Router Solicitation (NDP)";
                break;
            case 134:
                icmpTypeDesc = "Router Advertisement (NDP)";
                break;
            case 135:
                icmpTypeDesc = "Neighbor Solicitation (NDP)";
                break;
            case 136:
                icmpTypeDesc = "Neighbor Advertisement (NDP)";
                break;
            case 137:
                icmpTypeDesc = "Redirect Message (NDP)";
                break;
            case 138:
                icmpTypeDesc = "Router Renumbering";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "Router Renumbering Command";
                        break;
                    case 1:
                        icmpCodeDesc = "Router Renumbering Result";
                        break;
                    case 255:
                        icmpCodeDesc = "Sequence Number Reset";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 139:
                icmpTypeDesc = "ICMP Node Information Query";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "The Data field contains an IPv6 address which is the Subject of this Query.";
                        break;
                    case 1:
                        icmpCodeDesc = "The Data field contains a name which is the Subject of this Query, or is empty, as in the case of a NOOP.";
                        break;
                    case 2:
                        icmpCodeDesc = "The Data field contains an IPv4 address which is the Subject of this Query.";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 140:
                icmpTypeDesc = "ICMP Node Information Response";
                switch (code) {
                    case 0:
                        icmpCodeDesc = "A successful reply. The Reply Data field may or may not be empty.";
                        break;
                    case 1:
                        icmpCodeDesc = "The Responder refuses to supply the answer. The Reply Data field will be empty.";
                        break;
                    case 2:
                        icmpCodeDesc = "The Qtype of the Query is unknown to the Responder. The Reply Data field will be empty.";
                        break;
                    default:
                    	icmpCodeDesc = "";
                }
                break;
            case 141:
                icmpTypeDesc = "Inverse Neighbor Discovery Solicitation Message";
                break;
            case 142:
                icmpTypeDesc = "Inverse Neighbor Discovery Advertisement Message";
                break;
            case 143:
                icmpTypeDesc = "Multicast Listener Discovery (MLDv2) reports (RFC 3810)";
                break;
            case 144:
                icmpTypeDesc = "Home Agent Address Discovery Request Message";
                break;
            case 145:
                icmpTypeDesc = "Home Agent Address Discovery Reply Message";
                break;
            case 146:
                icmpTypeDesc = "Mobile Prefix Solicitation";
                break;
            case 147:
                icmpTypeDesc = "Mobile Prefix Advertisement";
                break;
            case 148:
                icmpTypeDesc = "Certification Path Solicitation (SEND)";
                break;
            case 149:
                icmpTypeDesc = "Certification Path Advertisement (SEND)";
                break;
            case 151:
                icmpTypeDesc = "Multicast Router Advertisement (MRD)";
                break;
            case 152:
                icmpTypeDesc = "Multicast Router Solicitation (MRD)";
                break;
            case 153:
                icmpTypeDesc = "Multicast Router Termination (MRD)";
                break;
            case 155:
                icmpTypeDesc = "RPL Control Message";
                break;
            case 200:
                icmpTypeDesc = "Private experimentation";
                break;
            case 201:
                icmpTypeDesc = "Private experimentation";
                break;
            case 255:
                icmpTypeDesc = "Reserved for expansion of ICMPv6 informational messages";
                break;
            default:
            	icmpTypeDesc = "";
        }
    }

    @Override
    public String toString() {
        return "IcmpPacket [icmpChecksum=" + icmpChecksum + ", icmpCode="
                + icmpCode + ", icmpType=" + icmpType + ", icmpTypeDesc=" + icmpTypeDesc
                + ", icmpIdentifier=" + icmpIdentifier + ", icmpSequence=" + icmpSequence + "]";
    }

    public short getIcmpType() {
        return icmpType;
    }

    public String getIcmpTypeDesc() {
        return icmpTypeDesc;
    }

    public String getIcmpCodeDesc() {
        return icmpCodeDesc;
    }

    public void setIcmpCodeDesc(String icmpCodeDesc) {
        this.icmpCodeDesc = icmpCodeDesc;
    }

    public short getIcmpCode() {
        return icmpCode;
    }

    public short getIcmpChecksum() {
        return icmpChecksum;
    }

    public short getIcmpIdentifier() {
        return icmpIdentifier;
    }

    public short getIcmpSequence() {
        return icmpSequence;
    }

}
