package com.lianyitech.modules.catalog.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.jxls.WriteBookLabelExportExcel;
import com.lianyitech.common.utils.*;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.dao.BookDirectoryDao;
import com.lianyitech.modules.catalog.entity.*;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.dao.CopyDao;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 馆藏复本管理Service
 *
 * @author zengzy
 * @version 2016-08-26
 */
@Service
public class CopyService extends CrudService<CopyDao, Copy> {

    @Autowired
    private BookDirectoryService bdService;
    @Autowired
    private BookDirectoryDao bdDao;
    @Autowired
    private ImportTemplateService importTemplateService;
    @Autowired
    private CopyDao copyDao;
    @Autowired
    private BindingDao bindingDao;
    @Autowired
    private BillDao billDao;
    @Autowired
    private BarcodeRecordService barcodeRecordService;

    private List<String> cardList = new ArrayList<String>();

    public Copy get(String id) {
        return super.get(id);
    }

    public List<Copy> findList(Copy copy) {
        return super.findList(copy);
    }

    public Page<Copy> findPage(Page<Copy> page, Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, copy);
    }

    @Transactional(readOnly = true)
    public List<Copy> collectionBookReports(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.findListByPage(copy);
    }

    public Page<Copy> newbookReportList(Page<Copy> page, Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        copy.setPage(page);
        page.setList(dao.newbookReportList(copy));
        return page;
    }

    @Transactional
    public void save(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        super.save(copy);
    }

    @Transactional
    public void delete(Copy copy) {
        super.delete(copy);
    }

    /*
    根据实体类条件得到条数信息
     */
    public int getCount(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.getCount(copy);
    }

    //丢失，剔旧，报废清单查询
    public Page<Copy> findScrapList(Page<Copy> page, Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        copy.setPage(page);
        page.setList(dao.findScrapList(copy));
        return page;
    }

    public List<Copy> exportScrapListReports(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.findScrapList(copy);
    }

    public List<Copy> exportLoseListReports(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.findScrapList(copy);
    }

    public List<Copy> exportWeedingListReports(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.findScrapList(copy);
    }

    public List<Copy> exportStainsListReports(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        return dao.findScrapList(copy);
    }

    /**
     * 馆藏复本信息查询结果集
     */
    public List<LibraryCopy> findLibraryCopyList(LibraryCopy libraryCopy) {
        //这里只查询在馆、借出、预借、剔旧四种状态，过滤掉：丢失和报废
        libraryCopy.setStatus("'" + EnumLibStoreStatus.IN_LIB.getValue() + "','" + EnumLibStoreStatus.OUT_LIB.getValue() + "','" + EnumLibStoreStatus.OUT_OLD.getValue() + "','" + EnumLibStoreStatus.ORDER_BORROW.getValue() + "'");//改成枚举0、1、2、5
        libraryCopy.setOrgId(UserUtils.getOrgId());
        return dao.findLibraryCopyList(libraryCopy);
    }

    /**
     * 新增/修改馆藏复本
     *
     * @param lc 复本
     */
    private void saveLibraryCopy(LibraryCopy lc) {
        Copy copy = new Copy();
        if (lc.getId() != null && !lc.getId().equals("")) {
            copy = dao.get(lc.getId());
        }
        if (lc.getBookDirectoryId() != null && !lc.getBookDirectoryId().equals("")) {
            copy.setBookDirectoryId(lc.getBookDirectoryId());
        }
        if (lc.getBatchId() != null && !lc.getBatchId().equals("")) {
            copy.setBatchId(lc.getBatchId());
        }
        if (lc.getBatchNo() != null && !lc.getBatchNo().equals("")) {
            copy.setBatchNo(lc.getBatchNo());
        }
        if (lc.getBarcode() != null && !lc.getBarcode().equals("")) {
            copy.setBarcode(lc.getBarcode());
        }
        if (lc.getSiteId() != null && !lc.getSiteId().equals("")) {
            copy.setCollectionSiteId(lc.getSiteId());
        }
        if (lc.getAssNo() != null && !lc.getAssNo().equals("")) {
            copy.setAssNo(lc.getAssNo());
        }
        save(copy);
    }

	/*//通过馆藏地找馆藏ID
    private String getSiteId(LibraryCopy lc){
		CollectionSite cs = new CollectionSite();
		cs.setDelFlag("0");
		cs.setName(lc.getSiteName());
		CollectionSite collectionSite = dao.getSiteId(cs);
		return collectionSite!=null&&collectionSite.getId()!=null?collectionSite.getId():"";
	}*/

    /**
     * 获取所有批次号 下拉框
     */
    public List<Batch> getAllBatchNo(String type, String orgId) {
        return dao.getAllBatchNo(type, orgId);
    }

    /**
     * 获取所有馆藏地 下拉框
     */
    public List<CollectionSite> getAllSiteName(Map<String, String> sdf) {
        sdf.put("orgId", UserUtils.getOrgId());
        return dao.getAllSiteName(sdf);
    }

    //书标模板上传
    public Map upBookLabelFile(MultipartFile multiFile) {
        Map<String, Object> reMap = new HashMap<>();
        String fileName = null;
        String afterFileName = null;
        //String beforeFileName = null;
        String uploadFilePath = Global.getUploadRootPath() + Global.UPLOADFILES_BASE_URL;
        File file = null;
        List<BookDirectory> list = null, errorList = null;
        if (multiFile.getOriginalFilename().length() > 0) {
            fileName = multiFile.getOriginalFilename();

            //beforeFileName = fileName.substring(0, fileName.lastIndexOf("."));
            afterFileName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

            if (afterFileName.equalsIgnoreCase("xls") || afterFileName.equalsIgnoreCase("xlsx")) {
            } else {
                reMap.put("result", "fail");
                reMap.put("message", "导入的文件只支持.xls、.xlsx类型的格式文件!");
                return reMap;
            }

            if (multiFile.getSize() > 10485760) {
                reMap.put("result", "fail");
                reMap.put("message", "导入的打印模板excel数据不能超过10M!");
                return reMap;
            }
        }

        ImportTemplate importTemplate = new ImportTemplate();
        importTemplate.setFileName(fileName);
        importTemplate.setFilePath(uploadFilePath);
        importTemplate.setTmpType("0");

        if (importTemplateService.findSameFileNameList(importTemplate).size() > 0) {
            reMap.put("result", "fail");
            reMap.put("message", "导入的打印模板excel文件名有重复!");
            return reMap;
        }

        try {
            file = new File(uploadFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (FileUtils.writeFile(uploadFilePath, fileName, multiFile.getInputStream())) {
                if (importTemplateService.findDefaultTemplate() != null) {
                    importTemplate.setStatus("1");
                } else {
                    importTemplate.setStatus("0");
                }
                try {
                    importTemplateService.save(importTemplate);
                } catch (Exception e) {
                    logger.error("操作失败",e);
                    File deleteFile = new File(uploadFilePath + File.separator + fileName);
                    deleteFile.delete();
                    reMap.put("result", "fail");
                    reMap.put("message", "书标打印模板保存数据库失败!" + e.getMessage());
                    return reMap;
                }

                reMap.put("result", "success");
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
        }

        return reMap;
    }

    public String exportBookLabelExcel(Copy copy, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Copy> page = new Page<>();
            page.setOrderBy(orderBy);
            copy.setPage(page);
        }
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = t.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = null;
        try {
            codedFileName = new String("书标打印".getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error("exportLibraryCollectionExcel UnsupportedEncodingException:" + e.getStackTrace());
            throw e;
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + date + ".xls");
        response.setBufferSize(2048);
        try {
            String filePathName = WriteBookLabelExportExcel.class.getClassLoader().getResource("").getPath() + "jxls/bookLabelTemplate.xls";
            List<Copy> list = findAllList(copy);
            WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
            writeBookLabelExportExcel.createSheet(list, filePathName, response.getOutputStream(),copy.getExportType());
        } catch (IOException e) {
            logger.error(" exportBookmarkExcel IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            logger.error(" exportBookmarkExcel Exception:" + e.getStackTrace());
            throw e;
        }
        return null;
    }

    public String exportBookmarkTxt(Copy copy, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Copy> page = new Page<>();
            page.setOrderBy(orderBy);
            copy.setPage(page);
        }
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = t.format(new Date());
        String codedFileName = null;
        response.setContentType("text/plain");

        try {
            codedFileName = new String("书标打印".getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error("exportBarcodeExt UnsupportedEncodingException:" + e.getStackTrace());
            throw e;
        }

        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + date + ".txt");
        response.setBufferSize(2048);
        try {
            List<Copy> list = findAllList(copy);
            WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
            writeBookLabelExportExcel.exportBookmarkList(list, response, copy.getExportType());
        } catch (IOException e) {
            logger.error(" exportBarcodeExt IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            logger.error(" exportBarcodeExt Exception:" + e.getStackTrace());
            throw e;
        }

        return null;
    }

    public Page<Copy> findAllList(Page<Copy> page, Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        copy.setPage(page);
        page.setList(this.findAllList(copy));
        return page;
    }

    public List<Copy> findAllList(Copy copy) {
        String string = copy.getLibrarsortCode();
        if (string != null && !string.equals("") && string.contains(",")) {
            String[] strList = string.split(",");
            copy.setLibrarsortCode(null);
            List indexNumList = Arrays.asList(strList);
            copy.setLibSortCodeList(indexNumList);
        }
        copy.setOrgId(UserUtils.getOrgId());
        List<Copy> list = dao.findAllList(copy);
        return list;
    }

    public Map<String, Object> deleteFile(String fileName) {
        Map<String, Object> map = new HashMap<>();
        String path = CopyService.class.getClassLoader().getResource("").getPath() + "jxls/" + fileName + ".xls";
        File file = new File(path);
        System.out.println(file.getName());
        if (!file.exists()) {
            map.put("fail", "删除失败");
            return map;
        }
        System.gc();
        file.getAbsoluteFile().delete();
        if (!file.exists()) {
            map.put("success", "删除成功");
        }
        return map;
    }


    /**
     * 根据条形码获取复本对象
     *
     * @param copy 条形码
     * @return 复本对象
     */
    public Copy findByBarCode(Copy copy) {
        copy.setOrgId(UserUtils.getOrgId());
        Copy c = dao.findByBarCode(copy);
        if (c != null && (c.getStatus() == null || "".equals(c.getStatus()))) {
            c.setStatus("0");
        }
        return c;
    }

    /**
     * 根据流通类型修改复本状态
     *
     */
    public void updateByBillType(Map<String, Object> param) {
        if(null!=param.get("dirType") && "1".equals(param.get("dirType"))){
            //更新期刊复本状态
            bindingDao.updateByBillType(param);
        }else{
            //更新图书复本状态
            copyDao.updateByBillType(param);
        }
    }

    /**
     * 复本保存接口修改--将之前2个接口整合成一个
     *
     * @param libraryCopy 复本信息
     * @return map 返回结果
     */
    public Map<String, Object> InsertCopy(LibraryCopy libraryCopy) throws Exception {
        Map<String, Object> map = new HashMap<>();
        boolean isTrue = true;
        //先判断书目是不是存在
        libraryCopy.setOrgId(UserUtils.getOrgId());
//        libraryCopy.setAssNo(StringUtils.ToDBC(libraryCopy.getAssNo()));
        if(StringUtils.isNotBlank(libraryCopy.getBarcode())) {
            libraryCopy.setBarcode(StringUtils.ToDBC(libraryCopy.getBarcode()));
        }
        int i = dao.countBookDirectory(libraryCopy);
        if (i == 0){
            map.put("fail","该书目己被删除!");
            return map;
        }
        //添加的情况下如果没有条形码则不能添加
        if (StringUtils.isBlank(libraryCopy.getId()) && StringUtils.isBlank(libraryCopy.getBarcode())) {
            map.put("fail", "条形码不能为空!");
            return map;
        }
        //如果不包括条形码字段修改则无须验证唯一
        if(!StringUtils.isBlank(libraryCopy.getBarcode())) {
            isTrue = SystemUtils.isTrueCardBarcode(libraryCopy.getBarcode(), libraryCopy.getId());
        }
        if (!isTrue) {
            map.put("fail", "条形码重复或与读者证号重复或该条码已下架!");
            return map;
        }
        if(StringUtils.isNotBlank(libraryCopy.getAssNo())){
            libraryCopy.setAssNo(StringUtils.ToDBC(libraryCopy.getAssNo()));
        }
        saveBookByCopy(libraryCopy);//反过来修改对应书目--根据条件判断
        String statusAndStock = "";
        if(StringUtils.isNotBlank(libraryCopy.getId())) {
            statusAndStock = copyDao.findStatusAndStockById(libraryCopy);
        }
        if ("10".equals(statusAndStock)) {
            map.put("fail", "该图书已被借出，暂不能修改库存属性");
        } else {
            saveLibraryCopy(libraryCopy);//新增复本
            map.put("success", "保存数据成功");
        }
        return map;
    }

    //根据复本传过来的条件反过去修改书目
    private void saveBookByCopy(LibraryCopy libraryCopy) {
        //这里通过传了是否传了种次号等信息来区分是不是没有复本信息的时候添加--后面新增了一个著者号的情况也需要判断
        if (libraryCopy.getTanejiNo() != null || libraryCopy.getAssNo() != null || libraryCopy.getBookNo() !=null) {
            BookDirectory bookDirectory = new BookDirectory();
            bookDirectory.setOrgId(UserUtils.getOrgId());
            bookDirectory.setId(libraryCopy.getBookDirectoryId());
            BookDirectory bd = bdDao.getBookDirectory(bookDirectory);
            if (bd != null) {
                bd.setTanejiNo(libraryCopy.getTanejiNo());
                bd.setAssNo(libraryCopy.getAssNo());
                bd.setBookNo(libraryCopy.getBookNo());
                bdService.saveSimpleBook(bd);//根据书目--这里包含马克数据也会跟着修改
            }
        }
    }

    /**
     * 判断复本下是否有流通记录
     *
     * @param ids 复本ids
     * @return 流通记录数
     */
    public int checkBillByCopy(String ids) {
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String, Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("orgId", UserUtils.getOrgId());
        return copyDao.checkBillByCopy(map);
    }

    //复本删除--如果对应的书目没有任何复本的情况下将其种次号和辅助区分号清空
    @Transactional
    public int deletecopy(String ids) {
        String[] idarr = ids.split(",");
        Map<String,Object> map = new HashMap<>();
        String orgId = UserUtils.getOrgId();
        map.put("orgId",orgId);
        map.put("idList",Arrays.asList(idarr));
        List<BookDirectory> listbook = bdDao.getDirectoryByCopyIds(map);
        int dels = this.delete(ids);
        for(BookDirectory bookDirectory : listbook){
            Copy c = new Copy();
            c.setBookDirectoryId(bookDirectory.getId());
            c.setOrgId(orgId);
            int count = dao.getCount(c);
            if (count <= 0) {//将书目信息中种次号和辅助区分号清空
                bookDirectory.setTanejiNo("");//清空种次号
                bookDirectory.setAssNo("");//清空辅助区分号
                bookDirectory.setBookNo("");//著者号清空
                bookDirectory.setUpdateDate(new Date());
                bookDirectory.setOrgId(UserUtils.getOrgId());//设置机构id

                bdDao.update(bookDirectory);
            }
        }
        return dels;
    }

    private Map<String, Object> validateBarcode(LibraryCopy libraryCopy) {
        Map<String, Object> result = new HashMap<>();
        if (libraryCopy == null) {
            result.put("fail", "传值有误，对象为空！");
            return result;
        }
        String begin_barcode = libraryCopy.getBeginBarcode();//开始条形码
        String end_barcode = libraryCopy.getEndBarcode();//结束条形码
        //第一步：验证条形码不能为空
        if (!(libraryCopy.getBeginBarcode() != null && !"".equals(libraryCopy.getBeginBarcode()) && end_barcode != null && !"".equals(end_barcode))) {
            result.put("fail", "初始条形码不能为空！");
            return result;
        }
        //第二步：验证是否是数字型--虽然这里前端已经验证过来
        BigDecimal begin_code;
        BigDecimal end_code;
        try {
            begin_code = new BigDecimal(begin_barcode);
            end_code = new BigDecimal(end_barcode);
        } catch (Exception e) {
            result.put("fail", "批量添加馆藏只支持数字！");
            return result;
        }
        //第三步：验证位数是否相等
        if (begin_barcode.length() != end_barcode.length()) {
            result.put("fail", "批量添加的时候保证起始条形码位数一致！");
            return result;
        }
        int subtract = end_code.compareTo(begin_code);
        //第四步：验证结束开始条形码不能大于等于结束条形码
        if (subtract <= 0) {
            result.put("fail", "初始条形码不能大于等于截止初始条形码!");
            return result;
        }

        //第五步：验证值跨度范围 是否超过100
        if (end_code.subtract(begin_code).compareTo(new BigDecimal("100"))>0) {
            result.put("fail", "截止条形码与初始条形码之间的值跨度范围不能超过100!");
            return result;
        }
        int len = begin_barcode.length();
        //第六步:验证长度不超过30(虽然前端可以限制-这里还是写下)
        if (len > 30) {
            result.put("fail", "条形码长度不要超过30位！");
            return result;
        }
        //将需要的值赋值过去
        result.put("begin_code", begin_code);
        result.put("end_code", end_code);
        result.put("len", len);
        return result;
    }

    /**
     * 批量添加馆藏 2016-10-21对馆藏批量添加修改
     * @param map  map
     * @return code
     * @throws Exception Exception
     */
    @Transactional
    public Map<String, Object> addCopyAll(Map map) throws Exception{
        Map<String, Object> result = new HashMap<>();
        boolean if_insert_copy = false;//是否插入馆藏复本--如果存在插入了则下面要返回去修改书目
        List<Map> list = (List<Map>)map.get("list");
        for (Map map1 : list) {
            LibraryCopy libraryCopy = new LibraryCopy();
            SystemUtils.transMapToBean(map1,libraryCopy);
            if(!SystemUtils.isTrueCardBarcode(libraryCopy.getBarcode(),null)){
                throw new RuntimeException("条形码己被占用或已下架,批量添加失败！");
            }
            Copy copy = new Copy();
            copy.setBarcode(libraryCopy.getBarcode());
            copy.setBookDirectoryId(map.get("bookDirectoryId").toString());
            copy.setBatchId(libraryCopy.getBatchId());
            copy.setCollectionSiteId(libraryCopy.getCollectionSiteId());
            copy.setAssNo(libraryCopy.getAssNo());
            save(copy);
            if_insert_copy = true;
        }
        if (if_insert_copy) {
            LibraryCopy libraryCopy1 = new LibraryCopy();
            libraryCopy1.setLibrarsortCode(map.get("librarsortCode").toString());
            libraryCopy1.setBookDirectoryId(map.get("bookDirectoryId").toString());
            if(map.get("tanejiNo")!=null && StringUtils.isNotBlank(map.get("tanejiNo").toString())) {
                libraryCopy1.setTanejiNo(map.get("tanejiNo").toString());
                libraryCopy1.setBookNo(null);
            } else if(map.get("bookNo")!=null && StringUtils.isNotBlank(map.get("bookNo").toString())) {
                libraryCopy1.setBookNo(map.get("bookNo").toString());
                libraryCopy1.setTanejiNo(null);
            }
            saveBookByCopy(libraryCopy1);//反过来修改书目信息根据条件
        }
        result.put("success", "批量添加成功!");
        return result;
    }

    /**
     * 批量修改馆藏的地点
     *
     * @param lc lc
     */
    @Transactional
    public Map<String, Object> updateCopyCollectionSite(Copy lc) {
        lc.setOrgId(UserUtils.getOrgId());
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotEmpty(lc.getId())) {
            String[] ids = lc.getId().split(",");
            lc.setIdList(ids);
        }
        //传了id的情况下则是根据id修改馆藏地
        lc.setUpdateDate(new Date());
        if (lc.getIdList() != null && lc.getIdList().length > 0) {
            dao.updateCopySiteByIds(lc);
        } else {//没有传id的情况下则是进行一键全选修改-但是必须传checkAll
            if (lc.getCheckAll() == null || "".equals(lc.getCheckAll())) {
                result.put("fail", "修改失败，请正确传参!");
                return result;
            }
            //一键修改馆藏地
            dao.updateCopySiteByCon(lc);
        }
        return result;
    }



    /**
     * 根据条码查出当条复本信息
     * @param oldBarcode oldBarcode
     * @return list
     */
    public List<LibraryCopy> getCopy(String oldBarcode) {
        String orgId = UserUtils.getOrgId();
        List<LibraryCopy> list = copyDao.getCopy(oldBarcode, orgId);
        return list;
    }

    /**
     * 判断手动输入的条形码是否已下架或者是否已存在或与读者证重复（完成）,未作机构过滤
     * @param newBarcode newBarcode
     * @return 返回状态
     * @throws Exception 异常
     */
    public String filterBarcode(String newBarcode) throws Exception {
        String message = "";
        LibraryCopy libraryCopy = new LibraryCopy();
        libraryCopy.setBarcode(newBarcode);
        if(!SystemUtils.isTrueCardBarcode(newBarcode,null)){
            message = "此条形码己被占用或已下架!";
        }
        return message;
    }

    private Map<String, Object> check(LibraryCopy libraryCopy) {
        Map<String, Object> result = new HashMap<>();
        if (libraryCopy == null) {
            result.put("fail", "传值有误，对象为空！");
            return result;
        }
        String begin_barcode = libraryCopy.getBeginBarcode();//开始条形码
        String end_barcode = libraryCopy.getEndBarcode();//结束条形码
        //第一步：验证条形码不能为空
        if (!(libraryCopy.getBeginBarcode() != null && !"".equals(libraryCopy.getBeginBarcode()) && end_barcode != null && !"".equals(end_barcode))) {
            result.put("fail", "初始条形码不能为空！");
            return result;
        }
        //第二步：验证是否是数字型--虽然这里前端已经验证过来
        BigDecimal begin_code;
        BigDecimal end_code;
        try {
            begin_code = new BigDecimal(begin_barcode);
            end_code = new BigDecimal(end_barcode);
        } catch (Exception e) {
            result.put("fail", "条码查缺的范围只支持纯数字！");
            return result;
        }
        //第三步：验证位数是否相等
        if (begin_barcode.length() != end_barcode.length()) {
            result.put("fail", "条码查缺的时候保证起始条形码位数一致！");
            return result;
        }
        int subtract = end_code.compareTo(begin_code);
        //第四步：验证结束开始条形码不能大于等于结束条形码
        if (subtract <= 0) {
            result.put("fail", "初始条形码不能大于等于截止初始条形码!");
            return result;
        }
        int len = begin_barcode.length();
        //第六步:验证长度不超过30(虽然前端可以限制-这里还是写下)
        if (len > 30) {
            result.put("fail", "条形码长度不要超过30位！");
            return result;
        }
        //将需要的值赋值过去
        result.put("begin_code", begin_code);
        result.put("end_code", end_code);
        result.put("len", len);
        return result;
    }

    /**
     * 条形码查缺（需做相关的逻辑判断）
     * @param libraryCopy libraryCopy
     * @return data
     * @throws Exception  异常
     */
    public Map<String, Object> barcodeCheck(LibraryCopy libraryCopy) throws Exception {
        List<Object> list = new ArrayList<>();
        Map<String, Object> result;
        libraryCopy.setOrgId(UserUtils.getOrgId());
        result = check(libraryCopy);
        if (result.containsKey("fail")) {
            return result;
        }
        BigDecimal begin_code = (BigDecimal) result.get("begin_code");
        BigDecimal end_code = (BigDecimal) result.get("end_code");
        int len = (int) result.get("len");
        List<String> barcodeList = copyDao.checkBarcode(libraryCopy);
        while (begin_code.compareTo(end_code) <= 0) {//循环条形码
            //不足长度左边补充0
            String barcode = SystemUtils.leftPaddingZero(begin_code.toEngineeringString(),len);
            if (!barcodeList.contains(barcode)) {
                list.add(barcode);
            }
            if (list.size() >= 10) {
                break;
            }
            begin_code = begin_code.add(new BigDecimal("1"));
        }
        result.put("list", list);
        return result;
    }

    /**
     * 条码置换
     * @param barcodeRecord barcodeRecord
     * @return code
     * @throws Exception  异常
     */
    public String updateBarcode(BarcodeRecord barcodeRecord) throws Exception {
        String message;
        barcodeRecord.setOrgId(UserUtils.getOrgId());
        LibraryCopy libraryCopy = new LibraryCopy();
        libraryCopy.setOldBarcode(barcodeRecord.getOldBarcode());
        libraryCopy.setNewBarcode(barcodeRecord.getNewBarcode());
        libraryCopy.setUpdateBy(UserUtils.getLoginName());
        libraryCopy.setUpdateDate(new Date());
        libraryCopy.setOrgId(barcodeRecord.getOrgId());
        //需做机构限制
        if(!SystemUtils.isTrueCardBarcode(barcodeRecord.getNewBarcode(),barcodeRecord.getId())){
            message = "此条形码己被占用或已下架!";
            return message;
        } else if (copyDao.updateBarcode(libraryCopy) > 0) {
            barcodeRecordService.save(barcodeRecord);
            message = "置换成功";
            return message;
        } else {
            message = "置换失败";
            return message;
        }
    }

    /**
     * 下架条码记录
     * @param page  page
     * @param barcodeRecord  barcodeRecord
     * @return list
     */
    public Page<BarcodeRecord> findPage(Page<BarcodeRecord> page, BarcodeRecord barcodeRecord) {
        barcodeRecord.setOrgId(UserUtils.getOrgId());
        return barcodeRecordService.findPage(page, barcodeRecord);
    }

    @Transactional
    public Map<String, Object> returnLossCopy(Copy lc)
    {
        Bill bill = new Bill();
        bill.setStatus("24");
        lc.setOrgId(UserUtils.getOrgId());
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotEmpty(lc.getId())) {
            String[] idarr = lc.getId().split(",");
            for (int i = 0; i < idarr.length; i++) {
                lc.setId(idarr[i]);
                lc.setUpdateDate(new Date());
                dao.returnLossCopy(lc);
                bill.setCopyId(idarr[i]);
                bill.setOrgId(UserUtils.getOrgId());
                bill.setUpdateDate(new Date());
                billDao.delete(bill);
            }
        }
        return result;
    }


}