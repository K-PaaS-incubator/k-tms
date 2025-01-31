package com.kglory.tms.web.services.trafficAnalysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.CommonMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.trafficAnalysis.IpMapper;
import com.kglory.tms.web.model.dto.IpDto;
import com.kglory.tms.web.model.vo.IpChartVO;
import com.kglory.tms.web.model.vo.IpVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;
import java.math.BigInteger;

@Service
public class IpService {

    private static Logger logger = LoggerFactory.getLogger(IpService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    IpMapper ipMapper;
    @Autowired
    CommonMapper commonMapper;

    public List<IpVO> selectIpList(IpDto ipDto) throws BaseException {
        List<IpVO> resultList = null;
        String startDate = ipDto.getStartDateInput();
        String endDate = ipDto.getEndDateInput();
        //timeDifference = endDate - startDate를 분으로 환산, 합계의 평균을 구할때 사용
        long timeDifference = timeDifference(startDate, endDate);
        ipDto.setTimeDifference(timeDifference);
        String tableName = "TRAFFIC_IP";
        String ipv6 = "_V6";
        if (ipDto.getIpType() == 6L) {
            tableName = tableName + ipv6;
        }
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables(tableName, startDate, endDate));
        ipDto.setTableNames(selectTables);

        if (ipDto.getToIpInput() == null || ipDto.getToIpInput().isEmpty()) {
            ipDto.setToIp(null);
            ipDto.setFromIp(null);
        } else {
            if (ipDto.getIpType() == 4L) {
                ipDto.setToIp(IpUtil.getHostByteOrderIpToLong(ipDto.getToIpInput()));
                ipDto.setFromIp(IpUtil.getHostByteOrderIpToLong(ipDto.getFromIpInput()));
            } else {
                ipDto.setIpInput(ipDto.getToIpInput());
            }
        }
        
        if (ipDto.getLnetgroupIndex() != null && ipDto.getLnetgroupIndex().intValue() > 0) {
            ipDto.setlNetworkList(commonMapper.selectNetGroupByNetwork(ipDto.getLnetgroupIndex()));
        } else if (ipDto.getLnetworkIndex()!= null && ipDto.getLnetworkIndex().intValue() > 0) {
            List<BigInteger> networkList = new ArrayList<>();
            networkList.add(ipDto.getLnetworkIndex());
            ipDto.setlNetworkList(networkList);
        }

        resultList = ipMapper.selectIpList(ipDto);
        if (resultList != null && resultList.size() > 0) {
            IpVO vo = ipMapper.selectIpListTotalRow(ipDto);
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i) != null) {
                    resultList.get(i).setrNum(Long.valueOf(i + 1));
                    resultList.get(i).setTotalRowSize(vo.getTotalRowSize());
                    resultList.get(i).setSumBps(vo.getSumBps());
                    resultList.get(i).setSumPps(vo.getSumPps());
                }
            }
        }
        return resultList;
    }

    public IpVO selectIpTotal(IpDto ipDto) throws BaseException {

        IpVO resultData = new IpVO();

        String startDate, endDate;
//		String listView = ipDto.getListViewInput();
// 최종값 5분에서 -> 기본검색 12시간(기간에 대한 검색을 다함) 으로 변경 하여 최종시간 조회부분을 주석처리함
//		if(listView.equals("defaultList")){ 
//			//기본 최종값 조회 시에는 startDate 가 변경 된다. 최종 시간으로
//			startDate = ipDto.getStartDateInput();
//			endDate = ipDto.getEndDateInput();
//			List<String> defaultTime = DefaultListTimeFinder.getDefaultTime(startDate, endDate);
//			logger.debug(defaultTime.toString());
//			startDate = defaultTime.get(0);
//			endDate = defaultTime.get(1);
//		}else{
        startDate = ipDto.getStartDateInput();
        endDate = ipDto.getEndDateInput();
//		}
        //가져온 데이터를 재 세팅 해준다.
        ipDto.setStartDateInput(startDate);
        ipDto.setEndDateInput(endDate);

        long timeDifference = timeDifference(startDate, endDate);
        ipDto.setTimeDifference(timeDifference);

        String tableName = "TRAFFIC_IP";
        String ipv6 = "_V6";
        if (ipDto.getIpType() == 6L) {
            tableName = tableName + ipv6;
        }
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables(tableName, startDate, endDate));
        ipDto.setTableNames(selectTables);
        
        if (ipDto.getLnetgroupIndex() != null && ipDto.getLnetgroupIndex().intValue() > 0) {
            ipDto.setlNetworkList(commonMapper.selectNetGroupByNetwork(ipDto.getLnetgroupIndex()));
        } else if (ipDto.getLnetworkIndex()!= null && ipDto.getLnetworkIndex().intValue() > 0) {
            List<BigInteger> networkList = new ArrayList<>();
            networkList.add(ipDto.getLnetworkIndex());
            ipDto.setlNetworkList(networkList);
        }

        resultData = ipMapper.selectIpTotal(ipDto);

        return resultData;
    }

    public List<IpChartVO> selectIpChart(IpDto ipDto) throws BaseException {

        List<IpChartVO> chartList = null;

        String graphItem = ipDto.getGraphItem();
        if (graphItem != null) {
            //ipDto.setGraphLongItem(IpUtil.getIpToLong(ipDto.getGraphItem()));
            Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(ipDto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(ipDto.getEndDateInput()).getTime());
            ipDto.setTimeDiffSecond(chartTimeDiffSecond);

            if (graphItem.equals("-1")) {
                ipDto.setGraphLongItem(-1L);
                ipDto.setStrSourceIp(graphItem);
            } else {
                if (ipDto.getIpType().equals(4L)) {
                    ipDto.setGraphLongItem(IpUtil.getHostByteOrderIpToLong(ipDto.getGraphItem()));
                } else {
                    ipDto.setStrSourceIp(ipDto.getGraphItem());
                }
            }
        }
        String tableName = "TRAFFIC_IP";
        String ipv6 = "_V6";
        if (ipDto.getIpType() == 6L) {
            tableName = tableName + ipv6;
        }
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables(tableName, ipDto.getStartDateInput(), ipDto.getEndDateInput()));
//        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("TRAFFIC_IP", ipDto.getStartDateInput(), ipDto.getEndDateInput()));
        ipDto.setTableNames(selectTables);

        if (selectTables.size() > 0) {
            if (ipDto.getLnetgroupIndex() != null && ipDto.getLnetgroupIndex().intValue() > 0) {
                ipDto.setlNetworkList(commonMapper.selectNetGroupByNetwork(ipDto.getLnetgroupIndex()));
            } else if (ipDto.getLnetworkIndex()!= null && ipDto.getLnetworkIndex().intValue() > 0) {
                List<BigInteger> networkList = new ArrayList<>();
                networkList.add(ipDto.getLnetworkIndex());
                ipDto.setlNetworkList(networkList);
            }
            chartList = ipMapper.selectIpChart(ipDto);
        } else {
            chartList = new ArrayList<IpChartVO>();
        }
        if (chartList.size() > 0) {
            chartList = ChartUtil.getIpChartDateTimeList(chartList, ipDto.getStartDateInput(), ipDto.getEndDateInput());
        }
        return chartList;
    }

    /**
     * // service 팝업( top5 service)
     *
     * @param ipDto
     * @return
     */
    public List<IpVO> selectTop5ServicePopup(IpDto ipDto) throws BaseException {
        List<IpVO> resultData = new ArrayList<IpVO>();
        String tableName = "IP_TRAFFIC";
        String ipv6 = "_V6";
        if (ipDto.getIpType() == 6L) {
            tableName = tableName + ipv6;
        }
//            List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("IP_TRAFFIC", ipDto.getStartDateInput(), ipDto.getEndDateInput()));
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables(tableName, ipDto.getStartDateInput(), ipDto.getEndDateInput()));
        ipDto.setTableNames(selectTables);

        //timeDifference = endDate - startDate를 분으로 환산, 합계의 평균을 구할때 사용
        long timeDifference = timeDifference(ipDto.getStartDateInput(), ipDto.getEndDateInput());
        ipDto.setTimeDifference(timeDifference);
        
        if (ipDto.getIp() == null || ipDto.getIp().isEmpty()) {
            ipDto.setLongIp(null);
        } else {
            if (ipDto.getIpType() == 4L) {
                ipDto.setLongIp(IpUtil.getHostByteOrderIpToLong(ipDto.getIp()));
            } else {
                ipDto.setIpInput(ipDto.getIp());
            }
        }

        resultData = ipMapper.selectTop5ServicePopup(ipDto);
        if (resultData != null && resultData.size() > 0) {
            for(int i = 0 ; i < resultData.size() ; i++) {
                resultData.get(i).setrNum(Long.valueOf(i +1));
            }
        }
        return resultData;
    }

    private long timeDifference(String startDate, String endDate) throws BaseException {
        long min = 0;
        try {
            //시간 설정
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startday = simpleDateFormat.parse(startDate);

            long startTime = startday.getTime();

            Date endDay = simpleDateFormat.parse(endDate);

            long endTime = endDay.getTime();
            long mills = endTime - startTime;

            //분으로 변환
            min = mills / 60000;

        } catch (ParseException e) {
            throw new BaseException(e);
            
        } catch (Exception e) {
        	throw new BaseException(e);
        }
        return min;

    }
}
