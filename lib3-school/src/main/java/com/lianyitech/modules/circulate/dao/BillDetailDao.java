/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.*;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

/**
 * 单据明细管理DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface BillDetailDao extends CrudDao<BillDetail> {

    void create(Map<String, String> param);

    //查询读者借了几本书
    int countReaderBorrow(Reader reader);//传个读者ID
    //根据读者证找读者
    Reader findReaderByCard(ReaderCard readerCard);
    //查询读者借的书的信息
    List<ReaderUnionBook> findBorrowByBill(Reader reader);
    //查询所有流通记录
    List<CirculateLogDTO> findAllCirculate(CirculateLogDTO clDto);




}