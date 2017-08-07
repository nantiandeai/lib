package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.circulate.entity.*;
import com.lianyitech.modules.circulate.service.BillService;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 操作单据管理Controller
 *
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/circulate/bill")
public class BillController extends ApiController {

    @Autowired
    private BillService billService;
    @Autowired
    private NewbookNotifiyService newbookNotifiyService;
    @Autowired
    FileService fileService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Bill bill, HttpServletRequest request, HttpServletResponse response) {
        Page<Bill> page = billService.findPage(new Page<>(request, response), bill);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 查询流通记录
     */
    @Secured({"ROLE_lib3school.api.circulate.bill.circulateInfo.get"})
    @RequestMapping(value = "/circulateInfo", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findReaderCirculateInfo(ReaderCard readerCard, HttpServletRequest request, HttpServletResponse response) {
        readerCard.setOrgId(UserUtils.getOrgId());
        ReaderCirculateInfo rcInfo = billService.findReaderCirculateInfo(readerCard, new Page<>(request, response), new Page<>(request, response));
        return new ResponseEntity<>(success(rcInfo), HttpStatus.OK);
    }

    /**
     * 查询图书信息和读者证
     *
     * @param copy  复本信息
     */
    @Secured({"ROLE_lib3school.api.circulate.bill.findBookByBarCode.get"})
    @RequestMapping(value = "/findBookByBarCode", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findBookByBarCode(Copy copy) {
        /*
        根据条形码搜期刊复本，如有则直接返回不可外借，如没有，继续往下走。
         */
//        Map<String,Object> map = billService.findPeriCopyByBarcode(copy);
//        if(map!=null && map.size()!=0){
//            map.clear();
//            map.put("fail","该文献类型为期刊，不可外借");
//            return new ResponseEntity<>(success(map),HttpStatus.OK);
//        }
        Map<String,Object> map= billService.findBookByBarCode(copy);//所以这里参数改成copy将orgid带过去
        return new ResponseEntity<>(success(map), HttpStatus.OK);
    }

    /**
     * 查询所有流通记录信息
     *
     * @param clDto 操作日志
     */
    @Secured({"ROLE_lib3school.api.circulate.bill.findAllCirculate.get"})
    @RequestMapping(value = "/findAllCirculate", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findAllCirculate(CirculateLogDTO clDto, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(clDto.getDirType())){
            return new ResponseEntity<>(fail("请选择流通类型"), HttpStatus.BAD_REQUEST);
        }
        clDto.setLogType("'0','1','2','7','8'");//流通记录列表查询中只显示借书，还书，续借，丢失，污损
        Page<CirculateLogDTO> page = billService.findAllCirculate(clDto, new Page<>(request, response));
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 流通记录详情导出
     * @param clDto 流通记录
     * @param response response
     * @throws IOException 异常
     */
    @Secured({"ROLE_lib3school.api.circulate.bill.exportCirculateInfo.get"})
    @RequestMapping(value = "/exportCirculateInfo", method = RequestMethod.GET)
    public void exportCirculateInfo(CirculateLogDTO clDto, HttpServletResponse response) throws IOException {
        List<CirculateLogDTO> detailList = billService.findAllCirculate(clDto);
        setCvsConfig(response,"流通记录");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,读者证号,读者姓名,条形码,题名,定价,借书日期,应还日期,还书日期,操作行为,操作日期");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserCirculate(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 超期记录详情导出
     * @param clDto 超期记录
     * @param response response
     * @throws IOException 异常
     */
    @Secured({"ROLE_lib3school.api.circulate.bill.exportPastDayInfo.get"})
    @RequestMapping(value = "/exportPastDayInfo", method = RequestMethod.GET)
    public void exportPastDayInfo(CirculateLogDTO clDto, HttpServletResponse response) throws IOException {
        clDto.setLogType("'0','1','2','7','8'");//流通记录列表查询中只显示借书，还书，续借，丢失，污损
        List<CirculateLogDTO> detailList = billService.findAllCirculate(clDto);
        setCvsConfig(response,"超期记录");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,读者证号,读者姓名,读者组织,条形码,题名,借书日期,应还日期,状态,超期天数");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserPastDayInfo(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 预约，预借查询接口
     * @param clDto 业务模型
     * @param request request
     * @param response response
     * @return page
     */
    @RequestMapping(value = "/orderBorrow", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> orderBorrow(CirculateLogDTO clDto, HttpServletRequest request, HttpServletResponse response) {
        //5预约，6预借circulate/bill
        try {
            Page<CirculateLogDTO> page = billService.findOrderBorrow(clDto, new Page<>(request, response));
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        }catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("参数解析错误"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 已借未还
     * @param clDto 业务模型
     * @param request request
     * @param response response
     * @return page
     */
    @RequestMapping(value = "/borrowing", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listBorrowing(CirculateLogDTO clDto, HttpServletRequest request, HttpServletResponse response) {
       Page<CirculateLogDTO> page = billService.listBorrowing(clDto, new Page<>(request, response));
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 已借未还导出
     * @param clDto  clDto
     * @param response response
     * @throws IOException 异常
     */
    @RequestMapping(value = "/borrowing/export", method = RequestMethod.GET)
    public void exportListBorrowing(CirculateLogDTO clDto, HttpServletResponse response) throws IOException {
        List<CirculateLogDTO> detailList = billService.exportListBorrowing(clDto);
        setCvsConfig(response,"已借未还记录");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,条形码,题名,借书日期,应还日期,读者证号,读者姓名,读者组织,读者类型,是否超期,超期天数");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserListBorrowing(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

}