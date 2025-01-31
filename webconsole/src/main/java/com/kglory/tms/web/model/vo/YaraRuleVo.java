/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.security.AesUtil;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author leecjong
 */
public class YaraRuleVo extends CommonBean implements Serializable {

    private static final long serialVersionUID = 3989508529011877161L;

    private Long lIndex;
    private Integer groupIndex;
    private String groupName;
    private Long sSeverity;
    private String ruleName;
    private String meta;
    private String strings;
    private String condition;
    private Integer bVendor;  //0:사용자 정의, 1:벤더 룰 
    private String insertDate;
    private String upDate;
    private Integer ruleTotal;
    private Long lvsensorIndex;
    private String strName;
    private Integer lgroupIndex;
    private Integer lUsed;
    private BigInteger totalRowSize;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
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

    public Long getsSeverity() {
        return sSeverity;
    }

    public void setsSeverity(Long sSeverity) {
        this.sSeverity = sSeverity;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Integer getLgroupIndex() {
        return lgroupIndex;
    }

    public void setLgroupIndex(Integer lgroupIndex) {
        this.lgroupIndex = lgroupIndex;
    }

    public Integer getlUsed() {
        return lUsed;
    }

    public void setlUsed(Integer lUsed) {
        this.lUsed = lUsed;
    }

    public BigInteger getTotalRowSize() {
        return totalRowSize;
    }

    public void setTotalRowSize(BigInteger totalRowSize) {
        this.totalRowSize = totalRowSize;
    }

    @Override
    public String toString() {
        return "YaraRuleVo [lIndex=" + lIndex + ", groupIndex=" + groupIndex
                + ", groupName=" + groupName + ", sSeverity=" + sSeverity
                + ", ruleName=" + ruleName + ", meta=" + meta + ", strings="
                + strings + ", condition=" + condition + ", bVendor=" + bVendor
                + ", insertDate=" + insertDate + ", upDate=" + upDate
                + ", ruleTotal=" + ruleTotal + ", lvsensorIndex="
                + lvsensorIndex + ", strName=" + strName + ", lgroupIndex="
                + lgroupIndex + ", lUsed=" + lUsed + ", totalRowSize="
                + totalRowSize + "]";
    }
}
