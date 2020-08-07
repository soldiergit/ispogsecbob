package com.ispogsecbob.modules.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import static sun.security.pkcs11.wrapper.Functions.toHexString;

/**
 * @ProjectName ispogsecbob-admin
 * @Author 麦奇
 * @Email biaogejiushibiao@outlook.com
 * @Date 3/15/20 5:00 PM
 * @Version 1.0
 * @Description:
 **/

public class JasyptUtils {

    /**
     * 加密
     * @throws Exception
     */
    @Test
    public void EncryptCode() throws Exception {

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("panda");                        // 加密的密钥
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "your need encrypt string";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    /**
     * 解密
     * @throws Exception
     */
    @Test
    public void DeCode() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("panda");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "T3PFeEs/mQPT9NFDWGUXLw==";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }

    /**
     * sha256
     * @param inputStream
     * @param hashType
     * @return
     * @throws Exception
     */
    public static String getHash(InputStream inputStream, String hashType) throws Exception {
//        InputStream fis = new FileInputStream(file);
        byte buffer[] = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        for (int numRead = 0; (numRead = inputStream.read(buffer)) > 0; ) {
            md5.update(buffer, 0, numRead);
        }
        inputStream.close();
        return toHexString(md5.digest());
    }

    /**
     * 获取文件sha256
     * @param file
     * @return
     * @throws Exception
     */
    public static String getSHA_256(File file) throws Exception{
        InputStream fis = new FileInputStream(file);
        return getHash(fis,"SHA-256");
    }

    /**
     * 获取流sha256
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getSHA_256(InputStream inputStream) throws Exception{
        return getHash(inputStream,"SHA-256");
    }
}
