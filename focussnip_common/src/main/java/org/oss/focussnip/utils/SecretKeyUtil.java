package org.oss.focussnip.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class SecretKeyUtil {

    public static final String KEY_ALGORITHM = "RSA";

    private static RSA256Key rsa256Key;

    //获得公钥
    public static String getPublicKey(RSA256Key rsa256Key) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = rsa256Key.getPublicKey();
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //获得私钥
    public static String getPrivateKey(RSA256Key rsa256Key) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = rsa256Key.getPrivateKey();
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    //使用KeyPairGenerator 生成公私钥，存放于map对象中
    public static RSA256Key initKey() throws Exception {
        /* RSA算法要求有一个可信任的随机数源 */
        //获得对象 KeyPairGenerator 参数 RSA 256个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(512);

        //通过对象 KeyPairGenerator 生成密匙对 KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入RSA256Key中
        RSA256Key rsa256Key = new RSA256Key();
        rsa256Key.setPublicKey(publicKey);
        rsa256Key.setPrivateKey(privateKey);
        return rsa256Key;
    }

    /**
     * 获取公私钥
     * @return
     * @throws Exception
     */
    public static synchronized RSA256Key getRSA256Key() throws Exception {
        if(rsa256Key == null){
            synchronized (RSA256Key.class){
                if(rsa256Key == null) {
                    rsa256Key = initKey();
                }
            }
        }
        return rsa256Key;
    }

    public static void main(String[] args) {
        RSA256Key rsa256Key;
        try {
            rsa256Key = getRSA256Key();  // 使用 java.security.KeyPairGenerator 生成 公/私钥
            String publicKey = getPublicKey(rsa256Key);
            System.out.println("公钥：\n"+publicKey);
            String privateKey = getPrivateKey(rsa256Key);
            System.out.println("私钥：\n"+privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
