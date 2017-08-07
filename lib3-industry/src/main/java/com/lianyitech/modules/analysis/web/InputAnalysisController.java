package com.lianyitech.modules.analysis.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.analysis.entity.InputAnalysis;
import com.lianyitech.modules.analysis.entity.School;
import com.lianyitech.modules.analysis.service.InputAnalysisService;
import com.lianyitech.modules.analysis.service.SchoolService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jordan jiang on 2017/4/5.
 */
@Api(value = "/api/analysis/inputanalysis", description = "有关于应用分析的操作")
@RestController
@RequestMapping(value = "/api/analysis/inputanalysis")
public class InputAnalysisController extends ApiController {

    @Autowired
    InputAnalysisService inputAnalysisService;
    @Autowired
    SchoolService schoolService;

    /**
     * 馆藏应用情况分析
     *
     * @param inputAnalysis 实体类
     * @return 返回
     */
    @GetMapping("/copys")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "市代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "county", value = "县代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "schoolArea", value = "学区代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "unitName", value = "学校名称", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "馆藏应用情况分析", notes = "查询没有馆藏录入的学校统计")
    public ResponseEntity<ResponseData> listCopyAnalysis(@ApiIgnore InputAnalysis inputAnalysis) throws Exception{
        return new ResponseEntity<>(success(inputAnalysisService.listCopyAnalysis(inputAnalysis)), HttpStatus.OK);
    }

    /**
     * 流通应用情况分析
     *
     * @param inputAnalysis 实体类
     * @return 返回
     */
    @GetMapping("/circulates")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "市代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "county", value = "县代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "schoolArea", value = "学区代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "unitName", value = "学校名称", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "流通应用情况分析", notes = "查询没有流通记录的学校统计")
    public ResponseEntity<ResponseData> getListCirculateAnalysis(@ApiIgnore InputAnalysis inputAnalysis) throws Exception {
        return new ResponseEntity<>(success(inputAnalysisService.listCirculateAnalysis(inputAnalysis)), HttpStatus.OK);
    }

    /**
     * 录入读者应用情况分析
     *
     * @param inputAnalysis 实体类
     * @return 返回
     */
    @GetMapping("/readers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "市代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "county", value = "县代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "schoolArea", value = "学区代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "unitName", value = "学校名称", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "录入读者应用情况分析", notes = "查询没有录入读者的学校统计")
    public ResponseEntity<ResponseData> getListReaderAnalysis(@ApiIgnore InputAnalysis inputAnalysis) throws Exception {
        return new ResponseEntity<>(success(inputAnalysisService.listReaderAnalysis(inputAnalysis)), HttpStatus.OK);
    }

    /**
     * 无录入信息的学校列表
     *
     * @param school 实体类
     * @return 返回
     */
    @GetMapping("/schools")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "市代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "county", value = "县代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "schoolArea", value = "学区代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "unitName", value = "学校名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "selectType", value = "查询类型：1：无馆藏  2：无读者   3：无流通", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "无录入信息的学校列表", notes = "查询没有录入信息的学校")
    public ResponseEntity<ResponseData> getListNoInputSchool(@ApiIgnore School school,
            HttpServletRequest request, HttpServletResponse response) {
        Page<School> page = schoolService.listNoInputSchool(new Page<>(request, response), school);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 无录入信息的学校导出
     *
     * @param school 实体类
     * @return null
     */
    @GetMapping("/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "市代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "county", value = "县代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "schoolArea", value = "学区代码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "unitName", value = "学校名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "selectType", value = "查询类型：1：无馆藏  2：无读者   3：无流通", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "导出无录入信息的学校", notes = "导出没有录入信息的学校列表")
    public ResponseEntity<ResponseData> exportNoInputSchool(@ApiIgnore School school, HttpServletResponse response) {
        schoolService.exportNoInputSchool(school, response);
        return null;
    }
}
