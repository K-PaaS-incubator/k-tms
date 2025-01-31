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
public class AccountIpVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -7934045688984668904L;

    private static String id;
    private static String ip;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        AccountIpVO.id = id;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        AccountIpVO.ip = ip;
    }
}
