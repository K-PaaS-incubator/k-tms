package com.kglory.tms.web.util.packet;


/**
 * @author idess
 * @since 2012. 10. 22.
 * @version 1.0
 * 
 */
public interface IPacketAnalyzer {
	public Packet analyzeHexStringPacket(String hexPacket, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String charset) throws Exception;
	
	public Packet analyze(String base64Bin, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String charset) throws Exception;
	
	public Packet analyze(byte[] binary, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String strData) throws Exception;
}
