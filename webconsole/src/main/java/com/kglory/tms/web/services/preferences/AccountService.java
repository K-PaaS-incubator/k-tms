package com.kglory.tms.web.services.preferences;

import com.kglory.tms.web.conf.ConfFile;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.preferences.AccountMapper;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.AccountDto;
import com.kglory.tms.web.model.dto.AccountIpDto;
import com.kglory.tms.web.model.dto.AccountUserFile;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.SpringUtils;
import com.kglory.tms.web.util.security.SeedCrypto64;
import java.util.ArrayList;

@Service("accountSvc")
public class AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    AccountMapper accountMapper;

    private static List<String> authLoginIp = new ArrayList<>();

    public void authLoginIpInti() {
        try {
            authLoginIp = new ArrayList<>();
            authLoginIp = selectLoginIpList();
            logger.info("authLoginIpInti >>>>>>> "+authLoginIp);
        } catch (BaseException ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    public static boolean isLoginIPCheck(String ip) {
        boolean rtn = false;
        if (authLoginIp.contains(ip)) {
            rtn = true;
        }
        return rtn;
    }

    public AccountVO selectAccountByUsername(String userid) {
        return accountMapper.selectAccountByUsername(userid);
    }
    public AccountVO selectAccountByUsernameForPath(AccountDto dto) {
    	return accountMapper.selectAccountByUsernameForPath(dto);
    }
    
    public AccountVO selectAccountByUserIndex(Integer index) {
        return accountMapper.selectAccountByUserIndex(index);
    }
    
    public String getAccountUserIdList(AccountDto dto) {
        String rtn = "";
        for(int i = 0 ; i < dto.getUserList().size() ; i++) {
            AccountVO vo = selectAccountByUserIndex(Integer.parseInt(dto.getUserList().get(i).getStrAccIndex()));
            if(!rtn.isEmpty()) {
                rtn = rtn + ", " + vo.getId();
            } else {
                rtn = vo.getId();
            }
        }
        return rtn;
    }

    /*
     * 계정 목록 조회 
     */
    /**
     * 계정 목록 조회
     *
     * @return List<AccountVO> result
     * @throws BaseException
     */
    public List<AccountVO> selectAccountList() throws BaseException {
        List<AccountVO> result = null;
        result = accountMapper.selectAccountList();
        return result;
    }

    /**
     * 관리자 계정 상세 정보 조회
     *
     * @param dto
     * @return AccountVO result
     * @throws BaseException
     */
    public AccountVO selectAccountDetails(AccountDto dto) throws BaseException {
        AccountVO result = null;
        result = accountMapper.selectAccountDetails(dto);
        result.setUserIpList(selectuserIpList(result.getId()));
        return result;
    }

    /**
     * 관리자 계정 수정
     *
     * @param dto
     * @throws BaseException
     */
    public void updateAccountDetails(AccountDto dto) throws BaseException {
        if (dto.getPassword() != null && !dto.getPassword().equals("")) {
            dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
            accountMapper.updateAccountDetails(dto);
        } else {
            accountMapper.updateAdminExceptPassword(dto);
        }
        insertUserIp(dto.getUserIpList(), dto.getId());
    }

    /**
     * 사용자 계정 수정
     *
     * @param dto
     * @throws BaseException
     */
    public void updateUserAccountDetails(AccountDto dto) throws BaseException {
        // null이 아니면 변경된 패스워드 값을 update
        if (dto.getPassword() != null && !dto.getPassword().equals("")) {
            dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
            accountMapper.updateUserAccountDetails(dto);
            // null이면 패스워드를 제외하고 update	
        } else {
            accountMapper.updateUserExceptPassword(dto);
        }
        insertUserIp(dto.getUserIpList(), dto.getId());
    }

    /**
     * 사용자 계정 삭제
     *
     * @param dto
     * @throws BaseException
     */
    public void deleteAccountList(AccountDto dto) throws BaseException {
//        TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
            
        AccountVO vo = accountMapper.selectAccountDetails(dto);
        
        accountMapper.deleteAccountList(dto);
        
        accountMapper.deleteUserIp(vo.getId());
            
//            this.txManager.commit(status);
    }

    /**
     * 계정 그룹 삭제
     *
     * @param dto
     * @throws BaseException
     */
    public void deleteAccountGroupList(AccountDto dto) throws BaseException {
        accountMapper.deleteAccountGroupList(dto);
    }

    /**
     * 계정 아이디 중복 유효성 검증
     *
     * @param dto
     * @return AccountVO result
     * @throws BaseException
     */
    public AccountVO isDuplicateAccountID(AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        result = accountMapper.isDuplicateAccountID(dto);
        if (result == null) {
            return new AccountVO();
        } else {
            return result;
        }
    }

    /**
     * 기존 비밀번호와 비교 유효성 검증
     *
     * @param dto
     * @return AccountVO result
     * @throws BaseException
     */
    public AccountVO isComparedPassword(AccountDto dto) throws BaseException {
        AccountVO accountVo = new AccountVO();
        accountVo = accountMapper.selectAccountUserPassword(dto);
        if (accountVo != null) {
            boolean isSame = SeedCrypto64.isPasswordChange(dto.getId(), dto.getPassword(), accountVo.getPassword());
            if (!isSame) {
                // 기존 비밀번호와 같지 않으면
                accountVo.setChangeYn(0);
            } else {
                // 기존 비밀번호와 같으면
                accountVo.setChangeYn(9);
            }
        }
        return accountVo;
    }

    /**
     * 계정 그룹 신규 등록
     *
     * @param dto
     * @return long insertLIndex
     * @throws BaseException
     */
    public long insertGroupAccountDetails(AccountDto dto) throws BaseException {
        long insertLIndex = 0;
        List<AccountUserFile> resultList = dto.getUserList();
        String userIndex = "";
        for (Integer i = 0; i < resultList.size(); i++) {
            if (i != 0) {
                userIndex = userIndex + "|";
            }
            userIndex = userIndex + resultList.get(i).getStrAccIndex();
        }
        dto.setStrAccIndex(userIndex);
        accountMapper.insertGroupAccountDetails(dto);
        insertLIndex = dto.getlIndex();

        return insertLIndex;

    }

    /**
     * 계정 그룹 목록 조회
     *
     * @return List<AccountVO> result
     * @throws BaseException
     */
    public List<AccountVO> selectAccountGroupList() throws BaseException {
        List<AccountVO> result = null;
        result = accountMapper.selectAccountGroupList();
        return result;
    }

    /**
     * 그룹 상세 목록 조회
     *
     * @param dto
     * @return AccountVO result
     * @throws BaseException
     */
    public AccountVO selectGroupDetailList(AccountDto dto) throws BaseException {
        AccountVO result = null;
        result = accountMapper.selectGroupDetailList(dto);
        return result;
    }

    /**
     * 계정 그룹 상세 정보 수정
     *
     * @param dto
     * @throws BaseException
     */
    public void updateGroupAccountDetails(AccountDto dto) throws BaseException {
        List<AccountUserFile> resultList = dto.getUserList();
        String userIndex = "";
        for (Integer i = 0; i < resultList.size(); i++) {
            if (i != 0) {
                userIndex = userIndex + "|";
            }
            userIndex = userIndex + resultList.get(i).getStrAccIndex();
        }
        dto.setStrAccIndex(userIndex);
        accountMapper.updateGroupAccountDetails(dto);
    }

    /**
     * 계정 그룹명 중복 유효성 검증
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public AccountVO isDuplicateGroupName(AccountDto dto) throws BaseException {
        AccountVO result = new AccountVO();
        result = accountMapper.isDuplicateGroupName(dto);
        if (result == null) {
            return new AccountVO();
        } else {
            return result;
        }
    }

    /**
     * 계정 관리자 목록 TOTAL 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<AccountVO> selectTotalAdminList(AccountDto dto) throws BaseException {
        List<AccountVO> result = null;
        result = accountMapper.selectTotalAdminList(dto);
        return result;
    }

    /**
     * 사용자 계정 목록 TOTAL 조회
     *
     * @param dto
     * @return List<AccountVO>
     * @throws BaseException
     */
    public List<AccountVO> selectTotalUserList(AccountDto dto) throws BaseException {
        List<AccountVO> result = null;
        result = accountMapper.selectTotalUserList(dto);
        return result;
    }

    /**
     * Default 정보 계정 수정 다른 update 모듈은 role, groupType, category, refindex 등 다른 속성들도 업데이트를 같이 해주어야 해서 따로 만듬. 이 모듈은 패스워드,
     * 이름, 회사, 설명, 전화, 휴대폰, 이메일 정보만 수정
     */
    public void updateAccountDefault(AccountDto dto) throws BaseException {
        if (dto.getPassword() != null && !dto.getPassword().equals("")) {
            dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
            accountMapper.updateAccountDefault(dto);
        } else {
            accountMapper.updateAccountDefaultExceptPassword(dto);
        }
    }

    /**
     * 관리자 계정 신규 생성
     *
     * @param dto
     * @return insertLIndex
     * @throws BaseException
     */
    public long insertAdminAccountDetails(AccountDto dto) throws BaseException {
        long insertLIndex = 0;
        dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
        accountMapper.insertAdminAccountDetails(dto);
        insertLIndex = dto.getUserIndex();
        
        insertUserIp(dto.getUserIpList(), dto.getId());
        return insertLIndex;
    }

    /**
     * 사용자 계정 신규 생성
     *
     * @param dto
     * @return insertLIndex
     * @throws BaseException
     */
    public long insertUserAccountDetails(AccountDto dto) throws BaseException {
        long insertLIndex = 0;
        dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
        accountMapper.insertUserAccountDetails(dto);
        insertLIndex = dto.getUserIndex();
        
        insertUserIp(dto.getUserIpList(), dto.getId());
        return insertLIndex;
    }

    /**
     * 비밀번호 일치여부 확인
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public boolean isPasswordCheck(AccountDto dto) throws BaseException {
        boolean rtn = false;
        AccountVO vo = accountMapper.selectAccountDetails(dto);
        if (vo != null && vo.getPassword() != null && vo.getPassword().equals(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()))) {
            rtn = true;
        }
        return rtn;
    }
    
    /**
     * 로그인 허용 IP 조회
     * @return
     * @throws BaseException 
     */
    public List<String> selectLoginIpList() throws BaseException {
        List<String> rtnList = new ArrayList<>();
        List<String> loginIPList = accountMapper.selectAuthIpList();
        logger.info("forlks >>> "+loginIPList);
        if (loginIPList != null) {
            for (String ip : loginIPList) {
                rtnList.add(ip);
            }
        }
        return rtnList;
    }
    
    /**
     * 로그인 허용 IP 등록 (삭제 후 등록)
     * @param loginIpList
     * @throws BaseException 
     */
    public void insertLoginAuthIp(List<String> loginIpList) throws BaseException {
//        TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
        accountMapper.deleteAuthIp();
        for (String ip : loginIpList) {
            accountMapper.insertAuthIp(ip);
        }
//            this.txManager.commit(status);
    }
    
    /**
     * 사용자별 로그인 허용 IP 목록 조회
     * @param id
     * @return
     * @throws BaseException 
     */
    public List<String> selectuserIpList(String id) throws BaseException {
        List<String> rtnList = new ArrayList<>();
        rtnList = accountMapper.selectuserIpList(id);
        return rtnList;
    }
    
    public void insertUserIp(List<String> list, String id) throws BaseException {
//        TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
        if (list != null && list.size() > 0) {
            accountMapper.deleteUserIp(id);
            AccountIpDto dto;
            for (String ip : list) {
                dto = new AccountIpDto();
                dto.setId(id);
                dto.setIp(ip);
                accountMapper.insertUserIp(dto);
            }
//                this.txManager.commit(status);
        }
    }
    
    public boolean isUserLoginIp(String id, String ip) throws BaseException {
        boolean rtn = false;
        List<String> list = selectuserIpList(id);
        if (list != null && list.size() > 0) {
            for (String str : list) {
                logger.debug("ip : " + str + ", remote : " + ip);
                if (str.equals(ip)) {
                    rtn = true;
                    break;
                }
            }
        } else {
            rtn = true;
        }
        return rtn;
    }
    
    /**
     * 사용자 권한에 따른 메일 목록 조회(null, empty 제외)
     * @param role
     * @return
     * @throws BaseException 
     */
    public List<AccountVO> selectAccountMailList(Long role) throws BaseException {
        List<AccountVO> rtnList = new ArrayList<>();
        if ( role > 1L) {
            rtnList = accountMapper.selectAdminMailList();
        } else {
            rtnList = accountMapper.selectUserMailList();
        }
        return rtnList;
    }
    
    /**
     * 사용자 권한에 따른 SMS 목록 조회(null, empty 제외)
     * @param role
     * @return
     * @throws BaseException 
     */
    public List<AccountVO> selectAccountSmsList(Long role) throws BaseException {
        List<AccountVO> rtnList = new ArrayList<>();
        if ( role > 1L) {
            rtnList = accountMapper.selectAdminSmsList();
        } else {
            rtnList = accountMapper.selectUserSmsList();
        }
        return rtnList;
    }
}
