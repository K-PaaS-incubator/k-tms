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
import com.kglory.tms.web.mapper.detectionAnalysis.OriginalLogMapper;
import com.kglory.tms.web.model.dto.DetectionAttackHelpDto;
import com.kglory.tms.web.model.dto.OriginalLogSearchDto;
import com.kglory.tms.web.model.dto.PolicyDto;
import com.kglory.tms.web.model.vo.DetectionEventVO;
import com.kglory.tms.web.model.vo.OriginalLogVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.ProtocolEnum;
import com.kglory.tms.web.util.TableFinder;

@Service
public class OriginalLogService {

    private static Logger logger = LoggerFactory.getLogger(OriginalLogService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    OriginalLogMapper originalLogMapper;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;

    /**
     * 원본로그 최근 5분 통계 조회
     *
     * @param dto
     * @return List<OriginalLogVO> resultList
     * @throws BaseException
     */
	public List<OriginalLogVO> selectOriginalLogBylindex(OriginalLogSearchDto dto) throws BaseException {

		List<OriginalLogVO> resultList = null;
		// 위험도
		ArrayList<Integer> severities = new ArrayList<Integer>();
		severities = getSeverity(dto);
		dto.setSeverities(severities);

		// 공격유형
		// ArrayList<Integer> attackTypeLcodes = new ArrayList<Integer>();
		// attackTypeLcodes = getAttackType(dto);
		// dto.setAttackTypeLcodes(attackTypeLcodes);
		// 공격명
		ArrayList<String> splitAttackNameTemp = new ArrayList<String>();
		splitAttackNameTemp = getStrtitle(dto);
		dto.setAttackNames(splitAttackNameTemp);

		String startDate = dto.getStartDateInput();
		String endDate = dto.getEndDateInput();

		String ip = "";
		if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
			ip = "";
		} else {
			ip = "_V6";
		}

		List<String> selectTables = oracleMapper
				.selectTables(TableFinder.getQueryTables("LOG" + ip, startDate, endDate));
		dto.setTableNames(selectTables);
		// 원본패킷 데이터 유무를 조회 해야함
		List<String> selectTablesRawdata = oracleMapper
				.selectTables(TableFinder.getQueryTables("RAWDATA" + ip, startDate, endDate));
		dto.setTableNamesSecond(selectTablesRawdata);

		if (dto.getToIpInput() == null || dto.getToIpInput().isEmpty()) {
			dto.setToIp(null);
			dto.setFromIp(null);
		} else {
			if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
				try {
					dto.setToIp(IpUtil.getHostByteOrderIpToLong(dto.getToIpInput()));
					dto.setFromIp(IpUtil.getHostByteOrderIpToLong(dto.getFromIpInput()));
				} catch (NumberFormatException ex) {
					dto.setToIp(-1L);
					dto.setFromIp(-1L);
				} catch (Exception ex) {
					dto.setToIp(-1L);
					dto.setFromIp(-1L);
				}
			} else {
				dto.setStrSourceIp(dto.getToIpInput());
				dto.setStrDestinationIp(dto.getFromIpInput());
			}
		}

		if (selectTables.size() > 0) {
			resultList = originalLogMapper.selectOriginalLogBylindex(dto);
			OriginalLogVO total = originalLogMapper.selectOriginalLogBylindexTotalCount(dto);
			if (resultList != null && resultList.size() > 0) {
				resultList.get(0).setTotalRowSize(total.getTotalRowSize());
				resultList.get(0).setCntHigh(total.getCntHigh());
				resultList.get(0).setCntInfo(total.getCntInfo());
				resultList.get(0).setCntLow(total.getCntLow());
				resultList.get(0).setCntMed(total.getCntMed());
				for (int i = 0; i < resultList.size(); i++) {
					resultList.get(i).setrNum(Long.valueOf((dto.getStartRowSize() + i + 1)));
				}
			}
		} else {
			resultList = new ArrayList<OriginalLogVO>();
		}
		return resultList;
	}

    /**
     * 상관분석 원본로그 조회
     *
     * @param dto
     * @return List<OriginalLogVO> resultList
     * @throws BaseException
     */
    public List<OriginalLogVO> selectOriginalLogPopupList(OriginalLogSearchDto dto) throws BaseException {
        List<OriginalLogVO> resultList = null;
        // 위험도
        ArrayList<Integer> severities = new ArrayList<Integer>();
        severities = getSeverity(dto);
        dto.setSeverities(severities);

        // 공격유형
        ArrayList<Integer> attackTypeLcodes = new ArrayList<Integer>();
        attackTypeLcodes = getAttackType(dto);
        dto.setAttackTypeLcodes(attackTypeLcodes);

        // 공격명
        ArrayList<String> splitAttackNameTemp = new ArrayList<String>();
        splitAttackNameTemp = getStrtitle(dto);
        dto.setAttackNames(splitAttackNameTemp);

        if (dto.getSrcIp() != null && !dto.getSrcIp().isEmpty()) {
            if (dto.getIpType().equals(4L) || dto.getIpType() == null) {
                try {
                    dto.setSourceIp(IpUtil.getHostByteOrderIpToLong(dto.getSrcIp()));
                } catch (NumberFormatException e) {
                    dto.setSourceIp(-1L);
                } catch (Exception e) {
                    dto.setSourceIp(-1L);
                }
            } else {
                dto.setStrSourceIp(dto.getSrcIp());
            }
        }
        if (dto.getDestIp() != null && !dto.getDestIp().isEmpty()) {
            if (dto.getIpType().equals(4L) || dto.getIpType() == null) {
                try {
                    dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getDestIp()));
                } catch (NumberFormatException e) {
                    dto.setDestinationIp(-1L);
                }  catch (Exception e) {
                    dto.setDestinationIp(-1L);
                }
            } else {
                dto.setStrDestinationIp(dto.getDestIp());
            }
        }
        if (dto.getProtocol() != null && !dto.getProtocol().isEmpty()) {
            dto.setnProtocol(ProtocolEnum.valueOf(dto.getProtocol()).ordinal());
        }

        String ip = "";
        if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
            ip = "";
        } else {
            ip = "_V6";
        }

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("LOG" + ip, dto.getStartDateInput(), dto.getEndDateInput()));
        dto.setTableNames(selectTables);

        // 원본패킷 데이터 유무를 조회 해야함 
        List<String> selectTablesRawdata = oracleMapper.selectTables(TableFinder.getQueryTables("RAWDATA" + ip, dto.getStartDateInput(), dto.getEndDateInput()));
        dto.setTableNamesSecond(selectTablesRawdata);

        if (selectTables.size() > 0) {
            resultList = originalLogMapper.selectOriginalLogPopupList(dto);
            if(resultList != null && resultList.size() > 0) {
                OriginalLogVO total = originalLogMapper.selectOriginalLogPopupListTotal(dto);
                resultList.get(0).setTotalRowSize(total.getTotalRowSize());
                resultList.get(0).setTotal_sum(total.getTotal_sum());
                for(int i = 0 ; i < resultList.size() ; i++) {
                    resultList.get(i).setrNum(Long.valueOf((dto.getStartRowSize() + i + 1)));
                }
            }
        } else {
            resultList = new ArrayList<OriginalLogVO>();
        }
        return resultList;

    }

    /**
     * 원본 패킷 팝업 팝업
     *
     * @param dto
     * @return OriginalLogVO resultData
     */
    public OriginalLogVO selectRawPacketPopup(OriginalLogSearchDto dto) {
        OriginalLogVO resultData = null;
        String startDate, endDate;
        startDate = dto.getStartDateInput();
        endDate = dto.getEndDateInput();

        dto.setStartDateInput(startDate);
        dto.setEndDateInput(endDate);

        String ip = "";
        if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
            ip = "";
        } else {
            ip = "_V6";
        }

        List<String> selectTablesRowdata = oracleMapper.selectTables(TableFinder.getQueryTables("RAWDATA" + ip, startDate, endDate));
        List<String> selectTablesLog = oracleMapper.selectTables(TableFinder.getQueryTables("LOG" + ip, startDate, endDate));
        dto.setTableNames(selectTablesRowdata);
        dto.setTableNamesSecond(selectTablesLog);
        resultData = originalLogMapper.selectRawPacketPopup(dto);

        return resultData;
    }

    //공통 함수

    public ArrayList<Integer> getSeverity(OriginalLogSearchDto dto) throws BaseException {
        //위험도가 true 이면, 위험도 값 세팅
        ArrayList<Integer> severities = new ArrayList<Integer>();

        if (dto.getSeverityHCheck() != null && dto.getSeverityHCheck()) {
            severities.add(new Integer(Constants.SEVERITY_HIGH));
        }
        if (dto.getSeverityMCheck() != null && dto.getSeverityMCheck()) {
            severities.add(new Integer(Constants.SEVERITY_MEDIUM));
        }
        if (dto.getSeverityLCheck() != null && dto.getSeverityLCheck()) {
            severities.add(new Integer(Constants.SEVERITY_LOW));
        }
        if (dto.getSeverityICheck() != null && dto.getSeverityICheck()) {
            severities.add(new Integer(Constants.SEVERITY_INFO));
        }

        return severities;
    }

    public ArrayList<String> getStrtitle(OriginalLogSearchDto dto) throws BaseException {
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

    public ArrayList<Integer> getAttackType(OriginalLogSearchDto dto) throws BaseException {
        ArrayList<Integer> attackTypeLcodes = new ArrayList<Integer>();

        BigInteger attackTypeSelect = dto.getAttackTypeSelect();

        if (attackTypeSelect != null && !attackTypeSelect.equals(BigInteger.ZERO)) {
            PolicyDto policyDto = new PolicyDto();
            policyDto.setAttackType(attackTypeSelect);
            attackTypeLcodes = policyMapper.selectPolicyLcodeByAttackType(policyDto);
        }

        return attackTypeLcodes;
    }

    /**
     * 공격 탐지 정보 조회
     *
     * @param dto
     * @return DetectionEventVO selectedList
     * @throws BaseException
     */
    public DetectionEventVO selectDetectionAttackHelpPopupData(DetectionAttackHelpDto dto) throws BaseException {

        DetectionEventVO selectedList = new DetectionEventVO();
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        String ip = "";
        if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
            ip = "";
        } else {
            ip = "_V6";
        }

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("LOG" + ip, startDate, endDate));
        dto.setTableNames(selectTables);

        if (dto.getStrSrcIp() != null && !dto.getStrSrcIp().isEmpty()) {
            if (dto.getIpType().equals(4L)) {
                try {
                    dto.setSourceIp(IpUtil.getHostByteOrderIpToLong(dto.getStrSrcIp()));
                } catch (NumberFormatException e) {
                    dto.setSourceIp(-1L);
                } catch (Exception e) {
                    dto.setSourceIp(-1L);
                }
            } else {
                dto.setStrSourceIp(dto.getStrSrcIp());
            }
        }
        if (dto.getStrDestIp() != null && dto.getStrDestIp().isEmpty()) {
            if (dto.getIpType().equals(4L)) {
                try {
                    dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getStrDestIp()));
                } catch (NumberFormatException e) {
                    dto.setDestinationIp(-1L);
                } catch (Exception e) {
                    dto.setDestinationIp(-1L);
                }
            } else {
                dto.setStrDestinationIp(dto.getStrDestIp());
            }
        }
        selectedList = originalLogMapper.selectDetectionAttackHelpPopupData(dto);

        return selectedList;
    }

}
