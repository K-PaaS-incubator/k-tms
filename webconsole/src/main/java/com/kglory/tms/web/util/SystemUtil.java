/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import com.kglory.tms.web.exception.BaseException;
import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author leecjong
 */
public class SystemUtil {

    public static org.slf4j.Logger logger = LoggerFactory.getLogger(SystemUtil.class);

    public static final String LF = SystemUtils.LINE_SEPARATOR;
    public static OperatingSystemMXBean operationBean;

    private static final String POLICY_COMMAND = "kill -SIGUSR1 `pidof sensor`";
    
    private static OperatingSystemMXBean getOperatingSystemMXBean() {
        if (operationBean == null) {
            operationBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        }
        return operationBean;
    }

    public static long getPhysicalTotalMemory() {
        OperatingSystemMXBean bean = SystemUtil.getOperatingSystemMXBean();
        return bean.getTotalPhysicalMemorySize();
    }

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();

    }

    /////////////////////// Exception Util ////////////////////////////////////
    public static String getSimpleStackTrace(Throwable throwable) {
        return SystemUtil.getSimpleStackTrace(throwable, 5);
    }

    /**
     * Excetption Stactrace 문자을 지정한 줄(라인)만큼만 출력한다.
     *
     * @param throwable
     * @param lineCnt
     * @return
     */
    public static String getSimpleStackTrace(Throwable throwable, int lineCnt) {
        String trace[] = ExceptionUtils.getRootCauseStackTrace(throwable);
        StringBuilder sb = new StringBuilder(900);
        int cnt = trace.length;
        if (trace.length > lineCnt) {
            cnt = lineCnt;
        }

        for (int i = 0; i < cnt; i++) {
            sb.append(trace[i]).append(SystemUtil.LF);
        }
        return sb.toString();
    }

    /**
     * OS가 Windows인지 확인
     *
     * @return
     */
    public static boolean isOsWindows() {
        String winName = "Windows";
        String osName = System.getProperty("os.name");
        boolean rtn = false;
		if (osName != null) {
			int osCheck = osName.indexOf(winName);
			if (osCheck >= 0) {
				rtn = true;
			}
		}

        return rtn;
    }

    public static String getBrowserName(HttpServletRequest request) {
        String headerStr = request.getHeader( "User-Agent" ).toUpperCase();
        String browser = "";

        if ( headerStr.indexOf("TRIDENT") > -1 || headerStr.indexOf("MSIE") > -1 ) { //IE

            if ( headerStr.indexOf("TRIDENT/7") > -1 ) {
                browser = "IE 11";
            } else if ( headerStr.indexOf("TRIDENT/6") > -1 ) {
                browser = "IE 10";
            } else if ( headerStr.indexOf("TRIDENT/5") > -1 ) {
                browser = "IE 9";
            } else if ( headerStr.indexOf("TRIDENT/4") > -1 ) {
                browser = "IE 8";
            }
        } else if ( headerStr.indexOf("EDG") > -1 || headerStr.indexOf("EDGE") > -1 ) {
            browser = "IE Edge";
        } else if ( headerStr.indexOf("WHALE") > -1 ) { //네이버 WHALE
            browser = "Whale";
        } else if ( headerStr.indexOf("OPERA") > -1 || headerStr.indexOf("OPR") > -1 ) { //오페라
            browser = "Opera";
        } else if ( headerStr.indexOf("FIREFOX") > -1 ) { //파이어폭스
            browser = "Firefox";
        } else if ( headerStr.indexOf("SAFARI") > -1 && headerStr.indexOf("CHROME") == -1 ) { //사파리
            browser = "Safari";
        } else if ( headerStr.indexOf("CHROME") > -1 ) { //크롬
            browser = "Chrome";
        } else {
            browser = "Other";
        }

        return browser;
    }

    /**
     * 센서 설정 정보 변경시 호출
     * 시그니처, 탐지예외, sensorInbound, .....
     */
    public static void execPolicyCommand() throws BaseException {
        Process proPid = null;
        Process proKill = null;
        BufferedReader br = null;
        BufferedReader brKill = null;
        try {
            List<String> pid = new ArrayList<>();
            pid.add("pidof");
            pid.add("sensor");
            String line;
            ProcessBuilder pidBilder = new ProcessBuilder();
            ProcessBuilder killBilder = new ProcessBuilder();
            pidBilder.command(pid);
            proPid = pidBilder.start();
            logger.debug("sensor process start !!!!!!");
            br = new BufferedReader(new InputStreamReader(proPid.getInputStream()));
            String strPid = "";
            while ((line = br.readLine()) != null) {
                logger.debug("sensor pid : " + line);
                strPid = line;
            }
            if (strPid != null && !strPid.isEmpty()) {
                List<String> process = new ArrayList<>();
                process.add("kill");
                process.add("-SIGUSR1");
                process.add(strPid);
                logger.debug("kill process signal : " + process.toString());
                killBilder.command(process);
                proKill = killBilder.start();
                String s = "";
                brKill = new BufferedReader(new InputStreamReader(proKill.getInputStream()));
                while ((s = brKill.readLine()) != null) {
                    logger.debug("kill process return value : " + s);
                    strPid = line;
                }
                
            } else {
                logger.debug("sensor pidof null ~~~~~~~~~~~");
            }
            logger.debug("sensor process end ~~~~~~~~~~~~~~~~~");
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        } finally {
    		try {
    			if(br != null) {
    				br.close();
    			}
    			if(brKill != null) {
    				brKill.close();
    			}
			} catch (IOException e) {
				throw new BaseException(e);
			}
            if (proPid != null) {
                proPid.destroy();
            }
            if (proKill != null) {
                proKill.destroy();
            }
        }
    }
    
    /**
     * 센서 설정 정보 변경시 호출
     * 시그니처, 탐지예외, sensorInbound, .....
     */
    public static void execIntegrityCommand() throws BaseException {
    	Process proPid = null;
    	Process proKill = null;
    	BufferedReader br = null;
    	BufferedReader brKill = null;
    	try {
    		List<String> pid = new ArrayList<>();
    		pid.add("pidof");
    		pid.add("sensor");
    		String line;
    		ProcessBuilder pidBilder = new ProcessBuilder();
    		ProcessBuilder killBilder = new ProcessBuilder();
    		pidBilder.command(pid);
    		proPid = pidBilder.start();
    		logger.debug("sensor process start !!!!!!");
    		br = new BufferedReader(new InputStreamReader(proPid.getInputStream()));
    		String strPid = "";
    		while ((line = br.readLine()) != null) {
    			logger.debug("sensor pid : " + line);
    			strPid = line;
    		}
    		if (strPid != null && !strPid.isEmpty()) {
    			List<String> process = new ArrayList<>();
    			process.add("kill");
    			process.add("-SIGHUP");
    			process.add(strPid);
    			logger.debug("kill process signal : " + process.toString());
    			killBilder.command(process);
    			proKill = killBilder.start();
    			String s = "";
    			brKill = new BufferedReader(new InputStreamReader(proKill.getInputStream()));
    			while ((s = brKill.readLine()) != null) {
    				logger.debug("kill process return value : " + s);
    				strPid = line;
    			}
    			
    		} else {
    			logger.debug("sensor pidof null ~~~~~~~~~~~");
    		}
    		logger.debug("sensor process end ~~~~~~~~~~~~~~~~~");
    	} catch (IOException e) {
    		logger.error(e.getLocalizedMessage(), e);
    		throw new BaseException(e);
    	} finally {
    		try {
    			if(br != null) {
    				br.close();
    			}
    			if(brKill != null) {
    				brKill.close();
    			}
    		} catch (IOException e) {
    			throw new BaseException(e);
    		}
    		if (proPid != null) {
    			proPid.destroy();
    		}
    		if (proKill != null) {
    			proKill.destroy();
    		}
    	}
    }
    
    /**
     * command 실행 공통
     * 실행 구문 별로 list목록에 추가 : date -s 2018-06-04 00:00:00 -> list.add("date").add("-s").add("2018-06-04 00:00:00")
     * @param command 
     */
    public static List<String> execCommand(List<String> command) throws BaseException {
        List<String> rtn = new ArrayList<>();
        Process process = null;
        BufferedReader br = null;
        try {
            String line;
            ProcessBuilder pidBilder = new ProcessBuilder();
            pidBilder.command(command);
            process = pidBilder.start();
            logger.debug("command process start : " + command.toString());
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                logger.debug("command process result : " + line);
                rtn.add(line);
            }
           
            logger.debug("command process end ~~~~~~~~~~~~~~~~~");
            
        } catch (IOException e) {
            logger.error("Command process error ~~~~");
            throw new BaseException(e);
        } finally {
            if(br != null) {
            	 try {
					br.close();
				} catch (IOException e) {
					throw new BaseException(e);
				}
            }
        	if (process != null) {
                process.destroy();
            }
            
        }
        return rtn;
    }
    
    /**
     * 파일 암호화 Command 실행 #16001
     * @param fileName
     * @param securityName 
     */
    public static void fileSecurityCommand(String fileName, String securityName) throws BaseException {
        // ciphertool --mode=enc --keyfile=/home/sensor/rule/AesKey_web.dat --subkeyfile=/home/sensor/rule/.subkey_web.dat --filein=/usr/local/mysql/data/db_web.properties --fileout=/usr/local/mysql/data/db.properties
        List<String> list = new ArrayList<>();
        list.add("ciphertool");
        list.add("--mode=enc");
        list.add("--keyfile=/home/sensor/rule/AesKey_web.dat");
        list.add("--subkeyfile=/home/sensor/rule/.subkey_web.dat");
        list.add("--filein=" + fileName);
        list.add("--fileout=" + securityName);
        execCommand(list);
    }

    public static void fileSecurityDecryptCommand(String fileName, String securityName) throws BaseException {
        // ciphertool --mode=dec --keyfile=/home/sensor/rule/AesKey_web.dat --subkeyfile=/home/sensor/rule/.subkey_web.dat --filein=/usr/local/mysql/data/db.properties --fileout=/usr/local/mysql/data/db_web.properties
        List<String> list = new ArrayList<>();
        list.add("ciphertool");
        list.add("--mode=dec");
        list.add("--keyfile=/home/sensor/rule/AesKey_web.dat");
        list.add("--subkeyfile=/home/sensor/rule/.subkey_web.dat");
        list.add("--filein=" + fileName);
        list.add("--fileout=" + securityName);
        execCommand(list);
    }

}
