package com.kglory.tms.web.model.dto;

import java.util.Date;

import com.kglory.tms.web.model.CommonBean;

public class LoginStatusDto extends CommonBean {

    private Long userIndex;
    String userName;
    Date loginDate;
    boolean loginYN;
    boolean controller;
    Long category;
    Long refIndex;
    Long role;
    String pathName;
    Integer systemType;
    Integer loginType;
    Integer loginIpCount;
    Integer dualSystem;

    public Long getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(Long userIndex) {
        this.userIndex = userIndex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public boolean isLoginYN() {
        return loginYN;
    }

    public void setLoginYN(boolean loginYN) {
        this.loginYN = loginYN;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getRefIndex() {
        return refIndex;
    }

    public void setRefIndex(Long refIndex) {
        this.refIndex = refIndex;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getLoginIpCount() {
        return loginIpCount;
    }

    public void setLoginIpCount(Integer loginIpCount) {
        this.loginIpCount = loginIpCount;
    }

    public Integer getDualSystem() {
        return dualSystem;
    }

    public void setDualSystem(Integer dualSystem) {
        this.dualSystem = dualSystem;
    }

    @Override
    public String toString() {
        return "LoginStatusDto [userIndex=" + userIndex + ", userName="
                + userName + ", loginDate=" + loginDate + ", loginYN="
                + loginYN + ", controller=" + controller + ", category="
                + category + ", refIndex=" + refIndex + ", role=" + role
                + ", pathName=" + pathName + ", systemType=" + systemType
                + ", loginType=" + loginType + "]";
    }

}
