package com.example.administrator.controlmydevices;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des {
    public static byte[] encrypt(byte[] src, String password) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            byte[] k = password.getBytes();
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(k));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(k));
            return cipher.doFinal(src);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            byte[] k = password.getBytes();
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(k));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(k));
            return cipher.doFinal(src);
        } catch (Exception e) {
            return null;
        }
    }
}
