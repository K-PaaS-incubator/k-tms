/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class AesUtil {

    private static Logger log = LoggerFactory.getLogger(AesUtil.class);

    private static final int KEY_SIZE = 256;
    private static final int INTERATION_COUNT = 10000;
    private static final String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    private static String passPhrase = "";

    private static Cipher cipher;

    public AesUtil() {
    }
    
      
    public static String getPassPhrase(HttpSession session) {
    	if("".equals(passPhrase) || passPhrase == null) {
    		initAesPass(session);
    	}
    	return passPhrase;
    }
    
    public static void destoryedPassPhrase(HttpSession session) {
    	session.removeAttribute("PASSPHRASE"); 
    	passPhrase = null;
    }
    
    public static void initAesPass(HttpSession session) {
    	String generatedString = "";
    	if(session.getAttribute("PASSPHRASE") == null || "".equals(session.getAttribute("PASSPHRASE"))) {
    		
    		generatedString = random(16);
    		passPhrase = generatedString;
    		session.setAttribute("PASSPHRASE", passPhrase);
//    		log.info("new gender key !!! "+generatedString);
    	}
    	else {
    		passPhrase = (String) session.getAttribute("PASSPHRASE");
    	}
    }
    
    public static String encrypt(String salt, String plaintext) {
        try {
//        	log.info("encrypt passPhrase>>>>>>> "+passPhrase);
//        	log.info("encrypt plaintext>>>>>>> "+plaintext);
//        	log.info("encrypt salt>>>>>>> "+salt);
            String hex = DatatypeConverter.printHexBinary(salt.getBytes("UTF-8"));
//            SecretKey key = generateKeyWeb(hex);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    		KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(),
    				hex(hex),
    				INTERATION_COUNT,
    				128);
    		SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, IV, plaintext.getBytes("UTF-8"));
            
//            log.info("encrypt result>>>>>>> "+base64(encrypted));
            return base64(encrypted);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeySpecException e) {
        	throw new IllegalStateException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
    }
    
    public static String encryptSignature(String plaintext, long code) {
        try {
            // 랜덤 키 생성
            byte[] salt = randomByte(8);
            // 랜덤 키 hex binary 변경
            String hex = DatatypeConverter.printHexBinary(salt);
            // 정책코드 와 랜덤키로 함호화 키 생성 (java SecretKey 사용)
            SecretKey key = generateKey(hex, String.valueOf(code));
            // 암호화 키로 정책 암호화 (java Cipher 사용 "AES/CBC/PKCS5Padding" 암호화)
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, IV, plaintext.getBytes("UTF-8"));
            // 랜덤키 binary 와 정책 암호화 binary 합친다
            encrypted = addSaltByte(encrypted, salt);
            // binary 문자 hexa 문장 변환
            return DatatypeConverter.printHexBinary(encrypted);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static String encryptString(String plaintext, String code) {
        try {
            byte[] salt = randomByte(8);
            String hex = DatatypeConverter.printHexBinary(salt);
//            log.info("encStr hex >>> "+hex);
//            log.info("encStr code >>> "+code);
//            log.info("encStr ciphertext >>> "+plaintext);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(code.toCharArray(),
                    hex(hex),
                    INTERATION_COUNT,
                    128);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, IV, plaintext.getBytes("UTF-8"));
            encrypted = addSaltByte(encrypted, salt);
//            log.info("encStr result >>> "+DatatypeConverter.printHexBinary(encrypted));
            return DatatypeConverter.printHexBinary(encrypted);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
        	throw new IllegalStateException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException(e);
		}
    }

    public static String decrypt(String salt, String ciphertext) {
    	try {
//        	log.info("decrypt passPhrase>>>>>>> "+passPhrase);
//        	log.info("decrypt ciphertext>>>>>>> "+ciphertext);
            String hex = DatatypeConverter.printHexBinary(salt.getBytes("UTF-8"));
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    		KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(),
    				hex(hex),
    				INTERATION_COUNT,
    				128);
    		SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, IV, base64(ciphertext));
//            log.info("decrypt result>>>>>>> "+(new String(decrypted, "UTF-8")));
            return new String(decrypted, "UTF-8");
        } catch (NullPointerException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeySpecException e) {
        	throw new IllegalStateException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
    }
    
    public static String decryptSignature(String ciphertext, long code) {
        try {
            // 암호화된 정책에서 랜덤 key 추출
            byte[] salt = getHextStrToSalt(ciphertext);
            // 추출된 key hexa 문장 변화
            String hex = DatatypeConverter.printHexBinary(salt);
            // 랜덤키와 정책 코드로 암호화 키 생성 (java SecretKey 사용)
            SecretKey key = generateKey(hex, String.valueOf(code));
            // 암화화된 정책에서 실제 정책부분만 binary 추출
            byte[] chipher = getHextStrToPlan(ciphertext);
            // 암호화정책 과 암호화 키로 정책 풀기 (java Cipher 사용 "AES/CBC/PKCS5Padding" 암호화 풀기)
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, IV, chipher);
            // 정책 binary를 문자열로 변경
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static String decryptString(String ciphertext, String code) {
        try {
            byte[] salt = getHextStrToSalt(ciphertext);
            String hex = DatatypeConverter.printHexBinary(salt);
//            log.info("decStr hex >>> "+hex);
//            log.info("decStr code >>> "+code);
//            log.info("decStr ciphertext >>> "+ciphertext);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(code.toCharArray(),
                    hex(hex),
                    INTERATION_COUNT,
                    128);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            byte[] chipher = getHextStrToPlan(ciphertext);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, IV, chipher);
//            log.info("decStr result >>> "+new String(decrypted, "UTF-8"));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
        	throw new IllegalStateException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException(e);
		}
    }

    private static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
		} catch (InvalidAlgorithmParameterException e) {
			throw fail(e);
		} catch (NoSuchPaddingException e) {
			throw fail(e);
		} catch (IllegalBlockSizeException e) {
			throw fail(e);
		} catch (NoSuchAlgorithmException e) {
			throw fail(e);
		} catch (BadPaddingException e) {
			throw fail(e);
		} catch (InvalidKeyException e) {
			throw fail(e);
		} catch (Exception e) {
			throw fail(e);
		}
	}

	private static IllegalStateException fail(Exception e) {
		return new IllegalStateException(e);
	}

    private static SecretKey generateKey(String salt, String pass) {
        try {
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(pass.toCharArray(),
                    hex(salt),
                    INTERATION_COUNT,
                    KEY_SIZE);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static SecretKey generateKey128(String salt, String pass) {
        try {
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(pass.toCharArray(),
                    hex(salt),
                    INTERATION_COUNT,
                    128);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }
    
    public static byte[] randomByte(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
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
    
    private static byte[] getHextStrToPlan(String hex) {
        byte[] rtn = null;
        byte[] arr = DatatypeConverter.parseHexBinary(hex);
        rtn = new byte[arr.length - 8];
        System.arraycopy(arr, 8, rtn, 0, arr.length-8);
        return rtn;
    }
    
}
