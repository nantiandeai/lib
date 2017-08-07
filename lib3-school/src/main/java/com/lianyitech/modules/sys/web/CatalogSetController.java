package com.lianyitech.modules.sys.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.sys.entity.CatalogSet;
import com.lianyitech.modules.sys.service.CatalogSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 编目管理Controller
 */
@RestController
@RequestMapping(value = "/api/sys/catalogset")
public class CatalogSetController extends ApiController {

    @Autowired
    private CatalogSetService catalogSetService;


    /**
     * 编目管理列表查询
     *
     * @return catalogSet
     */
/*    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(CatalogSet catalogSet, HttpServletRequest request, HttpServletResponse response) {
        Page<CatalogSet> page = catalogSetService.findPage(new Page<>(request, response), catalogSet);
        if (page.getList().size()<1) {
            List list =new ArrayList<>();
            CatalogSet cata = new CatalogSet();

        }
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }*/

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list1(CatalogSet catalogSet) {
        List<CatalogSet> list ;
        list =catalogSetService.findList(catalogSet);
        return new ResponseEntity<>(success(list), HttpStatus.OK);
    }

    /**
     * 编目管理修改
     *
     * @param catalogSet 对象
     * @return boolean
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(CatalogSet catalogSet) {
        try {
            if(catalogSet !=null){
                catalogSetService.save(catalogSet);
            }
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}