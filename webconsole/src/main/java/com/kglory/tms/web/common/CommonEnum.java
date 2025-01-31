/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.common;

import com.kglory.tms.web.util.MessageUtil;

/**
 *
 * @author leecjong
 */
public class CommonEnum {
    
    public enum EXPORT_TYPE {
        EXCEL("excel"), WORD("word"), HTML("html");
        
        private String value;
        private EXPORT_TYPE(String value){
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
    }
    
    public enum GLOBAL_THREAT {
        SEVERITY1(1, PropertiesConf.THREAT_SEVERITY_1), SEVERITY2(2, PropertiesConf.THREAT_SEVERITY_2), SEVERITY3(3, PropertiesConf.THREAT_SEVERITY_3), 
        SEVERITY4(4, PropertiesConf.THREAT_SEVERITY_4), SEVERITY5(5, PropertiesConf.THREAT_SEVERITY_5);
        
        private int value;
        private String severityName;
        
        private GLOBAL_THREAT(int value, String severityName) {
            this.value = value;
            this.severityName = severityName;
        }
        
        private GLOBAL_THREAT(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
        
        public String getSeverityName() {
            return MessageUtil.getMessage(this.severityName);
        }
    }
    
    public static String getGlrbalThreat(int value) {
        String rtn = "";
        switch(value) {
            case 1 : rtn = GLOBAL_THREAT.SEVERITY1.getSeverityName();
                break;
            case 2 : rtn = GLOBAL_THREAT.SEVERITY2.getSeverityName();
                break;
            case 3 : rtn = GLOBAL_THREAT.SEVERITY3.getSeverityName();
                break;
            case 4 : rtn = GLOBAL_THREAT.SEVERITY4.getSeverityName();
                break;
            case 5 : rtn = GLOBAL_THREAT.SEVERITY5.getSeverityName();
                break;
            default : rtn = "";
                break;
        }
        return rtn;
    }
}
