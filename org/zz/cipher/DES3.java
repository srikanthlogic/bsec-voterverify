package org.zz.cipher;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes3.dex */
public class DES3 {
    private static final String Algorithm = "DESede";

    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        byte[] keybyte24 = new byte[24];
        if (keybyte.length == 16) {
            System.arraycopy(keybyte, 0, keybyte24, 0, keybyte.length);
            System.arraycopy(keybyte, 0, keybyte24, 16, 8);
        } else if (keybyte.length != 24) {
            return null;
        } else {
            System.arraycopy(keybyte, 0, keybyte24, 0, keybyte.length);
        }
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(1, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        byte[] keybyte24 = new byte[24];
        if (keybyte.length == 16) {
            System.arraycopy(keybyte, 0, keybyte24, 0, keybyte.length);
            System.arraycopy(keybyte, 0, keybyte24, 16, 8);
        } else if (keybyte.length != 24) {
            return null;
        } else {
            System.arraycopy(keybyte, 0, keybyte24, 0, keybyte.length);
        }
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(2, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
