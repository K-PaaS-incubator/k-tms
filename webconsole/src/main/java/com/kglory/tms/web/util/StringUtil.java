/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import com.kglory.tms.web.common.Constants;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author leecjong
 */
public class StringUtil {
    
    public static final String LF = StringUtils.LF;

    public static String detectionBlockPacketUsedString(long value) {
        StringBuffer sb = new StringBuffer("");
        if (value == 131073L || value == 131074L) {
            if (value == 131073L) {
                sb.append("PACKET=N");
            } else {
                sb.append("PACKET=Y");
            }
            if (Constants.getSystemMode() == 2) {
                sb.append(", BLOCK=N)");
            } else {
                sb.append(")");
            }
        } else if (value == 268566529L || value == 268566530L) {
            if (value == 268566529L) {
                sb.append("PACKET=N");
            } else {
                sb.append("PACKET=Y");
            }
            if (Constants.getSystemMode() == 2) {
                sb.append(", BLOCK=Y)");
            } else {
                sb.append(")");
            }
        }
        return sb.toString();
    }
    
    /**
     * log message
     * @param str
     * @return 
     */
    public static String logDebugMessage(String str) {
        int len = 200;
        if (str.length() >= len) {
            return left(str, len) + "...";
        } else {
            return left(str, len);
        }
    }
    
    public static String listObjcetToString(List list) {
        StringBuffer sb = new StringBuffer("");
        if (list != null && list.size() > 0) {
            sb.append("list size = " + list.size() + " [");
            sb.append(list.get(0).toString());
            sb.append(", ....]");
        } else {
            sb.append("list size = 0");
        }
        return sb.toString();
    }
    
    public static boolean isStringEqualse(String value1, String value2) {
        if ((value1 == null && value2 == null) || (value1 == null && value2.isEmpty()) || (value1 != null && value1.equals(value2))) {
            return true;
        }
        return false;
    }
    
    public static String stringNullToEmpty(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    
    public static String intNullToEmpty(Integer value) {
        if (value == null) {
            return "";
        } else {
            return String.valueOf(value);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////
    public static String left(String str, int len) {
        return StringUtils.left(str, len);
    }

    public static boolean isEmpty(CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return StringUtils.isNotEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        return StringUtils.lastIndexOf(seq, searchChar);
    }

    public static String[] split(String str, char separatorChar) {
        return StringUtils.split(str, separatorChar);
    }

    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    public static String[] split(String str, String separatorChars) {
        return StringUtils.split(str, separatorChars);
    }

    public static String remove(String str, char remove) {
        return StringUtils.remove(str, remove);
    }

    public static String base64Encode(String str) {
        try {
            return org.apache.commons.codec.binary.Base64.encodeBase64String(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
    
    public static String rightPad(String str, int size, char padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

    public static String leftPad(String str, int size, char padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }
}
