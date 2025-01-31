package com.kglory.tms.web.mapper.common;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.PolicyDto;
import com.kglory.tms.web.model.vo.PolicyVO;

public interface PolicyMapper {

	//공격명 검색 시, lcode 가져오기
	@Transactional(readOnly = true)
	ArrayList<Integer> selectPolicyLcode(PolicyDto policyDto);
	
	//공격유형 검색 시, lcode 가져오기
	@Transactional(readOnly = true)
	ArrayList<Integer> selectPolicyLcodeByAttackType(PolicyDto policyDto);
	
	//lcode 검색시 시그니처룰 가져오기
	@Transactional(readOnly = true)
	PolicyVO selectPolicySignatureRuleBylCode(PolicyDto policyDto);
	
}
