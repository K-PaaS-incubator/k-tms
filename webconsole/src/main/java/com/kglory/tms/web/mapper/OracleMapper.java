package com.kglory.tms.web.mapper;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface OracleMapper {

    @Transactional(readOnly = true)
    public List<String> selectTables(List<String> tableNames);

    @Transactional(readOnly = true)
    public String selectNowDbDateTime();

    @Transactional(readOnly = true)
    public String isTable(String tableName);
    
    @Transactional(readOnly = true)
    public List<String> selectTableNames(HashMap<String, String> map);
    
    @Transactional(readOnly = true)
    public List<String> selectTableDeleteNames(HashMap<String, String> map);
    
    @Transactional
    public void dropTables(List<String> list);
    
    @Transactional
    public void dropTable(HashMap<String, String> map);
    
    @Transactional
    public void dropTableIndex(HashMap<String, String> map);
    
    @Transactional(readOnly = true)
    public List<String> selectTableDateList();
    
    @Transactional(readOnly = true)
    public List<String> selectTableNameList(String tableDate);
}
