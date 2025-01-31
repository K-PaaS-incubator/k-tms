package com.kglory.tms.web.controller;

import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.model.dto.TimeDto;

@Controller
public class SystemTimeController {
	
	private static Logger	logger	= LoggerFactory.getLogger(SystemTimeController.class);
	
	@Autowired
	MessageSource				messageSource;
	
	@RequestMapping(value = "/api/currentSystemTime", method = RequestMethod.POST)
	@ResponseBody
	public TimeDto currentSystemTime(HttpSession session) {
		TimeDto timeDto = new TimeDto();
 		timeDto.setDate(Calendar.getInstance().getTime());
		return timeDto;
	}
	
}
