/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.conf;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class TmsDatasource extends BasicDataSource{
    private static Logger log = LoggerFactory.getLogger(TmsDatasource.class);
    
    public void setEncPwd(String pwd) {
        try {
            this.setPassword(pwd);
            // pwd decription
/*
            String dePwd = pwd;
            if (!SystemUtil.isOsWindows() && StringUtil.isNotBlank(pwd)) {
                byte[] arr = pwd.getBytes("UTF-8");
                byte[] saltArr = new byte[8];
                byte[] content = new byte[arr.length - 8];
                System.arraycopy(arr, 0, saltArr, 0, 8);
                System.arraycopy(arr, 8, content, 0, arr.length - 8);
                String salt = new String(saltArr);
                List<String> list = new ArrayList<>();
                list.add("/bin/sh");
                list.add("-c");
                list.add("echo " + new String(content) + " | openssl enc -d -aes-256-cbc -a -salt -k " + salt);

                List<String> command = SystemUtil.execCommand(list);
                if (command != null && command.size() > 0) {
                    dePwd = command.get(0);
                }
            }
            this.setPassword(dePwd);
*/
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
