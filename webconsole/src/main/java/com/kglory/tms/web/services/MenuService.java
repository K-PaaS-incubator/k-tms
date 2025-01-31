package com.kglory.tms.web.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.MenuMapper;
import com.kglory.tms.web.model.vo.MenuVO;

@Service
public class MenuService {
	
	private static Logger	logger = LoggerFactory.getLogger(MenuService.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	MenuMapper					menuMapper;
	
	public List<MenuVO> menu(long roleNo, HttpSession session) throws BaseException {
		ArrayList<Long> roleNoArr = new ArrayList<Long>();
		if ((roleNo & Constants.AUTH_CONTROLVIEWER) == Constants.AUTH_CONTROLVIEWER) {
			roleNoArr.add(Constants.AUTH_CONTROLVIEWER);
		}
		if ((roleNo & Constants.AUTH_CONTROLCONTROLLER) == Constants.AUTH_CONTROLCONTROLLER) {
			roleNoArr.add(Constants.AUTH_CONTROLCONTROLLER);
		}
		if ((roleNo & Constants.AUTH_CONTROLREPORT) == Constants.AUTH_CONTROLREPORT) {
			roleNoArr.add(Constants.AUTH_CONTROLREPORT);
		}
		if ((roleNo & Constants.AUTH_SUPERADMIN) == Constants.AUTH_SUPERADMIN) {
			roleNoArr.clear();
			roleNoArr.add(Constants.AUTH_CONTROLVIEWER);
			roleNoArr.add(Constants.AUTH_CONTROLCONTROLLER);
			roleNoArr.add(Constants.AUTH_CONTROLREPORT);
		}
		
		return menuMapper.selectMenu(roleNoArr);
	}
}
