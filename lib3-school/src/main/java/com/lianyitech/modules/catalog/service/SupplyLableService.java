package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.jxls.WriteBookLabelExportExcel;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.dao.SupplyLableDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.SupplyLable;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.solr.client.solrj.impl.XMLResponseParser.log;

/**
 * Created by zcx on 2017/5/22.
 * 查缺打印service
 */
@Service
public class SupplyLableService extends CrudService<SupplyLableDao,SupplyLable> {
    @Autowired
    private SupplyLableDao supplyLableDao;
    @Autowired
    private CopyDao copyDao;
    @Autowired
    private BindingDao bindingDao;

    /**
     * （查缺打印）书目管理提取条码
     * @param page 分页
     * @param copy 传参
     * @return page
     */
    public Page<SupplyLable> listCopySupplyLable(Page<SupplyLable> page, Copy copy) throws Exception {
        SupplyLable supplyLable = new SupplyLable();
        supplyLable.setType("0");
        supplyLable.setOrgId(UserUtils.getOrgId());
        if (StringUtils.isNotEmpty(copy.getBarcode())){
            Copy copy2 = copyDao.getCopyByBarcode(copy);
            supplyLable.setBarcode(copy2.getBarcode());
            supplyLable.setLibrarsortCode(copy2.getLibrarsortCode());
            supplyLable.setTanejiNo(copy2.getTanejiNo());
            supplyLable.setAssNo(copy2.getAssNo());
            supplyLable.setBookNo(copy2.getBookNo());
            supplyLable.setTitleName(copy2.getTitle());
            supplyLable.setPutDate(copy2.getCreateDate());
            this.save(supplyLable);
        }
        supplyLable.setPage(page);
        page.setList(supplyLableDao.findList(supplyLable));
        return page;
    }

    //excel导出
    public void exportSupplyLableExcel(SupplyLable supplyLable, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<SupplyLable> page = new Page<>();
            page.setOrderBy(orderBy);
            supplyLable.setPage(page);
        }
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
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + date + ".xls");
            response.setBufferSize(2048);
            List<SupplyLable> list = supplyLableDao.findList(supplyLable);
            if (supplyLable.getType().equals("0")) {
                Copy copy = new Copy ();
                BeanUtils.copyProperties(supplyLable,copy);
                List<Copy> copyList = new ArrayList<>();
                for (SupplyLable lable : list) {
                    Copy copy1 = new Copy ();
                    BeanUtils.copyProperties(lable,copy1);
                    copy1.setLibrarsortCode(lable.getLibrarsortCode());
                    copy1.setAssNo(lable.getAssNo());
                    copy1.setTanejiNo(lable.getTanejiNo());
                    copy1.setBookNo(lable.getBookNo());
                    copy1.setExportType(lable.getExportType());
                    copyList.add(copy1);
                }
                WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
                writeBookLabelExportExcel.createSheet(copyList, filePathName, response.getOutputStream(),copy.getExportType());
            } else {
                Binding binding = new Binding();
                BeanUtils.copyProperties(supplyLable,binding);
                List<Binding> bindingList = new ArrayList<>();
                for (SupplyLable lable : list) {
                    Binding binding1 = new Binding();
                    BeanUtils.copyProperties(lable,binding1);
                    binding1.setLibrarsortCode(lable.getLibrarsortCode());
                    binding1.setAssNo(lable.getAssNo());
                    binding1.setBookTimeNo(lable.getBookTimeNo());
                    binding1.setExportType(lable.getExportType());
                    bindingList.add(binding1);
                }
                WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
                writeBookLabelExportExcel.createSheet1(bindingList, filePathName, response.getOutputStream(),binding.getExportType());
            }
        } catch (IOException e) {
            logger.error(" exportBookmarkExcel IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            logger.error(" exportBookmarkExcel Exception:" + e.getStackTrace());
            throw e;
        }
    }

    //txt导出
    public void exportSupplyLableTxt(SupplyLable supplyLable, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<SupplyLable> page = new Page<>();
            page.setOrderBy(orderBy);
            supplyLable.setPage(page);
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
        List<SupplyLable> list = supplyLableDao.findList(supplyLable);
        try {
           if (supplyLable.getType().equals("0")) {
                Copy copy = new Copy ();
                BeanUtils.copyProperties(supplyLable,copy);
                List<Copy> copyList = new ArrayList<>();
                for (SupplyLable lable : list) {
                    Copy copy1 = new Copy ();
                    BeanUtils.copyProperties(lable,copy1);
                    copy1.setLibrarsortCode(lable.getLibrarsortCode());
                    copy1.setAssNo(lable.getAssNo());
                    copy1.setTanejiNo(lable.getTanejiNo());
                    copy1.setBookNo(lable.getBookNo());
                    copyList.add(copy1);
                }
                WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
                writeBookLabelExportExcel.exportBookmarkList(copyList, response, copy.getExportType());
            } else {
                Binding binding = new Binding();
                BeanUtils.copyProperties(supplyLable,binding);
                List<Binding> bindingList = new ArrayList<>();
                for (SupplyLable lable : list) {
                    Binding binding1 = new Binding();
                    BeanUtils.copyProperties(lable,binding1);
                    binding1.setLibrarsortCode(lable.getLibrarsortCode());
                    binding1.setAssNo(lable.getAssNo());
                    binding1.setBookTimeNo(lable.getBookTimeNo());
                    bindingList.add(binding1);
                }
                WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
                writeBookLabelExportExcel.exportBookmarkList1(bindingList, response, binding.getExportType());
            }
        } catch (IOException e) {
            log.error(" exportBookmarkExcel IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            log.error(" exportBookmarkExcel Exception:" + e.getStackTrace());
            throw e;
        }
    }

    /**
     * （查缺打印）期刊管理提取条码
     * @param page 分页
     * @param binding 传参
     * @return page
     */
    public Page<SupplyLable> listBindingSupplyLable(Page<SupplyLable> page, Binding binding) throws Exception {
        SupplyLable supplyLable = new SupplyLable();
        supplyLable.setType("1");
        supplyLable.setOrgId(UserUtils.getOrgId());
        if (StringUtils.isNotEmpty(binding.getBarcode())){
            binding.setOrgId(UserUtils.getOrgId());
            Binding binding2 = bindingDao.getBinding(binding);
            supplyLable.setBarcode(binding2.getBarcode());
            supplyLable.setLibrarsortCode(binding2.getLibrarsortCode());
            supplyLable.setAssNo(binding2.getAssNo());
            supplyLable.setBookTimeNo(binding2.getBookTimeNo());
            supplyLable.setTitleName(binding2.getTitle());
            supplyLable.setPutDate(binding2.getCreateDate());
            this.save(supplyLable);
        }
        supplyLable.setPage(page);
        page.setList(supplyLableDao.findList(supplyLable));
        return page;
    }

    /**
     * 清空列表
     * @param supplyLable 传参实体
     * @throws Exception Exception
     */
    public void deleteAll(SupplyLable supplyLable) throws Exception {
        supplyLable.setUpdateDate(new Date());
        supplyLableDao.deleteAll(supplyLable);
    }

}
