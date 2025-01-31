/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class TableColumnsVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -1748239505058442152L;

    private String userId;
    private String menuKey;
    private String colId;
    private String enabled;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
