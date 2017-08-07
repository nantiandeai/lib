package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.entity.ImportTemplate;
import com.lianyitech.modules.catalog.service.ImportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导入模板Controller
 * @author zengzy
 * @version 2016-09-12
 */
@RestController
@RequestMapping(value = "/api/catalog/importtemplate")
public class ImportTemplateController extends ApiController {

	@Autowired
	private ImportTemplateService importTemplateService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(ImportTemplate importTemplate, HttpServletRequest request, HttpServletResponse response) {
		Page<ImportTemplate> page = importTemplateService.findPage(new Page<>(request, response), importTemplate);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(ImportTemplate importTemplate) {
		try {
            importTemplateService.save(importTemplate);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
         try {
            importTemplateService.delete(id);
            return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
        } catch (Exception e) {
             logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

    @RequestMapping(value = "/deleteTemplate", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> deleteTemplate(ImportTemplate importTemplate) {

        ImportTemplate defaultTemplate;
        defaultTemplate = importTemplateService.findDefaultTemplate();
        if (defaultTemplate!=null && importTemplate.getId()!=null){
            if (defaultTemplate.getId().equals(importTemplate.getId())){
                return new ResponseEntity<>(fail("该书标模板是默认模板，不能删除！"), HttpStatus.BAD_REQUEST);
            }
        }

        try {
            if (importTemplateService.deleteTemplate(importTemplate)>0){
                return new ResponseEntity<>(success("删除书标模板成功！"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(success("没有书标模板删除！"), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除书标模板失败！"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}