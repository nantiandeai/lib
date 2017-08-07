package com.lianyitech.modules.report.web;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.report.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/report/area")
public class AreaController extends ApiController {

    @Autowired
    private AreaService areaService;

    /**
     * 区域列表-根据查询类型，父级编号查询子区域
     *
     * @param type
     * @param parentCode
     * @return
     */
    @RequestMapping(value = "/{type}/{parentCode}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> areList(@PathVariable("type") String type, @PathVariable("parentCode") String parentCode) {
        return new ResponseEntity<>(success(areaService.findByParentCode(type, parentCode)), HttpStatus.OK);
    }

}
