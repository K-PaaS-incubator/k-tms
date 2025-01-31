/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.vo.YaraRuleVo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author leecjong
 */
public class YaraRuleDto extends CommonBean {

    private Long lIndex;
    private Integer groupIndex;
    private String groupName;
    private String ruleName;
    private Long sSeverity;
    private String meta;
    private String strings;
    private String condition;
    private Integer bVendor;  //0:사용자 정의, 1:벤더 룰 
    private String insertDate;
    private String upDate;
    private Integer ruleTotal;
    private Long lvsensorIndex;
    private Integer startRow;
    private Integer endRow;
    private List<Integer> groupList = new ArrayList<>();
    private List<VirtualSensorFile> vsensorList;
    private BigInteger severityLevel;
    private String attackNameInput;
    private BigInteger attackTypeSelect;
    private Integer startRowSize;
    private Integer endRowSize;
    private Integer lUsed;
    private Integer lUsedValue;

    private List<Integer> yaraRuleGroupList;
    private List<Long> virtualSensorList;
    private List<DeployPolicyDto> deployUserPolicyList;
    private List<DeployPolicyDto> deployYaraPolicyList;
    private Boolean lUsedCheck;
    private Integer lgroupIndex;

    public Long getlIndex() {
        return lIndex;
    }

    public long setlIndex(Long lIndex) {
        return this.lIndex = lIndex;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getsSeverity() {
        return sSeverity;
    }

    public void setsSeverity(Long sSeverity) {
        this.sSeverity = sSeverity;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getStrings() {
        return strings;
    }

    public void setStrings(String strings) {
        this.strings = strings;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getbVendor() {
        return bVendor;
    }

    public void setbVendor(Integer bVendor) {
        this.bVendor = bVendor;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public Integer getRuleTotal() {
        return ruleTotal;
    }

    public void setRuleTotal(Integer ruleTotal) {
        this.ruleTotal = ruleTotal;
    }

    public Long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(Long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public List<Integer> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Integer> groupList) {
        this.groupList = groupList;
    }

    public void addGroupList(Integer groupIntex) {
        this.groupList.add(groupIntex);
    }

    public List<VirtualSensorFile> getVsensorList() {
        return vsensorList;
    }

    public void setVsensorList(List<VirtualSensorFile> vsensorList) {
        this.vsensorList = vsensorList;
    }

    public BigInteger getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(BigInteger severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getAttackNameInput() {
        return attackNameInput;
    }

    public void setAttackNameInput(String attackNameInput) {
        this.attackNameInput = attackNameInput;
    }

    public BigInteger getAttackTypeSelect() {
        return attackTypeSelect;
    }

    public void setAttackTypeSelect(BigInteger attackTypeSelect) {
        this.attackTypeSelect = attackTypeSelect;
    }

    public Integer getStartRowSize() {
        return startRowSize;
    }

    public void setStartRowSize(Integer startRowSize) {
        this.startRowSize = startRowSize;
    }

    public Integer getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(Integer endRowSize) {
        this.endRowSize = endRowSize;
    }

    public Integer getlUsed() {
        return lUsed;
    }

    public void setlUsed(Integer lUsed) {
        this.lUsed = lUsed;
    }

    public Integer getlUsedValue() {
        return lUsedValue;
    }

    public void setlUsedValue(Integer lUsedValue) {
        this.lUsedValue = lUsedValue;
    }

    public List<Integer> getYaraRuleGroupList() {
        return yaraRuleGroupList;
    }

    public void setYaraRuleGroupList(List<Integer> yaraRuleGroupList) {
        this.yaraRuleGroupList = yaraRuleGroupList;
    }

    public List<Long> getVirtualSensorList() {
        return virtualSensorList;
    }

    public void setVirtualSensorList(List<Long> virtualSensorList) {
        this.virtualSensorList = virtualSensorList;
    }

    public List<DeployPolicyDto> getDeployUserPolicyList() {
        return deployUserPolicyList;
    }

    public void setDeployUserPolicyList(List<DeployPolicyDto> deployUserPolicyList) {
        this.deployUserPolicyList = deployUserPolicyList;
    }

    public List<DeployPolicyDto> getDeployYaraPolicyList() {
        return deployYaraPolicyList;
    }

    public void setDeployYaraPolicyList(List<DeployPolicyDto> deployYaraPolicyList) {
        this.deployYaraPolicyList = deployYaraPolicyList;
    }

    public Boolean getlUsedCheck() {
        return lUsedCheck;
    }

    public void setlUsedCheck(Boolean lUsedCheck) {
        this.lUsedCheck = lUsedCheck;
    }

    public Integer getLgroupIndex() {
        return lgroupIndex;
    }

    public void setLgroupIndex(Integer lgroupIndex) {
        this.lgroupIndex = lgroupIndex;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final YaraRuleVo other = (YaraRuleVo) obj;
        if (!Objects.equals(this.groupIndex, other.getGroupIndex())) {
            return false;
        }
        if (!Objects.equals(this.ruleName, other.getRuleName())) {
            return false;
        }
        if (!Objects.equals(this.sSeverity, other.getsSeverity())) {
            return false;
        }
        if (!Objects.equals(this.meta, other.getMeta())) {
            return false;
        }
        if (!Objects.equals(this.strings, other.getStrings())) {
            return false;
        }
        if (!Objects.equals(this.condition, other.getCondition())) {
            return false;
        }
        if (!Objects.equals(this.lUsed, other.getlUsed())) {
            return false;
        }
        return true;
    }
}
