/**
 *
 */
package com.lianyitech.modules.catalog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumMarcInfo;
import com.lianyitech.core.enmu.EnumMarcSubfileInfo;
import com.lianyitech.core.jxls.BookDirectoryImporter;
import com.lianyitech.core.jxls.ReaderImporter;
import com.lianyitech.core.jxls.WriteToOut;
import com.lianyitech.core.marc.ConvertRecord;
import com.lianyitech.core.marc.ReadBookDirMarc;
import com.lianyitech.core.utils.CommonUtils;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.dao.BatchDao;
import com.lianyitech.modules.catalog.dao.BookDirectoryDao;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.dao.ImportRecordDao;
import com.lianyitech.modules.catalog.entity.*;
import com.lianyitech.modules.catalog.utils.ImportConsumeMq;
import com.lianyitech.modules.catalog.utils.ImportProductMq;
import com.lianyitech.modules.catalog.utils.SolrPage;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.circulate.web.HttpSender;
import com.lianyitech.modules.common.BookDirectoryXmlParser;
import com.lianyitech.modules.report.service.BookDirectoryCopyService;
import com.lianyitech.modules.sys.dao.CollectionSiteDao;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.modules.sys.entity.Dict;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.service.DictService;
import com.lianyitech.modules.sys.utils.UserUtils;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lianyitech.core.config.ConfigurerConstants.*;
import static com.lianyitech.core.config.ContextConfigurer.IMPORT_QUEUE_NAME;
import static com.lianyitech.core.utils.CommonUtils.checkLength;

/**
 * 书目管理Service
 * @author zengzy
 * @version 2016-08-26
 */
@Service
@Transactional(readOnly = false)
public class BookImportService extends CrudService<BookDirectoryDao, BookDirectory> {
	private volatile Map<String,BookDirectory> bookDirectoryMap = new HashMap<String,BookDirectory>();

	@Autowired
	private BookDirectoryDao bookDirectoryDao ;
	@Autowired
	private CopyService copyService;
    @Autowired
	@Qualifier("bookSolrServer")
    private HttpSolrServer httpSolrServer;
	@Autowired
	private DictService dictService;
	@Autowired
	private BookDirectoryCopyService bookDirectoryCopyService;
	@Autowired
	ImportRecordDao importRecordDao;
	@Autowired
	private ImportService importService ;
	@Autowired
	private ReaderService readerService;
	@Autowired
	private BatchDao batchDao;
	@Autowired
	private CollectionSiteDao collectionSiteDao;
	@Autowired
	private CopyDao copyDao;

    @Autowired
    private RedisTemplate redisTemplate;

	private final static String OPERATOR_SUCCESS = "success";


	@Transactional(readOnly = false)
	public void batchSaveTemp(List<BookDirectoryForImport> list,String bookTemp,String copyTemp){
		List<Copy> copyList = new ArrayList<>();
		for (BookDirectoryForImport bookDirectory : list) {
			bookDirectory.setId(IdGen.uuid());
			if ( StringUtils.isNotEmpty(bookDirectory.getBarcode()) ){
				//验证条形码是否包括,隔开，如果有逗号“，”隔开的则是多个条形码
				String [] barcodes = bookDirectory.getBarcode().split(",");
				for (String barcode : barcodes) {
					Copy copy = new Copy();
					copy.setId(IdGen.uuid());
					copy.setCreateBy(bookDirectory.getCreateBy());
					copy.setUpdateBy(bookDirectory.getCreateBy());
					copy.setCreateDate(new Date());
					copy.setUpdateDate(new Date());
					copy.setOrgId(bookDirectory.getOrgId());
					copy.setBarcode(StringUtils.ToDBC(barcode));
					copy.setIndexnum(bookDirectory.getIndexnum());
					copy.setBatchNo(bookDirectory.getBatchNo());
					copy.setBookNo(bookDirectory.getBookNo());
					copy.setCollectionSiteName(bookDirectory.getCollectionSiteName());
					copy.setBatchNo(bookDirectory.getBatchNo());
					copy.setBookDirectoryId(bookDirectory.getId());
					copy.setStatus("0");  //在馆状态
					copy.setOrgId(bookDirectory.getOrgId());
					copyList.add(copy);
					if (copyList.size() >= 30) {
						dao.batchSaveCopyTemp(copyList,copyTemp);
						copyList = new ArrayList<>();
					}
				}
			}
		}
		if(!list.isEmpty()){
			dao.batchSaveTemp(list,bookTemp);
		}
		if(!copyList.isEmpty()) {
			dao.batchSaveCopyTemp(copyList,copyTemp);
		}
	}

	private String getKey(BookDirectory directory) {
		StringBuffer key = new StringBuffer();
		String isbn = directory.getIsbn();
		if (StringUtils.isNotBlank(isbn)) {
			isbn = isbn.replace("-", "");
		}
		key.append(isbn).append(directory.getTitle()).append(directory.getAuthor()).append(directory.getLibrarsortCode()).append(directory.getPublishingName()).append(directory.getPublishingTime())
				.append(directory.getPageNo()).append(directory.getPrice()).append(directory.getMeasure()).append(directory.getBindingForm()).append(directory.getEdition());
		return key.toString();
	}



	public String subTableName(String tableName){
		if (tableName.length() > 30) {
			tableName = tableName.substring(0, 30);
		}
		return tableName;
	}
	public void dropTempTable(String tableName){
		dao.dropTempTable(tableName);
	}
	public void createBookTemp(String tableName){
		dao.createBookTemp(tableName);
	}
	public void createCopyTemp(String tableName){
		dao.createCopyTemp(tableName);
	}


	/**
	 * 处理临时表
	 * 将临时表正确数据插入到正式库
	 */
	public int  importBookFromTemp(User user,String bookTemp,String copyTemp){
		//临时表内重复的条形码修改为验证不通过
		dao.updateTempByItself(copyTemp);
		//临时表中条形码跟正式库条形码去重对比
		dao.updateTempByOther(user.getOrgId(),copyTemp);
		//修改书目临时表中条形码都不匹配的数据修改为不匹配状态
		dao.updateNotMatchBookTemp(copyTemp, bookTemp);
		//插入新批次号
		batchDao.insertBatch(user.getOrgId(),copyTemp);
		//插入新的馆藏地
		collectionSiteDao.insertCollectionSite(user.getOrgId(),copyTemp);
		//修改临时表批次号id为对应批次表id
		dao.updateTempBatch(user.getOrgId(),copyTemp);
		//修改临时表中馆藏地对应的馆藏id
		dao.updateTempSite(user.getOrgId(),copyTemp);
		//操作临时书目（包括插入新书目，反过来修改对应id）
		operateBook(user,bookTemp);
		//插入正式复本表
		Date updateDate = new Date();
		int result = dao.insertCopy(user.getOrgId(),updateDate,copyTemp,bookTemp);
		return result;
	}

	public Map<String,Dict> getDictMap(String pid){
		Dict dict = new Dict();
		dict.setParentId(pid);
		return dictService.getDictMap(dict);
	}

	//操作书目
	private void operateBook(User user,String bookTemp){
		//修改临时表中id为正式库id
		dao.updateBookTemp(user.getOrgId(),bookTemp);
		//插入书目信息
		dao.insertBook(user.getOrgId(),bookTemp);
		//将新插入的书目id 反写到临时书目
		dao.updateBookTemp(user.getOrgId(),bookTemp);
		//2017-06-14功能改进(验证索书号编码规则是否一致)
		dao.updateBookTempByRule(user.getOrgId(),bookTemp);

	}

	public List<BookDirectoryForImport> findErrorListTemp(String bookTemp,String copyTemp){
		return dao.findErrorListTemp(bookTemp,copyTemp);
	}

	/**
	 * 书目导入验证（简单验证）
	 * @param bookDir 书目导入实体类
	 * @return boolean 是否成功
	 * @throws Exception 异常处理
	 */
	public synchronized boolean checkDirectory(BookDirectoryForImport bookDir,int type)throws Exception {
		if(StringUtils.isNotBlank(bookDir.getTanejiNo())) {
			bookDir.setTanejiNo(StringUtils.ToDBC(bookDir.getTanejiNo()));
		}
		if(StringUtils.isNotBlank(bookDir.getAssNo())) {
			bookDir.setAssNo(StringUtils.ToDBC(bookDir.getAssNo()));
		}
		String result = checkParamsForImport(bookDir,type);
		if(!result.equals(OPERATOR_SUCCESS)){
			bookDir.setErrorinfo(result);
			return false;
		}
		boolean flag = CommonUtils.resolveImportDate(bookDir);

		if(flag) {
			flag = checkLength(bookDir); //导入字段长度限制
		}

		return  flag;
	}

	private String checkParamsForImport(BookDirectoryForImport bookDir,int type){
		String result = requireRule(bookDir,type);
		if (bookDir.getUnitprice() != null && !bookDir.getUnitprice().equals("") && !bookDir.getUnitprice().equals("")) {
			String uprice = bookDir.getUnitprice().replaceAll("[`~!@#$^&*()=|{}':;',\\[\\]<>?~！@#￥……&*（）\\x20\u001e&mdash;—|{}【】‘；：”“'。，、？A-Za-z\u4e00-\u9fa5]", "");
			bookDir.setUnitprice(uprice);
		}
		//种次号判断著者号判断
		if(StringUtils.isNotEmpty(bookDir.getTanejiNo())&&StringUtils.isNotEmpty(bookDir.getBookNo())){
			result = "种次号和著者号不可同时存在";
		}
		if(StringUtils.isNotEmpty(bookDir.getTanejiNo())) {
			Boolean boo = SystemUtils.checkTanejiNo(StringUtils.ToDBC(bookDir.getTanejiNo()));
			if (!boo) {
				result = "种次号格式错误";
			}
		}
		if(StringUtils.isNotEmpty(bookDir.getBookNo())){
			bookDir.setBookNo(StringUtils.ToDBC(bookDir.getBookNo().toUpperCase()));
			Boolean boo = SystemUtils.checkBookNo(bookDir.getBookNo());
			if(!boo){
				result = "著者号格式不对";
			}
		}

		//ISBN逻辑判断
		if (StringUtils.isNotEmpty(bookDir.getIsbn())){
			try{
				if (!SystemUtils.checkISBN(bookDir.getIsbn())){
					result = "Isbn输入错误";
				}
			}catch (Exception e){
				result = "Isbn格式不对!" + e.getMessage();
			}
		}

		//单价转换从String到Double
		if (StringUtils.isNotEmpty(bookDir.getUnitprice())) {
			try {
				bookDir.setPrice(Double.valueOf(bookDir.getUnitprice()));
			}catch (Exception e){
				result = "单价输入不正确!";
			}
		}
		if(!result.equals(OPERATOR_SUCCESS)) {
			return result;
		}
		//图书分类号逻辑判断
		if (!SystemUtils.checkSortCode(bookDir.getLibrarsortCode())){
			result = "分类号不存在";//"图书分类号输入错误!";
		}else{
			//把全角转成半角
			bookDir.setLibrarsortCode(StringUtils.ToDBC(bookDir.getLibrarsortCode()));
		}
		if(!result.equals(OPERATOR_SUCCESS)) {
			return result;
		}
		//语种验证1
		if (bookDir.getLanguage() != null && !"".equals(bookDir.getLanguage())){
			Dict dict = new Dict();
			dict.setParentId("1");
			Map<String, Dict> dicMap = dictService.findListByCon(dict);
			if (dicMap != null && dicMap.containsKey(bookDir.getLanguage())) {
				bookDir.setLanguage(dicMap.get(bookDir.getLanguage()).getValue());
			} else{
				result = "语种字段值不正确，请参照馆藏导入模板!";
			}
		}
		if(!result.equals(OPERATOR_SUCCESS)) {
			return result;
		}
		//装帧形式验证 2
		if (bookDir.getBindingForm() != null && !"".equals(bookDir.getBindingForm())){
			Dict dict = new Dict();
			dict.setParentId("2");
			Map<String, Dict> dicMap = dictService.findListByCon(dict);
			if (dicMap != null && dicMap.containsKey(bookDir.getBindingForm())) {
				bookDir.setBindingForm(dicMap.get(bookDir.getBindingForm()).getValue());
			} else{
				result = "装帧形式字段值不正确，请参照馆藏导入模板!";
			}
		}
		if(!result.equals(OPERATOR_SUCCESS)) {
			return result;
		}
		//适读年龄 3
		if (bookDir.getBestAge() != null && !"".equals(bookDir.getBestAge())){
			Dict dict = new Dict();
			dict.setParentId("3");
			Map<String,Dict> dicMap = dictService.findListByCon(dict);
			if (dicMap != null && dicMap.containsKey(bookDir.getBestAge())) {
				bookDir.setBestAge(dicMap.get(bookDir.getBestAge()).getValue());
			} else {
				result = "适读年龄字段值不正确，请参照馆藏导入模板!";
			}
		}
		if(!result.equals(OPERATOR_SUCCESS)) {
			return result;
		}
		//图书用途 4
		if (bookDir.getPurpose() != null && !"".equals(bookDir.getPurpose())){
			Dict dict = new Dict();
			dict.setParentId("4");
			Map<String, Dict> dicMap = dictService.findListByCon(dict);
			if (dicMap != null && dicMap.containsKey(bookDir.getPurpose())) {
				bookDir.setPurpose(dicMap.get(bookDir.getPurpose()).getValue());
			} else {
				result = "图书用途字段值不正确，请参照馆藏导入模板!";
			}
		}
		return result ;
	}

	private String requireRule(BookDirectoryForImport bookDir,int type){
		if (StringUtils.isEmpty(bookDir.getTitle())){
			return "题名不能为空!";
		} else if (StringUtils.isEmpty(bookDir.getLibrarsortCode())){
			return "图书分类号不能为空!";
		} else if (StringUtils.isEmpty(bookDir.getBarcode())){
			return "条形码不能为空";
		} else if (StringUtils.isEmpty(bookDir.getTanejiNo()) && StringUtils.isEmpty(bookDir.getBookNo())){
			return "种次号跟著者号必须存在一个";
		}
		if (type==1) {//回溯建库
			if (StringUtils.isEmpty(bookDir.getIsbn())){
				return "ISBN不能为空!";
			}else if (StringUtils.isEmpty(bookDir.getAuthor())){
				return "著者不能为空!";
			}else if (StringUtils.isEmpty(bookDir.getPublishingName())){
				return "出版社不能为空!";
			}else if (StringUtils.isEmpty(bookDir.getPublishingTime())){
				return "出版时间不能为空!";
			}else if (StringUtils.isEmpty(bookDir.getUnitprice())){
				return "定价不能为空!";
			}else if (StringUtils.isNotEmpty(bookDir.getTitle()) && bookDir.getTitle().length() >= 256){
				return "题名太长";
			}
		}
		return OPERATOR_SUCCESS;
	}
}