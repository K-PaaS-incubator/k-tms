/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.file;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.DetectionPolicyDto;
import com.kglory.tms.web.model.dto.IntegrityFile;
import com.kglory.tms.web.model.vo.DetectionExceptionVO;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.model.vo.SensorInboundVO;
import com.kglory.tms.web.model.vo.SensorVO;
import com.kglory.tms.web.model.vo.SessionServiceDataVO;
import com.kglory.tms.web.model.vo.YaraRuleVo;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.security.AesUtil;
import com.kglory.tms.web.util.security.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author leecjong
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final String CSV_FOLDER = "/logs/data/temp/csv/";
    public static final String FILE_META_FOLDER = "/logs/files/";
    public static final String EXPORT_FOLDER = "/logs/data/temp/export/";
    //public static final String WEB_ROOT_PATH = "../webapps";
    public static final String WEB_ROOT_PATH = "../webapps/ROOT";
    public static final String LINUX_ROOT = "/usr/tomcat/webapps/ROOT";
    public static final String INTEGRITY_FILENAME = "webConsoleIntegrityFile.txt";
    public static final String DUALSYSTEM_FILE = "SyncManagerStatus.dat";

    private static final String POLICY_POL_FOLDER = "/home/sensor/rule/";
    public static final String SIG_POL = "Signature_web_temp.pol";
    public static final String SIG_POL_SEC = "Signature_web.pol";
    public static final String SIG_USER_POL = "UserRule_web_temp.pol";
    public static final String SIG_USER_POL_SEC = "UserRule_web.pol";
    public static final String SIG_RESP_POL = "SigResponse_temp.pol";
    public static final String SIG_RESP_POL_SEC = "SigResponse_web.pol";
    public static final String SIG_USER_RESP_POL = "UserRuleResp_temp.pol";
    public static final String SIG_USER_RESP_POL_SEC = "UserRuleResp_web.pol";
    public static final String EXCEPT_POL = "Exception_temp.pol";
    public static final String EXCEPT_POL_SEC = "Exception_web.pol";
    public static final String INBOUND_POL = "Inbound_temp.pol";
    public static final String INBOUND_POL_SEC = "Inbound_web.pol";
    public static final String NETWORK_CFG = "NetworkEx_temp.cfg";
    public static final String NETWORK_CFG_SEC = "NetworkEx_web.cfg";
    public static final String YARA_POL = "YaraRule_web_temp.pol";
    public static final String YARA_POL_SEC = "YaraRule_web.pol";
    public static final String YARA_USER_POL = "UserYaraRule_web_temp.pol";
    public static final String YARA_USER_POL_SEC = "UserYaraRule_web.pol";
    public static final String SESSION_MONITOR_CFG = "SessionMonitor_temp.cfg";
    public static final String SESSION_MONITOR_CFG_SEC = "SessionMonitor_web.cfg";
    public static final String SENSOR_CFG = "Sensor_temp.cfg";
    public static final String SENSOR_CFG_SEC = "Sensor_web.cfg";
    public static final String INTEGRITY_WEB = "Integrity_web.cfg";
    public static final String SFTP_CFG = "Sftp_temp.cfg";
    public static final String SFTP_CFG_SEC = "Sftp_web.cfg";

    private static final String CLASS_EXCEPTION = "POLICY_EXCEPTION";
    private static final String CLASS_INBOUND = "SYSTEM_INBOUND";
    private static final String CLASS_NETWORK = "SYSTEM_NETWORKEX";
    private static final String CLASS_SESSIONMON = "SYSTEM_SESSIONMONITORING";
    private static final String CLASS_SENSOR = "SYSTEM_SENSOR";
    private static final String CLASS_SFTP = "SFTP";

    private static ICsvBeanWriter csvWriter;
    private static ICsvBeanReader beanReader;

    private static final long MAX_ZIP_SIZE = 1024 * 1024 * 1024; // 1G

    public static final Charset RW_CHARSET = Charset.forName("UTF-8");

    private static final String ALGORITHM = "AES";
    private static final String TRANS_FORMATION = ALGORITHM + "/ECB/PKCS5Padding";
    
    private static final String INTEGRITY_EXCEPTION_FILENAMES = "|webConsoleIntegrityFile.txt|"; //예외처리시 |파일명|파일명2|... 구조로 작성
    
    private Key key;

    public FileUtil() {
    }

    public static void createFolder(String fileName) {
        File file = new File(fileName);
        
        if (!file.isDirectory()) {
        	file.setExecutable(false,true);
        	file.setReadable(true);
        	file.setWritable(false,true);
            file.mkdirs();
        }
    }

    public static void fileWrite(String path, byte[] fileData) throws BaseException {
    	FileOutputStream output = null;
    	try {
            output = new FileOutputStream(path);
            output.write(fileData);
            
            logger.debug("File Write Path : " + path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        } finally {
        	if(output != null) {
        		try {
					output.close();
				} catch (IOException e) {
		            logger.error(e.getLocalizedMessage(), e);
		            throw new BaseException(e);
		        }
        	}
        }
    }
    
    public static void fileWrite(String path, String fileData) throws BaseException {
    	FileOutputStream output = null;
    	OutputStreamWriter osw = null;
        try {
        	output = new FileOutputStream(path);
        	osw = new OutputStreamWriter(output);
            osw.write(fileData);
            logger.debug("File Write Path : " + path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        } finally {
        	try {
        		if(output != null) {
        			output.close();
        		}
        		if(osw != null) {
        			osw.close();
        		}
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
	            throw new BaseException(e);
			}
        }
    }

    public static void fileWriteString(String path, String body) throws BaseException {
        fileWrite(path, body.getBytes());
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static boolean isFile(String fileName) {
        File file = new File(fileName);
        logger.debug("file~~~~~~~~~~~~ " + file);
        return file.isFile();
    }

    /**
     * 파일 삭제
     *
     * @param fileName
     */
    public static void deleteFile(String fileName) {
        if (isFile(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
    }

    public static void deleteDirectory(String directory) {
        File file = new File(directory);
        File[] files = file.listFiles();
        for (File item : files) {
            if (item.isDirectory()) {
                deleteDirectory(item.getPath());
            } else {
                item.delete();
            }
        }
        file.delete();
    }

    public static String fileDuplCheck(String folder, String fileName) {
        String newFileName = fileName;

        if (isFile(folder + fileName)) {
            newFileName = DateTimeUtil.getNow() + "_" + fileName;
        }

        return newFileName;
    }

    /**
     * 침입탐지 정책 가져오기
     *
     * @param fileName
     * @return
     * @throws BaseException 
     */
    public static Map<String, Object> readerDetectionPoicy(String fileName) throws BaseException {
        Map<String, Object> rtnMap = new HashMap<>();
        List<DetectionPolicyDto> policyList = new ArrayList<>();
        List<DetectionPolicyDto> responseList = new ArrayList<>();
        long totTime = System.currentTimeMillis();
        try {
            // 주석 제거
            CsvPreference pf = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).skipComments(new CommentStartsWith("#")).build();
            beanReader = new CsvBeanReader(new FileReader(CSV_FOLDER + fileName), pf);

            // Hearder skip
            String[] header = beanReader.getHeader(true);

            DetectionPolicyDto item = null;
            boolean chk = true;
            int count = 1;
            boolean option = true;
            while (option) {
                item = new DetectionPolicyDto();
                try {
                    if (count == 1) {
                        item = beanReader.read(DetectionPolicyDto.class, DetectionPolicyVO.getDetectionCsvHeader(), getDetectionPolicyProcessor());
                        policyList.add(item);
//                    } else if (count == 2) {
//                        item = beanReader.read(DetectionPolicyVO.class, DetectionPolicyVO.getDetectionResponseCsvHeader(), getDetectionPolicyResponseProcessor());
//                        if (item == null) {
//                            break;
//                        }
//                        responseList.add(item);
                    } else {
                    	option = false;
                        break;
                    }
                } catch (IOException ex) {
                    count++;
                }
            }
            rtnMap.put("policyList", policyList);
//            rtnMap.put("responseList", responseList);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        } finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException ex) {
                	logger.error(ex.getLocalizedMessage());
                }
            }
            logger.debug("Total Time : " + (System.currentTimeMillis() - totTime));
        }
        return rtnMap;
    }

    public static CellProcessor[] getReputeProcessor() {
        CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // IP
            new NotNull(), // category
            new Optional(new ParseInt()) // score
        };
        return processors;
    }

    public static CellProcessor[] getDetectionPolicyProcessor() {
        CellProcessor[] processors = new CellProcessor[]{
            new Optional(new ParseLong()), // lCode
            new Optional(new ParseLong()), // sAlice
            new Optional(new ParseLong()), // sClassType
            new NotNull(), // strDecription
            new Optional(new ParseLong()), // sSeverity
            new NotNull(), // strSigRule
            new Optional(new ParseLong()), // lUsed
            new Optional(new ParseLong()), // lResponse
            new Optional(new ParseLong()), // lThresholdTime
            new Optional(new ParseLong()), // lThresholdNum
            new ConvertNullTo("") // strServiceGroup
        };
        return processors;
    }

    public static CellProcessor[] getDetectionPolicyResponseProcessor() {
        CellProcessor[] processors = new CellProcessor[]{
            new Optional(new ParseLong()), // lCode
            new Optional(new ParseLong()), // lUsed
            new Optional(new ParseLong()), // lResponse
            new Optional(new ParseLong()), // lThresholdTime
            new Optional(new ParseLong()), // lThresholdNum
            new Optional(new ParseLong()) // lvsensorIndex
        };
        return processors;
    }

    private static void createIntegrityFile() throws BaseException {
            File file = new File(".");
            String path="";
            String fileName="";
			try {
				path = file.getCanonicalPath() + "\\target\\ROOT";
				fileName = file.getCanonicalPath() + "\\src\\main\\webapp\\" + INTEGRITY_FILENAME;
			} catch (IOException e1) {
				throw new BaseException(e1);
			}
			// 복사할 파일경로
            List<String> list = getSubDirList(path);
            String content = getIntegrityString(list, path);
            fileWriteString(fileName, content);
    }

    public static String getIntegrityString(List<String> list, String placeStr) {
        StringBuffer sb = new StringBuffer("");
        //String placeStr = "\\target\\ROOT";
        File file = new File(".");
//        String placeStr = "\\KORNICGLORY\\TessTMS\\WebConsole";
        try {
            for (int i = 1; i <= list.size(); i++) {
                String str = list.get(i - 1).substring(list.get(i - 1).indexOf(placeStr)).replace(placeStr, "");
                String[] arr = str.split("\\\\");
                String fileName = arr[arr.length - 1];
                String path = str.substring(0, str.indexOf(fileName)).replaceAll("\\\\", "/");
                
                if(INTEGRITY_EXCEPTION_FILENAMES.indexOf(fileName) > -1) {
                	//무결성 예외 파일제거
                	//webConsoleIntegrityFile.txt => DB 입력후 제거됨
                	continue;
                }
                
                sb.append(i);
                sb.append(", ");
                sb.append(fileName);
                sb.append(", ");
                sb.append(path);
                sb.append(", ");
                sb.append(DigestUtils.extractFileHashSHA256(list.get(i - 1)));
                sb.append(StringUtil.LF);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
		}
        return sb.toString();
    }

    public static List<String> getSubDirList(String path) {
        List<String> rtn = new ArrayList<>();
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        try {
            for (File item : fileList) {
                if (item.isFile()) {
                    rtn.add(item.getPath());
                } else if (item.isDirectory()) {
                    String subDirName = item.getName();
                    logger.debug("subDirName: " + subDirName);
                    String subPath = item.getCanonicalPath();
                    logger.debug("subPath: " + subPath);
                    if (!subDirName.equals("images") && !subDirName.equals("report") && !subDirName.equals("META-INF") && !subDirName.equals("logs")) {
                        rtn.addAll(getSubDirList(subPath));
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    public static String getCurrentPath(String path) {
        String rtn = "";
        try {
            File file = new File(path);
            rtn = file.getCanonicalPath();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    public static List<IntegrityFile> readIntegrityFile(String filePath) {
        List<IntegrityFile> rtnList = new ArrayList<>();
        File file = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                IntegrityFile item = new IntegrityFile();
                String[] arr = lineStr.split(", ");
                if (arr.length == 4) {
                    item.setIntegrityLIndex(Long.valueOf(arr[0]));
                    item.setStrFileName(arr[1]);
                    item.setStrPath(arr[2]);
                    item.setFileHashcode(arr[3]);
                    rtnList.add(item);
                }
            }
            file.delete();
        } catch (FileNotFoundException e) {
        	logger.error(e.getLocalizedMessage());
        } catch (NumberFormatException e) {
        	logger.error(e.getLocalizedMessage());
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
        return rtnList;
    }

    /**
     * 파일에 있는 내용을 라인 단위로 읽어서 배열에 저장하낟.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> readLines(File file) throws IOException {
        return FileUtils.readLines(file, FileUtil.RW_CHARSET);
    }

    /**
     * 파일의 확장자 구하기 '.'는 제외
     *
     * @param fname
     * @return 학장자가 없으면 null
     */
    public static String getFileExt(String fname) {
        int pos = fname.lastIndexOf(".");
        if (pos < 0) {
            return null;
        }
        String ext = fname.substring(pos + 1);
        return ext;
    }

    public static String getFileFolder(String fname) {
        File file = new File(fname);
        return file.getParent();
    }

    public static ArrayList<String> getFileNameList(String path) {
        ArrayList<String> fileNameList = null;
        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
        Arrays.sort(fileList);

        fileNameList = new ArrayList<>();
        for (File tmpFile : fileList) {
            fileNameList.add(tmpFile.getName());
        }
        return fileNameList;
    }

    public static ArrayList<String> getFolderFilePathList(String path) {
        ArrayList<String> fileNameList = null;
        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
        Arrays.sort(fileList);

        fileNameList = new ArrayList<>();
        for (File tmpFile : fileList) {
            fileNameList.add(tmpFile.getAbsolutePath());
        }
        return fileNameList;
    }

    public static String reportFileZip(String filePath) {
        if (isFile(filePath)) {
            String zipPath = changeExtToZip(filePath);
            if (!isFile(zipPath)) {
                zipDirectory(new File(getFileFolder(filePath)), zipPath);
            }
            return zipPath;
        } else {
            logger.debug("Report html File is Empty : " + filePath);
        }
        return null;
    }

    public static void zipDirectory(File dir, String zipDirName) {
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	ZipOutputStream zos = null;
        try {
            List<String> filesListInDir = getFolderFilePathList(dir.getPath());

            fos = new FileOutputStream(zipDirName);
            zos = new ZipOutputStream(fos);
            for (String filePath : filesListInDir) {
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
                zos.putNextEntry(ze);
                fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
            	if(zos != null) {
            		zos.close();
            	}
            	if(fos != null) {
            		fos.close();
            	}
            	if(fis != null) {
            		fis.close();
            	}
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
    }

    public static String changeExtToZip(String fname) {
        int pos = fname.lastIndexOf(".");
        if (pos < 0) {
            return null;
        }
        return fname.substring(0, pos) + ".zip";
    }

    public static void closeQuietly(InputStream in, OutputStream out) {
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    public static void closeQuietly(Reader reader, Writer writer) {

        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(writer);

    }

    public static void closeQuietly(DatagramSocket socket) {
        IOUtils.closeQuietly(socket);
    }

    public static void closeQuietly(Socket socket) {
        IOUtils.closeQuietly(socket);
    }

    public static void closeQuietly(Process process) {
        if (process == null) {
            return;
        }
        closeQuietly(process.getInputStream(), process.getOutputStream());
        closeQuietly(process.getErrorStream(), null);

    }

    private static String getPolicyHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("Vendor:Infosec Technologies Co.,Ltd");
        sb.append(StringUtil.LF);
        sb.append("Version:4.2");
        sb.append(StringUtil.LF);
        sb.append("Update:Wed Jan 29 11:40:19 KST 2014");
        sb.append(StringUtil.LF);
        sb.append("Description:Infosec Snort rule set");
        sb.append(StringUtil.LF);
        sb.append("Hash:11,12,FF,FA,DD");
        sb.append(StringUtil.LF);
        sb.append(StringUtil.LF);
        return sb.toString();
    }

    public static void writeIntegirtyFile() throws BaseException {
        String body = "";
        fileWrite(FileUtil.POLICY_POL_FOLDER + FileUtil.INTEGRITY_WEB, body.getBytes(RW_CHARSET));
        SystemUtil.execPolicyCommand();
    }

    public static void writePolicy(List<DetectionPolicyVO> list, String sigFilename, String respFileName, String securityFileName, String securityResp) throws BaseException {
        StringBuffer sig = new StringBuffer();
        StringBuffer resp = new StringBuffer();
        StringBuffer insert = new StringBuffer();
        sig.append(getPolicyHeader());
        resp.append(getPolicyHeader());
        for (DetectionPolicyVO item : list) {
            sig.append(getSig(item));
            resp.append(getResponse(item));
            //초기 데이터 생성 룰 암호
//            insert.append(getSigSecuInsert(item));
        }
        createFolder(FileUtil.POLICY_POL_FOLDER);
        fileWrite(FileUtil.POLICY_POL_FOLDER + securityFileName, sig.toString().getBytes(RW_CHARSET));
        fileWrite(FileUtil.POLICY_POL_FOLDER + securityResp, resp.toString().getBytes(RW_CHARSET));
        //초기 데이터 생성 룰 암호
//        if (SIG_POL.equals(sigFilename)) {
//            fileWrite(FileUtil.POLICY_POL_FOLDER + "signature_sec_insert.sql", insert.toString().getBytes(RW_CHARSET));
//        }
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + sigFilename, FileUtil.POLICY_POL_FOLDER + securityFileName);
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + respFileName, FileUtil.POLICY_POL_FOLDER + securityResp);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + sigFilename);
//        deleteFile(FileUtil.POLICY_POL_FOLDER + respFileName);

        SystemUtil.execPolicyCommand();
    }

    public static void writeYaraPolicy(List<YaraRuleVo> list, String fileName, String securityFileName) throws BaseException {
        StringBuffer sb = new StringBuffer();
        StringBuffer insert = new StringBuffer();
        sb.append(getYaraHeader());
        for (YaraRuleVo item : list) {
        	if(item.getlUsed() != null && item.getlUsed().intValue() == 1) { //사용중인 룰만 배포하도록 추가
        		sb.append(getYaraBody(item, securityFileName));
        	}
            //초기 데이터 생성 룰 암호
//            insert.append(getYaraSecurityInsert(item));
        }
        createFolder(FileUtil.POLICY_POL_FOLDER);
        fileWrite(FileUtil.POLICY_POL_FOLDER + securityFileName, sb.toString().getBytes(RW_CHARSET));
        //초기 데이터 생성 룰 암호
//        if (FileUtil.YARA_POL.equals(fileName)) {
//            fileWrite(FileUtil.POLICY_POL_FOLDER + "yara_sec_insert.sql", insert.toString().getBytes(RW_CHARSET));
//        }
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + fileName, FileUtil.POLICY_POL_FOLDER + securityFileName);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + fileName);

        SystemUtil.execPolicyCommand();
    }

    private static String getSig(DetectionPolicyVO item) {
        StringBuffer sb = new StringBuffer();
        sb.append(item.getStrAlive());
        sb.append(" ");
        sb.append(item.getsClassType());
        sb.append(" ");
        if (item.getlCode() >= 1000000 && (item.getStrServiceGroup() == null || item.getStrServiceGroup().isEmpty())) {
            sb.append("USERSIG");
        } else {
            sb.append(item.getStrServiceGroup());
        }
        sb.append(" ");
        sb.append(item.getlCode());
        sb.append(" ");
        sb.append("\"");
        sb.append(item.getStrDescription());
        sb.append("\"");
        sb.append(" ");
        sb.append(item.getStrSeverity());
        sb.append(" ");
        sb.append(item.getStrSigRule());
        sb.append(StringUtil.LF);
        return sb.toString();
    }

    private static String getSigSecuInsert(DetectionPolicyVO item) {
        StringBuffer sb = new StringBuffer();
        sb.append("Insert ");
        sb.append("into ");
        sb.append("POLICY_SIGNATURE ");
        sb.append("(LCODE,SALIVE,SCLASSTYPE,STRDESCRIPTION,SSEVERITY,STRSIGRULE,LUSED,LRESPONSE,LTHRESHOLDTIME,LTHRESHOLDNUM,STRSERVICEGROUP) values (");
        sb.append(item.getlCode());
        sb.append(",");
        sb.append(1);
        sb.append(",");
        sb.append(item.getsClassType());
        sb.append(",'");
        sb.append(item.getStrDescription().replaceAll("\'", "\'\'"));
        sb.append("',");
        sb.append(item.getsSeverity());
        sb.append(",'");
        sb.append(AesUtil.encryptSignature(item.getStrSigRule(), item.getlCode()));
        sb.append("',");
        sb.append("1");
        sb.append(",");
        sb.append(item.getlResponse());
        sb.append(",");
        sb.append(item.getlThresholdTime());
        sb.append(",");
        sb.append(item.getlThresholdNum());
        sb.append(",'");
        sb.append(item.getStrServiceGroup());
        sb.append("'");
        sb.append(");");
        sb.append(StringUtil.LF);
        return sb.toString();
    }
    
    private static String getResponse(DetectionPolicyVO item) {
        StringBuffer sb = new StringBuffer();
        sb.append("code:");
        sb.append(item.getlCode());
        sb.append("; ");
        sb.append("used:");
        sb.append(item.getStrUsed());
        sb.append("; ");
        sb.append("resp:");
        sb.append(item.getlResponse());
        sb.append("; ");
        sb.append("threshold:");
        sb.append(item.getlThresholdNum());
        sb.append("/");
        sb.append(item.getlThresholdTime());
        sb.append(";");
        sb.append(StringUtil.LF);
        return sb.toString();
    }

    private static String getYaraHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("//This is YARA rule data file...");
        sb.append(StringUtil.LF);
        sb.append("//Date: 118/02/26 21:41:28");
        sb.append(StringUtil.LF);

        return sb.toString();
    }

    private static String getYaraSecurityInsert(YaraRuleVo item) {
        StringBuffer sb = new StringBuffer();
        sb.append("Insert ");
        sb.append("into ");
        sb.append("POLICY_YARA_RULE ");
        sb.append("(LINDEX, LGROUPINDEX, STRNAME, STRMETA, STRSTRINGS, STRCONDITION, SSEVERITY, TMINSERT, TMUPDATE, LUSED) values (");
        sb.append(item.getlIndex());
        sb.append(",");
        sb.append(item.getGroupIndex());
        sb.append(",'");
        sb.append(item.getRuleName());
        sb.append("','");
        sb.append(AesUtil.encryptSignature(item.getMeta(), item.getlIndex()));
        sb.append("','");
        sb.append(AesUtil.encryptSignature(item.getStrings(), item.getlIndex()));
        sb.append("','");
        sb.append(AesUtil.encryptSignature(item.getCondition(), item.getlIndex()));
        sb.append("',");
        sb.append(item.getsSeverity());
        sb.append(",");
        sb.append("NOW()");
        sb.append(",");
        sb.append("NOW()");
        sb.append(",");
        sb.append("1");
        sb.append(");");
        sb.append(StringUtil.LF);
        return sb.toString();
    }
    
    private static String getYaraBody(YaraRuleVo item, String fileName) {
        StringBuffer sb = new StringBuffer();
        sb.append("rule ");
        sb.append(item.getRuleName());
        sb.append(" : ");
        sb.append(item.getGroupName());
        sb.append(StringUtil.LF);
        sb.append("{");
        sb.append(StringUtil.LF);
        sb.append(" meta:");
        sb.append(StringUtil.LF);
        sb.append("rule_id = ");
        sb.append(item.getlIndex());
        sb.append(StringUtil.LF);
        sb.append("rule_group_id = ");
        sb.append(item.getGroupIndex());
        sb.append(StringUtil.LF);
        sb.append("severity = ");
        sb.append(item.getsSeverity());
        sb.append(StringUtil.LF);
        sb.append(item.getMeta());
        sb.append(StringUtil.LF);
        sb.append(" strings:");
        sb.append(StringUtil.LF);
        sb.append(item.getStrings());
        sb.append(StringUtil.LF);
        sb.append(" condition:");
        sb.append(StringUtil.LF);
        sb.append(item.getCondition());
        sb.append(StringUtil.LF);
        sb.append("}");
        sb.append(StringUtil.LF);
        return sb.toString();
    }

    private static String getCommonSection(String type) {
        StringBuffer sb = new StringBuffer();
        sb.append("#Common Section");
        sb.append(StringUtil.LF);
        sb.append("[COMMON]");
        sb.append(StringUtil.LF);
        sb.append("Class=");
        sb.append(type);
        sb.append(StringUtil.LF);
        sb.append("Version=1.0.0.0");
        sb.append(StringUtil.LF);
        sb.append("Time=2007/02/26 18:53:38");
        sb.append(StringUtil.LF);
        sb.append("Description=Infosec Technologies");
        sb.append(StringUtil.LF);
        sb.append("[/COMMON]");
        sb.append(StringUtil.LF);

        return sb.toString();
    }

    private static String getInfoSection(int count) {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtil.LF);
        sb.append("#Info Section");
        sb.append(StringUtil.LF);
        sb.append("[INFO]");
        sb.append(StringUtil.LF);
        sb.append("Count=");
        sb.append(count);
        sb.append(StringUtil.LF);
        sb.append("[/INFO]");
        sb.append(StringUtil.LF);

        return sb.toString();
    }

    public static void writePolicyException(List<DetectionExceptionVO> list) throws BaseException{
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_EXCEPTION));
        sb.append(getInfoSection(list.size()));
        for (DetectionExceptionVO item : list) {
            sb.append(getException(item));
        }
        fileWrite(FileUtil.POLICY_POL_FOLDER + EXCEPT_POL_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + EXCEPT_POL, FileUtil.POLICY_POL_FOLDER + EXCEPT_POL_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + EXCEPT_POL);
        SystemUtil.execPolicyCommand();
    }

    public static void writeSensorInbound(SensorInboundVO item) throws BaseException{
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_INBOUND));
        sb.append(getInfoSection(1));

        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("Enable=");
        sb.append(item.getStrEnable());
        sb.append(StringUtil.LF);
        sb.append("Type=");
        sb.append(item.getStrType());
        sb.append(StringUtil.LF);
        sb.append("NicInfo=");
        if (item.getStrNicInfo() != null) {
            sb.append(item.getStrNicInfo().replaceAll("[|]", ";"));
        }
        sb.append(StringUtil.LF);
        sb.append("IPInfo=");
        if (item.getStrIpInfo() != null) {
            sb.append(item.getStrIpInfo().replaceAll("[|]", ";"));
        }
        sb.append(StringUtil.LF);
        sb.append("[/REC]");
        sb.append(StringUtil.LF);

        fileWrite(FileUtil.POLICY_POL_FOLDER + INBOUND_POL_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + INBOUND_POL, FileUtil.POLICY_POL_FOLDER + INBOUND_POL_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + INBOUND_POL);
        SystemUtil.execPolicyCommand();
    }

    public static void writeNetwork(List<NetworkVO> list) throws BaseException{
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_NETWORK));
        sb.append(getInfoSection(list.size()));
        for (NetworkVO item : list) {
            sb.append(getNetworkBody(item));
        }
        fileWrite(FileUtil.POLICY_POL_FOLDER + NETWORK_CFG_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + NETWORK_CFG, FileUtil.POLICY_POL_FOLDER + NETWORK_CFG_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + NETWORK_CFG);
        SystemUtil.execPolicyCommand();
    }

    public static void writeSessionMonitor(List<SessionServiceDataVO> list) throws BaseException {
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_SESSIONMON));
        sb.append(getInfoSection(list.size()));
        for (SessionServiceDataVO item : list) {
            sb.append(getSessionMonitorBody(item));
        }
        fileWrite(FileUtil.POLICY_POL_FOLDER + SESSION_MONITOR_CFG_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + SESSION_MONITOR_CFG, FileUtil.POLICY_POL_FOLDER + SESSION_MONITOR_CFG_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + SESSION_MONITOR_CFG);
        SystemUtil.execPolicyCommand();
    }
    
    public static void writeSensorMonitor(List<SensorVO> monList, SensorVO sensor) throws BaseException{
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_SENSOR));
        sb.append(getInfoSection(1));
        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("MonitorIPRange=");
        if (monList != null && monList.size() > 0) {
            for(int i = 0 ; i < monList.size() ; i++) {
                sb.append(monList.get(i).getStrFromIp());
                sb.append("-");
                sb.append(monList.get(i).getStrToIp());
                if ((i+1) != monList.size()) {
                    sb.append(";");
                }
            }
        }
        sb.append(StringUtil.LF);
        sb.append("HyperScanHit=");
        sb.append(sensor.getnHyperScanHitCount());
        sb.append(StringUtil.LF);
        sb.append("UseBlackList=");
        sb.append(sensor.getsUseBlackList());
        sb.append(StringUtil.LF);
        sb.append("[/REC]");
        sb.append(StringUtil.LF);
        
        fileWrite(FileUtil.POLICY_POL_FOLDER + SENSOR_CFG_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + SENSOR_CFG, FileUtil.POLICY_POL_FOLDER + SENSOR_CFG_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + SENSOR_CFG);
        SystemUtil.execPolicyCommand();
    }
    
    public static void writeSftp(String sftpId, String sftpPwd) throws BaseException{
        StringBuffer sb = new StringBuffer();
        sb.append(getCommonSection(CLASS_SFTP));
        sb.append(getInfoSection(1));
        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("id=");
        sb.append(sftpId);
        sb.append(StringUtil.LF);
        sb.append("pwd=");
        sb.append(sftpPwd);
        sb.append(StringUtil.LF);
        sb.append("[/REC]");
        sb.append(StringUtil.LF);
        
        fileWrite(FileUtil.POLICY_POL_FOLDER + SFTP_CFG_SEC, sb.toString().getBytes(RW_CHARSET));
        // 파일 암호화
//        SystemUtil.fileSecurityCommand(FileUtil.POLICY_POL_FOLDER + SFTP_CFG, FileUtil.POLICY_POL_FOLDER + SFTP_CFG_SEC);
        //파일 암호화 후 파일 삭제
//        deleteFile(FileUtil.POLICY_POL_FOLDER + SFTP_CFG);
        SystemUtil.execPolicyCommand();
    }

    private static String getException(DetectionExceptionVO item) {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("Index=");
        sb.append(item.getlIndex());
        sb.append(StringUtil.LF);
        sb.append("VioCode=");
        sb.append(item.getlVioCode());
        sb.append(StringUtil.LF);
        sb.append("ClassType=");
        sb.append(item.getnClassType());
        sb.append(StringUtil.LF);
        sb.append("SrcNetworkIndex=");
        sb.append(item.getLsrcNetworkIndex());
        sb.append(StringUtil.LF);
        sb.append("DstNetworkIndex=");
        sb.append(item.getLdstNetworkIndex());
        sb.append(StringUtil.LF);
        sb.append("SrcIP_From=");
        sb.append(item.getStrSrcIpFrom());
        sb.append(StringUtil.LF);
        sb.append("SrcIP_To=");
        sb.append(item.getStrSrcIpTo());
        sb.append(StringUtil.LF);
        sb.append("DstIP_From=");
        sb.append(item.getStrDstIpFrom());
        sb.append(StringUtil.LF);
        sb.append("DstIP_To=");
        sb.append(item.getStrDstIpTo());
        sb.append(StringUtil.LF);
        sb.append("Protocol=");
        sb.append(item.getnProtocol());
        sb.append(StringUtil.LF);
        sb.append("SPort=");
        sb.append(item.getnSport());
        sb.append(StringUtil.LF);
        sb.append("DPort=");
        sb.append(item.getnDport());
        sb.append(StringUtil.LF);
        sb.append("Detect=");
        sb.append(item.getnDetect());
        sb.append(StringUtil.LF);
        sb.append("ChkVioCode=");
        sb.append(item.getChkVioCode());
        sb.append(StringUtil.LF);
        sb.append("ChkSrcNetwork=");
        sb.append(item.getChkSrcNetwork());
        sb.append(StringUtil.LF);
        sb.append("ChkDstNetwork=");
        sb.append(item.getChkDstNetwork());
        sb.append(StringUtil.LF);
        sb.append("ChkSrcIP=");
        sb.append(item.getChkSrcIP());
        sb.append(StringUtil.LF);
        sb.append("ChkDstIP=");
        sb.append(item.getChkDstIP());
        sb.append(StringUtil.LF);
        sb.append("ChkProtocol=");
        sb.append(item.getChkProtocol());
        sb.append(StringUtil.LF);
        sb.append("ChkSPort=");
        sb.append(item.getChkSPort());
        sb.append(StringUtil.LF);
        sb.append("ChkDPort=");
        sb.append(item.getChkDPort());
        sb.append(StringUtil.LF);

        sb.append("[/REC]");
        sb.append(StringUtil.LF);
        return sb.toString();
    }

    private static String getNetworkBody(NetworkVO item) {
        StringBuffer sb = new StringBuffer();
        String type = "";
        switch (item.getsType()) {
            case 0:
                type = "ALL";
                break;
            case 1:
                type = "IPBLOCK";
                break;
            case 2:
                type = "VLAN";
                break;
            case 3:
                type = "MPLS";
                break;
            case 4:
                type = "VPN-IPBLOCK";
                break;
            case 5:
                type = "URL";
                break;
            default:
            	type = "";
            	break;
        }
        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("Index=");
        sb.append(item.getLnetworkIndex());
        sb.append(StringUtil.LF);
        sb.append("Type=");
        sb.append(type);
        sb.append(StringUtil.LF);
        sb.append("Value1=");
        sb.append(item.getlValue1());
        sb.append(StringUtil.LF);
        sb.append("Value2=");
        sb.append(item.getlValue2());
        sb.append(StringUtil.LF);
        sb.append("VPN=0");
//        sb.append(item.get);
        sb.append(StringUtil.LF);
        sb.append("InnerLabel=0");
//        sb.append(item.get);
        sb.append(StringUtil.LF);
        sb.append("IPBlock=");
        if (item.getIpBolckList() != null && item.getIpBolckList().size() > 0) {
            for (int i = 0; i < item.getIpBolckList().size(); i++) {
                sb.append(item.getIpBolckList().get(i).getDwFromIp());
                sb.append("-");
                sb.append(item.getIpBolckList().get(i).getDwToIp());
                if (i != (item.getIpBolckList().size() - 1)) {
                    sb.append(";");
                }
            }
        }
        sb.append(StringUtil.LF);
        sb.append("URL=");
        sb.append(StringUtil.LF);
        sb.append("[/REC]");
        sb.append(StringUtil.LF);

        return sb.toString();
    }

    private static String getSessionMonitorBody(SessionServiceDataVO item) {
        StringBuffer sb = new StringBuffer();

        sb.append(StringUtil.LF);
        sb.append("[REC]");
        sb.append(StringUtil.LF);
        sb.append("Index=");
        sb.append(item.getlIndex());
        sb.append(StringUtil.LF);
        sb.append("Port=");
        sb.append(item.getnPort());
        sb.append(StringUtil.LF);
        sb.append("FilterOption=0");
        sb.append(StringUtil.LF);
        sb.append("TotalPacket=0");
        sb.append(StringUtil.LF);
        sb.append("KeepTime=0");
        sb.append(StringUtil.LF);
        sb.append("Func=1");
        sb.append(StringUtil.LF);
        sb.append("RenewOption=");
        sb.append(item.getnRenewOption());
        sb.append(StringUtil.LF);
        sb.append("Period=0");
        sb.append(StringUtil.LF);
        sb.append("[/REC]");
        sb.append(StringUtil.LF);
        return sb.toString();
    }

}
