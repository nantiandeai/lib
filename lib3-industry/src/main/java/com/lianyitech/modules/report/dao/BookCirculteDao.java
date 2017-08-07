/**
 * 
 */
package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.BookCirculte;


import java.util.List;

/**
 * 藏书流通有关统计报表（生均借阅统计和读者借阅率统计、图书流通率统计）Dao
 * @author luzhihuai
 * @version 2016-10-28
 */
@MyBatisDao
public interface BookCirculteDao extends CrudDao<BookCirculte> {

    List<BookCirculte> listStuStatistics(BookCirculte bookCirculte);

    List<BookCirculte> findByBookCirculte(BookCirculte bookCirculte );

    List<BookCirculte> readStatistics(BookCirculte bookCirculte);

    List<BookCirculte> bookCirStatistic(BookCirculte bookCirculte);

    List<BookCirculte> circulationRate(BookCirculte bookCirculte);

}