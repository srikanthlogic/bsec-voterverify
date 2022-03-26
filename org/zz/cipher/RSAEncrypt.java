package org.zz.cipher;

import com.nitgen.SDK.AndroidBSP.NBioBSPJNI;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/* loaded from: classes3.dex */
public class RSAEncrypt {
    public static int encryptX509(byte[] x509CertificateData, int x509Len, byte[] inputContent, int inputContentLen, byte[] outputContent, int[] outputContentLen) {
        byte[] output;
        try {
            try {
                PublicKey pk = ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(x509CertificateData))).getPublicKey();
                pk.getEncoded();
                try {
                    output = encrypt((RSAPublicKey) pk, inputContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    output = null;
                }
                if (output == null) {
                    return 25;
                }
                for (int i = 0; i < output.length; i++) {
                    outputContent[i] = output[i];
                }
                outputContentLen[0] = output.length;
                return 0;
            } catch (CertificateException e2) {
                e2.printStackTrace();
                return 24;
            }
        } catch (CertificateException e3) {
            e3.printStackTrace();
            return 24;
        }
    }

    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(1, publicKey);
                return cipher.doFinal(plainTextData);
            } catch (InvalidKeyException e) {
                throw new Exception("加密公钥非法,请检查");
            } catch (NoSuchAlgorithmException e2) {
                throw new Exception("无此加密算法");
            } catch (BadPaddingException e3) {
                throw new Exception("明文数据已损坏");
            } catch (IllegalBlockSizeException e4) {
                throw new Exception("明文长度非法");
            } catch (NoSuchPaddingException e5) {
                e5.printStackTrace();
                return null;
            }
        } else {
            throw new Exception("加密公钥为空, 请设置");
        }
    }

    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(2, privateKey);
                return cipher.doFinal(cipherData);
            } catch (InvalidKeyException e) {
                throw new Exception("解密私钥非法,请检查");
            } catch (NoSuchAlgorithmException e2) {
                throw new Exception("无此解密算法");
            } catch (BadPaddingException e3) {
                throw new Exception("密文数据已损坏");
            } catch (IllegalBlockSizeException e4) {
                throw new Exception("密文长度非法");
            } catch (NoSuchPaddingException e5) {
                e5.printStackTrace();
                return null;
            }
        } else {
            throw new Exception("解密私钥为空, 请设置");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static byte[] ReadData(String filepath) {
        BufferedInputStream in;
        File f = new File(filepath);
        ?? exists = f.exists();
        if (exists == 0) {
            return null;
        }
        try {
            exists = new ByteArrayOutputStream((int) f.length());
            in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(f));
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = in.read(buffer, 0, 1024);
                    if (-1 == len) {
                        break;
                    }
                    exists.write(buffer, 0, len);
                }
                byte[] byteArray = exists.toByteArray();
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    exists.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return byteArray;
            } catch (IOException e3) {
                e3.printStackTrace();
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                try {
                    exists.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                return null;
            }
        } catch (Throwable th) {
            try {
                in.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
            try {
                exists.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    public static int SaveData(String filepath, byte[] buffer, int size) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filepath));
            try {
                fos.write(buffer, 0, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
                return 0;
            } catch (IOException e2) {
                e2.printStackTrace();
                return -2;
            }
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return -1;
        }
    }

    public static byte[] JavaPubKeyToDeviceRSAPubKey(RSAPublicKey pk) {
        byte[] modulusbyte = new byte[256];
        byte[] exponentbyte = new byte[256];
        byte[] DeviceRSAPubKey = new byte[NBioBSPJNI.ERROR.NBioAPIERROR_USER_BACK];
        new RSAPublicKeySpec(pk.getModulus(), pk.getPublicExponent());
        System.arraycopy(pk.getModulus().toByteArray(), 1, modulusbyte, 0, pk.getModulus().toByteArray().length - 1);
        System.arraycopy(pk.getPublicExponent().toByteArray(), 0, exponentbyte, 0, pk.getPublicExponent().toByteArray().length);
        int bitlen = (pk.getModulus().toByteArray().length - 1) * 8;
        if (bitlen == 2048) {
            DeviceRSAPubKey[0] = 0;
            DeviceRSAPubKey[1] = 8;
        } else {
            DeviceRSAPubKey[0] = 0;
            DeviceRSAPubKey[1] = 4;
        }
        Inv_Bytes(exponentbyte);
        System.arraycopy(modulusbyte, 0, DeviceRSAPubKey, 2, bitlen / 8);
        System.arraycopy(exponentbyte, 0, DeviceRSAPubKey, NBioBSPJNI.ERROR.NBioAPIERROR_INVALID_DEVICE_ID, exponentbyte.length);
        return DeviceRSAPubKey;
    }

    public static RSAPublicKey DeviceRSAPubKeyToJavaPubKey(byte[] DeviceRSAPubKey) {
        PublicKey pubKey = null;
        KeyFactory keyFactory = null;
        byte[] modulusbyte = new byte[256];
        System.arraycopy(DeviceRSAPubKey, 2, modulusbyte, 0, 256);
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(bytesToHexString(modulusbyte), 16), new BigInteger("10001", 16));
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            pubKey = keyFactory.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException e2) {
            e2.printStackTrace();
        }
        return (RSAPublicKey) pubKey;
    }

    public static void Inv_Bytes(byte[] data) {
        int datalen = data.length;
        byte[] temp = new byte[datalen];
        for (int i = 0; i < datalen; i++) {
            temp[i] = data[(datalen - i) - 1];
        }
        System.arraycopy(temp, 0, data, 0, datalen);
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        for (byte b : bArray) {
            String sTemp = Integer.toHexString(b & 255);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
