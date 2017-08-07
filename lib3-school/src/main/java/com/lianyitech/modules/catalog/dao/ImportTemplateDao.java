/**
 *
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.ImportTemplate;

import java.util.List;

/**
 * 导入模板DAO接口
 *
 * @author zengzy
 * @version 2016-09-12
 */
@MyBatisDao
public interface ImportTemplateDao extends CrudDao<ImportTemplate> {
    ImportTemplate findDefaultTemplate(String orgId);

    List<ImportTemplate> findSameFileNameList(ImportTemplate importTemplate);
}