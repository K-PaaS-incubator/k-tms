package com.kglory.tms.web.util.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;

public class SeedCrypto64 {
    private static Logger logger = LoggerFactory.getLogger(SeedCrypto64.class);
    
    private static final String ENCRYPT = "SHA-256";
    
    public static String encrypt(String passwd) {
        return encrypt0(passwd, null);
    }

    // refs #21391
    public static boolean encrypt(String id, String passwd, String originPassword) {
        return isPassword(id, passwd, originPassword);
    }

    public static String encrypt(String passwd, String id) {
        return encrypt0(passwd, id);
    }

    private static String encrypt0(String passwd, String id) {
        if (id != null) {
            passwd = AesUtil.decrypt(id, passwd);
        }
        
        return encrypt256(passwd, id);
    }
    
    /**
     * 사용자 비밀번호 암호화
     * @param passwd
     * @param id
     * @return 
     */
    private static String encrypt256(String passwd, String id) {
        String rtn = "";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(ENCRYPT);
            // ramdom salt create
            byte[] salt = getSalt();
            
            md.update(salt);
            md.update(id.getBytes("UTF-8"));
            md.update(passwd.getBytes("UTF-8"));
            
            byte[] btHex = addSaltByte(md.digest(), salt);
            
            rtn = DatatypeConverter.printHexBinary(btHex);
        } catch(NoSuchAlgorithmException e) {
        	logger.error(e.getLocalizedMessage());
        } catch (UnsupportedEncodingException e) {
        	logger.error(e.getLocalizedMessage());
		}
        return rtn;
    }
    
    /**
     * 렌덤 솔트값생성
     * @return 
     */
    private static byte[] getSalt() {
        byte[] rtn = new byte[8];
        SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(e.getLocalizedMessage());
		}
        random.nextBytes(rtn);
        return rtn;
    }
    
    /**
     * 솔트값 비밀번호 결합
     * @param hexByte
     * @param salt
     * @return 
     */
    private static byte[] addSaltByte(byte[] hexByte, byte[] salt) {
        byte[] rtn = new byte[hexByte.length + 8];
        System.arraycopy(salt, 0, rtn, 0, 8);
        System.arraycopy(hexByte, 0, rtn, 8, hexByte.length);
        return rtn;
    }
    
    /**
     * 비밀번호 솔트값 추출
     * @param hex
     * @return 
     */
    private static byte[] getHextStrToSalt(String hex) {
        byte[] rtn = new byte[8];
        System.arraycopy(DatatypeConverter.parseHexBinary(hex), 0, rtn, 0, 8);
        return rtn;
    }
    
    /**
     * 사용자 비밀번호 비교
     * @param id
     * @param pwd
     * @param hexPass
     * @return 
     */
    public static boolean isPassword(String id, String pwd, String hexPass) {
        // Login 시 Encrypt Pwd 비교를 위해 Decrypt 로직 추가
        if (id != null) {
            pwd = AesUtil.decrypt(id, pwd);
        }
        return comparePassword(id, pwd, hexPass);
    }

    // refs #21391 - 관리자 계정 패스워드 규칙 기능 추가 요청
    public static boolean isPasswordChange(String id, String pwd, String hexPass) {
        if (id != null) {
            pwd = AesUtil.decrypt(id, pwd);
        }
        return comparePassword(id, pwd, hexPass);
    }

    // 패스워드 변경 규칙 적용을 위한 메소드
    private static boolean comparePassword(String id, String pwd, String hexPass) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPT);
            byte[] salt = getHextStrToSalt(hexPass);

            md.update(salt);
            md.update(id.getBytes("UTF-8"));
            md.update(pwd.getBytes("UTF-8"));

            byte[] btHex = addSaltByte(md.digest(), salt);

            String input = DatatypeConverter.printHexBinary(btHex);
            if (hexPass!= null && hexPass.equals(input)) {
                return true;
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
