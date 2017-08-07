package com.lianyitech.modules.catalog.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumMarcInfo;
import com.lianyitech.core.enmu.EnumMarcSubfileInfo;
import com.lianyitech.core.jxls.WriteToOut;
import com.lianyitech.core.jxls.BookDirectoryImporter;
import com.lianyitech.core.jxls.ReaderImporter;
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
import com.rabbitmq.client.Channel;
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
@Transactional(readOnly = true)
public class BookDirectoryService extends CrudService<BookDirectoryDao, BookDirectory> {
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
	BookImportService bookImportService;

	private final static String OPERATOR_SUCCESS = "success";

	@Transactional(readOnly = true)
	public void updateProgress(ImportRecord record) {
		importRecordDao.updateProgress(record);
	}

//	public BookDirectory get(String id) {
//		return super.get(id);
//	}

	@Transactional
	public void save(BookDirectory bookDirectory){
		bookDirectory.setOrgId(UserUtils.getOrgId());//设置机构id
		super.save(bookDirectory);
	}

	//这个是带复本数
	public BookDirectory getBookDirectory(String id) {
		BookDirectory bookDirectory = new BookDirectory();
		bookDirectory.setId(id);
		bookDirectory = getBookDirectory(bookDirectory);
		if(bookDirectory!=null){
			int booknumer = bookDirectoryDao.getBookNumber(bookDirectory);
			bookDirectory.setBooknumber(booknumer);
		}
		return bookDirectory;
	}
   //复制接口
	public HashMap<String,Object> copy(String id){
		HashMap<String, Object> result ;
		String industryUrl = Global.getConfig(INDUSTRY_URL);
		HashMap<String, String> param = new HashMap<>();
		param.put("id",id);
		result = HttpSender.sendForm(industryUrl + "/api/catalog/bookdirectory/copy",param);//根据id查询行馆接口
		return result;
	}

	public List<BookDirectory> findList(BookDirectory bookDirectory) {
		bookDirectory.setOrgId(UserUtils.getOrgId());//设置机构id
		return super.findList(bookDirectory);
	}

	public Page<BookDirectory> findPage(Page<BookDirectory> page, BookDirectory bookDirectory) {
		bookDirectory.setOrgId(UserUtils.getOrgId());//设置机构id
		return super.findPage(page, bookDirectory);
	}

	//保存简单书目
	@Transactional
	public Map<String,Object> saveSimpleBook(BookDirectory bookDirectory) {
		Map<String, Object> reMap = new HashMap<>();
		BookDirectory bookDirectory1 = dao.get(bookDirectory);
		save(bookDirectory);//这里只修改其简单编目信息(此时如果里面存在马克数据的情况，不会修改里面属性内容，修改其属性内容在下面)
		BookDirectory b = getBookDirectory(bookDirectory);//再到数据库里面查询一遍
		String marcdata =  convertMarcData(b,null);//根据数据库里面存在的数据重新去生成对应的马克格式数据
		bookDirectory.setMarc64(marcdata);
		bookDirectory.setLibrarsortCode(StringUtils.ToDBC(bookDirectory.getLibrarsortCode()));
		bookDirectory.setUpdateDate(new Date());
		bookDirectoryDao.updateMarc(bookDirectory);//再修改马克字段信息
		//提示分类号已修改，哪个副本的索书号有变更
		if(bookDirectory1!=null){
		if (!bookDirectory.getLibrarsortCode().equals(bookDirectory1.getLibrarsortCode())) {
			List<String> list = copyDao.listBarcode(bookDirectory);
			reMap.put("message", "您所修改的分类号已经影响到了以下复本的索书号变更，请重新打印书标，以防后续无法定位图书。\n" +
					"已变更索书号的图书为：");
			reMap.put("list", list);
			return reMap;
		}
		}
		return reMap;
	}
	//保存对应的马克数据以及对应的书目信息
	@Transactional
	public String saveBookMarc(BookDirectory bookDirectory, HttpServletRequest request) {
        bookDirectory.setOrgId(UserUtils.getOrgId());
		if( bookDirectory.getId()!=null && !bookDirectory.getId().equals("")){
			bookDirectory = getBookDirectory(bookDirectory);
		}
		if(bookDirectory == null){
			bookDirectory = new BookDirectory();
			bookDirectory.setOrgId(UserUtils.getOrgId());
		}
		String marcdata = convertMarcData(bookDirectory,request);
		int count = this.getCountByCon(bookDirectory);
		if (count > 0) {
			return "此条书目已重复";
		}
		save(bookDirectory);
		bookDirectory.setMarc64(marcdata);
		bookDirectoryDao.updateMarc(bookDirectory);//再修改马克字段信息
		return null;
	}

	public BookDirectory getBookDirectory(BookDirectory bookDirectory){
		if(StringUtils.isEmpty(bookDirectory.getOrgId())) {
			bookDirectory.setOrgId(UserUtils.getOrgId());
		}
		return dao.getBookDirectory(bookDirectory);
	}

	/*
	* 公用方法通过request为null区分
	* (request不为空将前端传过来的参数进行转成对应的马克数据字符串)--(request为空根据简单编目信息转换成需要的马克数据内容（取已有马克数据和模板并集）公用方法)
	* */
	private String convertMarcData(BookDirectory bookDirectory,HttpServletRequest request){
		 Record record;
		if (request != null) {
			 record = ConvertRecord.makeRecordfromRequest(bookDirectory,request);
		} else {
			 record = ConvertRecord.makeRecordfromBookDirectory(bookDirectory);//根据简单编目实体类将其对应的马克数据也要修改返回record
		}
		return  ConvertRecord.commonConvertMarcData(record);
	}

	@Transactional
	public void delete(BookDirectory bookDirectory) {
		bookDirectory.setOrgId(UserUtils.getOrgId());
		bookDirectory.setUpdateDate(new Date());
		super.delete(bookDirectory);
	}

	public int getCountByCon(BookDirectory bookDirectory) {
		return bookDirectoryDao.getCountByCon(bookDirectory);
	}

	//判断导入书目是否唯一
	private BookDirectory uniqueAttr(BookDirectory bookDirectory) {
		BookDirectory book = new BookDirectory();
		book.setIsbn(bookDirectory.getIsbn());
		book.setTitle(bookDirectory.getTitle());
		book.setAuthor(bookDirectory.getAuthor());
		book.setPublishingTime(bookDirectory.getPublishingTime());
		book.setPublishingName(bookDirectory.getPublishingName());
		book.setPageNo(bookDirectory.getPageNo());
		book.setPrice(bookDirectory.getPrice());
		book.setMeasure(bookDirectory.getMeasure());
		book.setBindingForm(bookDirectory.getBindingForm());
		book.setEdition(bookDirectory.getEdition());
		return book;
	}

	@Transactional
	private void saveThisAndsaveCopy(BookDirectory bookDirectory) {
		//判断导入书目是否唯一
		bookDirectory.setOrgId(UserUtils.getOrgId());//赋值orgid
		int count = dao.getCount(uniqueAttr(bookDirectory));
		if (count <1) {
			save(bookDirectory);
		}

		if (bookDirectory.getBarcode()!=null){
			if (!bookDirectory.getBarcode().equals("")){
				Copy copy = new Copy();
				copy.setOrgId(bookDirectory.getOrgId());
				copy.setBarcode(bookDirectory.getBarcode());
				copy.setIndexnum(bookDirectory.getIndexnum());
				copy.setBatchNo(bookDirectory.getBatchNo());

				//通过馆藏地名称获得馆藏地id再进行保存复本
				CollectionSite collectionSite = bookDirectoryDao.findSite(bookDirectory.getCollectionSiteName(),bookDirectory.getOrgId());
				if(collectionSite!=null){
					copy.setCollectionSiteId(collectionSite.getId());
				}
                bookDirectory=dao.findId(bookDirectory);
                if(bookDirectory !=null){
                    copy.setBookDirectoryId(bookDirectory.getId());
                }
				copy.setStatus("0");  //在馆状态
				try {
					copyService.save(copy);
				}catch (Exception ignored){
					logger.error(ignored.getMessage());
				}
			}
		}
	}
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
					copy.setBarcode(barcode);
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

	@Transactional(readOnly = false)
	public void batchSave(List<BookDirectory> list){
		List<BookDirectory> newList = new ArrayList<BookDirectory>();
		List<Copy> copyList = new ArrayList<Copy>();
		for (BookDirectory bookDirectory : list) {
			if(bookDirectoryMap.get(getKey(bookDirectory))==null){
				newList.add(bookDirectory);
				bookDirectoryMap.put(getKey(bookDirectory),bookDirectory);
			} else {//发现有重复的，应该用现有的书目
				bookDirectory.setId(bookDirectoryMap.get(getKey(bookDirectory)).getId());
			}

			if ( StringUtils.isNotEmpty(bookDirectory.getBarcode()) ){
				Copy copy = new Copy();
				copy.setId(IdGen.uuid());
				copy.setCreateBy(bookDirectory.getCreateBy());
				copy.setUpdateBy(bookDirectory.getCreateBy());
				copy.setCreateDate(new Date());
				copy.setUpdateDate(new Date());
				copy.setOrgId(bookDirectory.getOrgId());
				copy.setBarcode(bookDirectory.getBarcode());
				copy.setIndexnum(bookDirectory.getIndexnum());
				copy.setBatchNo(bookDirectory.getBatchNo());
				copy.setBookNo(bookDirectory.getBookNo());
				String orgId = bookDirectory.getOrgId();

				if(StringUtils.isNotBlank(bookDirectory.getCollectionSiteName())){
					//通过馆藏地名称获得馆藏地id再进行保存复本
					CollectionSite collectionSite = bookDirectoryCopyService.findSiteByObj(bookDirectory);
					if(collectionSite!=null){
						copy.setCollectionSiteId(collectionSite.getId());
					}else{//如果馆藏地为空，没做处理
						//现在如果为空，就新增馆藏地
						collectionSite = new CollectionSite();
						collectionSite.setId(IdGen.uuid());
						collectionSite.setOrgId(orgId);
						collectionSite.setName(bookDirectory.getCollectionSiteName());
						collectionSite.setStockAttr("1");//默认值
						collectionSite.setStockType("43");
						collectionSite.setCreateDate(new Date());
						collectionSite.setCreateBy(bookDirectory.getCreateBy());
						collectionSiteDao.insert(collectionSite);
						copy.setCollectionSiteId(collectionSiteDao.findIdByName(collectionSite.getName(),orgId));
					}
				}

				//如果馆藏导入中有批次号，先查询该批次号，如果存在获得批次号ID，如果没有就把该批次号插入到批次表中。
				if(StringUtils.isNotBlank(bookDirectory.getBatchNo())){
					Batch parmBatch = new Batch();
					parmBatch.setBatchNo(bookDirectory.getBatchNo());
					parmBatch.setType("0");
					parmBatch.setOrgId(bookDirectory.getOrgId());
					Batch batch = batchDao.getByBatchNo(parmBatch);
					if(batch!=null){
						copy.setBatchId(batch.getId());
					}else{
						parmBatch.setStatus("1");
						parmBatch.setId(IdGen.uuid());
						parmBatch.setCreateBy(bookDirectory.getUserLoginName());
						parmBatch.setCreateDate(new Date());
						batchDao.insert(parmBatch);
						copy.setBatchId(batchDao.getByBatchNo(parmBatch).getId());
					}
				}

				copy.setBookDirectoryId(bookDirectory.getId());
				copy.setStatus("0");  //在馆状态
				copy.setOrgId(bookDirectory.getOrgId());
				copyList.add(copy);
			}
		}

		if(!newList.isEmpty()){
			dao.batchinsert(newList);
		}
		if(!copyList.isEmpty()) {
			dao.batchInsertCopyList(copyList);
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


	//生成最新种次号
	public int createTaneJINo(BookDirectory bookDirectory) {
		//这里设置机构id
		bookDirectory.setOrgId(UserUtils.getOrgId());
		int maxtnejino = bookDirectoryDao.getMaxTanejiNo(bookDirectory);
		if(maxtnejino >= 99999){
			return 99999;
		}
		return maxtnejino+1;
	}



    @Autowired
    private Environment environment;
    @Autowired
    private ConnectionFactory connectionFactory;

    //馆藏数据上传
    public void upBookDirFile(MultipartFile multiFile,int type) throws IOException {
        long size = multiFile.getSize();
        if(size <= 0) {
            throw new RuntimeException("空的文件");
        }
        String fileName = multiFile.getOriginalFilename();
        //文件名长度限制
        if (fileName.length() > 30) {
            throw new RuntimeException("文件名太长不要超过30长度！");
        }
		String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match=pattern.matcher(fileName.substring(0,fileName.lastIndexOf(".")));
		if(!match.matches()) {
			throw new RuntimeException("文件名不合法！导入的文件名称不支持特殊字符，请以字母、汉字、数字命名文件");
		}
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        final List<String> extensionList = Arrays.asList(".xls", ".xlsx", ".iso");
        if(extensionList.indexOf(extension) == -1) {
            throw new RuntimeException("不支持的类型，支持xls，xlsx，iso三种格式。");
        }else if(size > 10485760 * 5) {
            throw new RuntimeException("文件过大， 支持10M内的Excel文件或者50M内的ISO文件。");
        }
        String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "") + extension;
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "bookDir"});
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = uploadPath + File.separator + uploadFileName;
        file = new File(filePath);
        if (file.createNewFile()) {
            FileCopyUtils.copy(multiFile.getBytes(), file);
            ImportRecord recode = importService.initImportRecord(fileName, filePath, extensionList.indexOf(extension), type);
            Object[] objects = new Object[]{UserUtils.getUser(), recode};
            try {
                this.sendMq(objects);
            } catch (Exception e) {
                logger.error("操作失败",e);
            }
        }
    }



	@PostConstruct
	public void initMqConsume(){
		String importEnable = environment.getProperty(IMPORT_ENABLE);
		if(StringUtils.isNotBlank(importEnable) && importEnable.equals("1")) {
			String import_queue = environment.getProperty(IMPORT_QUEUE);
			if(StringUtils.isNotBlank(import_queue)){
				String[] tmp = import_queue.split(",");
				for (String s : tmp) {
					String queueName = environment.getProperty("config.area")+"."+IMPORT_QUEUE_NAME+"."+ s;
					try{
						String deleteQueue = environment.getProperty(REBUILD_QUENE);
						if(StringUtils.isNotBlank(deleteQueue) && deleteQueue.equals("1")) {//是否重建队列
							connectionFactory.newConnection().createChannel().queueDelete(queueName);
						}
						ImportConsumeMq.consume(queueName, connectionFactory, handler);
					}catch (Exception e) {
						logger.error("初始化MQ队列监听失败",e);
					}
				}
			}
		}
	}



    /**
     * 动态创建rabbitMq导入
     *
     * @param msg object数组
     * @throws Exception 异常
     */
    ImportConsumeMq.Handler handler = (channel, consumerTag, envelope, properties, body) -> {
        try {
            //随机休眠下
            Thread.sleep((long) (Math.random() * 4000));
            ObjectMapper objectMapper = new ObjectMapper();
            Object[] o = objectMapper.readValue(body, Object[].class);
            commonImport(objectMapper, o);
        } catch (Exception e) {
            throw e;
        }

    };
    public synchronized void sendMq(Object[] msg) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(mapper.writeValueAsString(msg[0]), User.class);
        byte[] data=mapper.writeValueAsBytes(msg);
        String orgId = user.getOrgId();
        int num = Integer.parseInt(environment.getProperty(MQ_NUM));
		int hashCode = orgId.hashCode() % num;
		if(hashCode<0){
			hashCode = -hashCode;
		}
        String queueName = environment.getProperty("config.area")+"."+IMPORT_QUEUE_NAME+"."+ hashCode;
        new ImportProductMq(queueName,connectionFactory,handler,data);
    }


    /**
     * 公共导入代码--包括书目读者
     * @param mapper ObjectMapper
     * @param msg 传值数组
     * @throws IOException 异常
     */
    public void commonImport(ObjectMapper mapper,Object[] msg) throws Exception {
        ImportRecord recode = mapper.readValue(mapper.writeValueAsString(msg[1]), ImportRecord.class);
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "bookDir"});
        String filePath = recode.getFilePath();
        try (FileInputStream fin = new FileInputStream(filePath)) {
            String errorFile = uploadPath+File.separator+new StringBuilder(recode.getFileName()).insert(recode.getFileName().lastIndexOf("."), "-错误列表-"+new SimpleDateFormat("YYYYMMDDhhmmss").format(new Date())).toString(); //.insert(recode.getFileName().lastIndexOf("."), "错误列表").toString();//recode.getFileName() + "错误列表-"+new SimpleDateFormat("YYYYMMDDhhmmss").format(new Date()).trim();
            errorFile = errorFile.substring(0,errorFile.lastIndexOf("."))+".xlsx";
            if (recode.getType()==2) {
				new ReaderImporter(
						fin,
						recode,
						errorFile,
						readerService,
						mapper.readValue(mapper.writeValueAsString(msg[0]), User.class)
				).doImport();
            }else {
				new BookDirectoryImporter(
						fin,
						recode,
						errorFile,
						bookImportService,
						mapper.readValue(mapper.writeValueAsString(msg[0]), User.class)
				).doImport();
            }
        } catch (Exception e) {
			recode.setProgress("操作失败");
            logger.error("操作失败",e);
            throw e;
        } finally {
        	//修改导入记录日志表
			importService.updateImportRecord(recode);
		}
	}

    public boolean downloadExcle(String fileName,  HttpServletResponse response) throws UnsupportedEncodingException{
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "bookDir"});
        String path = uploadPath + File.separator + fileName;
        return WriteToOut.writeToResponse2(fileName, path, response);
    }

    //操作书目
    private void operateBook(User user,String bookTemp){
        //修改临时表中id为正式库id
        dao.updateBookTemp(user.getOrgId(),bookTemp);
        //插入书目信息
        dao.insertBook(user.getOrgId(),bookTemp);
        //将新插入的书目id 反写到临时书目
        dao.updateBookTemp(user.getOrgId(),bookTemp);
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
            Boolean boo = SystemUtils.checkTanejiNo(bookDir.getTanejiNo());
            if (!boo) {
                result = "种次号格式错误";
            }
        }
        if(StringUtils.isNotEmpty(bookDir.getBookNo())){
        	bookDir.setBookNo(bookDir.getBookNo().toUpperCase());
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

    /**
     * 书目导入验证（简单验证）
     * @param bookDir 书目导入实体类
     * @return boolean 是否成功
     * @throws Exception 异常处理
     */
    public synchronized boolean checkDirectory(BookDirectoryForImport bookDir,int type)throws Exception {
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

    //查缺种次号前面10条
    public List<String> checkTaneJINo(BookDirectory bookDirectory) {
        //设置机构id
        bookDirectory.setOrgId(UserUtils.getOrgId());
        List<String> list = bookDirectoryDao.getTaneJINo(bookDirectory);
        int max = bookDirectoryDao.getMaxTanejiNo(bookDirectory);
        List<String> checklist = new ArrayList<>();
        for(int i=1;i<=max;i++){
            if(!list.contains(String.valueOf(i))){
                checklist.add(String.valueOf(i));
            }
            if(checklist.size()==10){
                break;
            }
        }
        return checklist;
    }
	//种次号唯一判断
	public Map<String,String> uniqueTanejiNo(BookDirectory bookDirectory) {
		bookDirectory.setOrgId(UserUtils.getOrgId());
		Map<String,String> result = new HashMap<>();
		List<String> list = bookDirectoryDao.getTaneJINo(bookDirectory);//公用sql要多传一个条件
		if (list != null && list.size() > 0) {
			result.put("fail","该种次号已被占用，是否要重复使用该种次号？");
		}
		return result;
	}
	//根据条件得到马克数据信息
	public Object getMarcInfo(BookDirectory bookDirectory){
		BookDirectory b = getBookDirectory(bookDirectory);
		if (b == null) {
			b = bookDirectory;
		}
		String marcinfo = "";
		if (b.getMarc64() != null) {
			marcinfo = b.getMarc64();
		}
		InputStream inputS = null;
		try {
			inputS = new ByteArrayInputStream(marcinfo.getBytes("GBK"));
			ReadBookDirMarc readBookDirMarc = new ReadBookDirMarc();
			return readBookDirMarc.parseMarcLine(inputS,b);
		}catch (UnsupportedEncodingException e){
			//抛出异常的情况下另外解析
			logger.error("操作失败",e);
			return null;
		} finally {
			IOUtils.closeQuietly(inputS);
		}
	}
	//马克格式验证
	private String checkMarc(List<Map> list){
		if (list != null && list.size() > 0) {
			Map<String, Map<String,Object>> tempmarc =  EnumMarcInfo.getTempmarc();
			for(Map map : list){
				String tag = map.get("tag").toString();
				//String indx1 = map.get("indx1").toString();
				String mdata = map.get("mdata").toString();
				Map marcmap = EnumMarcInfo.getMainNotSubFiled(tag);//得到所有的马克字段不包括子字段的
				Map<String, Object> mapinfo =  EnumMarcInfo.getMainSubFiled(tag);
				Map suballfieldinfo = (Map)mapinfo.get("subfiled");
				if (marcmap!=null && marcmap.containsKey("mainfiled")) {

					Map tempmap = (Map) tempmarc.get(tag);
					Map tempsubfiledmap = new HashMap();//模板子字段
					if (tempmap != null && tempmap.get("subfiled") != null) {
						tempsubfiledmap.putAll((Map) tempmap.get("subfiled"));
					}
					EnumMarcInfo mainfiled = (EnumMarcInfo) marcmap.get("mainfiled");
					String require = mainfiled.getNnull();
					String name = mainfiled.getName();
					String ftype = mainfiled.getFtype();
					//得到主字段信息是否是必填项
					if (require != null && require.equals("1")) {
						if (mdata.trim().equals("")) {
							return tag+"("+name+")马克字段为必填项！";
						}
					}
					if (ftype.equals("3")) {//数据字段区--主要是验证这块(第一判断子字段是否存在重复，第二判断是否包含了模板里面的字段信息第三判断是否必填项的内容为空)
						int index_s = mdata.indexOf("$");
						if ((index_s != 0 && require!=null && require.equals("1"))||(index_s==-1 && !mdata.equals(""))) {//说明第一项不是正确的马克字段（如果是必填项的情况下）
							return tag+"("+name+")马克子字段格式有错误，第一个字母不是$";
						} else {
							Map<String,String> sfcfmap = new HashMap<>();//用来判断是否重复子字段
							String[] substrs = mdata.split("\\$");
							if ( substrs.length > 0) {
								for (String substr : substrs) {
									if (substr != null && substr.length()>0) {
										String code = substr.substring(0, 1);
										String data = substr.substring(1);
										String tag$code = tag+"$"+code;
										//验证isbn是否合法和分类号是否合法
										if( tag$code.equals("010$a") || tag$code.equals("690$a") ){
											BookDirectory bookDirectory = new BookDirectory();
											if(tag$code.equals("010$a")){
												bookDirectory.setIsbn(data);
											}
											if(tag$code.equals("690$a")){
												bookDirectory.setLibrarsortCode(data);
											}
											Map<String,String> chechmap = checkBookProperty(bookDirectory);
												for (String k : chechmap.keySet()) {//循环模板子字段
                                                    if(chechmap.get(k) !=null){
                                                        return chechmap.get(k);
                                                    }
												}
                                        }
										//首先要判断枚举里面是否存在
										if (suballfieldinfo != null && !suballfieldinfo.containsKey(code)) {
											return tag+"("+name+")马克子字段里不存在$"+code;
										}
										if(sfcfmap.containsKey(code)){
											return tag+"("+name+")"+code+"子字段重复";
										}else{
											sfcfmap.put(code,code);
										}
										//判断是否是必填项--只有模板里面才存在必填项不是模板不存在必填项之所
										if (tempsubfiledmap.containsKey(code)) {
											EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(tag,code);
											if (subfileInfo != null) {
												if(subfileInfo.getNnull()!=null && (subfileInfo.getNnull().equals("1"))){//必填
													if (data.trim().equals("")) {
														return tag+"("+name+")子字段$"+code+"("+subfileInfo.getName()+")为必填字段，不能为空！";
													}
												}
												tempsubfiledmap.remove(code);
											}
										}
									}
								}
							}
						}

						for (Object o : tempsubfiledmap.keySet()) {//循环模板子字段
							EnumMarcSubfileInfo subfileInfo =  EnumMarcSubfileInfo.getSubObject(tag,o.toString());
							if (subfileInfo != null) {
									return tag+"("+name+")子字段$"+o.toString()+"("+subfileInfo.getName()+")为模板必备字段不能缺少！";
							}
						}
					}

				}else{
					return tag+"马克字段不存在";
				}
			}
		}
		return null;
	}

	 public Map<String,Object> checkMarcMap(List<Map> list){
		 Map<String, Object> result = new HashMap<>();
		 String str = this.checkMarc(list);
		 if(str!=null){
			 result.put("fail",str);
		 }else{
			 result.put("success","验证通过");
		 }
		 return result;
	 }
	/**
	 * 验证书目属性是否合法
	 */
	public Map<String,String> checkBookProperty(BookDirectory bookDirectory) {
		Map<String, String> chechmap = new HashMap<>();
		//ISBN逻辑判断
		if (bookDirectory.getIsbn() != null && !bookDirectory.getIsbn().equals("")) {
				try{
                        if (!SystemUtils.checkISBN(bookDirectory.getIsbn().replace("-", ""))){
                            chechmap.put("isbn", "Isbn输入错误！");
                        }
				}catch (Exception e){
					logger.error("操作失败",e);
					chechmap.put("isbn",e.getMessage());
				}
		}
		/*+
		//分类号验证
		if (bookDirectory.getLibrarsortCode()!=null && !bookDirectory.getLibrarsortCode().equals("") ) {
			if (librarsortService.checkExistSortCode(bookDirectory.getLibrarsortCode()).containsKey("fail")){//if (!SystemUtils.checkSortCode(bookDirectory.getLibrarsortCode())) {
				chechmap.put("librarsortCode", "分类号不存在!");
			}
		}*/
		return chechmap;
	}
	//根据tag后面模糊搜索马克主字段
	public List<Map> getMarcinfo(String tag) {
		return EnumMarcInfo.getMarcByLikeTag(tag);
	}
    //根据条件通用查询
    private SolrDocumentList queryCommonSolr(int start,int rows,String constr,String filterstr){
        SolrQuery solrquery = new SolrQuery();
        solrquery.setQuery(constr);
        if(filterstr!=null && !("".equals(filterstr))){
            solrquery.addFilterQuery(filterstr);
        }
        solrquery.setStart(start);
        solrquery.setRows(rows);
        QueryResponse rsp;
        try {
            rsp = httpSolrServer.query(solrquery);
            return rsp.getResults();
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new SolrDocumentList();
        }
    }

	//单独写个验证只针对带有复本的书目马克编辑中图分类字段
	public Map<String, String> checkSortCodeMarc(Map map) {
		Map<String, String> result = new HashMap<>();
		String oldcode = map.get("oldcode") != null ? map.get("oldcode").toString() : "";
		String newcode = map.get("newcode") != null ? map.get("newcode").toString() : "";
		String[] olds = oldcode.split("\\$");
		String[] news = newcode.split("\\$");
		String returncode = "";
		String oldsortcode = "";
		String newsortcode = "";
		if (olds.length > 0) {
			for (String oldstr : olds) {
				if (oldstr != null && oldstr.length() > 0) {
					String code = oldstr.substring(0, 1);
					String data = oldstr.substring(1);
					if ("a".equals(code)) {//分类号写死验证
						oldsortcode = data;
						break;
					}
				}
			}
		}
		if (news.length > 0) {
			for (String newstr : news) {
				if (newstr != null && newstr.length() > 0) {
					String code = newstr.substring(0, 1);
					String data = newstr.substring(1);
					if ("a".equals(code)) {//分类号
						newsortcode = data;
						returncode += "$a" + oldsortcode;
					} else {
						returncode += "$"+newstr;
					}
				}
			}
		}
		if (!newcode.contains("$a")) {
			returncode = "$a" + oldsortcode + returncode;
		}
		if (!newsortcode.equals(oldsortcode)) {
			result.put("fail", "分类号不能修改");
		}
		result.put("returncode", returncode);
		return result;
	}
	/**
	 * 查询本馆分页书目、solr分页书目
	 * （2种结果并合显示并分页）
	 * -如果传入了id的情况下只需要显示本馆的(这个是针对修改返回列表的情况下)
	 * @param solrpage      solr分页实体
	 * @param page          本馆书目分页实体
	 * @param bookDirectory 书目实体
	 * @return solr分页实体
	 */
	public SolrPage findSolrBookPage(SolrPage solrpage, Page<BookDirectory> page, BookDirectory bookDirectory) {
		//查询本馆书目带分页
		bookDirectory.setOrgId(UserUtils.getOrgId());

		String[] keyWords3 = bookDirectory.getIsbn().split(" ");
		String sql ="";
		for (String keyWords0:keyWords3) {
			if (StringUtils.isEmpty(keyWords0))
				{
					continue;
				}
				String Sql1 = "(isbn like '%' ||'" + keyWords0.replace("-", "") + "'|| '%' or title like '%' ||'" + keyWords0 + "'|| '%'" +
						" or author like '%' ||'" + keyWords0 + "'|| '%'or PUBLISHING_NAME like '%' ||'" + keyWords0 + "'|| '%'or SERIES_NAME like '%' ||'" + keyWords0 + "'|| '%')";
				sql = sql + "and " + Sql1;
			}
            bookDirectory.setSqlStr(sql);


		page = super.findPage(page, bookDirectory);
		//定义一个list放所有的查询结果（本馆和solr书目(solr查询本身带从哪条到哪哪条)）
		List<Object> listAll = new ArrayList<>();
		//下面代码算剩下条数，和查行馆开始行等------
		if(page.getPageNo()==solrpage.getPageNo()){//page查询如果页数不存在会默认为1,
			listAll.addAll(page.getList());
		}else{
			page.setList(new ArrayList<>());//设置为空
		}
		//这里判断是否传入了id则只需要查询单馆书目
		if (bookDirectory.getId() != null && !"".equals(bookDirectory.getId())) {
			solrpage.setCount((int) page.getCount());//设置总条数
		} else {
			//solr查询其他馆书目起始数
			int otherStart = 0;
			//solr查询其他馆书目条数(由于solr查询是从起始条数 ，多少条进行查询的)
			int otherRows = 0;
			int start = (solrpage.getPageNo() - 1) * solrpage.getPageSize();
			//如果本馆查询的条数小于page页每页条数的话剩下的要将其他馆的书目填充到当前页
			if (listAll.size() < solrpage.getPageSize()) {
				otherRows = solrpage.getPageSize() - listAll.size();
				if (start - (int) page.getCount() > 0) {
					otherStart = start - (int) page.getCount();
				}
			}
			//上面代码算剩下条以及开始行等-----------
			//查询其他馆
			String other_con_str = "!orgId:" + bookDirectory.getOrgId();//其他学校的书目信息查询条件
			String filter_con_str = "";
			StringBuilder filter_con_str1 = new StringBuilder("");
			String str ="";
			if (bookDirectory.getIsbn() != null && !"".equals(bookDirectory.getIsbn())) {//拼凑组合查询
				String keyWords1 = StringEscapeUtils.unescapeHtml4(bookDirectory.getTitle());//解决特殊字符查询不出结果的问题
				String[] keyWords2 = keyWords1.split(" ");
				for (String keyWords:keyWords2){
					if("".equals(keyWords))
					{
						continue;
					}
					filter_con_str = "(isbn:" + keyWords.replace("-", "") + " OR title:" + keyWords + " OR author:" + keyWords + " OR publishingName:" + keyWords + " OR seriesName:" + keyWords + ")";

					filter_con_str1.append(filter_con_str+"AND");
				}
				 str = filter_con_str1.substring(0,filter_con_str1.length()-3);
			}
			SolrDocumentList other_results = queryCommonSolr(otherStart, otherRows, other_con_str, str);
			listAll.addAll(other_results);
			//如果listAll为空的情况下查询豆瓣接口--接口提出去
			if (listAll.isEmpty()) {
				queryDouban(bookDirectory,listAll);
			}
			//设置总条数
			solrpage.setCount((int) page.getCount() + other_results.getNumFound());
		}
		solrpage.setList(listAll);
		//初始化算出pageCount
		solrpage.initialize();
		return solrpage;
	}

	/**
	 * 查询豆瓣接口--（将之前放入solr里面的代码移过来的）
	 * @param bookDirectory 书目实体类
	 * @param listAll 结果
	 */
	private void queryDouban(BookDirectory bookDirectory,List<Object> listAll){
		//如果listAll为空的情况下继续查询豆瓣
		boolean checkIsbnFlag;
		try {
			checkIsbnFlag = SystemUtils.checkISBN(bookDirectory.getIsbn());
		} catch (Exception e) {
			checkIsbnFlag = false;
			logger.error("操作失败",e);
		}
		try {
			if (checkIsbnFlag) {
				BookDirectory book = BookDirectoryXmlParser.parse_book(bookDirectory.getIsbn());
				if (book != null) {
					Map<String, String> map_book = BeanUtils.describe(book);
					map_book.put("_version_", "douban");
					listAll.add(map_book);
				}
			}
		} catch (Exception e) {
			logger.error("操作失败",e);
		}
	}

	public List<BookDirectoryForImport> findErrorListTemp(String bookTemp,String copyTemp){
		return dao.findErrorListTemp(bookTemp,copyTemp);
	}
	public Map<String,Dict> getDictMap(String pid){
		Dict dict = new Dict();
		dict.setParentId(pid);
		return dictService.getDictMap(dict);
	}
}