package com.kglory.tms.web.services.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.controller.common.CommonController;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.common.CommonMapper;
import com.kglory.tms.web.model.dto.TableColumnsDto;
import com.kglory.tms.web.model.dto.TargetOrgDto;
import com.kglory.tms.web.model.dto.TargetSensorDto;
import com.kglory.tms.web.model.vo.TableColumnsVO;
import com.kglory.tms.web.model.vo.TargetVO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CommonService {
 
	private static Logger	logger= LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	MessageSource 	messageSource;
	@Autowired
	CommonMapper 	commonMapper;
	
	public List<TargetVO> selectTargetOrg(TargetOrgDto dto) throws BaseException{
		List<TargetVO> resultList = commonMapper.selectTargetOrg(dto);
		if (resultList != null && resultList.size() > 0) {
                    for(TargetVO item : resultList) {
                        item.setCntYType(resultList.size());
                    }
                }
		return resultList;
	}
	
        /**
         * 메뉴, 사용자 별 테이블 컬럼 조회
         * @param dto
         * @return
         * @throws BaseException 
         */
        public List<TableColumnsVO> selectTableColumnList(TableColumnsDto dto)  throws BaseException{
            return commonMapper.selectTableColumnList(dto);
        }
        
        public HashMap<String, String> selectTableColumns(TableColumnsDto dto) throws BaseException{
            HashMap<String, String> map = new HashMap<>();
            List<TableColumnsVO> list = this.selectTableColumnList(dto);
            if (list != null && list.size() > 0) {
                for (TableColumnsVO item : list) {
                    map.put(item.getColId(), item.getEnabled());
                }
            }
            return map;
        }
        
        /**
         * 메뉴, 사용자 별 테이블 컬럼 등록
         * @param dto
         * @throws BaseException 
         */
        public void insertTableColumn(List<TableColumnsDto> listDto, String userId)  throws BaseException{
            for (TableColumnsDto dto : listDto) {
                dto.setUserId(userId);
                commonMapper.insertTableColumn(dto);
            }
        }

        public void deleteTableColumn(TableColumnsDto dto)  throws BaseException{
            commonMapper.deleteTableColumn(dto);
        }
        
        public List<BigInteger> selectNetGroupByNetwork(BigInteger lnetgroupIndex) {
            List<BigInteger> rtnList = null;
            rtnList = commonMapper.selectNetGroupByNetwork(lnetgroupIndex);
            return rtnList;
        }
}
