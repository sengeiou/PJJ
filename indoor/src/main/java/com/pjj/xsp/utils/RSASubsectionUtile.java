package com.pjj.xsp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSASubsectionUtile {
    //公钥
    public static String public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn8aNSYM23LQXYANeE10Pl9AsLeh5LK8Rr0ygUi/KToHjpfw/HP6V1FJ6gcp654l6sq38kY8P01DycSiUvFjbC8uQkC8OYQAtNFp2Bs6BmXKfwODlUCqKhW8Pn8clCyoh4BF3wIKWEvjc1cBqYVwII0+gnNa2DlGdsrkAEgP0kVrK+eJoe1ECEoB9APiX9xvNw/G2HNlvLA71m5vtgen1XximKC6ZXSbFv0P0HKd6QNtHwllmwdZRshbwtKYoW5SUvnJrog8epoAz9yd67aCWUKZKPVa1VquVl6Gbas+xhNfLw54tqj0W0id3UjEl77+qzLG/ngawFIWopicLhG/YyQIDAQAB";

    static int enSegmentSize = 245; //keysize=1024时，分段不能大于117 ；keysize>=2048时，分段不能大于keySize/8+128；
    static int deSegmentSize = 256; //等于keySize/8
    private static final String RSA_TRANSFORM="RSA/ECB/PKCS1Padding";
    //    private static final String RSA_TRANSFORM="RSA";


    /**
     * 公钥加密
     *
     * @param context 明文
     * @return
     */
    public static String public_encipher(String context) {
        try {
            byte[] srcBytes = context.getBytes("utf-8");
            PublicKey pubKey = loadPublicKey();
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORM);
            // 对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] resultBytes = cipherDoFinal(cipher, srcBytes, enSegmentSize); //分段加密

            //return parseByte2HexStr(resultBytes);
            return Base64Utils.encode(resultBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥解密
     *
     * @param contentBase64 密文
     * @return
     */
    public static String public_decipher(String contentBase64) {
        try {
            byte[] srcBytes = Base64Utils.decode(contentBase64);

            PublicKey pubKey = loadPublicKey();
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance(RSA_TRANSFORM);
            // 对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, pubKey);

            byte[] decBytes = cipherDoFinal(deCipher, srcBytes, deSegmentSize); //分段解密

            String decrytStr = new String(decBytes,"utf-8");
            return decrytStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     * @throws
     * @method parseByte2HexStr
     * @since v1.0
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 从字符串中加载公钥
     * <p>
     * 公钥数据字符串
     *
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey() throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(public_key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


}
