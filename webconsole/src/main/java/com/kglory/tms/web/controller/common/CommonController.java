package com.kglory.tms.web.controller.common;

import com.kglory.tms.web.common.Constants;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.FileMetaSearchDto;
import com.kglory.tms.web.model.dto.ImDbBackupDto;
import com.kglory.tms.web.model.dto.TableColumnsDto;
import com.kglory.tms.web.model.dto.TargetOrgDto;
import com.kglory.tms.web.model.vo.DualSystemVO;
import com.kglory.tms.web.model.vo.FileMetaVO;
import com.kglory.tms.web.model.vo.ImDbBackupVO;
import com.kglory.tms.web.model.vo.LocaleVO;
import com.kglory.tms.web.model.vo.TargetVO;
import com.kglory.tms.web.services.common.CommonService;
import com.kglory.tms.web.services.detectionAnalysis.FileMetaService;
import com.kglory.tms.web.services.systemSettings.ManagerService;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.SpringUtils;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.file.FileUtil;
import com.kglory.tms.web.util.security.AesUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    FileMetaService fileMetaSvc;
    @Autowired
    ManagerService managerSvc;

    @Resource(name = "downloadView")
    private View downloadView;

    @RequestMapping(value = "/api/common/selectTargetOrg", method = RequestMethod.POST)
    @ResponseBody
    public List<TargetVO> selectTargetOrg(@RequestBody TargetOrgDto dto) throws BaseException {

        List<TargetVO> orgData = new ArrayList<TargetVO>();
        // 테이블 정보 가져오는 부분 추가
        try {
            orgData = commonService.selectTargetOrg(dto);
            logger.debug(orgData.toString());
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        } 
        return orgData;
    }

    @RequestMapping(value = "/api/common/fileDownload", method = RequestMethod.GET)
    public ModelAndView fileDownload(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        ModelAndView mv = new ModelAndView();
        try {
            String type = request.getParameter("type");
            String code = request.getParameter("code");
            if (logger.isDebugEnabled()) {
                logger.debug("download type = " + type + ", code = " + code);
            }
            Long lindex = 0L;
            if (code != null && code != "") {
                lindex = Long.valueOf(code);
            }
            String filename = "";
            if (type.equals("fileMeta")) {
                String startDateInput = request.getParameter("startDateInput");
                String endDateInput = request.getParameter("endDateInput");
                if (endDateInput == null || endDateInput.isEmpty()) {
                    endDateInput = DateTimeUtil.getDateToStr(DateTimeUtil.getChangeMinute(DateTimeUtil.string2Date(startDateInput, "yyyy-MM-dd HH:mm:ss"), 1), "yyyy-MM-dd HH:mm:ss");
                }
                FileMetaSearchDto dto = new FileMetaSearchDto();
                dto.setStartDateInput(startDateInput);
                dto.setEndDateInput(endDateInput);
                dto.setlIndex(lindex);
                FileMetaVO vo = fileMetaSvc.selectFileMetaHelpPopupList(dto);
                if (vo != null && vo.getStrFileName() != null) {
                    String dateFolder = DateTimeUtil.getDateToStr(DateTimeUtil.string2Date(vo.getTmLogTime(), "yyyy-MM-dd HH:mm:ss"), "yy_MM_dd");
                    String dataFolder = FileUtil.FILE_META_FOLDER + dateFolder + "/";
                    filename = dataFolder + vo.getStrFileHash();
                }
            } else if (type.equals("dbBackupFile")) {
                ImDbBackupDto dto = new ImDbBackupDto();
                dto.setlIndex(lindex);
                ImDbBackupVO vo = managerSvc.selectDbBackupFileDetail(dto);
                filename = vo.getStrFileName();
                mv.addObject("isDisPosition", Boolean.FALSE);
            }
            File file = new File(filename);
            mv.addObject("file", file);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } 
        mv.setView(this.downloadView);
        return mv;
    }
    
    @RequestMapping(value = "/api/common/selectTableColumns", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, String> selectTableColumns(@RequestBody TableColumnsDto dto, HttpSession session) throws BaseException {
        HashMap<String, String> map = new HashMap<>();
        try {
            String userId = (String) session.getAttribute("Username");
            dto.setUserId(userId);
            map = commonService.selectTableColumns(dto);
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return map;
    }
    
    @RequestMapping(value = "/api/common/insertTableColumns", method = RequestMethod.POST)
    @ResponseBody
    public void insertTableColumns(@RequestBody List<TableColumnsDto> listDto, HttpSession session) throws BaseException {
        try {
            if (listDto != null && listDto.size() > 0) {
                String userId = (String) session.getAttribute("Username");
                TableColumnsDto dto = new TableColumnsDto();
                dto = listDto.get(0);
                dto.setUserId(userId);
                logger.debug("table : " + dto.toString());
                commonService.deleteTableColumn(dto);
                commonService.insertTableColumn(listDto, userId);
            }
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
    
    @RequestMapping(value = "/api/common/getDualSystem", method = RequestMethod.POST)
    @ResponseBody
    public DualSystemVO getDualSystem(HttpSession session) throws BaseException {
        DualSystemVO rtn = new DualSystemVO();
        rtn.setCurrentMode(Constants.getCurrentMode());
        rtn.setMasterStat(Constants.getMasterStat());
        rtn.setSlaveStat(Constants.getSlaveStat());
        rtn.setWriteMode(Constants.getWriteMode());
        return rtn;
    }
    
    @RequestMapping(value = "/api/common/getLocale", method = RequestMethod.POST)
    @ResponseBody
    public LocaleVO getLocale(HttpSession session) throws BaseException {
        LocaleVO rtn = new LocaleVO();
	    rtn.setLocale(Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry().toLowerCase());
	    rtn.setLanguage(Locale.getDefault().getLanguage());
	    rtn.setCountry(Locale.getDefault().getCountry().toLowerCase());
	    
	    logger.debug("locale : " + rtn.getLocale());
        return rtn;
    }
    
    /**
     * 제품정보 조회
     * @param session
     * @return
     * @throws BaseException 
     */
    @RequestMapping(value = "/api/sensorModel", method = RequestMethod.POST)
    @ResponseBody
    public Map getSensorModel(HttpSession session) throws BaseException {
        Map<String, String> rtn = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("cat");
        list.add("/.version");
        List<String> command = SystemUtil.execCommand(list);
        if (command != null && command.size() > 0) {
            String str = command.get(0);
            String[] arr = str.split(" ");
            if (arr != null && arr.length == 9) {
                String[] arrVersion = arr[2].split("\\.");
                String versionAdd = arr[3];
                versionAdd = versionAdd.replaceAll("[(]", "");
                versionAdd = versionAdd.replaceAll("[)]", "");
                String[] versionAddArr = versionAdd.split("/");
                String addStr = "";
                if(versionAddArr.length == 2) {
                	addStr = versionAddArr[0];
                }
                rtn.put("sensorModel", arr[0].replace("/", " ") + " " + arr[1]);
                rtn.put("productModel", arr[0].replace("/", " ") + " " + arrVersion[0] + "." + arrVersion[1] + " " + arr[1]+" "+addStr);
                rtn.put("version", arr[2]);
                rtn.put("bildDate", DateTimeUtil.date2String(DateTimeUtil.getStrToDate(arr[6].substring(0, 6), "yyMMdd"), "yyyy.MM.dd"));
            }
        }
        return rtn;
    }
    @RequestMapping(value = "/api/common/getSessionTime", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> getSessionTime(HttpSession session) throws BaseException {
    	Map<String, Integer> result = new HashMap<>();
    	try{
    		SystemConfService systemConfSvc = (SystemConfService) SpringUtils.getBean("systemConfSvc");
    		int sessionTime = systemConfSvc.getSessionTime();
    		result.put("result", sessionTime);
    		//logger.debug("locale : " + result);
    	} catch(BaseException e) {
    		logger.error(e.getLocalizedMessage(), e);
    		result = new HashMap<>();
    		result.put("result", 0);
    	}
    	return result;
    }
    @RequestMapping(value = "/api/common/invalidateSession", method = RequestMethod.POST)
    @ResponseBody
    public void invalidateSession(HttpSession session) throws BaseException {
		session.invalidate();
    }
    
    @RequestMapping(value = "/api/common/getAesKey", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getAesKey(HttpSession session) throws BaseException {
    	Map<String, String>  result = new HashMap<>();
    	
    	String key = AesUtil.getPassPhrase(session);
    	result.put("AES_SESSION_KEY", key);
    	
    	return result;
    }
    
    /**
     * 권한 테스트 Controller
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/common/test", method = RequestMethod.GET)
    @ResponseBody
    public void test(HttpSession session) throws BaseException {
		session.invalidate();
    }
}
