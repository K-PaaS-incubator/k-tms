package com.kglory.tms.web.util;

public enum ProtocolEnum {
	HOPOPT, ICMP, IGMP, GGP, IP_in_IP, ST, TCP, CBT, EGP, IGP // 0~9
	, BBN_RCC_MON, NVP_II, PUP, ARGUS, EMCON, XNET, CHAOS, UDP, MUX, DCN_MEAS // 10~19
	, HMP, PRM, XNS_IDP, TRUNK_1, TRUNK_2, LEAF_1, LEAF_2, RDP, IRTP, ISO_TP4 // 20~29
	, NETBLT, MFE_NSP, MERIT_INP, DCCP, _3PC, IDPR, XTP, DDP, IDPR_CMTP, TPPP // 30~39
	, IL, IPv6, SDRP, Route_v, Fragv6, IDRP, RSVP, GRE, MHRP, BNA // 40~49
	, ESP, AH, I_NLSP, SWIPE, NARP, MOBILE, TLSP, SKIP, ICMPv6, NoNxtv6 // 50~59
	, Optsv6;
	
	public static ProtocolEnum at(Integer number) {
		ProtocolEnum[] values = values();
		return values[number];
	}
}
