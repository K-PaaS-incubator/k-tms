package com.kglory.tms.web.controller.preferences;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.CommonBean.ReturnType;
import com.kglory.tms.web.model.dto.AccountDto;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class AccountController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    AccountService accountService;
    @Autowired
    AuditLogService auditLogSvc;

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    /**
     * 계정 목록 조회
     *
     * @return List<AccountVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectAccountList", method = RequestMethod.POST)
    @ResponseBody
    public List<AccountVO> selectAccountList() throws BaseException {
        List<AccountVO> listData = null;
        try {
            listData = accountService.selectAccountList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<AccountVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    /**
     * 계정 상세 정보 조회
     *
     * @param dto
     * @return AccountVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO selectAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        AccountVO result = null;
        try {
            result = accountService.selectAccountDetails(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (result == null) {
            return new AccountVO();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
                logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
            }
            return result;
        }
    }

    /**
     * 일반 사용자 계정 조회 api/selectUserAccountDetails
     */
    @RequestMapping(value = "/api/selectUserAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO selectUserAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        AccountVO result = null;
        try {
            result = accountService.selectAccountDetails(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (result == null) {
            return new AccountVO();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
                logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
            }
            return result;
        }
    }

    /**
     * 관리자 계정 신규 등록
     *
     * @param dto
     * @return returnValue
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/insertAdminAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertAdminAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = accountService.insertAdminAccountDetails(dto);
            String msg = MessageUtil.getbuilMessage("audit.admin", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_ADD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_ADD_FAIL, (String) session.getAttribute("Username"));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_ADD_FAIL, (String) session.getAttribute("Username"));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : userIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("userIndex", insertLIndex);
        return returnValue;
    }

    /**
     * 관리자 계정 상세 정보 수정
     *
     * @param dto
     * @param result
     * @param session
     * @return AccountDto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/updateAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public AccountDto updateAccountDetails(@RequestBody AccountDto dto, BindingResult result, HttpSession session) throws BaseException {
        try {
            AccountVO user = accountService.selectAccountByUsername(dto.getId());
            if (!dto.equals(user)) {
                accountService.updateAccountDetails(dto);
                String msg = MessageUtil.getbuilMessage("audit.admin", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }

        return dto;
    }

    /**
     * 일반 사용자 상세 정보 수정
     *
     * @param dto
     * @param result
     * @param session
     * @return AccountDto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/updateUserAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public AccountDto updateUserAccountDetails(@RequestBody AccountDto dto, BindingResult result, HttpSession session) throws BaseException {
        try {
            AccountVO user = accountService.selectAccountByUsername(dto.getId());
            if (!dto.equals(user)) {
                accountService.updateUserAccountDetails(dto);
                String msg = MessageUtil.getbuilMessage("audit.user", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail(), dto.getPathName(), MessageUtil.getMessage("account.status" + dto.getLockout()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        }
        if (dto.getReturnType() != ReturnType.success) {
            return dto;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }

        return dto;
    }
    /*
     * 일반 사용자 상세 정보 등록
     */
//	@RequestMapping(value = "/api/preferences/insertUserAccountDetails", method = RequestMethod.POST)
//	@ResponseBody
//	public void insertUserAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
//		try {
//			accountService.insertUserAccountDetails(dto);
//			//AccountVO accountVO = accountService.selectAccountByUsername(dto.getId());
//			//dto.setUserIndex(accountVO.getUserIndex());
//			//Integer result = requestCommand(CommandService.ADD_ACCOUNT, dto.getUserIndex(), (String) session.getAttribute("Username"), "account.add.error");
//		}catch(Exception e) {
//			logger.error(e.getLocalizedMessage(), e);
//		}
//	}

    /**
     * 사용자 계정 신규 등록
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/insertUserAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertUserAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = accountService.insertUserAccountDetails(dto);
            String msg = MessageUtil.getbuilMessage("audit.user", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail(), dto.getPathName(), MessageUtil.getMessage("account.status" + dto.getLockout()));
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_ADD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_ADD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_ADD_FAIL, (String) session.getAttribute("Username"), dto.getId());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : userIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("userIndex", insertLIndex);

        return returnValue;

    }

    /**
     * 관리자 계정 및 사용자 목록 삭제
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/deleteAccountList", method = RequestMethod.POST)
    @ResponseBody
    public void deleteAccountList(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        String userId = "";
        try {
            AccountVO accountVo = accountService.selectAccountDetails(dto);
            if (accountVo != null) {
                userId = accountVo.getId();
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        try {
            accountService.deleteAccountList(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_DEL_SUCCESS, (String) session.getAttribute("Username"), userId);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_DEL_FAIL, (String) session.getAttribute("Username"), userId);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    /**
     * 계정 그룹 목록 삭제
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/deleteAccountGroupList", method = RequestMethod.POST)
    @ResponseBody
    public void deleteAccountGroupList(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        String strname = "";
        try {
            AccountVO accountVo = accountService.selectGroupDetailList(dto);
            if (accountVo != null) {
                strname = accountVo.getStrName();
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        try {
            accountService.deleteAccountGroupList(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_DEL_SUCCESS, (String) session.getAttribute("Username"), strname);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_DEL_FAIL, (String) session.getAttribute("Username"), strname);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    /**
     * 기존 비밀번호와 비교 유효성 검증
     *
     * @param dto
     * @return AccountVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/isComparedPassword", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO isComparedPassword(@RequestBody AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        try {
            result = accountService.isComparedPassword(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 계정 아이디 중복 유효성 검증
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/isDuplicateAccountID", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO isDuplicateAccountID(@RequestBody AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        try {
            result = accountService.isDuplicateAccountID(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

//	@RequestMapping(value = "/api/preferences/insertGroupAccountDetails", method = RequestMethod.POST)
//	@ResponseBody
//	public void insertGroupAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
//		try {
//			accountService.insertGroupAccountDetails(dto);
//		} catch(Exception e) {
//			logger.error(e.getLocalizedMessage());
//		}
//	}
    /**
     * 계정그룹 신규 등록
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/insertGroupAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertGroupAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = accountService.insertGroupAccountDetails(dto);
            String msg = accountService.getAccountUserIdList(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_ADD_SUCCESS, (String) session.getAttribute("Username"), dto.getStrName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_ADD_FAIL, (String) session.getAttribute("Username"), dto.getStrName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_ADD_FAIL, (String) session.getAttribute("Username"), dto.getStrName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : index=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);

        return returnValue;

    }

    /**
     * 계정 그룹 목록 조회
     *
     * @return List<AccountVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectAccountGroupList", method = RequestMethod.POST)
    @ResponseBody
    public List<AccountVO> selectAccountGroupList() throws BaseException {
        List<AccountVO> listData = null;
        try {
            listData = accountService.selectAccountGroupList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<AccountVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    /**
     * 계정 그룹 상세 조회
     *
     * @param dto
     * @return AccountVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectGroupDetailList", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO selectGroupDetailList(@RequestBody AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        try {
            result = accountService.selectGroupDetailList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
        }

        return result;

    }

    /**
     * 계정 그룹 상세 정보 수정
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/updateGroupAccountDetails", method = RequestMethod.POST)
    @ResponseBody
    public void updateGroupAccountDetails(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        try {
            accountService.updateGroupAccountDetails(dto);
            String msg = accountService.getAccountUserIdList(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getStrName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_GROUP_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    /**
     * 계정 그룹명 중복 유효성 검증
     *
     * @param dto
     * @return AccountVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/isDuplicateGroupName", method = RequestMethod.POST)
    @ResponseBody
    public AccountVO isDuplicateGroupName(@RequestBody AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        try {
            result = accountService.isDuplicateGroupName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 계정 관리자 목록 TOTAL 조회
     *
     * @param dto
     * @return List<AccountVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectTotalAdminList", method = RequestMethod.POST)
    @ResponseBody
    public List<AccountVO> selectTotalAdminList(@RequestBody AccountDto dto) throws BaseException {
        List<AccountVO> listData = null;
        try {
            listData = accountService.selectTotalAdminList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (listData == null) {
            return new ArrayList<AccountVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    /**
     * 사용자 계정 목록 TOTAL 조회
     *
     * @param dto
     * @return List<AccountVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/selectTotalUserList", method = RequestMethod.POST)
    @ResponseBody
    public List<AccountVO> selectTotalUserList(@RequestBody AccountDto dto) throws BaseException {
        List<AccountVO> listData = null;
        try {
            listData = accountService.selectTotalUserList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (listData == null) {
            return new ArrayList<AccountVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;

    }

    /**
     * Default 계정 정보 수정 다른 update 모듈은 role, groupType, category, refindex 등 다른 속성들도 업데이트를 같이 해주어야 해서 따로 만듬. 이 모듈은 패스워드,
     * 이름, 회사, 설명, 전화, 휴대폰, 이메일 정보만 수정
     *
     * @param dto
     * @return AccountDto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/updateAccountDefault", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateAccountDefault(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        HashMap<String, Integer> rtn = new HashMap<>();
        int rtnValue = 0;
        try {
            AccountVO user = accountService.selectAccountByUsername(dto.getId());
            if (dto.getUserIndex() != (long)session.getAttribute("UserIndex")) throw new BaseException();
            if (!dto.equals(user)) {
                accountService.updateAccountDefault(dto);
                String msg = MessageUtil.getbuilMessage("audit.admin", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
            }
        } catch (BaseException e) {
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
            logger.error(e.getLocalizedMessage(), e);
            rtnValue = -99;
        } catch (Exception e) {
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
            logger.error(e.getLocalizedMessage(), e);
            rtnValue = -99;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
        rtn.put("accountUpdateRtn", rtnValue);
        return rtn;
    }

    /**
     * 일반 사용자 로그인시 계정 정보 수정
     *
     * @param dto
     * @return AccountDto
     * @throws BaseException
     */
    @RequestMapping(value = "api/updateUserAccountDefault", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateUserAccountDefault(@RequestBody AccountDto dto, HttpSession session) throws BaseException {
        HashMap<String, Integer> rtn = new HashMap<>();
        int rtnValue = 0;
        try {
            AccountVO user = accountService.selectAccountByUsernameForPath(dto);
            if (dto.getUserIndex() != (long) session.getAttribute("UserIndex")) throw new BaseException();
            if (!dto.equals(user)) {
            	accountService.updateAccountDefault(dto);
            	if(dto.getLockout() == null) {
            		dto.setLockout(user.getLockout());
            	}
            	if(dto.getPathName() == null) {
            		dto.setPathName(user.getPathName());
            	}
            	
                String msg = MessageUtil.getbuilMessage("audit.user", dto.getName(), dto.getCompany(), dto.getDescription(), dto.getTelephone(), dto.getMobile(), dto.getEmail(), dto.getPathName(), MessageUtil.getMessage("account.status" + dto.getLockout()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getId(), msg);
            }
        } catch (BaseException e) {
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
            logger.error(e.getLocalizedMessage(), e);
            rtnValue = -99;
        } catch (Exception e) {
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.ACCOUNT_MOD_FAIL, (String) session.getAttribute("Username"), dto.getId());
            logger.error(e.getLocalizedMessage(), e);
            rtnValue = -99;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
        rtn.put("accountUpdateRtn", rtnValue);
        return rtn;
    }

    /**
     * 비밀번호 일치여부 확인
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/preferences/isPasswordCheck", method = RequestMethod.POST)
    @ResponseBody
    public boolean isPasswordCheck(@RequestBody AccountDto dto) throws BaseException {
        boolean rtn = false;
        try {
            rtn = accountService.isPasswordCheck(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }
}
