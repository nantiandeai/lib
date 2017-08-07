package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.utils.CommonUtils;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.BarcodeRecord;
import com.lianyitech.modules.catalog.entity.Batch;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.LibraryCopy;
import com.lianyitech.modules.catalog.service.CopyService;
import com.lianyitech.modules.catalog.utils.ExportCsv;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.service.BillService;
import com.lianyitech.modules.circulate.service.CirculateService;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lianyitech.core.config.ConfigurerConstants.LIB3_EXPORT_PAGESIZE;
import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 馆藏复本管理Controller
 * @author zengzy
 * @version 2016-08-26
 */
@RestController
@RequestMapping(value = "/api/catalog/copy")
public class CopyController extends ApiController {

	@Autowired
	private CopyService copyService;

	@Autowired
	private CirculateService service;

	@Autowired
	private BillService billService;

	@Autowired
	private Environment environment;
	/**
	 * 馆藏复本查询
	 * @param copy copy
	 * @param request request
	 * @param response response
	 * @return HttpStatus
	 */
//	@Secured({"ROLE_lib3school.api.catalog.copy.get"})
	@RequestMapping(value = "",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> list(Copy copy, HttpServletRequest request, HttpServletResponse response) {
		copy.setType("'0','1','5'");//馆藏清单页面至显示在馆、借出和预借
		Page<Copy> page = copyService.findPage(new Page<>(request, response), copy);
		resolveCatalogList(page.getList());
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}




	/**
	 * 馆藏清单导出
	 * @param copy copy
	 * @param response response
	 * @throws IOException 异常
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.collectionBookReports.get"})
	@RequestMapping(value = "/collectionBookReports",method = RequestMethod.GET)
	public void collectionBookReports(Copy copy, HttpServletResponse response) throws IOException {
		copy.setType("'0','1','5'");//馆藏清单页面至显示在馆、借出和预借
		copy.setOrgId(UserUtils.getOrgId());
		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;
		setCvsConfig(response,"馆藏清单");
		int pageNo = 1;
		copy.setPageSize(Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE)));
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("ISBN,索取号,条形码,分类号,题名,丛书名,著者,出版社,出版日期,价格,入库日期,馆藏地点,馆藏状态");
			CsvUtils.exportBookReportCsv(bw,resultList);
			while (true) {
				copy.setPageNo(pageNo);
				List<Copy> pageList = copyService.collectionBookReports(copy);
				resolveCatalogList(pageList);
				List<String> tmpList = new ArrayList<>();
				for (Copy copy1 : pageList) {
					tmpList.add(CsvUtils.parseLine(copy1));
				}
				CsvUtils.exportBookReportCsv(bw,tmpList);
				if(pageList.size()<Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
					break;
				}
				pageList = null ;
				tmpList = null ;
				pageNo++;
			}
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	private static void resolveCatalogList(List<Copy> copyList){
		for (Copy copy1 : copyList) {
			if(StringUtils.isNotBlank(copy1.getIsStained()) && copy1.getIsStained().equals("1")) {
				copy1.setIsStained("污损");
			} else {
				copy1.setIsStained("-");
			}
			copy1.setStatus(EnumLibStoreStatus.parse(copy1.getStatus()).getName());
		}
	}

	/**
	 * 新书通报查询
	 * @param copy copy
	 * @param request request
	 * @param response response
	 * @return HttpStatus
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.newbookReportList.get"})
	@RequestMapping(value = "/newbookReportList",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> newbookReportList(Copy copy, HttpServletRequest request, HttpServletResponse response) {
		Page<Copy> page = copyService.newbookReportList(new Page<>(request, response), copy);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	/**
	 * 丢失，剔旧，报废，污损清单查询
	 * @param copy copy
	 * @param request 请求
	 * @param response 响应
	 * @return HttpStatus
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.findScrapList.get"})
	@RequestMapping(value = "/findScrapList",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> findScrapList(Copy copy, HttpServletRequest request, HttpServletResponse response) {
		Page<Copy> page = copyService.findScrapList(new Page<>(request, response), copy);
		if (page!=null){
			return new ResponseEntity<>(success(page), HttpStatus.OK);
		}
		return new ResponseEntity<>(fail("获取失败"),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 污损清单导出
	 * @param copy copy
	 * @param response 响应
	 * @throws IOException 异常
	 */
	//@Secured({"ROLE_lib3school.api.catalog.copy.exportStainsListReports.get"})
	@RequestMapping(value = "/exportStainsListReports",method = RequestMethod.GET)
	public void exportStainsListReports(Copy copy, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)) {
			Page<Copy> page = new Page<>();
			page.setOrderBy(orderBy);
			copy.setPage(page);
		}
		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;
		CommonUtils.setCvsConfig(response,"污损清单");
		List<Copy> detailList = copyService.exportStainsListReports(copy);
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("条形码,索取号,ISBN,题名,丛书名,著者,出版社,出版日期,价格,馆藏地,馆藏状态,入库日期,污损日期");
			for (Copy copy1 : detailList) {
				resultList.add(ExportCsv.parseStainsCopyLine(copy1));
			}
			CsvUtils.exportBookReportCsv(bw,resultList);
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	/**
	 * 报废清单导出
	 * @param copy copy
	 * @param response 响应
	 * @throws IOException 异常
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.exportScrapListReports.get"})
	@RequestMapping(value = "/exportScrapListReports",method = RequestMethod.GET)
	public void exportScrapListReports(Copy copy, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)) {
			Page<Copy> page = new Page<>();
			page.setOrderBy(orderBy);
			copy.setPage(page);
		}
		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;
		CommonUtils.setCvsConfig(response,"报废清单");
		List<Copy> detailList = copyService.exportScrapListReports(copy);
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("ISBN,索取号,条形码,分类号,题名,著者,出版社,出版日期,价格,入库日期,报废日期,馆藏地,馆藏状态");
			for (Copy copy1 : detailList) {
				resultList.add(ExportCsv.parseCommonCopyLine(copy1,4));
			}
			CsvUtils.exportBookReportCsv(bw,resultList);
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	/**
	 *丢失清单导出
	 * @param copy copy
	 * @param response 响应
	 * @throws IOException 异常
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.exportLoseListReports.get"})
	@RequestMapping(value = "/exportLoseListReports",method = RequestMethod.GET)
	public void exportLoseListReports(Copy copy, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)) {
			Page<Copy> page = new Page<>();
			page.setOrderBy(orderBy);
			copy.setPage(page);
		}
		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;
		CommonUtils.setCvsConfig(response,"丢失清单");
		List<Copy> detailList = copyService.exportLoseListReports(copy);
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("ISBN,索取号,条形码,分类号,题名,著者,出版社,出版日期,价格,入库日期,丢失日期,馆藏地,馆藏状态");
			for (Copy copy1 : detailList) {
				resultList.add(ExportCsv.parseCommonCopyLine(copy1,2));
			}
			CsvUtils.exportBookReportCsv(bw,resultList);
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	/**
	 *剔旧清单导出
	 * @param copy 复本实体
	 * @param response 响应
	 * @throws IOException 异常
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.exportWeedingListReports.get"})
	@RequestMapping(value = "/exportWeedingListReports",method = RequestMethod.GET)
	public void exportWeedingListReports(Copy copy, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)) {
			Page<Copy> page = new Page<>();
			page.setOrderBy(orderBy);
			copy.setPage(page);
		}
		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;
		CommonUtils.setCvsConfig(response,"剔旧清单");
		List<Copy> detailList = copyService.exportWeedingListReports(copy);
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("ISBN,索取号,条形码,分类号,题名,著者,出版社,出版日期,价格,入库日期,剔旧日期,馆藏地,馆藏状态");
			for (Copy copy1 : detailList) {
				resultList.add(ExportCsv.parseCommonCopyLine(copy1,3));
			}
			CsvUtils.exportBookReportCsv(bw,resultList);
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	/**
	 * 删除
	 * @param id ID
	 * @return HttpStatus
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.delete"})
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseData> delete(@PathVariable String id) {
		int count  = copyService.checkBillByCopy(id);
		if (count >0){
			return new ResponseEntity<>(fail("此图书存在流通记录，禁止删除!"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		int i = copyService.deletecopy(id);
		if(i==0){
			return new ResponseEntity<>(fail("删除失败"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(success("删除成功"),HttpStatus.OK);
	}

	/**
	 * 查找书目ID的单条复本
	 * @param lc lc
	 * @return HttpStatus
	 */
	@RequestMapping(value = "{id}",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> findLibraryCopyList(LibraryCopy lc){
		List<LibraryCopy> list = copyService.findLibraryCopyList(lc);
		return new ResponseEntity<>(success(list),HttpStatus.OK);
	}

	/**
	 * 添加单条复本信息(代码整合)
	 * @param lc LibraryCopy
	 * @return HttpStatus
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.post"})
	@RequestMapping(value = "",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> saveLibraryCopy(LibraryCopy lc){
		//lc.setOrgId("");//设置机构id
		Map<String,Object> result = new HashMap<>();
		try {
			result = copyService.InsertCopy(lc);
		} catch (Exception e) {
			logger.error("操作失败",e);
			result.put("fail", "保存失败!" + e);
		}
		return new ResponseEntity<>(success(result),HttpStatus.OK);
	}

	/**
	 * 获取所有批次
	 * @return HttpStatus
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.getAllBatchNo.get"})
	@RequestMapping(value = "/getAllBatchNo",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> getAllBatchNo(String type){
		String orgId = UserUtils.getOrgId();
		List<Batch> list = copyService.getAllBatchNo(type, orgId);
		if(list!=null){
			return new ResponseEntity<>(success(list),HttpStatus.OK);
		}
		return new ResponseEntity<>(fail("获取失败"),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 获取所有馆藏地
	 * @return list 馆藏地
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.getAllSiteName.get"})
	@RequestMapping(value = "/getAllSiteName",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> findSiteByCopy(Map<String,String> sdf){
		List<CollectionSite> list = copyService.getAllSiteName(sdf);
		if(list!=null){
			return new ResponseEntity<>(success(list),HttpStatus.OK);
		}
		return new ResponseEntity<>(fail("获取失败"),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 书标打印模板导入接口API
	 * @param multipartRequest multipartRequest
	 * @return HttpStatus
	 */
	@RequestMapping(value = "upBookLabel",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> upBookLabel(MultipartHttpServletRequest multipartRequest){
		Map serviceMap = null;
		for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
			MultipartFile multiFile = multipartRequest.getFile(it.next());
			serviceMap = copyService.upBookLabelFile(multiFile);
		}
		if (serviceMap!=null){
			if (serviceMap.get("result").equals("fail")){
				return new ResponseEntity<>(fail((String)serviceMap.get("message")), HttpStatus.BAD_REQUEST);
			}else{
				return new ResponseEntity<>(success("书标打印模板导入成功！"), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(success("操作完成"), HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.copy.exportBookLabelExcel.get"})
	@RequestMapping(value = "/exportBookLabelExcel",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> exportBookLabelExcel(Copy copy, HttpServletRequest request, HttpServletResponse response) throws Exception{
		copyService.exportBookLabelExcel(copy,request,response);
		return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.copy.exportBookLabelTxt.get"})
	@RequestMapping(value = "/exportBookLabelTxt",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> exportBookmarkTxt(Copy copy, HttpServletRequest request, HttpServletResponse response) throws Exception{
		copyService.exportBookmarkTxt(copy, request, response);
		return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.copy.bookLabel.post"})
	@RequestMapping(value = "/bookLabel",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> findAllList(Copy copy,HttpServletRequest request, HttpServletResponse response) {
		Page<Copy> page = copyService.findAllList(new Page<>(request, response), copy);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}


	@RequestMapping(value = "/deleteFile/{fileName}",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> deleteFile(@PathVariable("fileName") String fileName){
		Map<String,Object> map = copyService.deleteFile(fileName);
		Set<String> set = map.keySet();
		for(String  str : set){
			if(str.equals("success")){
				return new ResponseEntity<>(success(map),HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 批添加
	 * @param map map
	 * @return code
	 */
	@Secured({"ROLE_lib3school.api.catalog.copy.addCopyAll.post"})
	@RequestMapping(value = "/addCopyAll", method = RequestMethod.POST,consumes = "application/json",produces="application/json;charset=UTF-8")
	public ResponseEntity<ResponseData> addCopyAll(@RequestBody Map map) {
		Map<String, Object> result;
		try {
			result = copyService.addCopyAll(map);
			return new ResponseEntity<>(success(result), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 库室调配list接口
	 * @param copy copy
	 * @param request request
	 * @param response response
	 * @return list
	 */
	@RequestMapping(value = "collectionSiteList",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> collectionSiteList(Copy copy ,HttpServletRequest request, HttpServletResponse response){
		copy.setType("'0','1','2','5'");
		Page<Copy> page = copyService.findPage(new Page<>(request, response), copy);
		resolveCatalogList(page.getList());
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	/**
	 * 库室调配导出
	 * @param copy copy
	 * @param response response
	 * @throws IOException 异常
	 */
	@RequestMapping(value = "/collections/reports",method = RequestMethod.GET)
	public void collectionsReports(Copy copy, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)) {
			Page<Copy> page = new Page<>();
			page.setOrderBy(orderBy);
			copy.setPage(page);
		}
		if(StringUtils.isEmpty(copy.getType())){
			copy.setType("'0','1','2','5'");
		}
		copy.getSqlMap().put("dsf",UserUtils.dataScopeFilter("a"));

		String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" +UUID.randomUUID().toString().replaceAll("-", "")+".csv";
		String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = uploadPath + File.separator + uploadFileName;

		setCvsConfig(response,"库室调配");
		int pageNo = 1;
		copy.setPageSize(Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE)));
		List<String> resultList = new ArrayList<>();
		try(
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
				InputStream in = new FileInputStream(path)
		) {
			resultList.add("馆藏地,条形码,ISBN,索取号,题名,著者,出版社,出版日期,价格,入库日期,馆藏状态");
			CsvUtils.exportBookReportCsv(bw,resultList);
			while (true) {
				copy.setPageNo(pageNo);
				copy.setOrgId(UserUtils.getOrgId());
				List<Copy> pageList = copyService.collectionBookReports(copy);
				resolveCatalogList(pageList);
				List<String> tmpList = new ArrayList<>();
				for (Copy copy1 : pageList) {
					tmpList.add(ExportCsv.parseCopyLine(copy1));
				}
				CsvUtils.exportBookReportCsv(bw,tmpList);
				if(pageList.size()<Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
					break;
				}
				pageList = null ;
				tmpList = null ;
				pageNo++;
			}
			CsvUtils.writeResponse(in,response);
		}
		response.flushBuffer();
	}

	/**
	 * 批量修改副本馆藏地
	 * @param lc lc
	 * @return code
	 */
	@RequestMapping(value = "updateCopyCollectionSite",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> updateCopyCollectionSite(Copy lc){
		Map<String,Object> result = new HashMap<>();
		if(lc.getSiteId()==null || "".equals(lc.getSiteId())){
			result.put("fail", "请选择馆藏地!" );
		} else {
			lc.setType("'0','1','2','5'");
			result = copyService.updateCopyCollectionSite(lc);

		}
		return new ResponseEntity<>(success(result),HttpStatus.OK);
	}

	/**
	 * 根据条码查询副本
	 * @param oldBarcode oldBarcode
	 * @return copy
	 */
	@RequestMapping(value = "/getCopy", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> getCopy(String oldBarcode) {
		try {
			List<LibraryCopy> list = copyService.getCopy(oldBarcode);
			if (list.size()==0){
				return new ResponseEntity<>(fail("该条码不存在，或该条码已下架"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(success(list), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 判断条码的可用性
	 * @param newBarcode newBarcode
	 * @return code
	 */
	@RequestMapping(value = "/filterBarcode", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> filterBarcode(String newBarcode) {
		try {
			String message = copyService.filterBarcode(newBarcode);
			if(StringUtils.isNotEmpty(message)){
				return new ResponseEntity<>(fail(message), HttpStatus.OK);
			}
			return new ResponseEntity<>(success(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 条码查缺
	 * @param libraryCopy libraryCopy
	 * @return list
	 */
	@RequestMapping(value = "/barcodeCheck", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> barcodeCheck(LibraryCopy libraryCopy) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = copyService.barcodeCheck(libraryCopy);
		} catch (Exception e) {
			logger.error("操作失败",e);
			result.put("fail", "操作失败");
		}
		return new ResponseEntity<>(success(result), HttpStatus.OK);
	}

	/**
	 * 条码置换(修改副本,条码置换添加数据)
	 * @param barcodeRecord barcodeRecord
	 * @return code
	 */
	@RequestMapping(value = "/updateBarcode", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> updateBarcode(BarcodeRecord barcodeRecord) {
		try {
			if (barcodeRecord.getStatus().equals("0")){
				String message = copyService.updateBarcode(barcodeRecord);
				return new ResponseEntity<>(success(message), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(fail("置换失败，请选择为在馆状态的条码进行置换"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 下架条码记录
	 * @param barcodeRecord barcodeRecord
	 * @param request request
	 * @param response response
	 * @return list
	 */
	@RequestMapping(value = "/findList",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> findList(BarcodeRecord barcodeRecord , HttpServletRequest request, HttpServletResponse response){
		Page<BarcodeRecord> page = copyService.findPage(new Page<>(request, response), barcodeRecord);

		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	/**
	 * 丢失返还
	 * @param cd cd
	 * @return code
	 */
	@RequestMapping(value = "/returnLossCopy",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> returnLossCopy(CirculateDTO cd){
		//lc.setOrgId("");//设置机构id
		Map<String,String> result = new HashMap<>();
		try {
			cd.setType("1");//还书
			Copy copy = new Copy();
			copy.setBarcode(cd.getBarcode());
			Map<String, Object> data = billService.findBookByBarCode(copy);
			if(null!=data){
				cd.setCard(data.get("readerCard").toString());
				result = service.createSingle(cd);//调用CREATE
			}else{
				result.put("fail", "该书未找到读者信息!");
			}

		} catch (Exception e) {
			logger.error("操作失败",e);
			result.put("fail", "返还失败!");
		}
		return new ResponseEntity<>(success(result),HttpStatus.OK);
	}

}