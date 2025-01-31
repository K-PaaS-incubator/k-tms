package com.kglory.tms.web.controller.systemStatus;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.SearchDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.DbUsageVO;
import com.kglory.tms.web.model.vo.ManagerStateVO;
import com.kglory.tms.web.services.systemStatus.DbUsageService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class DbUsageController {

    private static Logger logger = LoggerFactory.getLogger(DbUsageController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DbUsageService dbUsageService;
    @Autowired
    SearchDtoValidator searchDtoValidator;

    @RequestMapping(value = "/api/systemStatus/selectDbUsageList", method = RequestMethod.POST)
    @ResponseBody
    public List<DbUsageVO> selectDbUsageList() throws BaseException {

        List<DbUsageVO> listData = null;

        // 테이블 정보 가져오는 부분 추가
        try {
            listData = dbUsageService.selectDbUsageList();
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<DbUsageVO>();
        } else {
            return listData;
        }
    }

    @RequestMapping(value = "/api/systemStatus/selectManagerStateList", method = RequestMethod.POST)
    @ResponseBody
    public List<ManagerStateVO> selectManagerStateList() throws BaseException {

        List<ManagerStateVO> listData = null;

        try {
            listData = dbUsageService.selectManagerStateList();
        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }
        
        if (listData == null) {
            return new ArrayList<ManagerStateVO>();
        } else {
            return listData;
        }
    }

    @RequestMapping(value = "api/systemStatus/selectManagerStateCpuUsedGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectManagerStateCpuUsedGraphData(@RequestBody SearchDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = dbUsageService.selectManagerStateCpuUsedGraphData(dto);
            }
        } catch (BaseException e) {
        	logger.error(e.getLocalizedMessage(), e);
        	errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
        } finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        		logger.debug("result : " + StringUtil.listObjcetToString(results));
        	}
		}

        if (results.size() == 0) {
            return new ArrayList<ChartVO>();
        } else {
            return results;
        }
    }

    @RequestMapping(value = "api/systemStatus/selectManagerStateMemUsedGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectManagerStateMemUsedGraphData(@RequestBody SearchDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = dbUsageService.selectManagerStateMemUsedGraphData(dto);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
        }
        finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        		logger.debug("result : " + StringUtil.listObjcetToString(results));
        	}
		}

        if (results.size() == 0) {
            return new ArrayList<ChartVO>();
        } else {
            return results;
        }
    }

    @RequestMapping(value = "api/systemStatus/selectManagerStateHddUsedGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectManagerStateHddUsedGraphData(@RequestBody SearchDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = dbUsageService.selectManagerStateHddUsedGraphData(dto);
            }
        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
            errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
        }
        finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        		logger.debug("result : " + StringUtil.listObjcetToString(results));
        	}
        }
        

        if (results.size() == 0) {
            return new ArrayList<ChartVO>();
        } else {
            return results;
        }
    }

    @RequestMapping(value = "api/systemStatus/selectManagerStateProcessNumGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectManagerStateProcessNumGraphData(@RequestBody SearchDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {

                results = dbUsageService.selectManagerStateProcessNumGraphData(dto);
            }
        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
            
            errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
            
        }
        finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        		logger.debug("result : " + StringUtil.listObjcetToString(results));
        	}
        }

        if (results.size() == 0) {
            return new ArrayList<ChartVO>();
        } else {
            return results;
        }
    }
}
