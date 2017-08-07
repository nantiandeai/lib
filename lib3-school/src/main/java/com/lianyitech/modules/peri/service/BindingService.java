package com.lianyitech.modules.peri.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.jxls.WriteBookLabelExportExcel;
import com.lianyitech.modules.catalog.entity.SupplyLable;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.solr.client.solrj.impl.XMLResponseParser.log;

/**
 * Created by zcx on 2017/3/14.
 * BindingService
 */
@Service
public class BindingService extends CrudService<BindingDao,Binding> {
    @Autowired
    private BindingDao bindingDao;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private BindingDetailService bindingDetailService;

    public Page<Binding> findPage (Page<Binding> page ,Binding binding){
        binding.setOrgId(UserUtils.getOrgId());
        return super.findPage(page,binding);
    }

    /**
     * 导出过刊清单  不分页
     * @param binding binding
     * @return list
     */
    public List<Binding> collectionPeriReports(Binding binding) {
        binding.setOrgId(UserUtils.getOrgId());
        return dao.findList(binding);
    }

    /**
     * 点过刊登记新增按钮的新增
     * @param binding binding
     * @throws Exception Exception
     */
    @Transactional
    public void saveBinding(Binding binding) throws Exception {
        binding.setCheckStatus("0");
        binding.setOrgId(UserUtils.getOrgId());
        super.save(binding);
    }

    /**
     * 判断条码是否有流通记录(批量删除时作判断)
     * @param barcodeList barcodeList
     */
    public List<Binding> checkBindingBarcode (String barcodeList){
        String orgId = UserUtils.getOrgId();
        String[] idarr = barcodeList.split(",");
        List list = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("list", list);
        map.put("DEL_FLAG_DELETE", "1");
        map.put("orgId",orgId);
        return bindingDao.checkBindingBarcode(map);
    }

    /**
     * 查询过刊是通过什么方式来的 0登记 1合并
     * @param barcodeList barcodeList
     */
    public List<Binding> checkStatus (String barcodeList){
        String orgId = UserUtils.getOrgId();
        String[] idarr = barcodeList.split(",");
        List list = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("list", list);
        map.put("orgId",orgId);
        return bindingDao.checkStatus(map);
    }

    /**
     * 已合订期刊
     * @param page page
     * @param binding binding
     */
    public Page<Binding> findBinding(Page<Binding> page ,Binding binding){
        binding.setOrgId(UserUtils.getOrgId());
        binding.setPage(page);
        page.setList(bindingDao.findBinding(binding));
        return page;
    }

    /**
     * 期刊合订
     * @param ids ids
     * @param binding binding
     */
    @Transactional
    public void bindingPeri(String ids,Binding binding) throws Exception {
        binding.setCheckStatus("1");
        binding.setOrgId(UserUtils.getOrgId());
        binding.setStatus("0");//在馆
        super.save(binding);
        orderDetailService.bindingPeri(ids);//改订单明细的实到册数
        bindingDetailService.addBindingDetail(ids,binding);//添加过刊明细
    }

    /**
     * 取消期刊合订
     * @param ids ids
     */
    @Transactional
    public void removeBindingPeri(String ids) throws Exception {
        orderDetailService.removeBindingPeri(ids);
        bindingDetailService.delete(ids);
        super.delete(ids);
    }

    public void preProcessing(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMdd");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + t.format(new Date()) + ".xls");
        response.setBufferSize(2048);
    }

    /**
     * 书标打印导出（Excel）
     * @param binding binding
     * @param request request
     * @param response response
     */
    public String exportBookLabelExcel(Binding binding, HttpServletRequest request, HttpServletResponse response) throws Exception {
        binding.setOrgId(UserUtils.getOrgId());
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Binding> page = new Page<>();
            page.setOrderBy(orderBy);
            binding.setPage(page);
        }
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = t.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = null;
        try {
            codedFileName = new String("书标打印".getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("exportLibraryCollectionExcel UnsupportedEncodingException:" + e.getStackTrace());
            throw e;
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + date + ".xls");
        response.setBufferSize(2048);
        try {
            String filePathName = WriteBookLabelExportExcel.class.getClassLoader().getResource("").getPath() + "jxls/bookLabelTemplate.xls";
            List<Binding> list = dao.findAllList(binding);
            WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
            writeBookLabelExportExcel.createSheet1(list, filePathName, response.getOutputStream(), binding.getExportType());
        } catch (IOException e) {
            log.error(" exportBookmarkExcel IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            log.error(" exportBookmarkExcel Exception:" + e.getStackTrace());
            throw e;
        }

        return null;
    }

    /**
     * 书标打印导出（Txt）
     * @param binding binding
     * @param response response
     */
    public String exportBookmarkTxt(Binding binding, HttpServletRequest request, HttpServletResponse response) throws Exception {
        binding.setOrgId(UserUtils.getOrgId());
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Binding> page = new Page<>();
            page.setOrderBy(orderBy);
            binding.setPage(page);
        }
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = t.format(new Date());
        String codedFileName = null;
        response.setContentType("text/plain");

        try {
            codedFileName = new String("书标打印".getBytes("gb2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("exportBarcodeExt UnsupportedEncodingException:" + e.getStackTrace());
            throw e;
        }

        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + date + ".txt");
        response.setBufferSize(2048);
        try {
            List<Binding> list = dao.findAllList(binding);
            WriteBookLabelExportExcel writeBookLabelExportExcel = new WriteBookLabelExportExcel();
            writeBookLabelExportExcel.exportBookmarkList1(list, response, binding.getExportType());
        } catch (IOException e) {
            log.error(" exportBarcodeExt IOException:" + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            log.error(" exportBarcodeExt Exception:" + e.getStackTrace());
            throw e;
        }

        return null;
    }

    public Page<Binding> findAllList(Page page,Binding binding) {
        binding.setOrgId(UserUtils.getOrgId());
        if (binding.getLibrarsortCode() != null && !binding.getLibrarsortCode().equals("")) {
            String string = binding.getLibrarsortCode();
            if (!string.equals("") && string != null && string.contains(",")) {
                String[] strList = string.split(",");
                binding.setLibrarsortCode(null);
                List indexNumList = Arrays.asList(strList);
                binding.setLibSortCodeList(indexNumList);
            }
        }
        binding.setPage(page);
        page.setList(dao.findAllList(binding));
        return page;
    }

    /**
     * 书标打印
     * @param binding binding
     */
    public List<Binding> findAllList(Binding binding) {
        if (binding.getLibrarsortCode() != null && !binding.getLibrarsortCode().equals("")) {
            String string = binding.getLibrarsortCode();
            if (!string.equals("") && string.contains(",")) {
                String[] strList = string.split(",");
                List indexNumList = Arrays.asList(strList);
                binding.setLibSortCodeList(indexNumList);
            }
        }
        return dao.findAllList(binding);
    }
}
