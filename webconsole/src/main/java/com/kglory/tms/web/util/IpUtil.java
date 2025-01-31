package com.kglory.tms.web.util;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import com.googlecode.ipv6.IPv6NetworkMask;
import com.kglory.tms.web.exception.BaseException;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP 유틸리티
 */
public class IpUtil {

    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);

    static final long MAX_IP_NUM = 4294967295L;

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN
            = Pattern.compile(
                    "^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)" + // 0-6 hex fields
                    "::"
                    + "(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$"); // 0-6 hex fields
    
    private static final char COLON_CHAR = ':';
    private static final int MAX_COLON_COUNT = 7;

    /**
     * IP를 Long Type 으로 변환한다.
     *
     * @param ipString
     * @return
     */
    public static long getHostByteOrderIpToLong(String ipString) throws NumberFormatException {
        String[] ipClass = ipString.split("\\.");
        Long ipLong = Long.valueOf(0L);
        for (int i = 0; i < ipClass.length; i++) {
            ipLong |= Long.parseLong(ipClass[i]) << 24 - (i * 8);
        }
        return ipLong;
    }
    
    /**
     * 국가정보 ENDIP 로 변환
     *
     * @param ip
     * @param areaIp
     * @return
     * @throws BaseException
     */
    public static long getHostOrderEndIp(String ip, long areaIp) throws NumberFormatException {
        return getHostByteOrderIpToLong(ip) + areaIp - 1;
    }

    /**
     * 국가정보 ENDIP 로 변환
     *
     * @param ip
     * @param areaIp
     * @return
     */
    public static long getHostOrderEndIp(long ip, long areaIp) {
        return ip + areaIp - 1;
    }

    /**
     * Host Byte Order 방식으로 IP를 Long Type 으로 변환한다.
     *
     * @param ipString
     * @return
     */
    public static long getNetworkByteOrderIpToLong(String ipString) throws BaseException {
        String[] ipClass = ipString.split("\\.");
        Long ipLong = Long.valueOf(0L);
        for (int i = 0; i < ipClass.length; i++) {
            ipLong |= Long.parseLong(ipClass[i]) << i * 8;
        }
        return ipLong;
    }

    /**
     * Long Type 을 IP로 변환한다.
     *
     * @param ipNum
     * @return
     * @throws BaseException 
     */
    public static String getHostByteOrderIpToString(long ipNum) throws BaseException {
        if (ipNum == -1L) {
            return "255.255.255.255";
        }
        
        byte[] bytes = BigInteger.valueOf(ipNum).toByteArray();
        byte[] inetAddressBytes;

        // Should be 4 (IPv4) or 16 (IPv6) bytes long
        if (bytes.length == 5 || bytes.length == 17) {
            // Remove byte with most significant bit.
            bytes = ArrayUtils.remove(bytes, 0);
        }
        if (bytes.length < 4) {
            inetAddressBytes = new byte[4];
//			int startPos = 4 - bytes.length;
            int startPos = 0;
            for (int i = 0; i < bytes.length; i++) {
                inetAddressBytes[startPos + i] = bytes[i];
            }
        } else {
            inetAddressBytes = bytes;
        }
        InetAddress address = null;
        try {
            address = InetAddress.getByAddress(inetAddressBytes);
        } catch (UnknownHostException e) {
        	throw new BaseException(e);
        }

        return address.getHostAddress();
    }

    /**
     * byte[16] to IpV6 변환
     *
     * @param bytes
     * @return
     * @throws BaseException 
     */
    public static String byteToIpv6(byte[] bytes) throws BaseException {
        InetAddress address = null;
        String rtn = null;
        try {
            address = Inet6Address.getByAddress(null, bytes);
            rtn = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }

        return rtn;
    }

    /**
     * hexstring to ipv6
     *
     * @param value
     * @return
     * @throws BaseException 
     */
    public static String hexStringToIpv6(String value) throws BaseException {
        String rtn = null;
        if (value != null && !value.isEmpty()) {
            byte[] bytes = DatatypeConverter.parseHexBinary(value);
            if (bytes != null) {
                rtn = byteToIpv6(bytes);
            }
        }
        return rtn;
    }

    /**
     * ipv6 to hexstring
     *
     * @param value
     * @return
     * @throws BaseException 
     */
    public static String strIpv6Tohex(String value) throws BaseException {
        String rtn = "";
        InetAddress inet;
		try {
			inet = InetAddress.getByName(value);
		} catch (UnknownHostException e) {
			throw new BaseException(e);
		}
        byte[] bytes = inet.getAddress();
        rtn = DatatypeConverter.printHexBinary(bytes);
        return rtn;
    }

    /**
     * Long Type 을 IP로 변환한다.
     *
     * @param ipNum
     * @return
     * @throws BaseException 
     */
    public static String getNetworkByteOrderIpToString(long ipNum) throws BaseException {
        byte[] bytes = BigInteger.valueOf(ipNum).toByteArray();
        ArrayUtils.reverse(bytes);
        byte[] inetAddressBytes;

        // Should be 4 (IPv4) or 16 (IPv6) bytes long
        if (bytes.length == 5 || bytes.length == 17) {
            // Remove byte with most significant bit.
            bytes = ArrayUtils.remove(bytes, 0);
        }

        if (bytes.length < 4) {
            inetAddressBytes = new byte[4];
//			int startPos = 4 - bytes.length;
            int startPos = 0;
            for (int i = 0; i < bytes.length; i++) {
                inetAddressBytes[startPos + i] = bytes[i];
            }
        } else {
            inetAddressBytes = bytes;
        }
        InetAddress address = null;
        try {
            address = InetAddress.getByAddress(inetAddressBytes);
        } catch (UnknownHostException e) {
        	throw new BaseException(e);
        }

        return address.getHostAddress();
    }

    /**
     * 표준형 ipv6 표현인지 여부. "2001:0db8:0000:0000:0000:0000:1428:57ab"
     *
     * @param ipString
     * @return
     */
    public static boolean isIPv6StdAddress(final String ipString) {
        String ipStr = removePrefixLength(ipString);
        return IPV6_STD_PATTERN.matcher(ipStr).matches();
    }

    /**
     * 축약형 ipv6 표현인지 여부. "2001:0db8:0:0::1428:57ab"
     *
     * @param ipString
     * @return
     */
    public static boolean isIPv6HexCompressedAddress(final String ipString) {
        int colonCount = 0;
        String ipStr = removePrefixLength(ipString);
        for (int i = 0; i < ipStr.length(); i++) {
            if (ipString.charAt(i) == COLON_CHAR) {
                colonCount++;
            }
        }
        return colonCount <= MAX_COLON_COUNT && IPV6_HEX_COMPRESSED_PATTERN.matcher(ipStr).matches();
    }

    /**
     * IPv6 표현인지 여부
     *
     * @param ipString
     * @return
     */
    public static boolean isIPv6Address(final String ipString) {
        return isIPv6StdAddress(ipString) || isIPv6HexCompressedAddress(ipString);
    }
    
    /**
     * prefix length 부분을 제거한 순수 IP 부분을 조회한다. '
     * <p>
     * ex) 2001:db8:a0b:12f0::1/64, 2001:db8:a0b:12f0::1%eth0
     * http://www.gestioip.net/docu/ipv6_address_examples.html
     *
     * @param ipString
     * @return
     */
    public static String removePrefixLength(String ipString) {
        char cidr[] = {'/', '%'};
        for (char c : cidr) {
            int idx = ipString.indexOf(c);
            if (idx != -1) {
                return ipString.substring(0, idx);
            }
        }
        return ipString;

    }

    /**
     * 파라미터가 ip 형식인지를 체크한다.
     *
     * @param ip 확인할 ip
     * @return
     */
    public static boolean isIp(String ip) {
        if (ip == null) {
            return false;
        }

        return ip.matches("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)");
    }

    /**
     * 파라미터가 ip 형식의 일부인지를 체크한다.
     *
     * @param ip 확인할 ip
     * @return
     */
    public static boolean isPartOfIp(String ip) {
        if (ip == null) {
            return false;
        }

        return ip
                .matches("([2][5][0-5]|[2][0-4]\\d|1\\d{2}|[1-9]\\d|\\d)(\\.?|\\.([2][5][0-5]|[2][0-4]\\d|1\\d{2}|[1-9]\\d|\\d)(\\.?|\\.([2][5][0-5]|[2][0-4]\\d|1\\d{2}|[1-9]\\d|\\d)(\\.?|\\.([2][5][0-5]|[2][0-4]\\d|1\\d{2}|[1-9]\\d|\\d))))");
    }
    
    /**
     * ip4/6 여부 체크 
     * ipv4 : 4, ipv6 : 6, nonIp : -1
     * @param ip
     * @return 
     */
    public static int isIp4IpV6Check(String ip) {
        int rtn = -1;
        if (ip != null) {
            if (isIp(ip)) {
                rtn = 4;
            } else if (isIPv6Address(ip)) {
                rtn = 6;
            }
        }
        return rtn;
    }

    /**
     * 파라미터가 도메인명 형식인지를 체크한다.
     *
     * @param domainName 확인할 도메인 명
     * @return
     */
    public static boolean isDomainName(String domainName) {
        if (domainName == null) {
            return false;
        }

        return domainName
                .matches("([a-z0-9]([-a-z0-9]*[a-z0-9])?\\.)+((a[cdefgilmnoqrstuwxz]|aero|arpa)|(b[abdefghijmnorstvwyz]|biz)|(c[acdfghiklmnorsuvxyz]|cat|com|coop)|d[ejkmoz]|(e[ceghrstu]|edu)|f[ijkmor]|(g[abdefghilmnpqrstuwy]|gov)|h[kmnrtu]|(i[delmnoqrst]|info|int)|(j[emop]|jobs)|k[eghimnprwyz]|l[abcikrstuvy]|(m[acdghklmnopqrstuvwxyz]|mil|mobi|museum)|(n[acefgilopruz]|name|net)|(om|org)|(p[aefghklmnrstwy]|pro)|qa|r[eouw]|s[abcdeghijklmnortvyz]|(t[cdfghjklmnoprtvwz]|travel)|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw])");
    }

    /**
     * 문자형 ip 혹은 그 일부 형식의 파라미터를 정수형 ip 범위로 반환한다.
     *
     * @param ip
     * @return
     * @throws NumberFormatException
     */
    public static long[] getIpToLongRange(String ip) throws BaseException {

        long[] ipRange = new long[2];
        if (ip == null || ip.length() == 0) {
            ipRange[0] = 0;
            ipRange[1] = MAX_IP_NUM;
        } else {
            String[] ipClass = ip.split("\\.");
            long startIp = 0;
            for (int i = 0; i < ipClass.length; i++) {
                startIp |= Long.parseLong(ipClass[i]) << 24 - (i * 8);
            }

            long endIp = 0;
            switch (ipClass.length) {
                case 1:
                    endIp |= 255L << 16;
                    break;
                case 2:
                    endIp |= 255L << 8;
                    break;
                case 3:
                    endIp |= 255L;
                    break;
                case 4:
                    endIp |= startIp;
                    break;
                default:
                // throw an exception.
            }

            ipRange[0] = startIp;
            ipRange[1] = endIp;
        }

        logger.debug(String.format("IP %s is from %d to %d.", ip, ipRange[0], ipRange[1]));

        return ipRange;
    }
    
    public static String getServerIp() throws BaseException{
        String rtn = "";
        try {
			rtn = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new BaseException(e);
		} catch (Exception e) {
			throw new BaseException(e);
		}
        return rtn;
    }
    
    public static Map<String, String> getIpv6SubnetMask(String path) throws BaseException{
        Map<String, String> rtn = new HashMap<>();
        IPv6Network net = IPv6Network.fromString(path);
        IPv6Address startaddr = net.getFirst().add(1);
        IPv6Address endaddr = net.getLast();
        rtn.put("start", startaddr.toString());
        rtn.put("end", endaddr.toString());
        return rtn;
    }
    
    public static int getIpv6RangeMask(String fromIp, String toIp) throws BaseException{
        IPv6Address from = IPv6Address.fromString(fromIp);
        IPv6Address to = IPv6Address.fromString(toIp);
        IPv6Network net = IPv6Network.fromTwoAddresses(from, to);
        IPv6NetworkMask netMask = net.getNetmask();
        int rtn = netMask.asPrefixLength();
        return rtn;
    }
}
