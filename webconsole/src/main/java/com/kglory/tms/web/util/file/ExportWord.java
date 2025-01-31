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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class ExportWord {

    private static Logger logger = LoggerFactory.getLogger(ExportWord.class);

    public static String createGlobalWord(List<GlobalThreatVO> list) {
        String fileName = "";
        fileName = "GrobalThreat_" + DateTimeUtil.getNowSimpleDateFormat("yyyy_MM_dd_HH_mm_ss") + ".doc";
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        title.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(MessageUtil.getMessage(PropertiesConf.TITLE_GLOBAL));
        titleRun.setFontSize(25);
        titleRun.setBold(true);
        if (list != null && list.size() > 0) {
            XWPFTable table = document.createTable(1, 5);
            
            CTTblLayoutType type = table.getCTTbl().getTblPr().addNewTblLayout();
            type.setType(STTblLayoutType.FIXED);

            CTBody body = document.getDocument().getBody();
            if (!body.isSetSectPr()) {
                body.addNewSectPr();
            }
            CTSectPr section = body.getSectPr();
            if (!section.isSetPgSz()) {
                section.addNewPgSz();
            }
            CTPageSz pageSize = section.getPgSz();

            pageSize.setOrient(STPageOrientation.LANDSCAPE);
            pageSize.setW(BigInteger.valueOf(842 * 20));
            pageSize.setH(BigInteger.valueOf(595 * 20));
            
            table.getRow(0).getCell(0).setText(MessageUtil.getMessage(PropertiesConf.TABLE_HEADER_NUM));
            table.getRow(0).getCell(1).setText(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_GLOBALTHREAT));
            table.getRow(0).getCell(2).setText(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_TITLE));
            table.getRow(0).getCell(3).setText(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_CONTENT));
            table.getRow(0).getCell(4).setText(MessageUtil.getMessage(PropertiesConf.TABLE_DEADER_REGDATE));
            
            XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().get(0);
            p1.setAlignment(ParagraphAlignment.CENTER);
            p1.setVerticalAlignment(TextAlignment.CENTER);
            XWPFParagraph p2 = table.getRow(0).getCell(1).getParagraphs().get(0);
            p2.setAlignment(ParagraphAlignment.CENTER);
            p2.setVerticalAlignment(TextAlignment.CENTER);
            XWPFParagraph p3 = table.getRow(0).getCell(2).getParagraphs().get(0);
            p3.setAlignment(ParagraphAlignment.CENTER);
            p3.setVerticalAlignment(TextAlignment.CENTER);
            XWPFParagraph p4 = table.getRow(0).getCell(3).getParagraphs().get(0);
            p4.setAlignment(ParagraphAlignment.CENTER);
            p4.setVerticalAlignment(TextAlignment.CENTER);
            XWPFParagraph p5 = table.getRow(0).getCell(4).getParagraphs().get(0);
            p5.setAlignment(ParagraphAlignment.CENTER);
            p5.setVerticalAlignment(TextAlignment.CENTER);

            setCellWidth(table.getRow(0).getCell(0), 60);
            setCellWidth(table.getRow(0).getCell(1), 80);
            setCellWidth(table.getRow(0).getCell(2), 160);
            setCellWidth(table.getRow(0).getCell(3), 300);
            setCellWidth(table.getRow(0).getCell(4), 100);

            table.getRow(0).getTableCells().add(0, null);
            for (GlobalThreatVO item : list) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(item.getrNum()));
                row.getCell(1).setText(CommonEnum.getGlrbalThreat(item.getbSeverity()));
                row.getCell(2).setText(item.getStrSubject());
                row.getCell(3).setText(item.getStrContent());
                row.getCell(4).setText(item.getTmUpdate());
                
                XWPFParagraph row1 = row.getCell(0).getParagraphs().get(0);
                row1.setAlignment(ParagraphAlignment.CENTER);
                XWPFParagraph row2 = row.getCell(1).getParagraphs().get(0);
                row2.setAlignment(ParagraphAlignment.CENTER);
                XWPFParagraph row3 = row.getCell(2).getParagraphs().get(0);
                row3.setAlignment(ParagraphAlignment.LEFT);
                XWPFParagraph row4 = row.getCell(3).getParagraphs().get(0);
                row4.setAlignment(ParagraphAlignment.LEFT);
                XWPFParagraph row5 = row.getCell(4).getParagraphs().get(0);
                row5.setAlignment(ParagraphAlignment.CENTER);
                
            }
            fileWrite(document, fileName);
        }
        return fileName;
    }
    
    private static void setCellWidth(XWPFTableCell cell, int value) {
        cell.setColor("e0e0e0");
        CTTblWidth cellWidth = cell.getCTTc().addNewTcPr().addNewTcW();
//            CTTcPr pr = cell.getCTTc().addNewTcPr();
//            pr.addNewNoWrap();
        cellWidth.setType(STTblWidth.DXA);
        cellWidth.setW(BigInteger.valueOf(value * 20));
    }

    private static void fileWrite(XWPFDocument document, String fileName) {
        FileOutputStream out = null;
        try {
            FileUtil.createFolder(FileUtil.EXPORT_FOLDER);
            out = new FileOutputStream(FileUtil.EXPORT_FOLDER + fileName);
            document.write(out);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
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
