package com.kglory.tms.web.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 사용자 세션 관련
 */
public class SessionObject {
	
	private static Logger	logger	= LoggerFactory.getLogger(SessionObject.class);
	
	private static Map<String, HttpSession>	sessionMap	= new HashMap<String, HttpSession>();
	
	private static String					controller	= "";
	
	public static void removeSession(String id) {
		
		if (sessionMap.containsKey(id)) {
			sessionMap.remove(id);
		}
		
	}
	
	public static void addSession(String id, HttpSession httpSession) {
		sessionMap.put(id, httpSession);
	}
	
	public static HttpSession getSession(String id) {
		HttpSession result = sessionMap.get(id);
		return result;
	}
	
	public static Integer getSessionCount() {
		Integer result = sessionMap.size();
		return result;
	}
	
	public static Set<String> getUsers() {
		Set<String> result = new HashSet<>();
		for(String tmp : sessionMap.keySet()) {
			result.add(tmp);
		}
		return result;
	}
	
	public static String getController() {
		return controller;
	}
	
	public static void setController(String controller) {
		SessionObject.controller = controller;
	}
	
}
