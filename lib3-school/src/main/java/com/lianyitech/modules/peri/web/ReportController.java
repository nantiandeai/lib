package com.lianyitech.modules.peri.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.peri.service.DirectoryService;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import com.lianyitech.modules.sys.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value="期刊分类统计", description ="期刊分类统计相关业务")
@RestController
@RequestMapping(value = "/api/peri/classification")
public class ReportController extends ApiController{

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    FileService fileService;

    @ApiOperation(value="期刊分类统计-五大部类统计列表信息", notes = "按五大部类统计期刊分布情况", response = ResponseEntity.class)
    @RequestMapping(value = "/fiveClassRate",method = RequestMethod.GET)
    public ResponseEntity<ResponseData> fiveClassRate(@ApiParam(required =true, name ="actionTime", value="年份信息") String actionTime){
        try {
            Map<String, Object> map = directoryService.fiveClassRate(actionTime);
            return new ResponseEntity<>(success(map), HttpStatus.OK);
        }catch (Exception e){
            logger.error("期刊分类统计-五大部类统计列表信息异常",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiOperation(value="期刊分类统计-22大类统计列表信息", notes = "按22基本部类统计期刊分布情况", response = ResponseEntity.class)
    @RequestMapping(value = "/bookDistributeRate",method = RequestMethod.GET)
    public ResponseEntity<ResponseData> bookDistributeRate(@ApiParam(required =true, name ="actionTime", value="年份信息")String actionTime, @ApiParam(required =true, name ="orderBy", value="bookNum：册数 ; bookSpecies：种类 ； classPrice ： 金额,示例： bookNum desc-->按照册数降序排列，bookNum asc -->按照册数升序排列") String orderBy){
        try {
            Map<String, Object> map = directoryService.bookDistributeRate(actionTime,orderBy);
            return new ResponseEntity<>(success(map), HttpStatus.OK);
        }catch (Exception e){
            logger.error("期刊分类统计-22大类统计列表异常",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //五大部类导出
    @ApiOperation(value="期刊分类统计-五大部类导出", notes = "五大部类导出", response = ResponseEntity.class)
    @RequestMapping(value = "/fiveClassRate/export", method = RequestMethod.GET)
    public void fiveClassRateExport(@ApiParam(required =true, name ="actionTime",
                    value="年份信息")String actionTime, HttpServletResponse response) throws IOException {

        Map<String,Object> map = directoryService.fiveClassRate(actionTime);
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

        setCvsConfig(response,"期刊分类统计-五大部类详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("部类,分类名称,种类,种类占比,册数,册数占比,金额,金额占比");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserfiveClass(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    //22小类导出
    @ApiOperation(value="期刊分类统计-22小类导出", notes = "22小类导出", response = ResponseEntity.class)
    @RequestMapping(value = "/bookDistributeRate/export", method = RequestMethod.GET)
    public void bookDistributeRateExport(@ApiParam(required =true, name ="actionTime", value="年份信息")String actionTime,
                                         @ApiParam(required =true, name ="orderBy", value="bookNum：册数 ; bookSpecies：种类 ；" +
                                                   "classPrice ： 金额,示例： bookNum desc-->按照册数降序排列，bookNum asc -->按照册数升序排列") String orderBy,
                                         HttpServletResponse response) throws IOException {

        Map<String,Object> map = directoryService.bookDistributeRate(actionTime,orderBy);
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

        setCvsConfig(response,"期刊分类统计-藏书分布结构详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("类别,类别中文名,种类,种类占比,册数,册数占比,金额,金额占比");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserbookDistribute(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }
}
