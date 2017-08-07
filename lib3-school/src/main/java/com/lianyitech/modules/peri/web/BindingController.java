package com.lianyitech.modules.peri.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.Encodes;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.utils.ExportCsv;
import com.lianyitech.modules.circulate.service.BillService;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.peri.service.BindingService;
import com.lianyitech.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * Created by zcx on 2017/3/14.
 * BindingController
 */
@Api(value="期刊合订", description ="有关于期刊合订的操作")
@RestController
@RequestMapping(value = "/api/peri/bindings")
public class BindingController extends ApiController {
    @Autowired
    private BindingService bindingService;
    @Autowired
    private Environment environment;
    @Autowired
    private BillService billService;

    /**
     * 过刊记录查询
     * @param binding 过刊实体
     * @param request request
     * @param response response
     * @return code
     */
    //@RequestMapping(value = "list", method = RequestMethod.GET)
    @GetMapping("")
    public ResponseEntity<ResponseData> findPage (Binding binding, HttpServletRequest request, HttpServletResponse response) {
        try {
            //把传中文的全部decode issn中可能有中文的－-
            if(StringUtils.isNotEmpty(binding.getIssn())) {
                binding.setIssn(Encodes.urlDecode(binding.getIssn()));
            }
            if(StringUtils.isNotEmpty(binding.getTitle())){
                binding.setTitle(Encodes.urlDecode(binding.getTitle()));
            }
            if(StringUtils.isNotEmpty(binding.getPublishingName())){
                binding.setPublishingName(Encodes.urlDecode(binding.getPublishingName()));
            }
            if(StringUtils.isNotEmpty(binding.getCollectionSiteName())){
                binding.setCollectionSiteName(Encodes.urlDecode(binding.getCollectionSiteName()));
            }
            //统一刊号有两个字符可能中文 一个是／/ 一个是－-
            if(StringUtils.isNotEmpty(binding.getPeriNum())){
                binding.setPeriNum(Encodes.urlDecode(binding.getPeriNum()));
            }
            Page<Binding> page = bindingService.findPage(new Page<>(request, response), binding);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 过刊记录导出
     * @param binding 过刊实体
     * @param response response
     */
    //@RequestMapping(value = "/exportBinding", method = RequestMethod.GET)
    @GetMapping("/export")
    public void exportReader(Binding binding, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Binding> page = new Page<>();
            page.setOrderBy(orderBy);
            binding.setPage(page);
        }
        String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "")+".csv";
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = uploadPath + File.separator + uploadFileName;
        setCvsConfig(response,"过刊清单");
        List<Binding> detailList = bindingService.collectionPeriReports(binding);
        List<String> resultList = new ArrayList<>();
        try(
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
                InputStream in = new FileInputStream(path);
        ) {
            resultList.add("条形码,索取号,题名,出版社,装订卷期,价格,ISSN,统一刊号,邮发代号,出版年份,馆藏地,馆藏状态");
            for (Binding obj : detailList) {
                resultList.add(ExportCsv.parseBindingLine(obj));
            }
            CsvUtils.exportBookReportCsv(bw,resultList);
            CsvUtils.writeResponse(in,response);
        }
        response.flushBuffer();
    }

    /**
     * 判断新增/修改过刊时条码是否可用
     * @param binding 过刊实体
     * @return code
     *
     * 后期这个接口是要调整的，会把条码约束条件放一个地方，通过条码传参来判断，现先用check来代替
     */
    //@RequestMapping(value = "check/binding", method = RequestMethod.GET)
    @GetMapping("/check")
    public ResponseEntity<ResponseData> checkBinding (Binding binding) {
        try {
            if(!SystemUtils.isTrueCardBarcode(binding.getBarcode(),binding.getId())){
                return new ResponseEntity<>(fail("条形码已被占用,请重新输入"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(success("条形码可用"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 过刊登记/修改
     * @param binding 过刊实体
     * @return code
     */
    //@RequestMapping(value = "save/binding", method = RequestMethod.POST)
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiOperation(value="过刊登记/修改", notes = "添加一条过刊记录或根据过刊id修改过刊记录", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> saveBinding(
            @ApiParam(required =true, name ="过刊实体", value="过刊的相关参数")
            @RequestBody
            Binding binding
    ) {
        try {
            if(StringUtils.isEmpty(binding.getPeriDirectoryId())){
                return new ResponseEntity<>(fail("选择期刊目录"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(StringUtils.isEmpty(binding.getBarcode())){
                return new ResponseEntity<>(fail("请输入条形码"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            bindingService.saveBinding(binding);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除过刊记录
     * @param ids 过刊ids
     * @param barcodeList 过刊条码集合
     * @return code
     */
    //@RequestMapping(value = "", method = RequestMethod.DELETE)
    @DeleteMapping("")
    @ApiOperation(value="删除过刊记录", notes = "根据过刊id删除过刊记录,如该过刊已存在流通记录则不可删", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> deleteBinding(
            @ApiParam(required =true, name ="过刊ids", value="过刊的ids")
            String ids,
            @ApiParam(required =true, name ="过刊条码集合", value="过刊条码集合")
            String barcodeList
    ) {
        try {
            if (bindingService.checkStatus(barcodeList).size()>0){
                return new ResponseEntity<>(fail("该期刊来源于合订，不可删除，可去取消合订"), HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (bindingService.checkBindingBarcode(barcodeList).size()>0) {
                return new ResponseEntity<>(fail("该期刊存在流通记录，禁止删除"), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                bindingService.delete(ids);
                return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 已合订期刊
     * @param binding 合订期刊实体
     * @param request request
     * @param response response
     * @return page
     */
    //@RequestMapping(value = "find/binding", method = RequestMethod.GET)
    @GetMapping("/{status}")
    public ResponseEntity<ResponseData> findBinding (Binding binding, @PathVariable String status, HttpServletRequest request, HttpServletResponse response) {
        try {
            binding.setCheckStatus(status);
            Page<Binding> page = bindingService.findBinding(new Page<>(request,response),binding);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * 期刊合订
     * @param ids 合订期刊id集合
     * @param binding 合订期刊实体
     * @return code
     */
    //@RequestMapping(value = "binding/peri", method = RequestMethod.POST)
    @PostMapping("/detail")
    public ResponseEntity<ResponseData> bindingPeri(String ids,Binding binding) {
        try {
            String[] idarr = ids.split(",");
            List idList = Arrays.asList(idarr);
            if (idList.size()<2) {
                return new ResponseEntity<>(fail("合订数量不能小于2"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            bindingService.bindingPeri(ids,binding);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 取消期刊合订
     * @param ids 过刊ids
     * @return code
     */
    //@RequestMapping(value = "remove/binding/peri", method = RequestMethod.POST)
    @PostMapping("/cancel")
    public ResponseEntity<ResponseData> removeBindingPeri(String ids, String barcode) {
        try {
            Map<String,Object> map = new HashMap<>();
            List<String> list = billService.periBarcode(barcode);
            if (list.size()>0){
                map.put("message","选中的期刊中有流通记录，不支持取消合订！期刊条码为：");
                map.put("list",list);
                return new ResponseEntity<>(fail(map), HttpStatus.OK);
            }
            bindingService.removeBindingPeri(ids);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 书标导出excel
     * @param binding binding
     * @param request request
     * @param response response
     * @return code
     */
    //@RequestMapping(value = "/exportPeriLabelExcel",method = RequestMethod.GET)
    @GetMapping("/labels/export/excel")
    public ResponseEntity<ResponseData> exportBookLabelExcel(Binding binding,HttpServletRequest request,HttpServletResponse response) throws Exception{
        bindingService.exportBookLabelExcel(binding,request,response);
        return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
    }

    /**
     * 书标导出TXT
     * @param binding binding
     * @param response response
     * @return code
     */
    //@RequestMapping(value = "/exportPeriLabelTxt",method = RequestMethod.GET)
    @GetMapping("/labels/export/txt")
    public ResponseEntity<ResponseData> exportBookmarkTxt(Binding binding, HttpServletRequest request, HttpServletResponse response) throws Exception{
        bindingService.exportBookmarkTxt(binding, request, response);
        return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
    }

    /**
     * 书标打印
     * @param binding binding
     * @param request request
     * @param response response
     * @return code
     */
    //@RequestMapping(value = "findAllList", method = RequestMethod.GET)
    @GetMapping("/labels")
    public ResponseEntity<ResponseData> findAllList (Binding binding, HttpServletRequest request, HttpServletResponse response) {
        try {
            //把传中文的全部decode issn中可能有中文的－-
            if(StringUtils.isNotEmpty(binding.getIssn())) {
                binding.setIssn(Encodes.urlDecode(binding.getIssn()));
            }
            if(StringUtils.isNotEmpty(binding.getTitle())){
                binding.setTitle(Encodes.urlDecode(binding.getTitle()));
            }
            if(StringUtils.isNotEmpty(binding.getPublishingName())){
                binding.setPublishingName(Encodes.urlDecode(binding.getPublishingName()));
            }
            if(StringUtils.isNotEmpty(binding.getCollectionSiteName())){
                binding.setCollectionSiteName(Encodes.urlDecode(binding.getCollectionSiteName()));
            }
            //统一刊号有两个字符可能中文 一个是／/ 一个是－-
            if(StringUtils.isNotEmpty(binding.getPeriNum())){
                binding.setPeriNum(Encodes.urlDecode(binding.getPeriNum()));
            }
            Page<Binding> page = bindingService.findAllList(new Page<>(request, response), binding);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
