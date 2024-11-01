package com.kglory.tms.web.controller.systemStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.vo.AuditVO;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class AuditLogController {

	@Autowired
	MessageSource		messageSource;
	@Autowired
	AuditLogService		auditLogService;
	
	private static Logger	logger	= LoggerFactory.getLogger(AuditLogController.class);
	
	@RequestMapping(value = "/api/systemStatus/selectAuditLogList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> selectAuditLogList(@RequestBody AuditDto dto, BindingResult result) {

		List<AuditVO> listData = null;

		dto.setStrContent(dto.getStrContent().replaceAll("[\\-'\"=/:;\\\\]","").replaceAll("..",""));
		dto.setStrOperator(dto.getStrOperator().replaceAll("[\\-'\"=/:;\\\\]","").replaceAll("..",""));
		List<String> excludeRegExp = Arrays.asList("..", "=", "/", "\\", "%", "?", ":", ";", "'", "\"", "<", ">", "(", ")", "-");
		try {
			for (String reqStr : excludeRegExp) {
				if (dto.getStrContent().contains(reqStr) || dto.getStrOperator().contains(reqStr)) {
					throw new BaseException();
				}
			}
			listData = auditLogService.selectAuditLogList(dto);

			//View XSS(audit, detection) 처리
			for (AuditVO tmp : listData) {
				tmp.setStrComment(tmp.voCleanXSS(tmp.getStrComment()));
				tmp.setStrContent(tmp.voCleanXSS(tmp.getStrContent()));
			}
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
			logger.debug("result : " + StringUtil.listObjcetToString(listData));
		}

		if(listData == null) {
			return new ResponseEntity<>(new ArrayList<AuditVO>(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(listData, HttpStatus.OK);
		}
	}
}

