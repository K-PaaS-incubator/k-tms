/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.kglory.tms.web.exception.BaseException;

/**
 *
 * @author leecjong
 */
public class FileDownloadView extends AbstractView{
    private Logger logger = LoggerFactory.getLogger(FileDownloadView.class);
    

    public FileDownloadView() {
        super.setContentType("application/octet-stream; charset=utf-8");
    }
    
    private void setDownloadFileName(String fileName, HttpServletRequest request, HttpServletResponse response, Boolean isDisPosition) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");

        String browser = getBrowser(request);
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            logger.debug("Mime Type is null : " + fileName);
            mimeType = "application/octet-stream";
        }
        
        if (isDisPosition != false) {
            fileName = getDisposion(fileName, browser);
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName +"\""));
        response.setHeader("Content-Transfer-Encoding", "binary");
    }
    
    private void downloadFile(File downloadFile, HttpServletRequest request, HttpServletResponse response, boolean isDelete) throws BaseException {
    	OutputStream out = null;
    	FileInputStream in = null;
        try {
        	out = response.getOutputStream();
        	in = new FileInputStream(downloadFile);
            FileCopyUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            logger.debug(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        } finally {
            try { 
            	if (in != null) {
            		in.close(); 
            	}
            } 
            catch (IOException ioe) {
            	throw new BaseException(ioe);
            }
            try { 
            	if (out != null) {
            		out.close(); 
            	}
            } 
            catch (IOException ioe) {
            	throw new BaseException(ioe);
            }
            if (isDelete) {
               downloadFile.delete();
            }
        }
    }
    

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        try {
            this.setResponseContentType(request, response);

            File downloadFile = (File) model.get("file");
            Boolean isDelete = (Boolean) model.get("isDelete");
            Boolean isDisPosition = (Boolean) model.get("isDisPosition");
            if (isDelete == null) {
                isDelete = false;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("downloadFile: " + downloadFile.getName());
            }
            
            if (isDisPosition == null) {
                isDisPosition = Boolean.TRUE;
            }

            this.setDownloadFileName(downloadFile.getName(), request, response, isDisPosition);

            response.setContentLength((int) downloadFile.length());
            this.downloadFile(downloadFile, request, response, isDelete);
        } catch (UnsupportedEncodingException e) {
            logger.debug(e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    private String getBrowser(HttpServletRequest request) { 
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) { 
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) { 
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) { 
            return "Opera";
        } else if (header.indexOf("Trident/7.0") > -1){ 
            //IE 11 이상 //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8) 
            return "MSIE"; 
        } 
        return "Firefox";
    }
    
    private String getDisposion(String filename, String browser) throws UnsupportedEncodingException {
        String rtn = "";
        if (browser.equals("MSIE")) {
            rtn = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox") || browser.equals("Opera")) {
            rtn = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0 ; i < filename.length() ; i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            rtn = sb.toString();
        } else {
            logger.error("Not supported browser");
        }
        return rtn;
    }
}
