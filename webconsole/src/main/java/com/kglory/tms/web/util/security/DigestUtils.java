/*
\ * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.kglory.tms.web.exception.BaseException;

/**
 *
 * @author leecjong
 */
public class DigestUtils {
    
     
    public static String extractFileHashSHA256(String filename) throws BaseException {
         
        String sha = ""; 
        int buff = 16384;
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");

            MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
 
            byte[] buffer = new byte[buff];
            byte[] partialHash = null;
 
            long read = 0;
 
            // calculate the hash of the hole file for the test
            long offset = file.length();
            int unitsize;
            while (read < offset) {
                unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
                file.read(buffer, 0, unitsize);
 
                hashSum.update(buffer, 0, unitsize);
 
                read += unitsize;
            }
 
            file.close();
            partialHash = new byte[hashSum.getDigestLength()];
            partialHash = hashSum.digest();
             
            StringBuffer sb = new StringBuffer(); 
            for(int i = 0 ; i < partialHash.length ; i++){
                sb.append(Integer.toString((partialHash[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();
            file.close();
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        } catch (IOException e) {
        	throw new BaseException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException(e);
        }
        return sha;
    }
}
