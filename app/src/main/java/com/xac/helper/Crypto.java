package com.xac.helper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by simon_chen on 2017/11/17.
 */

public class Crypto {

    public enum ALGORITHM {
        TDES,
    }

    public static String getKcv(ALGORITHM alg, String key) {
        String kcv = encrypt(alg, "0000000000000000", key);
        if (!kcv.equals(""))
            return kcv.substring(0, 6);
        else
            return "000000";
    }

    public static String encrypt(ALGORITHM alg, String data, String key) {
        return encrypt(alg, data, key, "0000000000000000");
    }

    public static String encrypt(ALGORITHM alg, String data, String key, String iv) {

        byte [] ret = encrypt(alg, Utility.hex2Byte(data), Utility.hex2Byte(key), Utility.hex2Byte(iv));

        if (ret == null)
            return null;
        else
            return Utility.bytes2Hex(ret);
    }

    public static byte[] encrypt(ALGORITHM alg, byte[] data, byte[] key, byte[] iv) {

        if (alg == ALGORITHM.TDES) {
            Cipher c3des = null;

            try {
                c3des = Cipher.getInstance("DESede/CBC/NoPadding");
                SecretKeySpec myKey = new SecretKeySpec(key, "DESede");
                IvParameterSpec ivspec = new IvParameterSpec(iv);
                c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
                byte[] cipherText = c3des.doFinal(data);

                return cipherText;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
