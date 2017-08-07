package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.BookDirectoryCopy;

import java.util.List;
import java.util.Map;

/*
 * Created by chenxiaoding on 2016/11/4.
 */
@MyBatisDao
public interface BookDirectoryCopyDao extends CrudDao<BookDirectoryCopy>{

    List<BookDirectoryCopy> findAllDirectoryCopy(BookDirectoryCopy bdCopy);

    //复本统计导出
    List<BookDirectoryCopy> findCopyStatForExport(BookDirectoryCopy bdCopy);

    List<BookDirectoryCopy> findCopyDetail(BookDirectoryCopy bdCopy);

    BookDirectoryCopy countCirculate(BookDirectoryCopy bdCopy);

}
