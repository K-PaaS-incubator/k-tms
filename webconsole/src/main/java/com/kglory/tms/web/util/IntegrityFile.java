/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.security.DigestUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class IntegrityFile {
    
    public static final String INTEGRITY_FILENAME = "webConsoleIntegrityFile.txt";
    private static org.slf4j.Logger log = LoggerFactory.getLogger(IntegrityFile.class);

	/**
	 * 파일 해시를 만들기 위한 메소드(필수적 사용 메소드임)
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			createIntegrityFile();
		} catch (BaseException e) {
			log.error(e.getLocalizedMessage());
		}
	}
	
    private static void createIntegrityFile() throws BaseException {
    	try {
	        File file = new File(".");
	        String path = file.getCanonicalPath() + "/ROOT";
	        String fileName = file.getCanonicalPath() + "/ROOT/" + INTEGRITY_FILENAME;  // 복사할 파일경로
	        List<String> list = getSubDirList(path);
	        String content;
	        if (SystemUtil.isOsWindows()) {
	        	content = getIntegrityString(list, path.replace("/", "\\"));
	        } else {
	        	content = getIntegrityString(list, path);
	        }
			fileWriteString(fileName, content);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BaseException(e);
		}
    }

    public static String getIntegrityString(List<String> list, String placeStr) throws BaseException {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).substring(list.get(i).indexOf(placeStr)).replace(placeStr, "");
            String[] arr = str.split("/");
            String fileName = arr[arr.length - 1];
            String path = str.substring(0, str.indexOf(fileName)).replaceAll("\\\\", "/");
            fileName = fileName.replace("\\", "/");
            sb.append(i+1);
            sb.append(", ");
            sb.append(fileName);
            sb.append(", ");
            sb.append(path);
            sb.append(", ");
            sb.append(DigestUtils.extractFileHashSHA256(list.get(i)));
            sb.append(StringUtil.LF);
        }
        return sb.toString();
    }

    public static List<String> getSubDirList(String path) throws IOException {
        List<String> rtn = new ArrayList<>();
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        for (File item : fileList) {
            if (item.isFile()) {
                rtn.add(item.getPath());
            } else if (item.isDirectory()) {
                String subDirName = item.getName();
                String subPath = item.getCanonicalPath();
                if (!subDirName.equals("images") && !subDirName.equals("report") && !subDirName.equals("META-INF") && !subDirName.equals("logs")) {
                    rtn.addAll(getSubDirList(subPath));
                }
            }
        }
        return rtn;
    }
    
    public static void fileWriteString(String path, String body) {
        fileWrite(path, body.getBytes());
    }
    
    public static void fileWrite(String path, byte[] fileData) {
    	FileOutputStream output = null;
        try {
        	output = new FileOutputStream(path);
            output.write(fileData);
        } 
        catch (IOException e) { 
        	log.error(e.getLocalizedMessage());
        }
        finally {
        	try {
				output.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		}
    }
}
