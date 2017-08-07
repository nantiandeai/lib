package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.SupplyCard;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.circulate.service.SupplyCardService;
import com.lianyitech.modules.offline.utils.CustomException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api/circulate/supplyCard")
public class SupplyCardController extends ApiController {
    @Autowired
    private ReaderService readerService;
    @Autowired
    private SupplyCardService supplyCardService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value="查询读者证", notes = "根据读者名称或者读者证查询读者信息", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> list(SupplyCard supplyCard, HttpServletRequest request, HttpServletResponse response) {
        try{
            return new ResponseEntity<>(success(supplyCardService.findPage(new Page<>(request, response),supplyCard)), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(fail("系统异常"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ApiOperation(value="验证读者证", notes = "根据读者名称或者读者证来验证读者信息", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> check(SupplyCard supplyCard){
        try {
            supplyCardService.check(supplyCard);
        }catch (RuntimeException e){
            return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (CustomException e){
            return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.OK);//多个选择--是否继续
        }
        return new ResponseEntity<>(success(), HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value="添加读者证", notes = "根据读者名称或者读者证查询读者信息", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> save(SupplyCard supplyCard){
        int num = supplyCardService.saveSupplyCard(supplyCard);
        if(num > 0){
            return new ResponseEntity<>(success(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(fail("该证号不存在！该姓名不存在！"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiOperation(value="清空列表", notes = "根据机构id清空当天的列表", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> deleteAll(SupplyCard supplyCard) throws Exception {
        supplyCardService.deleteAll(supplyCard);
        return new ResponseEntity<>(success(), HttpStatus.OK);
    }
    /**
     * 打印读者证
     * @param
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> print(Reader reader, HttpServletRequest request, HttpServletResponse response) {
        Page<Reader> page = supplyCardService.findReaderPage(new Page<>(request, response),reader);
        return new ResponseEntity<>(success(readerService.printReadCard(page.getList())), HttpStatus.OK);
    }


}
