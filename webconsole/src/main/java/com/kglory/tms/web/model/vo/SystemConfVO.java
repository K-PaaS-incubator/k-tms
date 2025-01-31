/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class SystemConfVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -7387578801869954132L;

    
    private Integer idx;
    private String key;
    private String value;
    private String descp;
    
    // TSysConf 테이블에 저장되어있는 KeyNm 값
    public static enum Key {

        SESSION_TIMEOUT("session.time"),
        USER_LOCK_TIME("user.lock.time"),
        USER_LOCK_FAIL_COUNT("user.lock.fail.count"),
        SFTP_ID("sftp.id"),
        SFTP_PWD("sftp.pwd");
        private String value;

        private Key(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    };

    public SystemConfVO() {
    }

    public SystemConfVO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }
}
