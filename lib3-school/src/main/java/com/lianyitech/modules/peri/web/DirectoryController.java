package com.lianyitech.modules.peri.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.Encodes;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.catalog.utils.SolrPage;
import com.lianyitech.modules.peri.entity.Directory;
import com.lianyitech.modules.peri.service.DirectoryService;
import com.lianyitech.modules.sys.service.LibrarsortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 期刊Controller
 * @author chenxiaoding
 * @version 20170310
 */
@RestController
@RequestMapping(value = "/api/peri/directors")
public class DirectoryController extends ApiController {

    @Autowired
    private DirectoryService directoryService;
    @Autowired
    private BookDirectoryService bookDirectoryService;
    @Autowired
    private LibrarsortService librarsortService;



    /**保存简单编目--
     要将对应的马克数据信息也要对应保存
     要获取已存在马克数据和模板数据的并集，并且重新将简单编目的属性信息赋值过来）
     **/
    //@RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping("")
    public ResponseEntity<ResponseData> saveSimpleBook(Directory directory) {
        // 保存之前要对书目信息进行判断，判断是否馆藏书目库是否存在相同的书目信息
        try {
            if(StringUtils.isNotEmpty(directory.getIssn())) {
                directory.setIssn(Encodes.urlDecode(directory.getIssn()));
            }
            String returnstr = directoryService.saveSimpleBook(directory);
            if (returnstr != null) {
                return new ResponseEntity<>(fail(returnstr), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(success((Object)directory.getId()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 保存马克数据--这里要将对应的简单期刊信息也要保存
     * @param directory  directory
     * @param request  request
     * @return code
     */
    //@RequestMapping(value = "/periMarc", method = RequestMethod.POST)
    @PostMapping("/marc")
    public ResponseEntity<ResponseData> periMarc(Directory directory,HttpServletRequest request) {
        try {
            String returnstr = directoryService.savePeriMarc(directory,request);
            if (returnstr != null) {
                return new ResponseEntity<>(fail(returnstr), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(success((Object) directory.getId()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     **期刊查询，通过solr客户端查询期刊信息
     */
    //@RequestMapping(value = "/listSolr",method = RequestMethod.GET)
    @GetMapping("/solr")
    public ResponseEntity<ResponseData> listSolr(Directory directory, HttpServletRequest request, HttpServletResponse response) {
        SolrPage page = directoryService.findSolrBookPage(new SolrPage(request),new Page<>(request, response), directory);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }


    /**
     * 得到马克数据--马克编辑页面查看信息
     * @param directory  directory
     * @return 马克数据
     */
    //@RequestMapping(value = {"/marcinfo"}, method = RequestMethod.GET)
    @GetMapping("/marc/info")
    public ResponseEntity<ResponseData> marcInfo(Directory directory) {
        Object o = directoryService.getMarcInfo(directory);
        return new ResponseEntity<>(success(o), HttpStatus.OK);
    }


    //@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        try {
            Directory directory = null;
            if (StringUtils.isNotBlank(id)) {
                directory = directoryService.get(id);
            }
            //判断期刊编目下面1有没有记到，2有没有过刊
            int i = directoryService.findCountByBinding(id);
            int j = directoryService.findCountByOrder(id);
            if((j+i)>0){
                return new ResponseEntity<>(fail("该条目录存在登记信息，删除失败"),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            directory.setUpdateDate(new Date());
            directoryService.delete(directory);//这里删除单个删除暂时不改
            return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
        }catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@RequestMapping(value = "/mainMarcinfo",method = RequestMethod.GET)
    @GetMapping("/marc/info/{tag}")
    public ResponseEntity<ResponseData> listMainMarcInfo(@PathVariable("tag")String tag) {
        List<Map> list = directoryService.getMarcinfo(tag);
        return new ResponseEntity<>(success(list), HttpStatus.OK);
    }

    //@RequestMapping(value = {"/checkMarc"},consumes = "application/json",method = RequestMethod.POST)
    @PostMapping("/checkMarc")
    public ResponseEntity<ResponseData> checkMarc(@RequestBody List<Map> list) {
        Map<String,Object> result = directoryService.checkMarcMap(list);
        return new ResponseEntity<>(success(result), HttpStatus.OK);
    }

    /**
     * 写个方法用来验证马克编辑里面的分类号是否
     * @param map  map
     * @return code
     */
    //@RequestMapping(value = {"/checkSortCodeMarc"},consumes = "application/json",method = RequestMethod.POST)
    @PostMapping("/checkSortCodeMarc")
    public ResponseEntity<ResponseData> checkSortCodeMarc(@RequestBody Map map) {
        Map<String,String> result = bookDirectoryService.checkSortCodeMarc(map);
        return new ResponseEntity<>(success(result), HttpStatus.OK);
    }

    /**
     * 期刊ISSN验证
     */
    //@RequestMapping(value = {"/checkPeri"},method = RequestMethod.POST)
    @PostMapping("/issn")
    public ResponseEntity<ResponseData> checkPeri(Directory directory) {
        //前端encode传中文字符
        directory.setIssn(Encodes.urlDecode(directory.getIssn()));
        Map<String,String> checkmap = directoryService.checkPeriProperty(directory);
        return new ResponseEntity<>(success(checkmap), HttpStatus.OK);
    }

    /**
     * 期刊分类号验证
     * 用书目以前的验证方式，没毛病
     */
    //@RequestMapping(value = {"/checkSortCode"},method = RequestMethod.POST)
    @PostMapping("/sort")
    public ResponseEntity<ResponseData> checkSortCode(Directory directory) {
        if (StringUtils.isBlank(directory.getLibrarsortCode())) {
            Map<String,Object> result = new HashMap<>();
            result.put("fail","分类号不能为空");
            return new ResponseEntity<>(success(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(success(librarsortService.checkExistSortCode(directory.getLibrarsortCode())), HttpStatus.OK);
    }


    /**
     * 根据期刊数目id获取书目信息
     * @param id  id
     * @return 书目信息
     */
    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> get(@PathVariable String id) {
        Directory directory = directoryService.getDirectory(id);//这个查询复本
        if (null == directory) {
            return new ResponseEntity<>(fail("数据不存在"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success(directory), HttpStatus.OK);
    }
}