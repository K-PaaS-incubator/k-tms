package com.kglory.tms.web.services.securityPolicy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.securityPolicy.YaraPolicyMapper;
import com.kglory.tms.web.model.dto.YaraRuleDto;
import com.kglory.tms.web.model.vo.YaraRuleVo;
import com.kglory.tms.web.util.file.FileUtil;
import com.kglory.tms.web.util.security.AesUtil;

@Service
public class YaraPolicyService {

    private static Logger logger = LoggerFactory.getLogger(YaraPolicyService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    YaraPolicyMapper yaraPolicyMapper;

    /**
     * YARA 룰 신규 생성시 Max 인덱스 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public Long selectYaraRuleLastIndex() {
        return yaraPolicyMapper.selectYaraRuleLastIndex();
    }

    /**
     * YARA 룰 신규 생성 신규 생성시 인덱스를 셋팅
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public long insertYaraRule(YaraRuleDto dto) throws BaseException {
        long insertIndex = 0;
        dto.setMeta(AesUtil.encryptSignature(dto.getMeta(), dto.getlIndex()));
        dto.setStrings(AesUtil.encryptSignature(dto.getStrings(), dto.getlIndex()));
        dto.setCondition(AesUtil.encryptSignature(dto.getCondition(), dto.getlIndex()));
        yaraPolicyMapper.insertYaraRule(dto);
        insertIndex = dto.getlIndex();
        FileUtil.writeYaraPolicy(selectYaraUserRuleList(), FileUtil.YARA_USER_POL, FileUtil.YARA_USER_POL_SEC);
        return insertIndex;
    }

    /**
     * YARA 룰 목록 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<YaraRuleVo> selectYaraRuleList(YaraRuleDto dto) throws BaseException {
        List<YaraRuleVo> result = null;
        result = yaraPolicyMapper.selectYaraRuleList(dto);
        yaraDecription(result);
        return result;
    }

    public List<YaraRuleVo> selectYaraVandorRuleList() throws BaseException {
        List<YaraRuleVo> result = null;
        result = yaraPolicyMapper.selectYaraVandorRuleList();
        yaraDecription(result);
        return result;
    }

    public List<YaraRuleVo> selectYaraUserRuleList() throws BaseException {
        List<YaraRuleVo> result = null;
        result = yaraPolicyMapper.selectYaraUserRuleList();
        yaraDecription(result);
        return result;
    }

    /**
     * YARA 룰 그룹 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<YaraRuleVo> getYaraGroupList(YaraRuleDto dto) throws BaseException {
        List<YaraRuleVo> result = null;
        result = yaraPolicyMapper.getYaraGroupList(dto);

        YaraRuleDto groupCount = new YaraRuleDto();
        if (result != null && result.size() > 0) {
            for (YaraRuleVo item : result) {
                groupCount.addGroupList(item.getGroupIndex());
            }
            List<YaraRuleVo> groupPerCount = yaraPolicyMapper.getYaraGroupPerCount(groupCount);
            if (groupPerCount != null && groupPerCount.size() > 0) {
                int total = 0;
                for (int i = 0; i < result.size(); i++) {
                    total = 0;
                    for (YaraRuleVo count : groupPerCount) {
                        if (result.get(i).getGroupIndex().equals(count.getGroupIndex())) {
                            total = count.getRuleTotal();
                            break;
                        }
                    }
                    result.get(i).setRuleTotal(total);
                }
            }
        }
        return result;
    }

    /**
     * YARA 룰 목록에 대한 상세 정보 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public YaraRuleVo selectYaraRuleDetailInfo(YaraRuleDto dto) throws BaseException {
        YaraRuleVo result = new YaraRuleVo();
        result = yaraPolicyMapper.selectYaraRuleDetailInfo(dto);
        if (result != null && result.getlIndex() != null) {
            result.setMeta(AesUtil.decryptSignature(result.getMeta(), result.getlIndex()));
            result.setStrings(AesUtil.decryptSignature(result.getStrings(), result.getlIndex()));
            result.setCondition(AesUtil.decryptSignature(result.getCondition(), result.getlIndex()));
        }
        return result;
    }

    /**
     * YARA 룰 상세 정보 update
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public boolean updateYaraRule(YaraRuleDto dto) throws BaseException {
        boolean rtn = false;
        YaraRuleVo vo = yaraPolicyMapper.selectYaraRuleDetailInfo(dto);
        if (dto.getGroupIndex() < 99) {
            if (dto.getlUsed() != vo.getlUsed()) {
                yaraPolicyMapper.updateYaraRule(dto);
                FileUtil.writeYaraPolicy(selectYaraVandorRuleList(), FileUtil.YARA_POL, FileUtil.YARA_POL_SEC);
                rtn = true;
            }
        } else {
            if (!dto.equals(vo)) {
                dto.setMeta(AesUtil.encryptSignature(dto.getMeta(), dto.getlIndex()));
                dto.setStrings(AesUtil.encryptSignature(dto.getStrings(), dto.getlIndex()));
                dto.setCondition(AesUtil.encryptSignature(dto.getCondition(), dto.getlIndex()));
                yaraPolicyMapper.updateUserYaraRule(dto);
                FileUtil.writeYaraPolicy(selectYaraUserRuleList(), FileUtil.YARA_USER_POL, FileUtil.YARA_USER_POL_SEC);
                rtn = true;
            }
        }
        return rtn;
    }

    /**
     * YARA 룰 그룹 생성
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public void insertYaraGroup(YaraRuleDto dto) throws BaseException {
        yaraPolicyMapper.insertYaraGroup(dto);
    }

    /**
     * YARA 룰 그룹 삭제
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public void deleteYaraRuleGroupType(YaraRuleDto dto) throws BaseException {
        YaraRuleVo result = yaraPolicyMapper.getYaraGroupDetail(dto);
        dto.setGroupName(result.getGroupName());
        yaraPolicyMapper.deleteYaraRuleGroupType(dto);
    }

    /**
     * YARA 룰 삭제
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public void deleteYaraUserRule(YaraRuleDto dto) throws BaseException {
        yaraPolicyMapper.deleteYaraUserRule(dto);
        FileUtil.writeYaraPolicy(selectYaraUserRuleList(), FileUtil.YARA_USER_POL, FileUtil.YARA_USER_POL_SEC);
    }

    /**
     * YARA 그룹 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<YaraRuleVo> selectYaraRuleGroupType(YaraRuleDto dto) throws BaseException {
        List<YaraRuleVo> result = null;
        result = yaraPolicyMapper.selectYaraRuleGroupType(dto);
        return result;
    }

    /**
     * 룰 명칭 중복 검사
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public YaraRuleVo isDuplicateYaraRuleName(YaraRuleDto dto) throws BaseException {
        YaraRuleVo result = new YaraRuleVo();
        result = yaraPolicyMapper.isDuplicateYaraRuleName(dto);
        if (result == null) {
            return new YaraRuleVo();
        }
        return result;
    }

    /**
     * 그룹유형 인덱스 조회
     *
     * @return
     * @throws BaseException
     */
    public YaraRuleVo selectYaraGroupIndex() throws BaseException {
        YaraRuleVo result = new YaraRuleVo();
        result = yaraPolicyMapper.selectYaraGroupIndex();
        return result;
    }

    public void writeYaraPolicy() throws BaseException{
        FileUtil.writeYaraPolicy(selectYaraVandorRuleList(), FileUtil.YARA_POL, FileUtil.YARA_POL_SEC);
        FileUtil.writeYaraPolicy(selectYaraUserRuleList(), FileUtil.YARA_USER_POL, FileUtil.YARA_USER_POL_SEC);
    }

    private void yaraDecription(List<YaraRuleVo> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getlIndex() != null) {
                    list.get(i).setMeta(AesUtil.decryptSignature(list.get(i).getMeta(), list.get(i).getlIndex()));
                    list.get(i).setStrings(AesUtil.decryptSignature(list.get(i).getStrings(), list.get(i).getlIndex()));
                    list.get(i).setCondition(AesUtil.decryptSignature(list.get(i).getCondition(), list.get(i).getlIndex()));
                }
            }
        }
    }
}
