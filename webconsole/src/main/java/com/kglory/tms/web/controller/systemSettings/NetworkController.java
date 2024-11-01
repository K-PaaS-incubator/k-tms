package com.kglory.tms.web.controller.systemSettings;

import com.kglory.tms.web.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.services.systemSettings.NetworkService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class NetworkController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    NetworkService networkService;
    @Autowired
    AuditLogService auditLogSvc;

    private static Logger logger = LoggerFactory.getLogger(NetworkController.class);

    @RequestMapping(value = "/api/systemSetting/selectNetworkList", method = RequestMethod.POST)
    @ResponseBody
    public List<NetworkVO> selectNetworkList() throws BaseException {

        List<NetworkVO> resultList = null;
        try {
            resultList = networkService.selectNetworkList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (resultList == null) {
            return new ArrayList<NetworkVO>();
        }
        return resultList;
    }

    @RequestMapping(value = "/api/systemSetting/selectNetworkDetailList", method = RequestMethod.POST)
    @ResponseBody
    public NetworkVO selectNetworkDetailList(@RequestBody NetworkDto dto) throws BaseException {
        NetworkVO result = new NetworkVO();
        try {
            result = networkService.selectNetworkDetailList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/api/systemSetting/selectNetworkIpBlockList", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<NetworkVO> selectNetworkIpBlockList(@RequestBody NetworkDto dto) throws BaseException {
        ArrayList<NetworkVO> selectedList = null;
        try {
            selectedList = networkService.selectNetworkIpBlockList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return selectedList;
    }

    /**
     * netIpBlock정보 fromIp, ToIp 정보를 삭제한다.(lId)
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/deleteNetworkIpBlockLid", method = RequestMethod.POST)
    @ResponseBody
    public void deleteNetworkIpBlockLid(@RequestBody NetworkDto dto) throws BaseException {

        try {
            networkService.deleteNetworkIpBlockLid(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * netIpBlock정보 from ip와 to ip 정보를 신규등록한다.
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/insertNetworkIpBlockList", method = RequestMethod.POST)
    @ResponseBody
    public void insertNetworkIpBlockList(@RequestBody NetworkDto dto) throws BaseException {
        try {
            networkService.insertNetworkIpBlockList(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return;
    }

    /**
     * 기존 네트워크 정보를 수정한다.
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/updateNetworkDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateNetworkDetailInfo(@RequestBody NetworkDto dto, HttpSession session) throws BaseException {
        try {
            boolean chk = networkService.updateNetworkDetailInfo(dto);
            if(chk) {
                StringBuffer sb = new StringBuffer();
                sb.append("Name:");
                sb.append(dto.getStrName());
                sb.append(", Description:");
                sb.append(dto.getStrDescription());
                sb.append(", IPBlock:");
                for(int i = 0 ; i < dto.getIpBlockList().size(); i++) {
                    sb.append("FromIp:");
                    sb.append(dto.getIpBlockList().get(i).getDwFromIp());
                    sb.append(" ToIp:");
                    sb.append(dto.getIpBlockList().get(i).getDwToIp());
                    if (i < (dto.getIpBlockList().size() - 1)) {
                        sb.append(" || ");
                    }
                }
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.NETWORK_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getLnetworkIndex(), sb.toString());
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_MOD_FAIL, (String) session.getAttribute("Username"), dto.getLnetworkIndex());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_MOD_FAIL, (String) session.getAttribute("Username"), dto.getLnetworkIndex());
        }
        return;
    }

    /**
     * 네트워크 신규정보를 등록한다.
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/insertNetworkDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertNetworkDetailInfo(@RequestBody NetworkDto dto, HttpSession session) throws BaseException {

        long lnetworkIndex = 0;
        try {
            lnetworkIndex = networkService.insertNetworkDetailInfo(dto);
            StringBuffer sb = new StringBuffer();
            sb.append("IPBlock:");
            for(int i = 0 ; i < dto.getIpBlockList().size(); i++) {
                sb.append("FromIp:");
                sb.append(dto.getIpBlockList().get(i).getDwFromIp());
                sb.append(" ToIp:");
                sb.append(dto.getIpBlockList().get(i).getDwToIp());
                if (i < (dto.getIpBlockList().size() - 1)) {
                    sb.append(" || ");
                }
            }
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.NETWORK_ADD_SUCCESS, (String) session.getAttribute("Username"), lnetworkIndex, dto.getStrName(), sb.toString());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_ADD_FAIL, (String) session.getAttribute("Username"), lnetworkIndex, dto.getStrName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_ADD_FAIL, (String) session.getAttribute("Username"), lnetworkIndex, dto.getStrName());
        }
        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lnetworkIndex", lnetworkIndex);
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lnetworkIndex=" + lnetworkIndex);
        }
        
        return returnValue;
    }

    /**
     * 해당 네트워크를 삭제한다. 하위에 포함된 netipblock 정보도 함께 삭제한다.
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/deleteNetworkSettingInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteNetworkSettingInfo(@RequestBody NetworkDto dto, HttpSession session) throws BaseException {
        try {
            networkService.deleteNetworkSettingInfo(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.NETWORK_DEL_SUCCESS, (String) session.getAttribute("Username"), dto.getLnetworkIndex());
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
            
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_DEL_FAIL, (String) session.getAttribute("Username"), dto.getLnetworkIndex());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.NETWORK_DEL_FAIL, (String) session.getAttribute("Username"), dto.getLnetworkIndex());
        }
        return;
    }

    /**
     * 네트워크 인덱스 중복확인
     *
     * @param dto
     * @param session
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/isDuplicatelnetworkIndex", method = RequestMethod.POST)
    @ResponseBody
    public Integer isDuplicatelnetworkIndex(@RequestBody NetworkDto dto, HttpSession session) throws BaseException {
        int duplicationCount = 0;
        try {
            duplicationCount = networkService.isDuplicatelnetworkIndex(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return duplicationCount;
    }

    /**
     * 네트워크명 중복검사
     *
     */
    @RequestMapping(value = "/api/systemSetting/isDuplicateNetworkName", method = RequestMethod.POST)
    @ResponseBody
    public NetworkVO isDuplicateNetworkName(@RequestBody NetworkDto dto) throws BaseException {
        NetworkVO result = new NetworkVO();
        try {
            result = networkService.isDuplicateNetworkName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 네트워크명 인덱스 Max+1 생성( 신규생성시 이용 )
     *
     */
    @RequestMapping(value = "api/systemSetting/selectNextNetworkIndex", method = RequestMethod.POST)
    @ResponseBody
    public NetworkVO selectNextNetworkIndex() throws BaseException {
        NetworkVO result = new NetworkVO();
//            result = networkService.selectNextNetworkIndex();
        return result;
    }
    
    @RequestMapping(value = "/api/systemSetting/getIpv6SubnetMask", method = RequestMethod.POST)
    @ResponseBody
    public Map getIpv6SubnetMask(@RequestBody String path, HttpSession session) {
        Map<String, String> rtn = new HashMap<>();
        try {
            rtn = IpUtil.getIpv6SubnetMask(path);
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }
    
    @RequestMapping(value = "/api/systemSetting/getIpv6RangeMask", method = RequestMethod.POST)
    @ResponseBody
    public int getIpv6RangeMask(@RequestBody Map<String, String> map, HttpSession session) {
        int rtn = 0;
        try {
            rtn = IpUtil.getIpv6RangeMask(map.get("fromIp"), map.get("toIp"));
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }
}
