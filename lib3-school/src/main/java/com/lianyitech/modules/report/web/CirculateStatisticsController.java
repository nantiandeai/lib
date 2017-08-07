package com.lianyitech.modules.report.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.NewbookNotifiy;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.circulate.entity.CirculateLogDTO;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import com.lianyitech.modules.report.service.CirculateRateService;
import com.lianyitech.modules.sys.service.FileService;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/*
 * Created by chenxiaoding on 2016/11/4.
 */
@RestController
@RequestMapping(value = "/api/report/circulateRate")
public class CirculateStatisticsController extends ApiController{
    @Autowired
    private CirculateRateService crService;
    @Autowired
    private NewbookNotifiyService newbookNotifiyService;

    @Autowired
    FileService fileService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findAllCirculateRate(String startDate, String endDate,String dirType){
        try {
            Map<String,Object> map = crService.unionCirculateRate(startDate, endDate,dirType);
            return new ResponseEntity<>(success(map), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 图书分布报表
     * @return code
     */
    @RequestMapping(value = "/bookDistributeRate",method = RequestMethod.GET)
    public ResponseEntity<ResponseData> bookDistributeRate(String actionTime){
        try {
            Map<String, Object> map = crService.bookDistributeRate(actionTime);
            return new ResponseEntity<>(success(map), HttpStatus.OK);
        }catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/fiveClassRate",method = RequestMethod.GET)
    public ResponseEntity<ResponseData> fiveClassRate(String actionTime){
        try {
            Map<String, Object> map = crService.fiveClassRate(actionTime);
            return new ResponseEntity<>(success(map), HttpStatus.OK);
        }catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //五大部类导出
    @RequestMapping(value = "/fiveClassRate/export", method = RequestMethod.GET)
    public void fiveClassRateExport(String actionTime, HttpServletResponse response) throws IOException {
        Map<String,Object> map = crService.fiveClassRate(actionTime);
        List<CirculateStatistics> detailList = (List<CirculateStatistics>)map.get("detailList");
        CirculateStatistics cr = new CirculateStatistics();
        cr.setFiveClass("合计");
        cr.setBookNum((Integer)map.get("totalBookNum"));
        cr.setBookSpecies((Integer)map.get("totalSpecies"));
        cr.setClassPrice(Double.valueOf(map.get("totalPrice")+""));
        cr.setSpeciesRate(map.get("totalSpeciesRate")+"");
        cr.setBookNumRate(map.get("totalBookNumRate")+"");
        cr.setPriceRate(map.get("totalpriceRate")+"");
        detailList.add(cr);
        setCvsConfig(response,"五大部类详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("部类,分类名称,种类,种类占比,册数,册数占比,金额,金额占比");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserClassRateExport(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    //22小类导出
    @RequestMapping(value = "/bookDistributeRate/export", method = RequestMethod.GET)
    public void bookDistributeRateExport(String actionTime, HttpServletResponse response) throws IOException {
        Map<String,Object> map = crService.bookDistributeRate(actionTime);
        List<CirculateStatistics> detailList = (List<CirculateStatistics>)map.get("detailList");
        CirculateStatistics cr = new CirculateStatistics();
        cr.setClassCode("合计");
        cr.setBookNum((Integer)map.get("totalBookNum"));
        cr.setBookSpecies((Integer)map.get("totalSpecies"));
        cr.setClassPrice(Double.valueOf(map.get("totalPrice")+""));
        cr.setSpeciesRate(map.get("totalSpeciesRate")+"");
        cr.setBookNumRate(map.get("totalBookNumRate")+"");
        cr.setPriceRate(map.get("totalpriceRate")+"");
        detailList.add(cr);
        setCvsConfig(response,"藏书分布结构详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("类别,类别中文名,种类,种类占比,册数,册数占比,金额,金额占比");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserBookDistributeRate(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    //流通率统计报表导出
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(String startDate,String endDate, String dirType,HttpServletResponse response) throws IOException {
        Map<String,Object> map = crService.unionCirculateRate(startDate, endDate,dirType);
        List<CirculateStatistics> detailList = (List<CirculateStatistics>)map.get("circulateRateList");
        CirculateStatistics cr = new CirculateStatistics();
        cr.setClassCode("合计");
        cr.setBookNum((Integer)map.get("totalBookNum"));
        cr.setBorrowNum((Integer)map.get("totalBorrowNum"));
        cr.setCirculateRate(map.get("totalCirculateRate")+"");
        detailList.add(cr);
        setCvsConfig(response,"流通率统计详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("类别,类别中文名,藏书册数,借阅册数,流通率");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserCirculateRate(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }


}
