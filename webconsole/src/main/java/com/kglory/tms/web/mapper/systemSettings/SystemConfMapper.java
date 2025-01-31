/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.mapper.systemSettings;

import com.kglory.tms.web.model.dto.SystemConfDto;
import com.kglory.tms.web.model.vo.SystemConfVO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author leecjong
 */
public interface SystemConfMapper {
    @Transactional(readOnly = true)
    public List<SystemConfVO> getSystemConfList();
    
    @Transactional
    public void updateSystemConf(SystemConfDto dto);
}
