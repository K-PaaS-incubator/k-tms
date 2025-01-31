package com.kglory.tms.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CommonBean {

    public enum ReturnType {

        success, warning, error
    };

    ReturnType returnType;
    String errorCode;
    String errorMessage;
    private String exportType;
    private Integer rowSize;

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public void setRowSize(Integer rowSize) {
        this.rowSize = rowSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    public String toMultiLineString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public String voCleanXSS(String value) {
    	if(value != null) {
    		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    		value = value.replaceAll("/cookie/gi", "cook1e");
    		value = value.replaceAll("/document/gi", "d0cument");
    		value = value.replaceAll("/script/gi", "scr1pt");
    		value = value.replaceAll("/javascript/gi", "javascr1pt");
    	}
    	return value;
    }
}
