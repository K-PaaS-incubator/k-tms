package com.kglory.tms.web.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.vo.MenuVO;

public interface MenuMapper {
	
	@Transactional(readOnly = true)
	List<MenuVO> selectMenu(List<Long> list);
}
