/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import com.kglory.tms.web.util.DateTimeUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author leecjong
 */
public abstract class AbstractCsvView extends AbstractView {

    private String fileName = DateTimeUtil.getNow() + ".csv";

    public void setFileName(String fileName) {
        this.fileName = fileName + "_" +DateTimeUtil.getNow() + ".csv";
    }

    protected void prepareResponse(HttpServletRequest request,
            HttpServletResponse response) {
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                fileName);
        response.setContentType("text/csv");
        response.setHeader(headerKey, headerValue);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        buildCsvDocument(csvWriter, model);
        csvWriter.close();
    }

    /**
     * The concrete view must implement this method.
     */
    protected abstract void buildCsvDocument(ICsvBeanWriter csvWriter,
            Map<String, Object> model) throws IOException;

}
