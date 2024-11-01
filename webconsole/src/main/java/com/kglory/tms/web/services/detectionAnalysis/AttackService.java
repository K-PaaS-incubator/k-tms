package com.kglory.tms.web.services.detectionAnalysis;

import java.math.BigInteger;
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
import com.kglory.tms.web.mapper.detectionAnalysis.AttackMapper;
import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.dto.PolicyDto;
import com.kglory.tms.web.model.vo.AttackVO;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.ProtocolEnum;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;

@Service
public class AttackService {
	
	private static Logger	logger= LoggerFactory.getLogger(AttackService.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	AttackMapper				attackMapper;
	@Autowired
	PolicyMapper				policyMapper;
	@Autowired
	OracleMapper				oracleMapper;
	
	// 공통 함수
	public ArrayList<Integer> getSeverity(AttackDto dto) throws BaseException {
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
	
	public ArrayList<String> getStrtitle(AttackDto dto) throws BaseException {
		String fullAttackName = dto.getAttackNameInput();
		
		ArrayList<String> splitAttackNameTemp = new ArrayList<String>();
		if (fullAttackName != null && !fullAttackName.isEmpty()) {
			String[] splitAttackName = fullAttackName.split(";");
			
			if (splitAttackName.length > 0) {
				for (int i = 0; i < splitAttackName.length; i++) {
					splitAttackNameTemp.add(new String(splitAttackName[i]));
				}
			}
		}
		
		return splitAttackNameTemp;
	}
	
	public ArrayList<Integer> getAttackType(AttackDto dto) throws BaseException {
		ArrayList<Integer> attackTypeLcodes = new ArrayList<Integer>();
		
		BigInteger attackTypeSelect = dto.getAttackTypeSelect();
		if (attackTypeSelect != null && !attackTypeSelect.equals(BigInteger.ZERO)) {
			PolicyDto policyDto = new PolicyDto();
			policyDto.setAttackType(attackTypeSelect);
			attackTypeLcodes = policyMapper.selectPolicyLcodeByAttackType(policyDto);
		}
		
		return attackTypeLcodes;
	}

	public List<ChartVO> selectAttackAttackCountGraphData(AttackDto dto) throws BaseException {
		List<ChartVO> selectedList = null;
		dto.setTableNames(oracleMapper.selectTables(
				TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

		// 데이터 0처리 추가
		String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
		dto.setTableUnit(chartTableUnit);

		Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(),
				TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
		dto.setTimeDiffSecond(chartTimeDiffSecond);

		selectedList = attackMapper.selectAttackAttackCountGraphData(dto);
		ChartVO chart = attackMapper.selectAttackAttackCountGraphDataMinMax(dto);
		ChartVO chartAvg = attackMapper.selectAttackAttackCountGraphDataAvg(dto);
		if (selectedList != null && selectedList.size() > 0) {
			selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
			for (int i = 0; i < selectedList.size(); i++) {
				selectedList.get(i).setMinDData(chart.getMinDData());
				selectedList.get(i).setMaxDData(chart.getMaxDData());
				selectedList.get(i).setSumData(chartAvg.getSumData());
			}
		}

		return selectedList;
	}

	public List<ChartVO> selectAttackBpsGraphData(AttackDto dto) throws BaseException {
		List<ChartVO> selectedList = null;
		dto.setTableNames(oracleMapper.selectTables(
				TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

		// 데이터 0처리 추가
		String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
		dto.setTableUnit(chartTableUnit);

		Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(),
				TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
		dto.setTimeDiffSecond(chartTimeDiffSecond);

		selectedList = attackMapper.selectAttackBpsGraphData(dto);
		ChartVO chart = attackMapper.selectAttackBpsGraphDataMinMax(dto);
		ChartVO chartAvg = attackMapper.selectAttackBpsGraphDataAvg(dto);
		if (selectedList != null && selectedList.size() > 0) {
			selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
			for (int i = 0; i < selectedList.size(); i++) {
				selectedList.get(i).setMinDData(chart.getMinDData());
				selectedList.get(i).setMaxDData(chart.getMaxDData());
				selectedList.get(i).setAvgData(chartAvg.getAvgData());
			}
		}

		return selectedList;
	}

	public List<ChartVO> selectAttackPpsGraphData(AttackDto dto) throws BaseException {
		List<ChartVO> selectedList = null;
		dto.setTableNames(oracleMapper.selectTables(
				TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

		// 데이터 0처리 추가
		String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
		dto.setTableUnit(chartTableUnit);

		Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(),
				TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
		dto.setTimeDiffSecond(chartTimeDiffSecond);

		selectedList = attackMapper.selectAttackPpsGraphData(dto);
		ChartVO chart = attackMapper.selectAttackPpsGraphDataMinMax(dto);
		ChartVO chartAvg = attackMapper.selectAttackPpsGraphDataAvg(dto);
		if (selectedList != null && selectedList.size() > 0) {
			selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
			for (int i = 0; i < selectedList.size(); i++) {
				selectedList.get(i).setMinDData(chart.getMinDData());
				selectedList.get(i).setMaxDData(chart.getMaxDData());
				selectedList.get(i).setAvgData(chartAvg.getAvgData());
			}
		}

		return selectedList;
	}
}
