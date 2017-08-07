package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.jxls.WriteToOut;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.core.utils.ImageIODemo;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.circulate.entity.CardPrintConfig;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import com.lianyitech.modules.circulate.service.CardPringConfigService;
import com.lianyitech.modules.circulate.service.CirculateService;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 读者管理Controller
 *
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/circulate/reader")
public class ReaderController extends ApiController {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private CirculateService circulateService;

    @Autowired
    FileService fileService;

    @Autowired
    CardPringConfigService cardPringConfigService;


    //@Secured({"ROLE_lib3school.api.circulate.reader.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Reader reader, HttpServletRequest request, HttpServletResponse response) {
        Page<Reader> page = readerService.findPage(new Page<>(request, response), reader);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 读者导出
     * @param reader   reader
     * @param response response
     * @throws IOException 异常
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.exportReader.get"})
    @RequestMapping(value = "/exportReader", method = RequestMethod.GET)
    public void exportReader(Reader reader, HttpServletResponse response) throws IOException {
            List<Reader> detailList = readerService.exportReader(reader);
            setCvsConfig(response,"读者");
            List<String> resultList = new ArrayList<>();
            resultList.add("读者证号,读者姓名,读者性别,读者组织,读者类型,读者押金,办证日期,终止日期,读者状态");
            for (Reader reader1 : detailList) {
                resultList.add(CsvUtils.parseReader(reader1));
            }
            fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 读者查询
     *
     * @param reader //传入的读者参数
     * @return // 返回操作结果
     */
    @RequestMapping(value = "/queryReader", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> queryByReaderPlatfrom(Reader reader) {
        Reader readerTmp = new Reader();
        BeanUtils.copyProperties(reader,readerTmp);
        readerTmp.setPassword("");
        Reader reader1 =  readerService.queryByReaderPlatfrom(readerTmp);
        if(reader1==null){
            return  new ResponseEntity<>(fail("读者证号错误"), HttpStatus.OK);
        }
        Reader r =  readerService.queryByReaderPlatfrom(reader);
        if(r==null){
            return  new ResponseEntity<>(fail("密码错误"), HttpStatus.OK);
        }
        return new ResponseEntity<>(success(r), HttpStatus.OK);
    }


    /**
     * 读者新增和修改
     *
     * @param reader //传入的读者参数
     * @return // 返回操作结果
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(Reader reader) {
        List<Reader> errorList = new ArrayList<>();
        String reStr;
        try {
            readerService.util(reader, errorList);
            if (errorList.size() > 0) {
                for (Reader me : errorList) {
                    reStr = me.getErrorinfo();
                    return new ResponseEntity<>(fail(reStr), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            readerService.save(reader);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/check/name", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> checkCard(Reader reader) {//这个判断重复了。。在后面会进行判断的
        int count = readerService.checkCard(reader);
        Map<String, String> result = new HashMap<>();
        if (count > 0) {
            //result.put("fail","操作失败,读者证号需要唯一");
            return new ResponseEntity<>(fail("操作失败,读者证号需要唯一"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        result.put("success", "该读者证号可用");
        return new ResponseEntity<>(success(result), HttpStatus.OK);
    }

    /**
     * @param id 这里传的是读者ID，如需传读者证ID，请换一个，谢谢
     * @return ResponseEntity
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.delete"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        Map map = readerService.deleteReader(id);
        if (map != null) {
            return new ResponseEntity<>(fail(map.get("fail").toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
    }


    /**
     * 批量修改
     *
     * @param reader 前端传的对象
     * @return 返回结果给前端
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.updateReader.post"})
    @RequestMapping(value = "/updateReader", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseData> updateReader(@RequestBody Reader reader) {
        try {
            readerService.updateReader(reader);
            return new ResponseEntity<>(success("修改成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("修改失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_lib3school.api.circulate.reader.downloadReaderTemplate.get"})
    @RequestMapping(value = {"/downloadReaderTemplate"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> downloadReaderTemplate(HttpServletResponse response) {

        String fileName = "读者导入模板.xls";
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("template/readerTemplate.xls");
            if (WriteToOut.writeToResponse(fileName, in, response)) {
                return new ResponseEntity<>(fail("操作错误！"), HttpStatus.BAD_REQUEST);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("操作失败",e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new ResponseEntity<>(success("操作完成！"), HttpStatus.OK);
    }

    @Secured({"ROLE_lib3school.api.circulate.reader.importReaderCard.post"})
    @RequestMapping(value = "/importReaderCard", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> importReaderCard(MultipartHttpServletRequest multipartRequest) throws IOException {
        // 支持多文件上传
        try {
            for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
                MultipartFile multiFile = multipartRequest.getFile(it.next());
                readerService.upReaderFile(multiFile);
            }
            return new ResponseEntity<>(success("操作完成"), HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("参数解析错误"), HttpStatus.OK);
        }

    }

    /**
     * 批量恢复读者证
     *
     * @param ids 多个读者ID用，隔开
     * @return ResponseEntity
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.regainReaderCard.delete"})
    @RequestMapping(value = "regainReaderCard/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> regainReaderCard(@PathVariable("id") String ids) {
        Map map;
        try {
            map = readerService.regain(ids);
            if (map != null) {
                return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(fail(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success((Object) null), HttpStatus.OK);
    }

    /**
     * 改变读者证状态接口，如挂失，换证，解挂，续期
     *
     * @param readerCard 传读者证
     * @return responseEntity
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.changeStatusAction.post"})
    @RequestMapping(value = "/changeStatusAction", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> changeStatusAction(ReaderCard readerCard) {
        String msg = readerService.changeStatusAction(readerCard);
        if (StringUtils.isNotBlank(msg)) {
            return new ResponseEntity<>(fail(msg), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(success(), HttpStatus.OK);
        }
    }

    /**
     * 批量注销读者证
     *
     * @param ids 多个读者ID用,隔开
     * @return ResponseEntity
     */
    @Secured({"ROLE_lib3school.api.circulate.reader.logOutReaderCard.delete"})
    @RequestMapping(value = "logOutReaderCard/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> logOutReaderCard(@PathVariable("id") String ids) {
        try {
            String msg = readerService.logOutReaderAndCard(ids);
            if (StringUtils.isNotBlank(msg)) {
                return new ResponseEntity<>(fail(msg), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(success(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(e.getMessage().contains("图书未归还")?e.getMessage():"异常"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 读者换证列表接口
     * @param reader  前端传过来的读者对象
     * @return  换证信息的列表
     */
//    @Secured({"ROLE_lib3school.api.circulate.reader.renewal.get"})
    @RequestMapping(value = "renewal" , method = RequestMethod.GET)
    @ApiOperation(value="读者换证列表", notes = "根据前端传来的条件查询换证的记录", response = ResponseEntity.class)
  public ResponseEntity<ResponseData> renewalList(Reader reader, HttpServletRequest request, HttpServletResponse response){
        Page page = readerService.findRenewalPage(new Page<>(request, response), reader);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
  }

    /**
     * 换证记录导出
     * @param reader reader
     * @param response response
     * @throws IOException 异常
     */
    @GetMapping("/renewal/export/excel")
    public void exportRenewalExcel(Reader reader, HttpServletResponse response) throws IOException {
        reader.setOrgId(UserUtils.getOrgId());
        List<Reader> detailList = readerService.exportRenewal(reader);
        setCvsConfig(response,"换证记录");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,处理时间,读者姓名,读者组织,读者类型,旧读者证号,新读者证号");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parseRenew(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 停借
     * @param dto dto
     * @return code
     */
    @PostMapping("/stop/card")
    public ResponseEntity<ResponseData> stopBy(CirculateDTO dto){
        try {
            dto.setOrgId(UserUtils.getOrgId());
            circulateService.stopBy(dto);
            return new ResponseEntity<>(success("该读者已经设置成停借状态，需解除后才可继续借阅"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 解除
     * @param dto dto
     * @return code
     */
    @PostMapping("/relieve/card")
    public ResponseEntity<ResponseData> relieve(CirculateDTO dto){
        try {
            dto.setOrgId(UserUtils.getOrgId());
            circulateService.relieve(dto);
            return new ResponseEntity<>(success("解除成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据读者证号查询读者信息
     * @param readerCard
     * @return
     */
    @RequestMapping(value = "/info/{readerCard}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findReaderInfo(@PathVariable("readerCard") String readerCard) {
        return new ResponseEntity<>(success(readerService.findReaderInfoByCard(readerCard)), HttpStatus.OK);
    }


    /**
     * 打印读者证
     * @param
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> print(Reader reader , HttpServletRequest request, HttpServletResponse response) {
        reader.setOrgId(UserUtils.getOrgId());
        Page<Reader> page = readerService.findPage(new Page<>(request, response), reader);
        return new ResponseEntity<>(success(readerService.printReadCard(page.getList())), HttpStatus.OK);
    }

    /**
     * 预览读者证
     * @return
     */
    @RequestMapping(value = "/print/view", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> printView() {
        return new ResponseEntity<>(success(), HttpStatus.OK);
    }
}