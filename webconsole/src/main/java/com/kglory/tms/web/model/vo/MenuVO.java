package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class MenuVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 1682196618980559767L;

	
	private long				menuNo;
	private String				menuKey;
	private String				menuName;
	private String				menuNameEng;
	private long				displayOrder;
	private String				enabled;
	private String				upperMenuKey;
	private long				roleNo;
	private String				url;
	
	public long getMenuNo() {
		return menuNo;
	}
	
	public void setMenuNo(long menuNo) {
		this.menuNo = menuNo;
	}
	
	public String getMenuKey() {
		return menuKey;
	}
	
	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}
	
	public String getMenuName() {
		return menuName;
	}
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public String getMenuNameEng() {
		return menuNameEng;
	}
	
	public void setMenuNameEng(String menuNameEng) {
		this.menuNameEng = menuNameEng;
	}
	
	public long getDisplayOrder() {
		return displayOrder;
	}
	
	public void setDisplayOrder(long displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public String getEnabled() {
		return enabled;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	public String getUpperMenuKey() {
		return upperMenuKey;
	}
	
	public void setUpperMenuKey(String upperMenuKey) {
		this.upperMenuKey = upperMenuKey;
	}
	
	public long getRoleNo() {
		return roleNo;
	}
	
	public void setRoleNo(long roleNo) {
		this.roleNo = roleNo;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "MenuVO [menuNo=" + menuNo + ", menuKey=" + menuKey + ", menuName=" + menuName + ", menuNameEng="
				+ menuNameEng + ", displayOrder=" + displayOrder + ", enabled=" + enabled + ", upperMenuKey="
				+ upperMenuKey + ", roleNo=" + roleNo + ", url=" + url + "]";
	}
	
}
