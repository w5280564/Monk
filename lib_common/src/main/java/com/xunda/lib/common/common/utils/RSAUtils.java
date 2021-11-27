package com.xunda.lib.common.common.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @文件描述： RSA加解密工具类
 * @作者: ouyang
 * @日期: 2021/8/6
 */
public class RSAUtils {
    /**
     * RSA算法
     */
    public static final String RSA = "RSA";

    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvL1DtPruTPBm5ZasOJ+FMl/VHx3b3Bjuq0IdfNkM+TK6kwJmiU2EyIlUPwEIm3zI1+N7Fl6Ykr8hspDfn2iBG0HSNpZAz8wp73DALOSBLYWBPn0PQXY9j89L0bF2r9ygZXP++jXkv9i6c25jqyEzQzpU0MDCiVuGNo10Q+NT6BtBx9U2zj/tztks5dkT1BPAqVO0WKospAUFHSSoJAadx4yaFiwtFt2M1FWCr/iDjYAI1dq+ade6vcj+0qjyPoFkpuzs7C53JU7Qvfj3jf+D7LG6hVROFi3OB5K2+r/pXQcvLXvwgIgHl1yYp56YqfxEQ2pNzPCM2Oa67Lw+Fj9/OQIDAQAB";

    /**
     * 加密方式，android的
     */
//    public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**加密方式，标准jdk的*/
    public static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    /**
     * 使用公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }


    /** 生成密钥对，即公钥和私钥。key长度是512-2048，一般为1024 */
    public static KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(keyLength);
        return kpg.genKeyPair();
    }

    /** 获取公钥，打印为48-12613448136942-12272-122-913111503-126115048-12...等等一长串用-拼接的数字 */
    public static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /** 获取私钥，同上 */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }






    public static void test() {
        String data = "扫地僧RSA测试";
        try {
            int keyLength = 2048;
            //生成密钥对
            KeyPair keyPair = RSAUtils.generateRSAKeyPair(keyLength);
            //获取公钥
            byte[] publicKey = RSAUtils.getPublicKey(keyPair);
            //获取私钥
            byte[] privateKey = RSAUtils.getPrivateKey(keyPair);

            //用公钥加密
            byte[] encrypt = RSAUtils.encryptByPublicKey(data.getBytes(), publicKey);
            L.e("TAG", "加密后的数据：" + Base64.encode(encrypt));

            //用私钥解密
            byte[] decrypt = RSAUtils.decryptByPrivateKey(encrypt, privateKey);
            String string_decrypt = new String(decrypt,"utf-8");
            L.e("TAG", "解密后的数据：" +string_decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
