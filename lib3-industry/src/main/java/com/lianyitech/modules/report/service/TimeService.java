package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.BookCirculteDao;
import com.lianyitech.modules.report.entity.BookCirculte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yangkai on 2016/11/1.
 */
@Service

public class TimeService extends CrudService<BookCirculteDao,BookCirculte> {

    public List<BookCirculte> findByBookCirculte(BookCirculte bookCirculte) {
        return dao.findByBookCirculte(bookCirculte);
    }


}
