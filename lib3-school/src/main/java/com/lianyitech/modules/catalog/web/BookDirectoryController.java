package com.lianyitech.modules.catalog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianyitech.core.jxls.WriteToOut;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.catalog.service.CopyService;
import com.lianyitech.modules.catalog.utils.SolrPage;
import com.lianyitech.modules.common.BookDirectoryXmlParser;
import com.lianyitech.modules.sys.utils.UserUtils;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * 书目管理Controller
 * @author zengzy
 * @version 2016-08-26
 */
@RestController
@RequestMapping(value = "/api/catalog/bookdirectory")
public class BookDirectoryController extends ApiController {

	@Autowired
	private BookDirectoryService bookDirectoryService;
	@Autowired
	private CopyService copyService;

	/**
	 **书目查询，通过solr客户端查询行管书目信息
	 *zym
    */
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.listsolr.get"})
	@RequestMapping(value = {"listsolr"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> listsolr(BookDirectory bookDirectory, HttpServletRequest request, HttpServletResponse response) {
		SolrPage page = bookDirectoryService.findSolrBookPage(new SolrPage(request),new Page<>(request, response), bookDirectory);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> get(@PathVariable String id) {
		BookDirectory bookDirectory = bookDirectoryService.getBookDirectory(id);//这个查询复本
		if (null == bookDirectory) {
			return new ResponseEntity<>(fail("数据不存在"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(success(bookDirectory), HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.copy.get"})
	@RequestMapping(value = "/copy/{id}/{isbn}", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> copy(@PathVariable String id,@PathVariable String isbn) {//复制行馆接口
		if ((StringUtils.isBlank(id) || "null".equals(id)) && StringUtils.isNotBlank(isbn)) {
			try{
				BookDirectory book = BookDirectoryXmlParser.parse_book(isbn);
				if (book != null) {
					Map<String, String> map_book = BeanUtils.describe(book);
					map_book.put("_version_", "douban");
				}
				return new ResponseEntity<>(success(book), HttpStatus.OK);
			}catch (Exception e) {
				logger.error("操作失败",e);
				return new ResponseEntity<>(fail("获取数据失败"), HttpStatus.NOT_FOUND);
			}
		}

		HashMap<String,Object> result = bookDirectoryService.copy(id);
		if ((result==null ||(result.containsKey("code")&&"-1".equals(result.get("code").toString())))) {
			return new ResponseEntity<>(fail("数据不存在"), HttpStatus.NOT_FOUND);
		}
		Object data = new Object();
		if(result.get("data")!=null){//接口调整之后对应返回的结果也要处理
			data = result.get("data");
		}
		return new ResponseEntity<>(success(data), HttpStatus.OK);
	}

	/**保存简单编目--
	  要将对应的马克数据信息也要对应保存
	  要获取已存在马克数据和模板数据的并集，并且重新将简单编目的属性信息赋值过来）
	**/
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.post"})
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> saveSimpleBook(BookDirectory bookDirectory) {
		// 保存之前要对书目信息进行判断，判断是否馆藏书目库是否存在相同的书目信息
		try {
			bookDirectory.setOrgId(UserUtils.getOrgId());//设置机构id
			int count = bookDirectoryService.getCountByCon(bookDirectory);
			if (count > 0) {
				return new ResponseEntity<>(fail("此条书目已重复"), HttpStatus.BAD_REQUEST);
			}
			Map<String,Object> map = bookDirectoryService.saveSimpleBook(bookDirectory);
			map.put("id",bookDirectory.getId());
			return new ResponseEntity<>(success(map), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//保存马克数据--这里要将对应的简单编目信息也要保存
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.bookMarc.post"})
	@RequestMapping(value = "/bookMarc", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> bookMarc(BookDirectory bookDirectory,HttpServletRequest request) {
		try {
			String returnstr = bookDirectoryService.saveBookMarc(bookDirectory,request);
			if (returnstr != null) {
				return new ResponseEntity<>(fail(returnstr), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(success((Object) bookDirectory.getId()), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.delete"})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
		try {
			BookDirectory bookDirectory = null;
			if (StringUtils.isNotBlank(id)) {
				BookDirectory bd = new BookDirectory();
				bd.setOrgId(UserUtils.getOrgId());
				bd.setId(id);
				bookDirectory = bookDirectoryService.getBookDirectory(bd);
			}else{
				return new ResponseEntity<>(fail("ID不能为空，删除失败！"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//2016-11-30 产品提出删除书目的情况下,判断是否存在复本信息,如果存在不予删除
			Copy copy = new Copy();
			copy.setBookDirectoryId(id);
			int count = copyService.getCount(copy);
			if (count > 0) {
				return new ResponseEntity<>(fail("该条目录存在复本信息，删除失败"), HttpStatus.BAD_REQUEST);
			}
			bookDirectory.setOrgId(UserUtils.getOrgId());
			bookDirectoryService.delete(bookDirectory);//这里删除单个删除暂时不改
			return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
		}catch (Exception e){
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * 旧书导入
	 * @param multipartRequest 上传文件
	 * @return 返回
	 */
	@RequestMapping(value = "/importOldBookDir", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> importOldBookDir(MultipartHttpServletRequest multipartRequest) {
		// 支持多文件上传
		return commonImport(multipartRequest,3);
	}

	/**
	 * 非旧书导入
	 * @param multipartRequest 上传文件
	 * @return 返回
	 */
	@RequestMapping(value = "/importBookDir", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> importBookDir(MultipartHttpServletRequest multipartRequest) {
		// 支持多文件上传
		return commonImport(multipartRequest,1);
	}

	private ResponseEntity<ResponseData> commonImport(MultipartHttpServletRequest multipartRequest,int type) throws RuntimeException {
			for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext(); ) {
				MultipartFile multiFile = multipartRequest.getFile(it.next());
				try {
					bookDirectoryService.upBookDirFile(multiFile,type);
				}  catch (Exception e) {
					return new ResponseEntity<>(fail("操作失败"), HttpStatus.OK);
				}
			}

		return new ResponseEntity<>(success("操作完成"), HttpStatus.OK);
	}

	@RequestMapping(value = {"/downloadExcle"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> downloadExcle(HttpServletRequest request,  HttpServletResponse response) {
		String fileName = request.getParameter("errorFileName");
		try {
			if (bookDirectoryService.downloadExcle(fileName, response)) {
				return new ResponseEntity<>(fail("操作错误！"), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("操作异常！"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(success("操作完成！"), HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.downloadBookDirTemplate.get"})
	@RequestMapping(value = {"/downloadOldBookDirTemplate"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> downloadOldBookDirTemplate(HttpServletResponse response) {
		String type = "3";
		return commonDownBookDir(response,type);
	}
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.downloadBookDirTemplate.get"})
	@RequestMapping(value = {"/downloadBookDirTemplate"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> downloadBookDirTemplate(HttpServletResponse response) {
		String type = "1";
		return commonDownBookDir(response,type);
	}
	private ResponseEntity<ResponseData> commonDownBookDir(HttpServletResponse response,String type){
		String fileName = "馆藏导入模板.xls";
		String path = "template/BookDirTemplate.xls";
		if (type != null && (type == "3" || type.equals("3"))) {//回溯建库
			fileName = "回溯建库导入模板.xls";
			path = "template/OldBookDirTemplate.xls";
		}
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(path);
			if (WriteToOut.writeToResponse(fileName, in, response)){
				return new ResponseEntity<>(fail("操作错误！"), HttpStatus.BAD_REQUEST);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("操作失败",e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return new ResponseEntity<>(success("操作完成！"), HttpStatus.OK);
	}

	//生成种次号
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.createTanejiNo.get"})
	@RequestMapping(value = {"/createTanejiNo"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> createTanejiNo(BookDirectory bookDirectory) {
		int tanejino =  bookDirectoryService.createTaneJINo(bookDirectory);
		return new ResponseEntity<>(success(tanejino), HttpStatus.OK);
	}

	//生成种次号
	@RequestMapping(value = {"/checkTanejiNo"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> checkTanejiNo(BookDirectory bookDirectory) {
		//bookDirectory.setOrgId("1");//暂时写死
		List<String> list = bookDirectoryService.checkTaneJINo(bookDirectory);
		return new ResponseEntity<>(success(list), HttpStatus.OK);
	}
	//种次号唯一判断 （分类号、种次号  再传一个书目id来区分下除了当条其他是否占用）
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.uniqueTanejiNo.get"})
	@RequestMapping(value = {"/uniqueTanejiNo"},method = RequestMethod.GET)
	public ResponseEntity<ResponseData> uniqueTanejiNo(BookDirectory bookDirectory) {
		//bookDirectory.setOrgId("1");//暂时写死
		Map<String,String> result = bookDirectoryService.uniqueTanejiNo(bookDirectory);
		return new ResponseEntity<>(success(result), HttpStatus.OK);
	}

	//得到马克数据--马克编辑页面查看信息
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.marcinfo.get"})
	@RequestMapping(value = {"/marcinfo"}, method = RequestMethod.GET)
	public ResponseEntity<ResponseData> marcInfo(BookDirectory bookDirectory) {
		Object o = bookDirectoryService.getMarcInfo(bookDirectory);
		return new ResponseEntity<>(success(o), HttpStatus.OK);
	}

	//马克主字段信息验证
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.checkMarc.post"})
	@RequestMapping(value = {"/checkMarc"},consumes = "application/json",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> checkMarc(@RequestBody List<Map> list) {
		Map<String,Object> result = bookDirectoryService.checkMarcMap(list);
		return new ResponseEntity<>(success(result), HttpStatus.OK);
	}

	//写个方法用来验证马克编辑里面的分类号是否
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.checkSortCodeMarc.post"})
	@RequestMapping(value = {"/checkSortCodeMarc"},consumes = "application/json",method = RequestMethod.POST)
	public ResponseEntity<ResponseData> checkSortCodeMarc(@RequestBody Map map) {
		Map<String,String> result = bookDirectoryService.checkSortCodeMarc(map);
		return new ResponseEntity<>(success(result), HttpStatus.OK);
	}

	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.mainMarcinfo.get"})
	@RequestMapping(value = "/mainMarcinfo",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> listMainMarcinfo(String tag) {
		List<Map> list = bookDirectoryService.getMarcinfo(tag);
		return new ResponseEntity<>(success(list), HttpStatus.OK);
	}

	/**
	 * 简单书目验证
     */
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.checkBook.post"})
	@RequestMapping(value = {"/checkBook"},method = RequestMethod.POST)
	public ResponseEntity<ResponseData> checkBook(BookDirectory bookDirectory) {
		Map<String,String> checkmap = bookDirectoryService.checkBookProperty(bookDirectory);
		return new ResponseEntity<>(success(checkmap), HttpStatus.OK);
	}
}