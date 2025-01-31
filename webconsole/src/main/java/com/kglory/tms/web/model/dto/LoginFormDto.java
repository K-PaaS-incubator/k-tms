package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class LoginFormDto extends CommonBean {

    private String username;  // 사용자 아이디
    private String password;
    private String successLogin;
    private String token;
    private String sessionId;
    private Long role;
    private String loginIp;
    private String isEpki;
    private String strRequestData;
    private String strServerCert;
    private String sessionID;
    private String userDn;
    private final static int LOGIN_USER_ROLE_ADMIN = 7;

    public LoginFormDto() {
    }
    
    // 로그인 후 상태값  1:성공  -1:아이디가 틀렸을 경우  -2:패스워드가 틀렸을 경우  -3:LOCK_YN이 Y일 경우(일시 잠김) -4:계정 일시잠김상태(로그인 성공)  -5:패스워드 초기화  -6:계정 일시 정지 상태, -7:로그인 허용 IP 실패
    public enum LoginStatus {

        SUCCESS(1), ID_FAIL(-1), PWD_FAIL(-2), LOCK_FAIL(-3), LOCK_FAIL_SUCCESS(-4), LOCK_FAIL_PWDINIT(-5), USER_LOCK(-6), LOGIN_IP_FAIL(-7);
        private int value;

        private LoginStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuccessLogin() {
        return successLogin;
    }

    public void setSuccessLogin(String successLogin) {
        this.successLogin = successLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

	public String getIsEpki() {
		return isEpki;
	}

	public void setIsEpki(String isEpki) {
		this.isEpki = isEpki;
	}

	public String getStrRequestData() {
		return strRequestData;
	}

	public void setStrRequestData(String strRequestData) {
		this.strRequestData = strRequestData;
	}

	public String getStrServerCert() {
		return strServerCert;
	}

	public void setStrServerCert(String strServerCert) {
		this.strServerCert = strServerCert;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}
	public boolean isAdmin() {
		if(this.role == null) return false;
		
		if(this.role.intValue() == this.LOGIN_USER_ROLE_ADMIN) {
			return true;
		}
		else {
			return false;
		}
	}
}
