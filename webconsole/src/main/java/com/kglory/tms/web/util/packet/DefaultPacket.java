/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.packet;

import com.kglory.tms.web.exception.BaseException;

/**
 *
 * @author leecjong
 */
public class DefaultPacket extends IpPacket {

    public DefaultPacket(byte[] binary) throws BaseException {
        super(binary);
    }
    
}
