package com.kglory.tms.web.mapper.common;

import com.kglory.tms.web.model.dto.TableColumnsDto;
import java.util.List;

import com.kglory.tms.web.model.dto.TargetOrgDto;
import com.kglory.tms.web.model.dto.TargetSensorDto;
import com.kglory.tms.web.model.vo.TableColumnsVO;
import com.kglory.tms.web.model.vo.TargetVO;
import java.math.BigInteger;
import org.springframework.transaction.annotation.Transactional;

public interface CommonMapper {
    @Transactional(readOnly = true)
    List<TargetVO> selectTargetOrg(TargetOrgDto dto);
    
    // 사용자, 메뉴 별 테이블 컬럼 목록 조회
    @Transactional(readOnly = true)
    public List<TableColumnsVO> selectTableColumnList(TableColumnsDto dto);
    
    // 사용자, 메뉴 별 테이블 컬럼 등록
    @Transactional
    public void insertTableColumn(TableColumnsDto dto);
    
    // 사용자, 메뉴 테이블 삭제
    @Transactional
    public void deleteTableColumn(TableColumnsDto dto);
    
    // 네트워크 그룹 하위 네트워크 조회
    @Transactional(readOnly = true)
    public List<BigInteger> selectNetGroupByNetwork(BigInteger lnetgroupIndex);

    // 사용자 패스워드 변경
    @Transactional
    public void modifyDbPassword(TableColumnsDto dto);
}
