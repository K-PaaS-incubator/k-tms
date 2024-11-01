package com.kglory.tms.web.services.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.dashboard.DashboardMapper;
import com.kglory.tms.web.model.dto.StatEPDto;
import com.kglory.tms.web.model.vo.StatEPVO;
import com.kglory.tms.web.model.vo.VictimIpVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.TableFinder;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class DashboardStatEP {

    private static Logger logger = LoggerFactory.getLogger(DashboardStatEP.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DashboardMapper dashboardMapper;
    @Autowired
    OracleMapper oracleMapper;

    /**
     * 공격 TOP5
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<StatEPVO> selectEventTopN(@RequestBody StatEPDto dto) throws BaseException {
        List<StatEPVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("LOG", startDate, endDate, ipType));
        dto.setTableNames(selectTables);

        rtnList = dashboardMapper.selectEventTopN(dto);
        if (rtnList != null && rtnList.size() > 0) {
            long total = dashboardMapper.selectEventTopNTotal(dto);
            for (int i = 0; i < rtnList.size(); i++) {
                rtnList.get(i).setTotalCount(total);
            }
        }
        return rtnList;
    }

    /**
     * 전체/유해트래픽 그래프 조회 데이터
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<StatEPVO> selectStatTraffic(@RequestBody StatEPDto dto) throws BaseException {
        List<StatEPVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> protocolTableNames = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, ipType));
        dto.setProtocolTableNames(protocolTableNames);

        rtnList = dashboardMapper.selectStatTraffic(dto);
        rtnList = ChartUtil.getDashboardStatEpChart(rtnList, startDate, endDate);
        return rtnList;
    }

    /**
     * 전체/유해트래픽 그래프 공격건수 조회 데이터
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<StatEPVO> selectStatEvent(@RequestBody StatEPDto dto) throws BaseException {
        List<StatEPVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> statEPTableNames = oracleMapper.selectTables(TableFinder.getQueryTables("LOG", startDate, endDate, ipType));
        dto.setStatEPTableNames(statEPTableNames);

        rtnList = dashboardMapper.selectStatEvent(dto);
        rtnList = ChartUtil.getDashboardStatEpChart(rtnList, startDate, endDate);
        return rtnList;
    }

    /**
     * 공격건수 TOP5 순위
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<StatEPVO> selectOrg(@RequestBody StatEPDto dto) throws BaseException {
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> statEPTableNames = oracleMapper.selectTables(TableFinder.getQueryTables("LOG", startDate, endDate, ipType));
        dto.setStatEPTableNames(statEPTableNames);

        List<String> protocolTableNames = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, ipType));
        dto.setProtocolTableNames(protocolTableNames);

        return dashboardMapper.selectOrg(dto);
    }

    /**
     * 피해자IP TOP5
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<VictimIpVO> selectVictimIpTopN(@RequestBody StatEPDto dto) throws BaseException {
        List<VictimIpVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("LOG", startDate, endDate, ipType));
        dto.setTableNames(selectTables);

        rtnList = dashboardMapper.selectVictimIpTopN(dto);
        if (rtnList != null && rtnList.size() > 0) {
            VictimIpVO vo = dashboardMapper.selectVictimIpTopNTotal(dto);
            for (int i = 0; i < rtnList.size(); i++) {
                rtnList.get(i).setTotalBps(vo.getTotalBps());
                rtnList.get(i).setTotalNSum(vo.getTotalNSum());
            }
        }
        return rtnList;
    }
}
