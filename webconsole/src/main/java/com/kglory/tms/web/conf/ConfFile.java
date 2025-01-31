/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.conf;

import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.file.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 *
 * @author leecjong
 */
public class ConfFile {
    private HashMap<String, String> prop = new HashMap<>();
    private List<String> contents;
    private String location;
    private File file;
    private String filePath = "conf/config.properties";
    public static final String SYSTEM_MODE = "system.mode";
    public static final String SYSTEM_DUAL = "system.dual";
    public static final String LOGIN_MODE = "login.mode";
    public static final String LOGIN_IP_COUNT = "login.ip.count";
    public static final String SMS_USER_ID = "sms.user.id";
    public static final String SMS_USER_PWD = "sms.user.pwd";
    public static final String SMS_SERVER_IP = "sms.server.ip";
    public static final String SMS_SERVER_PORT = "sms.server.port";
    public static final String ADMIN_ID_LIMIT = "admin.id.limit";
    public static final String LOGIN_FIRST_ID = "login.first.id";
    
    public ConfFile() {
    }

    public void setLocation(String location) throws IOException {
        this.location = SystemPropertyUtils.resolvePlaceholders(location);
        this.file = ResourceUtils.getFile(this.location);
    }

    public String getLocation() {
        return location;
    }
    
    /**
     * file path
     *
     * @return
     * @throws java.io.FileNotFoundException
     */
    public String getFilePath() throws FileNotFoundException {
        return this.file.getAbsolutePath();
    }

    public void load() throws IOException {
        File file = null;
        if(this.file != null) {
            file = this.file;
        }else {
            file = ResourceUtils.getFile(this.location);
        }
        if (!file.exists()) {
            throw new FileNotFoundException("config.properties file [" + this.location + "] not found");
        }
        load0(file.getAbsolutePath());
    }
    
    public void load(String filePath) throws IOException {
        this.filePath = filePath;
        load0(this.filePath);
    }

    private void load0(String filePath) throws IOException {

        contents = FileUtil.readLines(new File(filePath));
        prop.clear();

        Iterator<String> iter = contents.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            if (StringUtil.isBlank(item)) {
                continue;
            }

            if (item.startsWith("#")) //주석
            {
                continue;
            }

            int idx = item.indexOf('=');
            String key = item.substring(0, idx);
            String value = item.substring(idx + 1);

            prop.put(key.trim(), value.trim());

        }
    }

    public String getValue(String key) {
    	String value = prop.get(key);
        return value;
    }

    public Integer getIntValue(String key) {
        String value = prop.get(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return Integer.valueOf(value);
    }
    
    /**
     * 관리자 id 변경 제한 체크
     * @param id
     * @return 
     */
    public boolean isLimitAdmin(String id) {
        String limitIdStr = getValue(ADMIN_ID_LIMIT);
        String[] arrLimitId = limitIdStr.split(",");
        for(int i = 0 ; i < arrLimitId.length ; i++) {
            if (id.equals(arrLimitId[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 관리자 최초 로그인 체크
     * @param id
     * @return 
     */
    public boolean isAdminFirstLogin(String id) {
        String loginid = getValue(LOGIN_FIRST_ID);
        if (id.equals(loginid)) {
            return true;
        }
        return false;
    }
    
    public String toStringInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("config.properties = ").append(this.location).append(SystemUtil.LF);
        sb.append(prop.toString());

        return sb.toString();
    }
}
