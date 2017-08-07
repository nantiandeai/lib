package com.lianyitech.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/** 
 * 文件操作工具类
 *  
 */

public class FileUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	/** 
	 * 写文件到磁盘
	 * @param dirPath
	 * @param fileName
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(String dirPath, String fileName, byte[] fileData) {
		File dir = new File(dirPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return writeFileToDisc(dir, fileName, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param dir
	 * @param fileName
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(File dir, String fileName, byte[] fileData) {
		File file = new File(dir, fileName);

		return writeToFileOutputStream(file, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param fileFullPath
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(String fileFullPath, byte[] fileData) {
		File file = new File(fileFullPath);
		return writeFileToDisc(file, fileData);
	}
	
	/** 
	 * 写文件到磁盘
	 * @param file
	 * @param fileData
	 * @return  
	 * @throws 
	 */
	public static boolean writeFileToDisc(File file, byte[] fileData) {
		return writeToFileOutputStream(file, fileData);
	}
	
	/**
	 * 创建文件夹路径
	 * @param dirPath
	 * @return
	 */
	public static boolean createdir(String dirPath){
		boolean flag = true;
		File dirFile = new File(dirPath);
		if(!dirFile.exists()){
			flag = dirFile.mkdirs();
		}
		return flag;
	}
	
	
	/**
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static boolean delFile(String filePath){
		File dirFile = new File(filePath);
		return dirFile.delete();
	}
	
	/**
	 * 将临时文件写入磁盘
	 * @param stream 文件流
	 * @param dirPath 文件目录
	 * @param prefix 临时文件前缀
	 * @param suffix 临时文件后缀
	 * @param delOnExit jvm退出时删除临时文件
	 * @return
	 */
	public static boolean writeTempFile(InputStream stream,String dirPath,String prefix, String suffix,boolean delOnExit) {
		boolean res = true;
		File file = null;
		try {
			file = File.createTempFile(prefix, suffix, new File(dirPath));
		} catch (IOException e) {
			res =false;
			logger.error("操作失败",e);
		}
		if(null != file){
			res = writeFileOutputStream(file,stream);
		}
		if(delOnExit && null != file){
			file.deleteOnExit();
		}
		return res;
	}

	
	/**
	 * 将文件写入磁盘
	 * @param fileName 文件名
	 * @param stream 文件流
	 * @param dirPath
	 * @return
	 */
	public static boolean writeFile(String dirPath,String fileName, InputStream stream) {
		
		File file =new File(dirPath+File.separator+fileName);
		
		return  writeFileOutputStream(file,stream);
	}
	
	/**
	 * 写数据到文件输出流
	 * @param file
	 * @param stream
	 * @return
	 */
	private static boolean writeFileOutputStream(File file, InputStream stream){
		if(null == file || null == stream){
			return false;
		}
		
		boolean res = true;
		FileOutputStream fs = null;		
		try {
			fs = new FileOutputStream(file);
			byte[] buffer = new byte[10485760];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (IOException e) {
			res = false;
			logger.error("操作失败",e);
		} finally {
			try {
				if (null != fs) {
					fs.close();
				}
				if (null != stream) {
					stream.close();
				}
			} catch (IOException e) {
				logger.error("操作失败",e);
			}
		}
		return res;
	}

	/** 
	 * 写数据到文件输出流
	 * @param file
	 * @param buf
	 * @return  
	 * @throws 
	 */
	private static boolean writeToFileOutputStream(File file, byte[] buf) {
		boolean res = false;

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(buf);
			
			res = true;
		} catch (FileNotFoundException e) {
			logger.error("操作失败",e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("操作失败",e);
			throw new RuntimeException(e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("操作失败",e);
				}
			}
		}
		
		return res;
	}
	//获得指定文件的byte数组
	public static byte[] getBytes(String filePath){
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			logger.error("操作失败",e);
		} catch (IOException e) {
			logger.error("操作失败",e);
		}finally {
			try {
				if(null != bos){
					bos.close();
				}
				if(null != fis){
					fis.close();
				}
			} catch (IOException e) {
				logger.error("操作失败",e);
			}
		}

		return buffer;
	}

}