package com.kglory.tms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.vo.MenuVO;
import com.kglory.tms.web.services.MenuService;

@Controller
public class MenuController {
	
	private static Logger	logger	= LoggerFactory.getLogger(MenuController.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	MenuService					menuService;
	
	@RequestMapping(value = "/api/selectMenu", method = RequestMethod.POST)
	@ResponseBody
	public List<MenuVO> selectMenu(HttpSession session) {
		Long roleNo = (Long) session.getAttribute("RoleNo");
                logger.debug("menu roleNo : " + roleNo);
		List<MenuVO> commonBeans = new ArrayList<MenuVO>();
		CommonBean errorBean = new CommonBean();
		try {
			commonBeans = menuService.menu(roleNo, session);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
			commonBeans.add((MenuVO) errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
			try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				logger.error(e1.getLocalizedMessage(), e1);
				errorBean = e1.getErrorBean(errorBean);
				commonBeans.add((MenuVO) errorBean);
			}
		}
		return commonBeans;
	}
}
