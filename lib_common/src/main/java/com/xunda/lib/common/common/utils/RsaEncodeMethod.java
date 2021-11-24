package com.xunda.lib.common.common.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaEncodeMethod {

    /**
     * @params str 要加密的字符串
     */
    public static String rsaEncode(String str) {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvL1DtPruTPBm5ZasOJ+FMl/VHx3b3Bjuq0IdfNkM+TK6kwJmiU2EyIlUPwEIm3zI1+N7Fl6Ykr8hspDfn2iBG0HSNpZAz8wp73DALOSBLYWBPn0PQXY9j89L0bF2r9ygZXP++jXkv9i6c25jqyEzQzpU0MDCiVuGNo10Q+NT6BtBx9U2zj/tztks5dkT1BPAqVO0WKospAUFHSSoJAadx4yaFiwtFt2M1FWCr/iDjYAI1dq+ade6vcj+0qjyPoFkpuzs7C53JU7Qvfj3jf+D7LG6hVROFi3OB5K2+r/pXQcvLXvwgIgHl1yYp56YqfxEQ2pNzPCM2Oa67Lw+Fj9/OQIDAQAB";
        String outStr = "";
        try {
            // base64编码的公钥
            byte[] decoded = Base64.decode(publicKey, Base64.DEFAULT);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStr = Base64.encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outStr;
    }



}
