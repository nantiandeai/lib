package com.lianyitech.modules.report.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.GroupRank;
import com.lianyitech.modules.report.service.GroupRankService;
import com.lianyitech.modules.sys.service.FileService;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * Created by zcx on 2016/11/9.
 * 组织借阅排行controller
 */
@Controller
@RequestMapping(value = "/api/report/groupRanking")
public class GroupRankController extends ApiController {
    @Autowired
    private GroupRankService groupRankService;
    @Autowired
    FileService fileService;

    //组织借阅排行榜
    @RequestMapping(value = "/group/ranking", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findList (GroupRank groupRank, HttpServletRequest request, HttpServletResponse response) {
        Page<GroupRank> page = groupRankService.findPage(new Page<>(request, response),groupRank);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    //组织借阅排行榜导出
    @RequestMapping(value = "/group/ranking/export", method = RequestMethod.GET)
    public void exportReaderRank(GroupRank groupRank, HttpServletResponse response) throws IOException {
        List<GroupRank> detailList = groupRankService.groupRanking(groupRank);
        setCvsConfig(response,"组织借阅排行榜");
        List<String> resultList = new ArrayList<>();
        resultList.add("借阅排名,读者组织,组织人数,借阅次数");
        int ranking=1;
        for (GroupRank groupRank1 : detailList) {
            resultList.add(CsvUtils.parseGroupRank(groupRank1,ranking));
            ranking++;
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }
}
