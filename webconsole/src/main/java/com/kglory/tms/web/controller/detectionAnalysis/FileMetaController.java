package com.kglory.tms.web.controller.detectionAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.FileMetaSearchDto;
import com.kglory.tms.web.model.vo.FileMetaVO;
import com.kglory.tms.web.services.detectionAnalysis.FileMetaService;
import com.kglory.tms.web.util.StringUtil;
import javax.annotation.Resource;
import org.springframework.web.servlet.View;

@Controller
public class FileMetaController {

    private static Logger logger = LoggerFactory.getLogger(FileMetaController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    FileMetaService fileMetaService;

    @Resource(name = "downloadView")
    private View downloadView;

    @RequestMapping(value = "/api/detectionAnalysis/selectFileMetaList", method = RequestMethod.POST)
    @ResponseBody
    public List<FileMetaVO> selectFileMetaList(@RequestBody FileMetaSearchDto dto, BindingResult result) throws BaseException {

        logger.debug("FileMetaController selectFileMetaList FileMetaSearchDto : " + dto);
        List<FileMetaVO> listData = null;
        try {
            listData = fileMetaService.selectFileMetaList(dto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<FileMetaVO>();
        } else {
            return listData;
        }
    }

    /**
     * 도움말 팝업
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/api/detectionAnalysis/selectFileMetaHelpPopupList", method = RequestMethod.POST)
    @ResponseBody
    public FileMetaVO selectFileMetaHelpPopupList(@RequestBody FileMetaSearchDto dto) {
        FileMetaVO fileMetaVO = new FileMetaVO();
        try {
            fileMetaVO = fileMetaService.selectFileMetaHelpPopupList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return fileMetaVO;

    }
}
