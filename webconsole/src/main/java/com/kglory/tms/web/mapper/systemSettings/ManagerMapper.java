package com.kglory.tms.web.mapper.systemSettings;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.ImDbBackupDto;
import com.kglory.tms.web.model.dto.IntegrityFile;
import com.kglory.tms.web.model.dto.ManagerBackupDto;
import com.kglory.tms.web.model.dto.ManagerDto;
import com.kglory.tms.web.model.dto.ManagerIntegrityFileDto;
import com.kglory.tms.web.model.dto.ManagerSyslogDto;
import com.kglory.tms.web.model.dto.SystemDto;
import com.kglory.tms.web.model.vo.ImDbBackupVO;
import com.kglory.tms.web.model.vo.ManagerBackupVO;
import com.kglory.tms.web.model.vo.ManagerIntegrityFileVO;
import com.kglory.tms.web.model.vo.ManagerSyslogVO;
import com.kglory.tms.web.model.vo.ManagerVO;
import com.kglory.tms.web.model.vo.SystemVO;

public interface ManagerMapper {

    // 매니저 등록 정보 상세 
    @Transactional(readOnly = true)
    public SystemVO selectSystemSettingInfo();

    // 이메일 서버 등록 정보 수정 
    @Transactional
    public void updateEmailServer(SystemDto dto);
    
    // 시간 동기화 등록 정보 수정 
    @Transactional
    public void updateManagerTimeSync(SystemDto dto);
    
    // 매니저 등록 정보 수정 
    @Transactional
    public void updateManagerSettingInfo(ManagerDto dto);

    //매니저 무결성 등록 정보
    @Transactional(readOnly = true)
    public ManagerVO selectManagerIntegrityInfo();

    // 매니저 무결성 정보 수정 
    @Transactional
    void updateManagerIntegrityInfo(ManagerIntegrityFileDto dto);

    // 매니저 등록 정보 상세 
    @Transactional(readOnly = true)
    public ManagerVO selectDbManagement();

    // 매니저 등록 정보 수정 
    @Transactional
    public void updateDbManagement(ManagerDto dto);

    // 매니저 시간동기화 정보
    @Transactional(readOnly = true)
    public ManagerSyslogVO selectManagerTimeSync();

    // 매니저 시간동기화 정보 수정
    @Transactional
    public void updateManagerTimeSync(ManagerSyslogDto dto);

    // 매니저 DB백업 정보
    @Transactional(readOnly = true)
    public ManagerBackupVO selectDbBackup();

    // 매니저 DB백업 정보 업데이트
    @Transactional
    public void updateDbBackup(ManagerBackupDto dto);

    //매니저 오프라인패턴업데이트 경로 조회
    @Transactional(readOnly = true)
    public String selectUpdatePath();

    @Transactional
    long insertImDbBackup(ImDbBackupDto dto);

    @Transactional
    long insertImDbBackupFile(ImDbBackupDto dto);

    // 콘솔 무결성 파일 목록 
    @Transactional(readOnly = true)
    List<IntegrityFile> selectConsoleIntegrityFileList();

    // 콘솔 무결성 파일 일괄 삽입
    @Transactional
    void insertConsoleIntegrityFile(ManagerIntegrityFileDto dto);

    // 콘솔 무결성 파일 저장 batch
    @Transactional
    void insertConsoleIntegrityFileBatch(IntegrityFile dto);

    //콘솔 무결성 파일 삭제
    @Transactional
    void deleteConsoleIntegrityFile();
    
    @Transactional(readOnly = true)
    List<ImDbBackupVO> selectDbBackupFileList(ImDbBackupDto dto);
    
    @Transactional(readOnly = true)
    Long selectDbBackupFileListTotal(ImDbBackupDto dto);
    
    @Transactional(readOnly = true)
    ImDbBackupVO selectDbBackupFileDetail(ImDbBackupDto dto);
}
