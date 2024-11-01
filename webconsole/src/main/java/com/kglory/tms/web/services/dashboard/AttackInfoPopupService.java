package com.kglory.tms.web.services.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.dashboard.AttackInfoPopupMapper;
import com.kglory.tms.web.model.dto.AttackInfoPopupDto;
import com.kglory.tms.web.model.vo.AttackHelpVO;

@Service
public class AttackInfoPopupService {

    private static Logger logger = LoggerFactory.getLogger(AttackInfoPopupService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    AttackInfoPopupMapper attackInfoPopupMapper;
    @Autowired
    PolicyMapper policyMapper;

    public List<AttackHelpVO> selectAttackInfoPopupList(AttackInfoPopupDto dto) throws BaseException {
        List<AttackHelpVO> selectedList = null;

        try {
            // 위험도
            ArrayList<Integer> severities = new ArrayList<Integer>();
            severities = getSeverity(dto);
            dto.setSeverities(severities);

            selectedList = attackInfoPopupMapper.selectAttackInfoPopupList(dto);
            if (selectedList != null && selectedList.size() > 0) {
                AttackHelpVO vo = attackInfoPopupMapper.selectAttackInfoPopupListSeverityCount(dto);
                for(int i = 0 ; i < selectedList.size() ; i++) {
                    selectedList.get(i).setCntHigh(vo.getCntHigh());
                    selectedList.get(i).setCntInfo(vo.getCntInfo());
                    selectedList.get(i).setCntLow(vo.getCntLow());
                    selectedList.get(i).setCntMed(vo.getCntMed());
                    selectedList.get(i).setTotalRowSize(vo.getTotalRowSize());
                    selectedList.get(i).setrNum(dto.getStartRowSize() + i + 1);
                }
            }

        } catch (BaseException e) {
            logger.error("(errorCode)" + e.getLocalizedMessage());
            throw new BaseException(messageSource, "errorCode", null, "", e);
        }

        return selectedList;
    }

    // 위험도
    public ArrayList<Integer> getSeverity(AttackInfoPopupDto dto) throws BaseException {
        // 위험도가 true 이면, 위험도 값 세팅
        ArrayList<Integer> severities = new ArrayList<Integer>();

        if (dto.getSeverityHCheck() != null && dto.getSeverityHCheck()) {
            severities.add(new Integer(Constants.SEVERITY_HIGH));
        }
        if (dto.getSeverityHCheck() != null && dto.getSeverityMCheck()) {
            severities.add(new Integer(Constants.SEVERITY_MEDIUM));
        }
        if (dto.getSeverityHCheck() != null && dto.getSeverityLCheck()) {
            severities.add(new Integer(Constants.SEVERITY_LOW));
        }
        if (dto.getSeverityHCheck() != null && dto.getSeverityICheck()) {
            severities.add(new Integer(Constants.SEVERITY_INFO));
        }

        return severities;
    }

    /**
     * 취약유형 리스트 구하기
     *
     * @return
     */
    public List<AttackHelpVO> selectTypeOfVulnerabilityList() throws BaseException {
        List<AttackHelpVO> selectedList = null;

        selectedList = attackInfoPopupMapper.selectTypeOfVulnerabilityList();


        return selectedList;
    }

}
