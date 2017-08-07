package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.Global;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import net.sf.jxls.exception.ParsePropertyException;
import org.apache.commons.io.IOUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WriteBookDirExcel {
	private static Logger logger = LoggerFactory.getLogger(WriteBookDirExcel.class);
	public static void write(List list, String errorFileName){

		Context context = new Context();
		context.putVar("booklibslist", list);
        String path=WriteBookDirExcel.class.getClassLoader().getResource("").getPath();
		String uploadFilePath = Global.getUploadRootPath() + Global.UPLOADFILES_BASE_URL + "/tmp";
		OutputStream outputStream = null;
		InputStream inputStream = null;
        try {
			outputStream = new FileOutputStream(uploadFilePath + File.separator +errorFileName);
			inputStream = new FileInputStream(path+"jxls/bookDirTemplate-jxls.xlsx");
			JxlsHelper.getInstance().processTemplate(inputStream,outputStream,context);
		} catch (ParsePropertyException e) {
			logger.error("操作失败",e);
		} catch (IOException e) {
			logger.error("操作失败",e);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	public static void main(String[] args) {

		List<BookDirectory> errorList = new ArrayList<>();

		BookDirectory bookDirectory = new BookDirectory();
		bookDirectory.setIsbn("1111");
		bookDirectory.setSubject("jianrong");
		bookDirectory.setAuthor("jianrong  aaaa");
		bookDirectory.setPublishingTime("2011/11/11");
		bookDirectory.setErrorinfo("aaaaaaaaaaaaaaabbb");

		errorList.add(bookDirectory);
		WriteBookDirExcel.write(errorList, "aaa.xls");
	}
}
