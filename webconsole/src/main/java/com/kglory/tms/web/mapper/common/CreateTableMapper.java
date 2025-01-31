package com.kglory.tms.web.mapper.common;

import java.util.HashMap;
import org.springframework.transaction.annotation.Transactional;

public interface CreateTableMapper {
    
    @Transactional
    public void createTable(HashMap map);
    
    @Transactional
    public void insertTempDateTime(HashMap map);
}
