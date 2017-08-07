/**
 *
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.modules.sys.dao.PublishingDao;
import com.lianyitech.modules.sys.entity.Publishing;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出版社管理Service
 *
 * @author zengzy
 * @version 2016-08-31
 */
@Service
public class PublishingService extends CrudService<PublishingDao, Publishing> {

    public Publishing get(String id) {
        return super.get(id);
    }

    public List<Publishing> findList(Publishing publishing) {
        return super.findList(publishing);
    }

    public Page<Publishing> findPage(Page<Publishing> page, Publishing publishing) {
        return super.findPage(page, publishing);
    }

    @Transactional
    public void save(Publishing publishing) {
        super.save(publishing);
    }

    @Transactional
    public void delete(Publishing publishing) {
        super.delete(publishing);
    }

}