package com.kglory.tms.web.controller.systemSettings;

import com.kglory.tms.web.common.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.kglory.tms.web.model.dto.SensorInboundDto;
import com.kglory.tms.web.model.dto.SensorIntegrityFileDto;
import com.kglory.tms.web.model.dto.SystemConfigDto;
import com.kglory.tms.web.model.vo.SensorInboundVO;
import com.kglory.tms.web.model.vo.SensorVO;
import com.kglory.tms.web.services.systemSettings.SensorService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class SensorController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    SensorService sensorService;
    @Autowired
    AuditLogService auditLogSvc;

    private static Logger logger = LoggerFactory.getLogger(SensorController.class);

    // 센서 목록 조회 
    @RequestMapping(value = "/api/systemSetting/selectSensorSettingInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<SensorVO> selectSensorSettingInfo() throws BaseException {

        List<SensorVO> listData = null;
        try {
            listData = sensorService.selectSensorSettingInfo();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<SensorVO>();
        } else {
            return listData;
        }
    }

    /**
     * 센서 등록 정보 상세 조회
     *
     * @param dto
     * @return result
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/selectSensorDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public SensorVO selectSensorDetailInfo(@RequestBody SystemConfigDto dto) throws BaseException {

        SensorVO result = new SensorVO();
        try {
            result = sensorService.selectSensorDetailInfo();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 센서 IP 모니터링 정보 조회
     *
     * @param dto
     * @return selectedList
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/selectSensorIpMonitoringList", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<SensorVO> selectSensorIpMonitoringList() throws BaseException {
        ArrayList<SensorVO> selectedList = null;
        try {
            selectedList = sensorService.selectSensorIpMonitoringList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return selectedList;
    }

    // 센서 정보 수정 (업데이트)
    @RequestMapping(value = "/api/systemSetting/updateSensorDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateSensorDetailInfo(@RequestBody SystemConfigDto dto, HttpSession session) throws BaseException {
        try {
//            SensorVO sensor = sensorService.selectSensorDetailInfo();
            boolean isWrite = false;
            String ipMonList = "";
            String deleteIpMonList = "";
            if (dto != null && dto.getIpMonitorList() != null && dto.getIpMonitorList().size() > 0) {
                ipMonList = sensorService.insertSensorIpMonitoring(dto.getIpMonitorList());
                // 추후 사용시
//                ipmonitorMsg = dto.getIpMonitorListStr();
            }
            if (dto != null && dto.getDeleteIpMonitorList() != null && dto.getDeleteIpMonitorList().size() > 0) {
                deleteIpMonList = sensorService.deleteSensorIpMonitor(dto.getDeleteIpMonitorList());
            }
            if (!ipMonList.isEmpty()) {
                ipMonList = "[Add MonIP" + ipMonList + "]";
            }
            if (!ipMonList.isEmpty() || !deleteIpMonList.isEmpty()) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_IPMONITOR_MOD_SUCCESS, (String) session.getAttribute("Username"), MessageUtil.getbuilMessage("audit.system.ipMonitor", ipMonList + deleteIpMonList));
                isWrite = true;
            }
            if (isWrite) {
                sensorService.writeSensorDetailInfo();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
//            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SENSOR_MOD_FAIL, (String) session.getAttribute("Username"), dto.getlPrivateIpInput(), dto.getlPublicIpInput());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
//          auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SENSOR_MOD_FAIL, (String) session.getAttribute("Username"), dto.getlPrivateIpInput(), dto.getlPublicIpInput());
      }
    }

    @RequestMapping(value = "/api/systemSetting/selectSensorIntegrityInfo", method = RequestMethod.POST)
    @ResponseBody
    public SensorVO selectSensorIntegrityInfo(@RequestBody SystemConfigDto dto) throws BaseException {

        SensorVO result = new SensorVO();
        try {
            result = sensorService.selectSensorIntegrityInfo(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 무결성 검사 수정
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/updateSensorIntegrityInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateSensorIntegrityInfo(@RequestBody SensorIntegrityFileDto dto, HttpSession session) throws BaseException {
        try {
            sensorService.updateSensorIntegrityInfo(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    // 센서 무결성 검사 신규 등록 
    @RequestMapping(value = "/api/systemSetting/insertSensorIntegrityInfo", method = RequestMethod.POST)
    @ResponseBody
    public void insertSensorIntegrityInfo(@RequestBody SensorIntegrityFileDto dto, HttpSession session) throws BaseException {
        try {
            sensorService.insertSensorIntegrityInfo(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    // 센서 인바운드 설정 정보 조회 
    @RequestMapping(value = "/api/systemSetting/selectSensorInboundInfo", method = RequestMethod.POST)
    @ResponseBody
    public SensorInboundVO selectSensorInboundInfo(@RequestBody SystemConfigDto dto) throws BaseException {
    	logger.info("selectSensorInboundInfo dto>> "+dto);
        SensorInboundVO result = null;
        try {
            result = sensorService.selectSensorInboundInfo();

        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        logger.info("selectSensorInboundInfo result>> "+result);
        if (result == null) {
            return new SensorInboundVO();
        } else {
            return result;
        }
    }

    // 센서 인바운드 설정 정보 수정 
    @RequestMapping(value = "/api/systemSetting/updateSensorInboundInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateSensorInboundInfo(@RequestBody SensorInboundDto dto, HttpSession session) throws BaseException {
        try {
            sensorService.updateSensorInboundInfo(dto, session);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }

    // 센서 인바운드 설정 정보 신규생성 
    @RequestMapping(value = "/api/systemSetting/insertSensorInboundInfo", method = RequestMethod.POST)
    @ResponseBody
    public void insertSensorInboundInfo(@RequestBody SensorInboundDto dto, HttpSession session) throws BaseException {
        try {
            sensorService.insertSensorInboundInfo(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    // 센서 등록시  IP 중복검사  
    @RequestMapping(value = "/api/systemSetting/duplicatePrivateIpAndPublicIp", method = RequestMethod.POST)
    @ResponseBody
    public SensorVO duplicatePrivateIpAndPublicIp(@RequestBody SystemConfigDto dto) throws BaseException {
        SensorVO resultVo = new SensorVO();
        try {
            resultVo = sensorService.duplicatePrivateIpAndPublicIp(dto);
//			if(count != 0){
//				resultVo.setErrorMessage("해당 사설IP와 공인IP는 이미 사용중입니다. 다른 사설IP또는 공인IP를 입력하세요.");
//			}
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (resultVo == null) {
            return new SensorVO();
        } else {
            return resultVo;
        }
    }

    // 센서 신규생성 및 업데이트시 이름 중복검사 
    @RequestMapping(value = "/api/systemSetting/isDuplicateSensorName", method = RequestMethod.POST)
    @ResponseBody
    public SensorVO isDuplicateSensorName(@RequestBody SystemConfigDto dto) throws BaseException {
        SensorVO result = new SensorVO();
        try {
            result = sensorService.isDuplicateSensorName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }
}
