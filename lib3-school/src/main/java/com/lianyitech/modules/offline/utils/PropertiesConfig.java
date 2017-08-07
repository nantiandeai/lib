package com.lianyitech.modules.offline.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class PropertiesConfig {
    private static Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);
    /**
     * 根据KEY，读取文件对应的值
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties
     * @param key 键
     * @return key对应的值
     */
    public static String readData(String filePath,String fileName, String key) throws IOException {
        //获取绝对路径
        Properties props = new Properties();
        File file = new File(String.join(File.separator,filePath,fileName));
        if (!file.exists()){
            return null;
        }
        try (
            InputStream in = new BufferedInputStream(new FileInputStream(String.join(File.separator,filePath,fileName)));
            ) {
                props.load(in);
                in.close();
                String value = props.getProperty(key);
                return value;
              }
    }
    /**
     * 修改或添加键值对 如果key存在，修改, 反之，添加。
     * @param filePath 文件路径
     * @param key 键
     * @param value 键对应的值
     */
    public static void writeData(String filePath,String fileName, String key, String value) throws IOException {
        Properties prop = new Properties();
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        File proFile = new File(String.join(File.separator,filePath,fileName));
        if(!proFile.exists()){
            proFile.createNewFile();
        }
        try (InputStream fis = new FileInputStream(proFile);)
        {
            prop.load(fis);
        }
        try (OutputStream fos = new FileOutputStream(String.join(File.separator,filePath,fileName));)
        {
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
        }
    }

    public static void main(String[] args) {
        try {
            PropertiesConfig.writeData("info","aa.properties", "port", "44555");
           System.out.println(PropertiesConfig.readData("info","aa.properties", "port"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
      //PropertiesConfig.writeData("info","aa.properties", "port", "12345");
    }
}