package com.lianyitech.fileparse;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.file.importer.Engine;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 将中国可供书目爬虫下来的txt解析
 */
//@PropertySource("classpath:project.properties")
public class TxtFileReader implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
    private static final String txt_dir = "/txt_dir";
    private static final String split_str = "@td@ ";
    @Override
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue, boolean toQueue) throws Exception {
        File dir = new File(txt_dir);
        if(dir.exists() && dir.isDirectory()) {
            File[] dirFiles = dir.listFiles();
            //循环文件夹里面的文件
            for(File temp : dirFiles != null ? dirFiles : new File[0])
                if (temp.isFile()) {
                    readerFile(temp, rowTaskQueue);
                    System.out.println(temp.getName() + "已经读取");
                }
        }else{
            System.out.println("txt文件所在的文件夹"+txt_dir+"不存在");
        }
    }
    //读取txt里面的内容
    private void readerFile(File file,BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue){
        try {
            String encoding="GBK";
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while((lineTxt = bufferedReader.readLine()) != null){
                    String info[] = lineTxt.split(split_str);
                    //这里需要将info截取的内容放入rowTaskQueue
                    setToMap(rowTaskQueue,info);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    //将数组内容放入BlockingQueue<ConcurrentNavigableMap<String, String>>
    private void setToMap(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue,String[] info){
        if(info.length<14){
            return;
        }
        ConcurrentNavigableMap<String, String> map = new ConcurrentSkipListMap<>();
        if(StringUtils.isNotEmpty(info[0].trim())){
            map.put("isbn",info[0].trim());
        }
        if(StringUtils.isNotEmpty(info[1].trim())){
            map.put("title",info[1].trim());
        }
        if(StringUtils.isNotEmpty(info[2].trim())){
            map.put("author",info[2].trim());
        }
        if(StringUtils.isNotEmpty(info[3].trim())){
            map.put("price",info[3].trim().replace("￥",""));
        }
        if(StringUtils.isNotEmpty(info[4].trim())){
            map.put("publisher",info[4].trim());
        }
        if(map.size()<4){//上面4个必填项不满足的情况下忽略此条
            return;
        }
        if(StringUtils.isNotEmpty(info[5].trim())){
            map.put("pubAddr",info[5].trim());
        }
        if(StringUtils.isNotEmpty(info[6].trim())){
            map.put("pubTime",info[6].trim());
        }
        if(StringUtils.isNotEmpty(info[7].trim())){
            map.put("size",info[7].trim());
        }
        if(StringUtils.isNotEmpty(info[8].trim())){
            map.put("page",info[8].trim());
        }
        if(StringUtils.isNotEmpty(info[9].trim())){
            map.put("ver",info[9].trim());
        }
        if(StringUtils.isNotEmpty(info[10].trim())){
            map.put("form",info[10].trim());
        }
        if(StringUtils.isNotEmpty(info[11].trim())){
            map.put("sort",info[11].trim());
        }
        if(StringUtils.isNotEmpty(info[12].trim())){
            map.put("content",info[13].trim());
        }
        if(StringUtils.isNotEmpty(info[13].trim())){
            map.put("path",info[13].trim());
        }
        try {
            rowTaskQueue.put(map);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
