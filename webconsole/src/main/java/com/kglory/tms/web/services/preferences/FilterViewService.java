package com.kglory.tms.web.services.preferences;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.preferences.FilterViewMapper;
import com.kglory.tms.web.model.dto.FilterViewAnomalyDto;
import com.kglory.tms.web.model.dto.FilterViewApplicationDto;
import com.kglory.tms.web.model.dto.FilterViewDetectionDto;
import com.kglory.tms.web.model.dto.FilterViewFileMetaDto;
import com.kglory.tms.web.model.dto.FilterViewSessionDto;
import com.kglory.tms.web.model.vo.FilterViewAnomalyVO;
import com.kglory.tms.web.model.vo.FilterViewApplicationVO;
import com.kglory.tms.web.model.vo.FilterViewDetectionVO;
import com.kglory.tms.web.model.vo.FilterViewFileMetaVO;
import com.kglory.tms.web.model.vo.FilterViewSessionVO;

@Service
public class FilterViewService {

    private static Logger logger = LoggerFactory.getLogger(FilterViewService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    FilterViewMapper filterViewMapper;

    /*
     * 세션 필터뷰 목록 조회 
     */
    public List<FilterViewSessionVO> selectFilterViewSession(FilterViewSessionDto dto) throws BaseException {
        List<FilterViewSessionVO> result = null;
        result = filterViewMapper.selectFilterViewSession(dto);
        return result;
    }

    /*
     * 세션 필터뷰 등록 
     */
    public long insertFilterViewSession(FilterViewSessionDto dto) throws BaseException {
        long insertLIndex = 0;
        filterViewMapper.insertFilterViewSession(dto);

        insertLIndex = dto.getlIndex();

        return insertLIndex;
    }
    /*
     * 세션 필터뷰 수정
     */

    public Integer updateFilterViewSession(FilterViewSessionDto dto) throws BaseException {
        int rtn = 0;
        FilterViewSessionVO vo = filterViewMapper.selectFilterViewSessionDetail(dto);
        if (!dto.equals(vo)) {
            filterViewMapper.updateFilterViewSession(dto);
            rtn = 1;
        }
        return rtn;
    }
    /*
     * 세션 필터뷰 삭제
     */

    public void deleteFilterViewSession(FilterViewSessionDto dto) throws BaseException {
        filterViewMapper.deleteFilterViewSession(dto);
    }

    /*
     * 침입탐지 필터뷰 목록 조회 
     */
    public List<FilterViewDetectionVO> selectFilterViewDetection(FilterViewDetectionDto dto) throws BaseException {
        List<FilterViewDetectionVO> result = null;
        result = filterViewMapper.selectFilterViewDetection(dto);
        return result;
    }

    /*
     * 침입탐지 필터뷰 등록 
     */
    public long insertFilterViewDetection(FilterViewDetectionDto dto) throws BaseException {
        long insertLIndex = 0;
        filterViewMapper.insertFilterViewDetection(dto);

        insertLIndex = dto.getlIndex();

        return insertLIndex;
    }
    /*
     * 침입탐지 필터뷰 수정
     */

    public Integer updateFilterViewDetection(FilterViewDetectionDto dto) throws BaseException {
        int rtn = 0;
        FilterViewDetectionVO vo = filterViewMapper.selectFilterViewDetectionDetail(dto);
        if (!dto.equals(vo)) {
            filterViewMapper.updateFilterViewDetection(dto);
            rtn = 1;
        }
        return rtn;
    }

    /*
     * 침입탐지 필터뷰 삭제
     */
    public void deleteFilterViewDetection(FilterViewDetectionDto dto) throws BaseException {
        filterViewMapper.deleteFilterViewDetection(dto);
    }

    /*
     * Application 필터뷰 목록 조회 
     */
    public List<FilterViewApplicationVO> selectFilterViewApplication(FilterViewApplicationDto dto) throws BaseException {
        List<FilterViewApplicationVO> result = null;
        result = filterViewMapper.selectFilterViewApplication(dto);
        return result;
    }

    /*
     * Application 필터뷰 등록 
     */
    public long insertFilterViewApplication(FilterViewApplicationDto dto) throws BaseException {
        long insertLIndex = 0;
        filterViewMapper.insertFilterViewApplication(dto);

        insertLIndex = dto.getlIndex();

        return insertLIndex;
    }

    /*
     * Application 필터뷰 수정
     */
    public Integer updateFilterViewApplication(FilterViewApplicationDto dto) throws BaseException {
        int rtn = 0;
        FilterViewApplicationVO vo = filterViewMapper.selectFilterViewApplicationDetail(dto);
        if (!dto.equals(vo)) {
            filterViewMapper.updateFilterViewApplication(dto);
            rtn = 1;
        }

        return rtn;
    }

    /*
     * Application 필터뷰 삭제
     */
    public void deleteFilterViewApplication(FilterViewApplicationDto dto) throws BaseException {
        filterViewMapper.deleteFilterViewApplication(dto);
    }

    /*
     * FileMeta 필터뷰 목록 조회 
     */
    public List<FilterViewFileMetaVO> selectFilterViewFileMeta(FilterViewFileMetaDto dto) throws BaseException {
        List<FilterViewFileMetaVO> result = null;
        result = filterViewMapper.selectFilterViewFileMeta(dto);
        return result;
    }

    /*
     * FileMeta 필터뷰 등록 
     */
    public long insertFilterViewFileMeta(FilterViewFileMetaDto dto) throws BaseException {
        long insertLIndex = 0;
        filterViewMapper.insertFilterViewFileMeta(dto);

        insertLIndex = dto.getlIndex();

        return insertLIndex;
    }

    /*
     * FileMeta 필터뷰 수정
     */
    public Integer updateFilterViewFileMeta(FilterViewFileMetaDto dto) throws BaseException {
        int rtn = 0;
        FilterViewFileMetaVO vo = filterViewMapper.selectFilterViewFileMetaDetail(dto);
        if (!dto.equals(vo)) {
            filterViewMapper.updateFilterViewFileMeta(dto);
            rtn = 1;
        }
        return rtn;
    }

    /*
     * FileMeta 필터뷰 삭제
     */
    public void deleteFilterViewFileMeta(FilterViewFileMetaDto dto) throws BaseException {
        filterViewMapper.deleteFilterViewFileMeta(dto);

    }

    /**
     * 침입탐지 필터뷰 중복 검사
     *
     * @param dto
     * @return
     */
    public FilterViewDetectionVO isDuplicateDetectionFilterName(FilterViewDetectionDto dto) throws BaseException {
        FilterViewDetectionVO result = new FilterViewDetectionVO();
        result = filterViewMapper.isDuplicateDetectionFilterName(dto);
        if (result == null) {
            return new FilterViewDetectionVO();
        } else {
            return result;
        }
    }

    public FilterViewSessionVO isDuplicateSessionFilterName(FilterViewSessionDto dto) throws BaseException {
        FilterViewSessionVO result = new FilterViewSessionVO();
        result = filterViewMapper.isDuplicateSessionFilterName(dto);
        if (result == null) {
            return new FilterViewSessionVO();
        }
        return result;
    }

    public FilterViewApplicationVO isDuplicateApplayerFilterName(FilterViewApplicationDto dto) throws BaseException{
        FilterViewApplicationVO result = new FilterViewApplicationVO();
        result = filterViewMapper.isDuplicateApplayerFilterName(dto);
        if (result == null) {
            return new FilterViewApplicationVO();
        }
        return result;
    }

    public FilterViewFileMetaVO isDuplicateFileMetaFilterName(FilterViewFileMetaDto dto) throws BaseException{
        FilterViewFileMetaVO result = new FilterViewFileMetaVO();
        result = filterViewMapper.isDuplicateFileMetaFilterName(dto);
        if (result == null) {
            return new FilterViewFileMetaVO();
        }
        return result;
    }
}
