package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.SystemUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglory.tms.web.util.packet.idx.DataIndex;
import com.kglory.tms.web.util.packet.idx.IcmpHeaderIndex;
import com.kglory.tms.web.util.packet.idx.Ipv4HeaderIndex;
import com.kglory.tms.web.util.packet.idx.Ipv6HeaderIndex;
import com.kglory.tms.web.util.packet.idx.MacHeaderIndex;
import com.kglory.tms.web.util.packet.idx.PatternDetectedIndex;
import com.kglory.tms.web.util.packet.idx.TcpHeaderIndex;
import com.kglory.tms.web.util.packet.idx.UdpHeaderIndex;
import com.kglory.tms.web.util.security.Base64;
import java.nio.ByteOrder;
import org.apache.commons.codec.DecoderException;

/**
 * 패킷 분석기
 *
 * @author idess
 * @since 2012. 10. 27.
 * @version 1.0
 *
 */
public class PacketAnalyzer implements IPacketAnalyzer {

    private static Logger logger = LoggerFactory.getLogger(PacketAnalyzer.class);

    /**
     * 패킷을 분석한다.
     *
     * @param hexPacket HEX로 인코딩된 패킷 문자열
     * @return 분석된 패킷
     * @throws BaseException
     */
    public Packet analyzeHexStringPacket(String hexPacket, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String charset) throws BaseException {
        byte[] bin = null;
        String strData = null;
        if (hexPacket != null) {
            try {
				bin = Hex.decodeHex(hexPacket.toUpperCase().toCharArray());
				strData = new String(Hex.decodeHex(hexPacket.toUpperCase().toCharArray()), charset);
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				throw new BaseException(e);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				throw new BaseException(e);
			}
        }
        return analyze(bin, dwMaliciousSrvFrame, dwMaliciousSrvByte, strData);
    }

    public static byte[] hextTobyte(String hexStr) {
        byte[] bin = null;
        if (hexStr != null) {
            try {
                bin = Hex.decodeHex(hexStr.toUpperCase().toCharArray());
            } catch (DecoderException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        return bin;
    }

    public static String applicationHexToString(String hexString, int totalSize) {
        int size = 0;
        int packetLength = 2;
        int packetType = 2;
        String a = "---------------";
        StringBuffer sb = new StringBuffer("");
        byte[] bin = hextTobyte(hexString);
        while (size < totalSize) {

            byte[] packetLengthByte = byteArrayCopy(bin, size, packetLength);

            int packetTotal = byteToInt(packetLengthByte, ByteOrder.LITTLE_ENDIAN);

            size = size + packetLength;
            byte[] packetTypeByte = byteArrayCopy(bin, size, packetLength);
            int packetTypeStr = byteToInt(packetTypeByte, ByteOrder.LITTLE_ENDIAN);
//                    String packetTypeStr = new String(packetTypeByte);

            size = size + packetType;
            byte[] packet = byteArrayCopy(bin, size, packetTotal);
            String packetStr = new String(packet);

            size = size + packetTotal;
            String type = intToType(packetTypeStr);
            if (!type.isEmpty()) {
                if (!sb.toString().equals("")) {
                    sb.append(SystemUtil.LF);
                }
                sb.append(a).append(type).append(a).append(SystemUtil.LF);
            }
            sb.append(packetStr);
        }

        return sb.toString();
    }

    public static byte[] byteArrayCopy(byte[] bin, int start, int length) {
        byte[] result = new byte[length];
        int tot = bin.length;
        if (tot >= (start + length)) {
            System.arraycopy(bin, start, result, 0, length);
        } else {
            logger.debug("ERROR byteArrayCopy : tot=" + tot + ", start=" + start + ", length=" + length);
        }

        return result;
    }

    public static byte[] byteArrayCopy(Byte[] bin, int start, int length) {
        byte[] result = new byte[length];
        if (bin != null) {
            return byteArrayCopy(bin, start, length);
        }
        return result;
    }

    public static int byteToInt(byte[] bin, ByteOrder order) {
        ByteBuffer bf = ByteBuffer.wrap(bin);
        bf.order(order);
        return bf.getShort();
    }

    public static String byteToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        String hexNumber;
        for (int i = 0; i < bytes.length; i++) {
            hexNumber = "0" + Integer.toHexString(0xff & bytes[i]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    public static String intToType(int type) {
        String rtn = "";
        if (type == 1) {
            rtn = "request";
        } else if (type == 2) {
            rtn = "response";
        }
        return rtn;
    }

    // 패킷
    public static byte[] longTo4Byte(long value) {
        byte[] rtn = new byte[4];
        for (int i = 0; i < 4; i++) {
            rtn[i] = (byte) (value >>> (i * 8));
        }
        return rtn;
    }

    /**
     * 패킷을 분석한다.
     *
     * @param base64Bin Base64 인코딩된 패킷 문자열
     * @return 분석된 패킷
     * @throws BaseException
     * @throws UnsupportedEncodingException 
     */
    public Packet analyze(String base64Bin, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String charset) throws BaseException {
        byte[] binary = Base64.decode(base64Bin);
        try {
			return analyze(binary, dwMaliciousSrvFrame, dwMaliciousSrvByte, new String(binary, charset));
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e);
		}
    }

    /**
     * 패킷을 분석한다.
     *
     * @param binary 패킷 바이너리
     * @param strData 바이너리 패킷을 String 으로 변환한 데이터
     * @return 분석된 패킷
     * @throws BaseException
     */
    public Packet analyze(byte[] binary, int dwMaliciousSrvFrame, int dwMaliciousSrvByte, String strData) throws BaseException {
        Packet packet = null;
        int offset = 0;
        short version = (short) ((0xff & binary[14]) >>> 4);
        int value = binary[23];
        if (version == 6) {
            value = binary[20];
        }
        if (binary != null) {
            switch (value) {
                case 1: // ICMP
                    packet = getIcmpPacket(binary);
                    if (version == 4) {
                        offset = ((binary[14] & 0x0f) * 4) + 22;
                    }
                    if (version == 6) {
                        offset = 54;
                    }
                    packet.setIcmpHeaderIndex(getIcmpHeaderIndex(packet.offset));
                    packet.setDataIndex(getDataIndex(offset, binary.length - 1));
                    break;
                case 6: // TCP
                    short options = getTcpPacket(binary).getOptions();
                    packet = getTcpPacket(binary);
                    if (version == 4) {
                        offset = ((binary[14] & 0x0f) * 4) + 34 + options;
                    }
                    if (version == 6) {
                        offset = 54;
                    }
                    packet.setTcpHeaderIndex(getTcpHeaderIndex(packet.offset, options));
                    packet.setDataIndex(getDataIndex(offset, binary.length - 1));

                    break;
                case 17: // UDP
                    packet = getUdpPacket(binary);
                    if (version == 4) {
                        offset = ((binary[14] & 0x0f) * 4) + 22;
                    }
                    if (version == 6) {
                        offset = 54;
                    }
                    packet.setUdpHeaderIndex(getUdpHeaderIndex(packet.offset));
                    packet.setDataIndex(getDataIndex(offset, binary.length - 1));
                    break;
                default:
                    packet = getDefaultPacket(binary);
            }
            if (version == 4) {
                packet.setIpv4HeaderIndex(getIpv4HeaderIndex());
            }
            if (version == 6) {
                packet.setIpv6HeaderIndex(getIpv6HeaderIndex());
            }
        }

        packet.setMacHeaderIndex(getMacHeaderIndex());		// MAC 헤더 정보 INDEX 		
        packet.setDatalink(getEthernetPacket(binary));		// MAC DEST, SOURCE 정보
        packet.setStrData(strData);							// 문자열로 변환
        packet.setPatternDetectedIndex(
                getPatternDetectedIndex(
                        dwMaliciousSrvFrame, dwMaliciousSrvByte)); 	// 탐지영역 

        return packet;
    }

    private Packet getPacket(byte[] binary) {
        Packet packet = new Packet(binary);
        return packet;
    }

    private EthernetPacket getEthernetPacket(byte[] binary) {
        if (binary == null) {
            return null;
        }
        return new EthernetPacket(binary);
    }

    private DefaultPacket getDefaultPacket(byte[] binary) throws BaseException {
        return new DefaultPacket(binary);
    }

    private IcmpPacket getIcmpPacket(byte[] binary) throws BaseException {
        return new IcmpPacket(binary);
    }

    private TcpPacket getTcpPacket(byte[] binary) throws BaseException {
        return new TcpPacket(binary);
    }

    private UdpPacket getUdpPacket(byte[] binary) throws BaseException {
        return new UdpPacket(binary);
    }

    private MacHeaderIndex getMacHeaderIndex() {
        return new MacHeaderIndex();
    }

    private Ipv4HeaderIndex getIpv4HeaderIndex() {
        return new Ipv4HeaderIndex();
    }

    private Ipv6HeaderIndex getIpv6HeaderIndex() {
        return new Ipv6HeaderIndex();
    }

    private IcmpHeaderIndex getIcmpHeaderIndex(int offset) {
        return new IcmpHeaderIndex(offset);
    }

    private TcpHeaderIndex getTcpHeaderIndex(int offset, short options) {
        return new TcpHeaderIndex(offset, options);
    }

    private UdpHeaderIndex getUdpHeaderIndex(int offset) {
        return new UdpHeaderIndex(offset);
    }

    private DataIndex getDataIndex(int offset, int endOffset) {
        return new DataIndex(offset, endOffset);
    }

    private PatternDetectedIndex getPatternDetectedIndex(int dwMaliciousSrvFrame, int dwMaliciousSrvByte) {
        return new PatternDetectedIndex(dwMaliciousSrvFrame, dwMaliciousSrvByte);
    }

}
