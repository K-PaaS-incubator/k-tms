package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import java.util.ArrayList;
import java.util.List;

public class AccountVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 8325859627946261279L;

    private Long userIndex;
    private String id;
    private String password;
    private String groupType;
    private String name;
    private String description;
    private String company;
    private String telephone;
    private String mobile;
    private String email;
    private Integer accountStatus;
    private Long category;
    private Long refIndex;
    private Long role;
    private Long login;
    private Integer lockout; // 1:계정 잠김, 0:계정 활성
    private String stationName;
    private Integer changeYn;
    private String strName;
    private Long lIndex;
    private String strAccIndex;
    private String pathName;
    private Integer failCount;
    private String loginDate;
    private List<String> userIpList = new ArrayList<>();

    public AccountVO() {
    }

    public AccountVO(Long userIndex) {
        this.userIndex = userIndex;
    }

    public Long getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(Long userIndex) {
        this.userIndex = userIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
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

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public Integer getLockout() {
        return lockout;
    }

    public void setLockout(Integer lockout) {
        this.lockout = lockout;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getChangeYn() {
        return changeYn;
    }

    public void setChangeYn(Integer changeYn) {
        this.changeYn = changeYn;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public String getStrAccIndex() {
        return strAccIndex;
    }

    public void setStrAccIndex(String strAccIndex) {
        this.strAccIndex = strAccIndex;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public List<String> getUserIpList() {
        return userIpList;
    }

    public void setUserIpList(List<String> userIpList) {
        this.userIpList = userIpList;
    }
}
