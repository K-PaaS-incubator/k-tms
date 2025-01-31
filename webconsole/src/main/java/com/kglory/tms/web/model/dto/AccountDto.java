package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.util.NumberUtil;
import java.util.List;

import java.util.ArrayList;
import java.util.Objects;

public class AccountDto extends SearchDto {

    private long userIndex;
    private String id;
    private String name;
    private String password;
    private long groupType;
    private String description;
    private String company;
    private String telephone;
    private String mobile;
    private String email;
    private Integer accountStatus;
    private long category;
    private long refIndex;
    private long role;
    private long login;
    private Integer lockout; // 1:계정 잠김, 0:계정 활성
    private String stationName;
    private long lIndex;
    private String strName;
    private long nacIndexCount;
    private String strAccIndex;
    private Integer failCount;
    private List<AccountUserFile> userList;
    private List<String> userIpList = new ArrayList<>();

    public long getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(long userIndex) {
        this.userIndex = userIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getGroupType() {
        return groupType;
    }

    public void setGroupType(long groupType) {
        this.groupType = groupType;
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

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public long getRefIndex() {
        return refIndex;
    }

    public void setRefIndex(long refIndex) {
        this.refIndex = refIndex;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    public long getLogin() {
        return login;
    }

    public void setLogin(long login) {
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

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public long getNacIndexCount() {
        return nacIndexCount;
    }

    public void setNacIndexCount(long nacIndexCount) {
        this.nacIndexCount = nacIndexCount;
    }

    public String getStrAccIndex() {
        return strAccIndex;
    }

    public void setStrAccIndex(String strAccIndex) {
        this.strAccIndex = strAccIndex;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public List<AccountUserFile> getUserList() {
        return userList;
    }

    public void setUserList(List<AccountUserFile> userList) {
        this.userList = userList;
    }

    public List<String> getUserIpList() {
        return userIpList;
    }

    public void setUserIpList(List<String> userIpList) {
        this.userIpList = userIpList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public boolean equals(AccountVO vo) {
        if (vo == null) {
            return false;
        }
        if (!Objects.equals(this.id, vo.getId())) {
            return false;
        }
        if (!Objects.equals(this.name, vo.getName())) {
            return false;
        }
        if (!Objects.equals(this.description, vo.getDescription())) {
            return false;
        }
        if (!Objects.equals(this.company, vo.getCompany())) {
            return false;
        }
        if (!Objects.equals(this.telephone, vo.getTelephone())) {
            return false;
        }
        if (!Objects.equals(this.mobile, vo.getMobile())) {
            return false;
        }
        if (!Objects.equals(this.email, vo.getEmail())) {
            return false;
        }
        if (this.lockout != vo.getLockout()) {
            return false;
        }
        if (!NumberUtil.longEquals(this.category, vo.getCategory())) {
            return false;
        }
        if (!NumberUtil.longEquals(this.refIndex, vo.getRefIndex())) {
            return false;
        }
        if (this.password != null && !this.password.isEmpty()) {
            return false;
        }
        return true;
    }
}
