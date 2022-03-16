package info.bfly.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


/**
 * 使用DES加密和解密的方法
 */
public class ThreeDES {
    private static final Logger log = LoggerFactory.getLogger(ThreeDES.class);

    private AlgorithmParameterSpec iv  = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
    private Key                    key = null;

    public ThreeDES(String deskey, String desiv) {
        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(deskey.getBytes(StandardCharsets.UTF_8));// 设置密钥参数
            iv = new IvParameterSpec(desiv.getBytes(StandardCharsets.UTF_8));// 设置向量
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");// 获得密钥工厂
            key = keyFactory.generateSecret(keySpec);// 得到密钥对象

        } catch (NoSuchAlgorithmException| InvalidKeySpecException | InvalidKeyException e) {
            log.debug(e.getMessage());
        }


    }

    public String encode(String data) {
        try {
            Cipher enCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");// 得到加密对象Cipher
            enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
            byte[] pasByte = enCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(pasByte);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public String decode(String data) {
        try {
            Cipher deCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            deCipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] pasByte = deCipher.doFinal(Base64.getDecoder().decode(data));

            return new String(pasByte, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            log.debug(e.getMessage());
        }
        return null;

    }
/*    public static void main(String[] args) {
        try {
            String test = "aaaaaa";
            ThreeDES des = new ThreeDES("123456781234567812345678", "01234567");//自定义密钥
            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + des.encode(test));
            System.out.println("解密后的字符：" + des.decode(des.encode(test)));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }*/
}