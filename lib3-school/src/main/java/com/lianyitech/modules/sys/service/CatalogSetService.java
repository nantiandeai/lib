/**
 *
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.sys.dao.CatalogSetDao;
import com.lianyitech.modules.sys.entity.CatalogSet;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 编目设置Service
 */
@Service
public class CatalogSetService extends CrudService<CatalogSetDao, CatalogSet> {

    @Autowired
    private CatalogSetDao catalogSetDao;

    public Page<CatalogSet> findPage(Page<CatalogSet> page, CatalogSet catalogSet) {
//        catalogSet.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, catalogSet);
    }

    public List<CatalogSet> findList(CatalogSet catalogSet) {
        catalogSet.setOrgId(UserUtils.getOrgId());
        List<CatalogSet> list ;
        list = super.findList(catalogSet);
        if (list.size() < 1) {
            CatalogSet cc = new CatalogSet();
            cc.setCnfType("0");
            cc.setCnfMethod("0");
            list.add(cc);
//            CatalogSet dd = new CatalogSet();
//            dd.setCnfType("1");
//            dd.setCnfMethod("0");
//            list.add(dd);
        }
        return list;
    }

    @Transactional
    public void save(CatalogSet catalogSet) {
        catalogSet.setOrgId(UserUtils.getOrgId());
        super.save(catalogSet);
    }

}
