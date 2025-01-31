package com.kglory.tms.web.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Number 관련 유틸
 *
 * @version 1.0
 * @author jrkang
 */
public class NumberUtil {

    private static Logger logger = LoggerFactory.getLogger(NumberUtil.class);

    /**
     * 난수값을 얻는다.
     *
     * @param
     * @return String
     * @throws
     * @author jrkang
     * @date 2014-07-17
     */
    public static String getRandomValue() {
        Random rand = null;
        String random = null;

        rand = new Random(System.currentTimeMillis());
        random = Integer.valueOf(Math.abs(rand.nextInt())).toString();

        return random;
    }

    public static long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }
    
    public static long portSignedToLong(long value) {
        return 0x0000FFFFl & value;
    }
    
    public static boolean longEquals(Long check, Long value) {
        if (check == null && value == null) {
            return true;
        } else if(check != null && value != null) {
            if (Long.valueOf(check).intValue() == Long.valueOf(value).intValue()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isStringInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    public static String intToBinaryString(int value) {
        return Integer.toBinaryString(value);
    }
    
    public static int binaryStringToInt(String value) {
        try {
            return Integer.parseInt(value, 2);
        } catch(NumberFormatException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }
}
