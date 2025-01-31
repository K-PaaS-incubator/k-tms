/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class AccountIpDto extends CommonBean implements Serializable {
    private static final long serialVersionUID = -413921736069974960L;

    private static String id;
    private static String ip;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        AccountIpDto.id = id;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        AccountIpDto.ip = ip;
    }
}
