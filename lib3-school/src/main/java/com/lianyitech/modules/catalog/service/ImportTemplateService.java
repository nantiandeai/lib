/**
 *
 */
package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.Global;
import com.lianyitech.modules.catalog.dao.ImportTemplateDao;
import com.lianyitech.modules.catalog.entity.ImportTemplate;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 导入模板Service
 *
 * @author zengzy
 * @version 2016-09-12
 */
@Service
public class ImportTemplateService extends CrudService<ImportTemplateDao, ImportTemplate> {

    public Page<ImportTemplate> findPage(Page<ImportTemplate> page, ImportTemplate importTemplate) {
        importTemplate.getSqlMap().put("dsf", UserUtils.dataScopeFilter("a"));
        return super.findPage(page, importTemplate);
    }

    @Transactional
    public void save(ImportTemplate importTemplate) {
        importTemplate.setOrgId(UserUtils.getOrgId());
        //如果传入的书标打印模板是默认的，就修改原默认的模块为非默认。
        if (importTemplate.getStatus() != null && importTemplate.getStatus().equals("0")) {
            ImportTemplate defaultTemplate = findDefaultTemplate();
            if (defaultTemplate != null) {
                defaultTemplate.setStatus("1");
                super.save(defaultTemplate);
            }
        }
        super.save(importTemplate);
    }

    @Transactional
    public int delete(String ids) {
        return super.delete(ids);
    }

    @Transactional
    public int deleteTemplate(ImportTemplate importTemplate) {

        String upTemplatePath = Global.getUploadRootPath() + Global.UPLOADFILES_BASE_URL;
        File deleteFile = new File(upTemplatePath + "/" + importTemplate.getFileName());
        deleteFile.delete();

        return super.delete(importTemplate.getId());
    }

    public ImportTemplate findDefaultTemplate() {
        return dao.findDefaultTemplate(UserUtils.getOrgId());
    }

    public List<ImportTemplate> findSameFileNameList(ImportTemplate importTemplate) {
        importTemplate.setOrgId(UserUtils.getOrgId());
        return dao.findSameFileNameList(importTemplate);
    }
}