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
public class EthoNetVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -3807703621227433356L;

    private String ethoNet;
    private String ip;

    public String getEthoNet() {
        return ethoNet;
    }

    public void setEthoNet(String ethoNet) {
        this.ethoNet = ethoNet;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
