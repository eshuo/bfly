package info.bfly.core.util;


import info.bfly.core.exception.DecryptException;
import info.bfly.core.exception.EncryptException;
import info.bfly.core.exception.SignException;
import info.bfly.core.exception.VerifyException;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class RSAEncryptUtils {
    /**
     * 加密算法RSA
     */
    private String KEY_ALGORITHM  = "RSA";
    /**
     * 签名算法
     */
    private String SIGN_ALGORITHM = "SHA1WithRSA";

    /**
     * 获取公钥的key
     */
    private String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private int MAX_ENCRYPT_BLOCK = 245;

    /**
     * RSA最大解密密文大小
     */
    private int MAX_DECRYPT_BLOCK = 256;

    private static RSAEncryptUtils instance;

    public static RSAEncryptUtils getInstance() {
        if (instance == null)
            instance = new RSAEncryptUtils(2048);
        return instance;
    }

    public static RSAEncryptUtils getInstance(int length) {
        return new RSAEncryptUtils(length);
    }

    private RSAEncryptUtils(int length) {
        if(length == 1024){
            MAX_ENCRYPT_BLOCK=117;
            MAX_DECRYPT_BLOCK = 128;
        }

    }

    /**
     * <P> 私钥解密 </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return byte[]
     * @throws Exception
     */
    public byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws DecryptException {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            // 对数据分段解密
            return splitCipher(encryptedData, cipher, getMaxDecryptBlock());

        } catch (Exception e) {
            throw new DecryptException("decrypt error");
        }
    }


    /**
     * <p> 公钥加密 </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return byte[]
     * @throws Exception
     */
    public byte[] encryptByPublicKey(byte[] data, String publicKey) throws EncryptException {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);

            // 对数据分段加密
            return splitCipher(data, cipher, getMaxDecryptBlock());

        } catch (Exception e) {
            throw new EncryptException("encrypt error");
        }
    }

    /**
     * <P> 私钥解密 </p>
     *
     * @param encryptedData 已加密数据(BASE64编码)
     * @param privateKey    私钥(BASE64编码)
     * @return String
     * @throws Exception
     */
    public String decryptByPrivateKey(String encryptedData, String privateKey) throws DecryptException {
        try {
            return new String(decryptByPrivateKey(Base64.getDecoder().decode(encryptedData.getBytes(StandardCharsets.UTF_8)), privateKey), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptException("decrypt error");
        }

    }


    /**
     * <p> 公钥加密 </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return String BASE64
     * @throws Exception
     */
    public String encryptByPublicKey(String data, String publicKey) throws EncryptException {
        try {
            return new String(Base64.getEncoder().encode(encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), publicKey)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptException("encrypt error");
        }
    }

    /**
     * <p>  获取私钥 </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(getPrivateKey());
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * <p> 获取公钥 </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(getPublicKey());
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 分段加密解密
     *
     * @param data    需要加密解密的数据
     * @param cipher  加密解密工具
     * @param MaxSize 单次加密解密最大字节
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private byte[] splitCipher(byte[] data, Cipher cipher, int MaxSize) throws IllegalBlockSizeException, BadPaddingException {
        byte[] cache;
        int inputLen = data.length;
        int offSet = 0;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MaxSize) {
                cache = cipher.doFinal(data, offSet, MaxSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MaxSize;
        }
        byte[] result = out.toByteArray();
        IOUtils.closeQuietly(out);
        return result;
    }

    public String sign(String content, String privateKey) throws SignException {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance(getKeyAlgorithm());
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(getSignAlgorithm());

            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));

            byte[] signed = signature.sign();

            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new SignException("sign error");
        }
    }

    public boolean checkSign(String content, String sign, String publicKey) throws VerifyException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(getSignAlgorithm());

            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.getDecoder().decode(sign));

        } catch (Exception e) {
            throw new VerifyException("verify error");
        }

    }

    public String getKeyAlgorithm() {
        return KEY_ALGORITHM;
    }

    public String getSignAlgorithm() {
        return SIGN_ALGORITHM;
    }

    public String getPublicKey() {
        return PUBLIC_KEY;
    }

    public String getPrivateKey() {
        return PRIVATE_KEY;
    }

    public int getMaxEncryptBlock() {
        return MAX_ENCRYPT_BLOCK;
    }

    public int getMaxDecryptBlock() {
        return MAX_DECRYPT_BLOCK;
    }
}