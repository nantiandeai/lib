package com.lianyitech.common.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncrypt {
    private static Logger logger = LoggerFactory.getLogger(RSAEncrypt.class);
    /**
     * 私钥 
     */  
    private RSAPrivateKey privateKey;  
  
    /** 
     * 公钥 
     */  
    private RSAPublicKey publicKey;  
      
    /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
      
  
    /** 
     * 获取私钥 
     * @return 当前的私钥对象 
     */  
    public RSAPrivateKey getPrivateKey() {  
        return privateKey;  
    }  
  
    /** 
     * 获取公钥 
     * @return 当前的公钥对象 
     */  
    public RSAPublicKey getPublicKey() {  
        return publicKey;  
    }  
  
    /** 
     * 随机生成密钥对 
     */  
    public void genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen= KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();
        this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey= (RSAPublicKey) keyPair.getPublic();
    }  
  
    /** 
     * 从文件中输入流中加载公钥 
     * @param in 公钥输入流 
     */
    public void loadPublicKey(InputStream in) throws  InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader br= null;
        String readLine= null;
        try{
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
        loadPublicKey(sb.toString());
        }catch (IOException e){
            logger.error("操作失败",e);
        }
        finally {
            try {
                if (null != br) {
                    br.close();
                }
                } catch (IOException e) {
                   logger.error("操作失败",e);
                }

        }
    }
  
  
    /** 
     * 从字符串中加载公钥 
     * @param publicKeyStr 公钥数据字符串
     */
    public void loadPublicKey(String publicKeyStr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        BASE64Decoder base64Decoder= new BASE64Decoder();
        byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
        this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }  
  
    /** 
     * 从文件中加载私钥 
     * @param in 私钥文件名
     */
    public void loadPrivateKey(InputStream in) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader br= new BufferedReader(new InputStreamReader(in));
        String readLine;
        StringBuilder sb= new StringBuilder();
        while((readLine= br.readLine())!=null){
            if(readLine.charAt(0)!='-'){
                sb.append(readLine);
                sb.append('\r');
            }
        }
        loadPrivateKey(sb.toString());
    }  
  
    public void loadPrivateKey(String privateKeyStr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        BASE64Decoder base64Decoder= new BASE64Decoder();
        byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
        PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }  
  
    /** 
     * 加密过程 
     * @param publicKey 公钥 
     * @param plainTextData 明文数据 
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainTextData);
    }  
  
    /** 
     * 解密过程 
     * @param privateKey 私钥 
     * @param cipherData 密文数据 
     * @return 明文 
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher= cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherData);
    }  
  
      
    /** 
     * 字节数据转十六进制字符串 
     * @param data 输入数据 
     * @return 十六进制内容 
     */  
    public static String byteArrayToString(byte[] data){  
        StringBuilder stringBuilder= new StringBuilder();  
        for (int i=0; i<data.length; i++){  
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移  
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);  
            //取出字节的低四位 作为索引得到相应的十六进制标识符  
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);  
            if (i<data.length-1){  
                stringBuilder.append(' ');  
            }  
        }  
        return stringBuilder.toString();  
    }
}  