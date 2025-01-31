package com.kglory.tms.web.mapper.securityPolicy;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.YaraRuleDto;
import com.kglory.tms.web.model.vo.YaraRuleVo;

public interface YaraPolicyMapper {
	
	@Transactional(readOnly = true)
	public Long selectYaraRuleLastIndex();

	@Transactional
	public long insertYaraRule(YaraRuleDto dto);

	@Transactional(readOnly = true)
	public List<YaraRuleVo> selectYaraRuleList(YaraRuleDto dto);
        
	@Transactional(readOnly = true)
	public List<YaraRuleVo> selectYaraVandorRuleList();
        
	@Transactional(readOnly = true)
	public List<YaraRuleVo> selectYaraUserRuleList();

	@Transactional
	public void insertYaraGroup(YaraRuleDto dto);

	@Transactional(readOnly = true)
	public List<YaraRuleVo> getYaraGroupList(YaraRuleDto dto);
        
	@Transactional(readOnly = true)
	public YaraRuleVo getYaraGroupDetail(YaraRuleDto dto);
        
	@Transactional(readOnly = true)
	public List<YaraRuleVo> getYaraGroupPerCount(YaraRuleDto dto);

	@Transactional(readOnly = true)
	public YaraRuleVo selectYaraRuleDetailInfo(YaraRuleDto dto);

	@Transactional
	public void updateYaraRule(YaraRuleDto dto);
        
	@Transactional
	public void updateUserYaraRule(YaraRuleDto dto);

	@Transactional
	public void deleteYaraRuleGroupType(YaraRuleDto dto);

	@Transactional
	public void deleteYaraUserRule(YaraRuleDto dto);

	@Transactional(readOnly = true)
	public List<YaraRuleVo> selectYaraRuleGroupType(YaraRuleDto dto);

	@Transactional
	public YaraRuleVo isDuplicateYaraRuleName(YaraRuleDto dto);

	@Transactional
	public YaraRuleVo selectYaraGroupIndex();

	@Transactional
	public void deleteYaraRuleList(YaraRuleDto dto);
}
