package com.pjj.xsp.utils;

import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

/**
 * Created by XinHeng on 2018/12/29.
 * describe：
 */
public class ARSAUtils {
    private static final String TAG = "ARSAUtils";
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //return android.util.Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()), android.util.Base64.DEFAULT);
            return null;
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

}
