package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.NewbookNotifiy;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.sys.service.FileService;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 新书通报管理Controller
 *
 * @author zengzy
 * @version 2016-08-26
 */
@RestController
@RequestMapping(value = "/api/catalog/newbooknotifiy")
public class NewbookNotifiyController extends ApiController {

    @Autowired
    private NewbookNotifiyService newbookNotifiyService;

    @Autowired
    FileService fileService;

    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(NewbookNotifiy newbookNotifiy, HttpServletRequest request, HttpServletResponse response) {
        Page<NewbookNotifiy> page = newbookNotifiyService.findPage(new Page<>(request, response), newbookNotifiy);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 新书通报详细查询
     *
     * @param newbookNotifiy newbookNotifiy
     * @param request        request
     * @param response       response
     * @return HttpStatus
     */
    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.findNewbookList.get"})
    @RequestMapping(value = {"/findNewbookList"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findNewbookList(NewbookNotifiy newbookNotifiy, HttpServletRequest request, HttpServletResponse response) {
        Page<NewbookNotifiy> page = newbookNotifiyService.findNewbookList(new Page<>(request, response), newbookNotifiy);
        if (page != null) {
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        }
        return new ResponseEntity<>(fail("获取失败"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(NewbookNotifiy newbookNotifiy) {
        try {
            int b = newbookNotifiyService.countByName(newbookNotifiy);
            if (b > 0) {
                return new ResponseEntity<>(fail("该通报名称已经存在"), HttpStatus.BAD_REQUEST);
            }
            newbookNotifiyService.save(newbookNotifiy);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.delete"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        try {
            newbookNotifiyService.delete(id);
            return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 通报详细导出
     * @param newbookNotifiy newbookNotifiy
     * @param response response
     * @throws IOException 异常
     */
    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.exportNewBooksReports.get"})
    @RequestMapping(value = "/exportNewBooksReports", method = RequestMethod.GET)
    public void exportNewBooksReports(NewbookNotifiy newbookNotifiy, HttpServletResponse response) throws IOException {
        List<NewbookNotifiy> detailList = newbookNotifiyService.exportNewBooksReports(newbookNotifiy);
        setCvsConfig(response,"新书通报详情");
        List<String> resultList = new ArrayList<>();
        resultList.add("ISBN,索书号,题名,著者,出版社,出版时间,定价");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserNewBookReports(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 新书通报
     *
     * @param ids id
     * @return HttpStatus
     */
    @Secured({"ROLE_lib3school.api.catalog.newbooknotifiy.addNewBooksToReports.post"})
    @RequestMapping(value = "/addNewBooksToReports", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> addNewBooksToReports(@RequestBody String[] ids, HttpServletRequest request) {
        String id = request.getParameter("id");
        try {
            newbookNotifiyService.addNewBooksToReports(ids, id);
            return new ResponseEntity<>(success("通报成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("通报失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}