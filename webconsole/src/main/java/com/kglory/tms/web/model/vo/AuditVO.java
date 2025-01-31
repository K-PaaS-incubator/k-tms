package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author really486
 *
 */
public class AuditVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 1682196618980559767L;

    private Long lAuditLogIndex;
    private String tmOccur;
    private String strContent;
    private String strOperator;
    private Long lAuditSetIndex;
    private Long ltype1;
    private Long ltype2;
    private String strComment;

    private long cntAction = 0;
    private long cntError = 0;
    private long cntWarning = 0;

    private String tableName;
    private int auditType;
    private long rNum = 0;
    private long totalRowSize = 0;
    private Integer logTarget;

    public Long getlAuditLogIndex() {
        return lAuditLogIndex;
    }

    public void setlAuditLogIndex(Long lAuditLogIndex) {
        this.lAuditLogIndex = lAuditLogIndex;
    }

    public String getTmOccur() {
        return tmOccur;
    }

    public void setTmOccur(String tmOccur) {
        this.tmOccur = tmOccur;
    }

    public String getStrContent() {
        return strContent;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public String getStrOperator() {
        return strOperator;
    }

    public void setStrOperator(String strOperator) {
        this.strOperator = strOperator;
    }

    public Long getlAuditSetIndex() {
        return lAuditSetIndex;
    }

    public void setlAuditSetIndex(Long lAuditSetIndex) {
        this.lAuditSetIndex = lAuditSetIndex;
    }

    public Long getLtype1() {
        return ltype1;
    }

    public void setLtype1(Long ltype1) {
        this.ltype1 = ltype1;
    }

    public Long getLtype2() {
        return ltype2;
    }

    public void setLtype2(Long ltype2) {
        this.ltype2 = ltype2;
    }

    public String getStrComment() {
        return strComment;
    }

    public void setStrComment(String strComment) {
        this.strComment = strComment;
    }

    public long getCntAction() {
        return cntAction;
    }

    public void setCntAction(long cntAction) {
        this.cntAction = cntAction;
    }

    public long getCntError() {
        return cntError;
    }

    public void setCntError(long cntError) {
        this.cntError = cntError;
    }

    public long getCntWarning() {
        return cntWarning;
    }

    public void setCntWarning(long cntWarning) {
        this.cntWarning = cntWarning;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }

    public long getrNum() {
        return rNum;
    }

    public void setrNum(long rNum) {
        this.rNum = rNum;
    }

    public long getTotalRowSize() {
        return totalRowSize;
    }

    public void setTotalRowSize(long totalRowSize) {
        this.totalRowSize = totalRowSize;
    }

    public Integer getLogTarget() {
        return logTarget;
    }

    public void setLogTarget(Integer logTarget) {
        this.logTarget = logTarget;
    }
}
