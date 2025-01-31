package com.kglory.tms.web.filter;

import java.util.HashMap;
import java.util.Map;

import com.kglory.tms.web.util.NumberUtil;

/**
 * 사용자 토큰 관련
 */
public class TokenObject {
	
	private static Map<String, String>	userToken;
	
	static {
		Object obj = new Object();
		synchronized (obj) {
			if (userToken == null) {
				userToken = new HashMap<String, String>();
			}
		}
	}
	
	/**
	 * 로그인 사용자의 토큰값을 얻는다.
	 * 
	 * @param String
	 *            userId
	 * @return String
	 * @throws
	 */
	public static String getToken(String userId) {
		String token = null;
		token = userToken.get(userId);
		return token;
	}
	
	/**
	 * 로그인 사용자의 토큰값을 설정한다.
	 * 
	 * @param String
	 *            userId, String token
	 * @return
	 * @throws
	 */
	public static void setToken(String userId, String token) {
		userToken.put(userId, token);
	}
	
	/**
	 * 토큰 생성
	 * 
	 * @return 생성된 토큰
	 */
	public static String makeToken(String username) {
		return username + NumberUtil.getRandomValue();
	}
	
	public static int getTokenCount() {
		int result = userToken.size();
		return result;
	}
	
}
