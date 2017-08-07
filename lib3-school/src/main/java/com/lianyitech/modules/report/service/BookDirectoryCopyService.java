package com.lianyitech.modules.report.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.dao.BookDirectoryDao;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.report.dao.BookDirectoryCopyDao;
import com.lianyitech.modules.report.entity.BookDirectoryCopy;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*
 *
 * Created by chenxiaoding on 2016/11/4.
 */
@Service

public class BookDirectoryCopyService extends CrudService<BookDirectoryCopyDao, BookDirectoryCopy> {
    @Autowired
    private BookDirectoryDao bookDirectoryDao;

    /**
     * 复本统计查询
     * @param page   分页对象
     * @param bdCopy  bdCopy
     * @return page
     */
    public Page<BookDirectoryCopy> findPage(Page<BookDirectoryCopy> page, BookDirectoryCopy bdCopy){
        bdCopy.setOrgId(UserUtils.getOrgId());
        bdCopy.setPage(page);
        page.setList(dao.findAllDirectoryCopy(bdCopy));
        return page;
    }

    /**
     * 复本统计导出
     * @param bdCopy bdCopy
     * @return list
     */
    public List<BookDirectoryCopy> findAllDirectoryCopy(BookDirectoryCopy bdCopy){
        bdCopy.setOrgId(UserUtils.getOrgId());
        return dao.findCopyStatForExport(bdCopy);
    }

    @Cacheable(value = "bookDirectorySite", key = "#bookDirectory.collectionSiteName+#bookDirectory.orgId")
    public CollectionSite findSiteByObj(BookDirectory bookDirectory){
        return bookDirectoryDao.findSiteByObj(bookDirectory);
    }

    /**
     * 复本明细统计查询
     * @param page 分页对象
     * @param bdCopy 书目复本对象
     * @return page
     */
    public Page<BookDirectoryCopy> findCopyDetail(Page<BookDirectoryCopy> page,BookDirectoryCopy bdCopy){
        bdCopy.setOrgId(UserUtils.getOrgId());
        bdCopy.setPage(page);
        page.setList(dao.findCopyDetail(bdCopy));
        return page;
    }

    public BookDirectoryCopy countCirculate(BookDirectoryCopy bdCopy) {
        bdCopy.setOrgId(UserUtils.getOrgId());
        return dao.countCirculate(bdCopy);
    }
}