package com.kglory.tms.web.services.systemStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import org.springframework.context.MessageSource;
import com.kglory.tms.web.mapper.systemStatus.DbUsageMapper;
import com.kglory.tms.web.model.dto.SearchDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.DbUsageVO;
import com.kglory.tms.web.model.vo.ManagerStateVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.TableFinder;

@Service
public class DbUsageService {

    private static Logger logger = LoggerFactory.getLogger(DbUsageService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DbUsageMapper dbUsageMapper;
    @Autowired
    OracleMapper oracleMapper;

    public List<DbUsageVO> selectDbUsageList() throws BaseException {
        List<DbUsageVO> resultList = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String startDate = formatter.format(Calendar.getInstance().getTime());
        String endDate = formatter.format(Calendar.getInstance().getTime());

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("SYSTEMLOG_TABLESPACE", startDate, endDate));

        if (selectTables.size() > 0) {
            resultList = dbUsageMapper.selectDbUsageList(selectTables);
        } else {
            resultList = new ArrayList<DbUsageVO>();
        }

        return resultList;
    }

    public List<ManagerStateVO> selectManagerStateList() throws BaseException {

        List<ManagerStateVO> resultList = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String startDate = formatter.format(Calendar.getInstance().getTime());
        String endDate = formatter.format(Calendar.getInstance().getTime());

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("SENSOR_ALIVE", startDate, endDate));

        if (selectTables.size() > 0) {
            resultList = dbUsageMapper.selectManagerStateList(selectTables);
            if (resultList.get(0) != null) {
                resultList.get(0).setDbUsed(getDbUsed());
            }
        } else {
            resultList = new ArrayList<ManagerStateVO>();
        }

        return resultList;
    }

    public List<ChartVO> selectManagerStateCpuUsedGraphData(SearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SENSOR_ALIVE", dto.getStartDateInput(), dto.getEndDateInput())));

        selectedList = dbUsageMapper.selectManagerStateCpuUsedGraphData(dto);
        if (selectedList != null && selectedList.size() > 0) {
            ChartVO chart = dbUsageMapper.selectManagerStateCpuUsedGraphDataMinMaxAvg(dto);
            setMinMaxAvgData(selectedList, chart);
        }
        selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());

        return selectedList;
    }

    public List<ChartVO> selectManagerStateMemUsedGraphData(SearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SENSOR_ALIVE", dto.getStartDateInput(), dto.getEndDateInput())));

        selectedList = dbUsageMapper.selectManagerStateMemUsedGraphData(dto);
        if (selectedList != null && selectedList.size() > 0) {
            ChartVO chart = dbUsageMapper.selectManagerStateMemUsedGraphDataMinMaxAvg(dto);
            setMinMaxAvgData(selectedList, chart);
        }
        selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());

        return selectedList;
    }

    public List<ChartVO> selectManagerStateHddUsedGraphData(SearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SENSOR_ALIVE", dto.getStartDateInput(), dto.getEndDateInput())));

        selectedList = dbUsageMapper.selectManagerStateHddUsedGraphData(dto);
        if (selectedList != null && selectedList.size() > 0) {
            ChartVO chart = dbUsageMapper.selectManagerStateHddUsedGraphDataMinMaxAvg(dto);
            setMinMaxAvgData(selectedList, chart);
        }
        selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());

        return selectedList;
    }

    public List<ChartVO> selectManagerStateProcessNumGraphData(SearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SENSOR_ALIVE", dto.getStartDateInput(), dto.getEndDateInput())));

        selectedList = dbUsageMapper.selectManagerStateProcessNumGraphData(dto);
        if (selectedList != null && selectedList.size() > 0) {
            ChartVO chart = dbUsageMapper.selectManagerStateProcessNumGraphDataMinMaxAvg(dto);
            setMinMaxAvgData(selectedList, chart);
        }
        selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());

        return selectedList;
    }

    public void setMinMaxAvgData(List<ChartVO> list, ChartVO chart) {
        if (list != null && list.size() > 0 && chart != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setAvgData(chart.getAvgData());
                list.get(i).setMaxDData(chart.getMaxDData());
                list.get(i).setMinDData(chart.getMinDData());
            }
        }
    }
    
    public String getDbUsed() throws BaseException {
        String dbUsed = "0";
        String line = "";
        String[] arr = new String[50];
        List<String> commandList = new ArrayList<>();
        List<String> rtn = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("df");
        list.add("-h");
        list.add("/logs/");
        rtn = SystemUtil.execCommand(list);
        if (rtn != null && rtn.size() == 2) {
            line = rtn.get(1);
            arr = line.split(" ");
            for(int i = 0 ; i < arr.length ; i++) {
                if (arr[i] != null && !arr[i].isEmpty()) {
                    commandList.add(arr[i]);
                }
            }
        }
        if (commandList.size() >= 6) {
            dbUsed = commandList.get(4).substring(0, commandList.get(4).lastIndexOf("%"));
        }
        return dbUsed;
    }
}
