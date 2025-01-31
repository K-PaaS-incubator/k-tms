/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import com.kglory.tms.web.common.CommonEnum;
import com.kglory.tms.web.common.PropertiesConf;
import com.kglory.tms.web.model.vo.GlobalThreatVO;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.MessageUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class ExportExcel {
    private static Logger logger = LoggerFactory.getLogger(ExportExcel.class);
    
    public static String createGlobalExcel(List<GlobalThreatVO> list) {
        String fileName = "";
        fileName = "GrobalThreat_" + DateTimeUtil.getNowSimpleDateFormat("yyyy_MM_dd_HH_mm_ss") + ".xls";
        int startTableRow = 3;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
//            HSSFSheet sheet = workbook.createSheet("name");
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        HSSFFont txtFont = (HSSFFont)workbook.createFont();
        txtFont.setBold(true);
        txtFont.setFontHeightInPoints((short)25);
        titleStyle.setFont(txtFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        sheet.setColumnWidth(0, 20 * 100);
        sheet.setColumnWidth(1, 30 * 100);
        sheet.setColumnWidth(2, 60 * 100);
        sheet.setColumnWidth(3, 150 * 100);
        sheet.setColumnWidth(4, 40 * 100);
        
        
        HSSFRow row = sheet.createRow(1);
        HSSFCell cell;
        
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TITLE_GLOBAL));
        cell.setCellStyle(titleStyle);
        
        HSSFCellStyle cs1 = workbook.createCellStyle();
        cs1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cs1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setWrapText(true);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        
        row = sheet.createRow(startTableRow);
        cell = row.createCell(0);
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TABLE_HEADER_NUM));
        cell.setCellStyle(cs1);
        cell = row.createCell(1);
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_GLOBALTHREAT));
        cell.setCellStyle(cs1);
        cell = row.createCell(2);
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_TITLE));
        cell.setCellStyle(cs1);
        cell = row.createCell(3);
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_CONTENT));
        cell.setCellStyle(cs1);
        cell = row.createCell(4);
        cell.setCellValue(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_REGDATE));
        cell.setCellStyle(cs1);
        
        GlobalThreatVO item;
        for(int i = 0 ; i < list.size(); i++) {
            item = list.get(i);
            
            row = sheet.createRow(i+1+startTableRow);
            
            cell = row.createCell(0);
            cell.setCellValue(String.valueOf(item.getrNum()));
            cell.setCellStyle(bodyStyle);
            cell = row.createCell(1);
            cell.setCellValue(CommonEnum.getGlrbalThreat(item.getbSeverity()));
            cell.setCellStyle(bodyStyle);
            cell = row.createCell(2);
            cell.setCellValue(item.getStrSubject());
            cell.setCellStyle(bodyStyle);
            cell = row.createCell(3);
            cell.setCellValue(item.getStrContent());
            cell.setCellStyle(bodyStyle);
            cell = row.createCell(4);
            cell.setCellValue(item.getTmUpdate());
            cell.setCellStyle(bodyStyle);
        }
        
        fileWrite(workbook, fileName);
            
        return fileName;
    }
    
    private static void fileWrite(HSSFWorkbook workbook, String fileName) {
        FileOutputStream out = null;
        try {
            FileUtil.createFolder(FileUtil.EXPORT_FOLDER);
            out = new FileOutputStream(FileUtil.EXPORT_FOLDER + fileName);
            workbook.write(out);
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
        	logger.error(e.getLocalizedMessage(), e);
		} finally {
            if (workbook != null) {
                try {workbook.close();} catch(IOException ex) {logger.error(ex.getLocalizedMessage(), ex);}
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                }
            }
        }
    }
}
