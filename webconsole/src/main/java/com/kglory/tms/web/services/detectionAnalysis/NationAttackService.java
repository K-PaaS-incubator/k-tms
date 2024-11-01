package com.kglory.tms.web.services.detectionAnalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.detectionAnalysis.NationAttackMapper;
import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.dto.PolicyDto;
import com.kglory.tms.web.model.vo.AttackHelpVO;
import com.kglory.tms.web.model.vo.PolicyVO;
import com.kglory.tms.web.util.security.AesUtil;

@Service
public class NationAttackService {

    private static Logger logger = LoggerFactory.getLogger(NationAttackService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    NationAttackMapper nationAttackMapper;

    // 공격등록정보 조회 팝업을 위한 탐지 기본 정보를 가져온다. (policy)
	public String selectPolicySignatureRuleBylCode(Long lCode) {

		// PolicyVO selectedData = null;
		PolicyVO selectedData = new PolicyVO();

		PolicyDto policyDto = new PolicyDto();
		policyDto.setlCode(lCode);

		selectedData = policyMapper.selectPolicySignatureRuleBylCode(policyDto);
		if (selectedData != null && selectedData.getSignatureRule() != null) {
			selectedData.setSignatureRule(
					AesUtil.decryptSignature(selectedData.getSignatureRule(), selectedData.getlCode()));
		}

		return selectedData.getSignatureRule();
	}

    // 공격등록정보 

	public AttackHelpVO selectAttackHelpPopupData(AttackDto dto) throws BaseException {
		AttackHelpVO selectedData = null;

		selectedData = nationAttackMapper.selectAttackHelpPopupData(dto);

		Long lCode = dto.getlCode();
		if (selectedData == null) {
			selectedData = new AttackHelpVO();
		}
		String siginatureRule = selectPolicySignatureRuleBylCode(lCode);
		selectedData.setSignatureRule(siginatureRule);

		return selectedData;
	}

}
