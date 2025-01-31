/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author leecjong
 */
public class TSCheckrules {
    private static Logger logger = LoggerFactory.getLogger(TSCheckrules.class);
    
    public String errorMessage;

    public native int  CheckRuleV2(String rule, String sigversion);
}
