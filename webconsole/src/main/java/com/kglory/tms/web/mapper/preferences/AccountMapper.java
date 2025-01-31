package com.kglory.tms.web.mapper.preferences;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AccountDto;
import com.kglory.tms.web.model.dto.AccountIpDto;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.model.vo.AccountIpVO;
import com.kglory.tms.web.model.vo.AccountVO;

public interface AccountMapper {

    @Transactional(readOnly = true)
    AccountVO selectAccountByUsername(String userid);
    
    @Transactional(readOnly = true)
    AccountVO selectAccountByUserIndex(Integer index);

    @Transactional(readOnly = true)
    AccountVO selectAccountByUsernameForPath(AccountDto dto);

    @Transactional(readOnly = true)
    List<AccountVO> selectAccounts();

    // 사용자 목록 조회 
    @Transactional(readOnly = true)
    List<AccountVO> selectAccountList();

    // 사용자 기본 정보 조회
    @Transactional(readOnly = true)
    AccountVO selectAccountDetails(AccountDto dto);

    // 관리자 상세 정보 저장
    @Transactional
    void updateAccountDetails(AccountDto dto);

    // 사용자 상세 정보 저장
    @Transactional
    int updateUserAccountDetails(AccountDto dto);

    // 사용자 상세 정보 신규 등록 
    @Transactional
    long insertUserAccountDetails(AccountDto dto);

    // 계정 삭제 
    @Transactional
    void deleteAccountList(AccountDto dto);

    // 관리자 상세 정보 신규 등록 
    @Transactional
    long insertAdminAccountDetails(AccountDto dto);

    // 비밀번호 없이 사용자 수정/저장
    @Transactional
    void updateUserExceptPassword(AccountDto dto);

    // 비밀번호 없이 관리자 수정/저장
    @Transactional
    void updateAdminExceptPassword(AccountDto dto);

    // 기존 비밀번호와 비교 
    @Transactional
    AccountVO selectAccountUserPassword(AccountDto dto);

    // 아이디 중복 검사
    @Transactional
    AccountVO isDuplicateAccountID(AccountDto dto);

    // 그룹 상세 정보 신규 등록 
    @Transactional
    long insertGroupAccountDetails(AccountDto dto);

    // 그룹 목록 조회 
    @Transactional(readOnly = true)
    List<AccountVO> selectAccountGroupList();

    // 그룹 상세 조회 
    @Transactional(readOnly = true)
    AccountVO selectGroupDetailList(AccountDto dto);

    // 그룹 상세 정보 수정
    @Transactional
    void updateGroupAccountDetails(AccountDto dto);

    // 그룹 삭제 
    @Transactional
    void deleteAccountGroupList(AccountDto dto);

    // 그룹명 중복검사 
    @Transactional(readOnly = true)
    AccountVO isDuplicateGroupName(AccountDto dto);

    // 관리자 계정 TOTAL 조회 
    @Transactional(readOnly = true)
    List<AccountVO> selectTotalAdminList(AccountDto dto);

    // 사용자 계정 TOTAL 조회 
    @Transactional(readOnly = true)
    List<AccountVO> selectTotalUserList(AccountDto dto);

    //로그인 성공 저장
    @Transactional
    void updateLoginSuccess(AccountDto dto);

    //로그인 실패 저장
    @Transactional
    void updateLoginFail(AccountDto dto);

    @Transactional
    void updateAccountDefault(AccountDto dto);

    @Transactional
    void updateAccountDefaultExceptPassword(AccountDto dto);

    // 로그인 허용 IP 목록 조회
    @Transactional(readOnly = true)
    List<String> selectAuthIpList();

    // 로그인 허용  IP 삭제
    @Transactional
    void deleteAuthIp();

    // 로그인 혀용 IP 등록
    @Transactional
    void insertAuthIp(String parameterName);

    //사용자 별 로그인 허용 IP 목록 조회
    @Transactional(readOnly = true)
    public List<String> selectuserIpList(String id);

    //사용자 로그인 허용 IP 등록
    @Transactional
    public void insertUserIp(AccountIpDto dto);

    //사용자 별 로그인 허용 IP 삭제
    @Transactional
    public void deleteUserIp(String id);

    // 사용자 권한별 메일 리스트 조회(null, empty 제외)
    @Transactional(readOnly = true)
    public List<AccountVO> selectAdminMailList();
    
    // 사용자 권한별 메일 리스트 조회(null, empty 제외)
    @Transactional(readOnly = true)
    public List<AccountVO> selectUserMailList();
    
    // 사용자 권한별 SMS 리스트 조회(null, empty 제외)
    @Transactional(readOnly = true)
    public List<AccountVO> selectAdminSmsList();
    
    // 사용자 권한별 SMS 리스트 조회(null, empty 제외)
    @Transactional(readOnly = true)
    public List<AccountVO> selectUserSmsList();
    
    // 최초로그인 사용자 아이디 비밀번호 변경
    @Transactional
    public void updateAdmin(AccountDto dto);
}
