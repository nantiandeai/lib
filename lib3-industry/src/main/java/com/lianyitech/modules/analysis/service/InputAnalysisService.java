package com.lianyitech.modules.analysis.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.analysis.dao.InputAnalysisDao;
import com.lianyitech.modules.analysis.entity.InputAnalysis;
import com.lianyitech.modules.common.Compare;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.service.AreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jordan jiang on 2017/4/5.
 */
@Service
public class InputAnalysisService extends CrudService<InputAnalysisDao,InputAnalysis> {

    @Autowired
    private InputAnalysisDao inputAnalysisDao;
    @Autowired
    private AreaService areaService;
    /**
     * 流通应用情况分析
     */
    public Map<String, Object> listCirculateAnalysis(InputAnalysis inputAnalysis) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<InputAnalysis> resultList;
        //定义一个类用来获取求和的属性
        InputAnalysis total_inputAnalysis = new InputAnalysis();

        if ( StringUtils.isEmpty(inputAnalysis.getProvince()) ) {
            inputAnalysis = (InputAnalysis)areaService.getArea(inputAnalysis);
        }

        if (inputAnalysis!=null){
            resultList = inputAnalysisDao.listCirculateAnalysis(inputAnalysis);
            ReportCommon.totalcommon(resultList, new String[]{"registerCount", "noInput", "yesInput"}, total_inputAnalysis);
            Collections.sort(resultList, Compare.noInput_Desc);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", total_inputAnalysis);
        return result;
    }

    /**
     * 录入读者应用情况分析
     */
    public Map<String, Object> listReaderAnalysis(InputAnalysis inputAnalysis) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<InputAnalysis> resultList;
        //定义一个类用来获取求和的属性
        InputAnalysis total_inputAnalysis = new InputAnalysis();

        if ( StringUtils.isEmpty(inputAnalysis.getProvince()) ) {
            inputAnalysis = (InputAnalysis)areaService.getArea(inputAnalysis);
        }

        if (inputAnalysis!=null){
            resultList = inputAnalysisDao.listReaderAnalysis(inputAnalysis);
            ReportCommon.totalcommon(resultList, new String[]{"registerCount", "noInput", "yesInput"}, total_inputAnalysis);
            Collections.sort(resultList, Compare.noInput_Desc);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", total_inputAnalysis);
        return result;
    }

    /**
     * 录入馆藏应用情况分析
     */
    public Map<String, Object> listCopyAnalysis(InputAnalysis inputAnalysis) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<InputAnalysis> resultList;
        //定义一个类用来获取求和的属性
        InputAnalysis total_inputAnalysis = new InputAnalysis();

        if ( StringUtils.isEmpty(inputAnalysis.getProvince()) ) {
            inputAnalysis = (InputAnalysis)areaService.getArea(inputAnalysis);
        }

        if (inputAnalysis!=null){
            resultList = inputAnalysisDao.listCopyAnalysis(inputAnalysis);
            ReportCommon.totalcommon(resultList, new String[]{"registerCount", "noInput", "yesInput"}, total_inputAnalysis);
            Collections.sort(resultList, Compare.noInput_Desc);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", total_inputAnalysis);
        return result;
    }
}
