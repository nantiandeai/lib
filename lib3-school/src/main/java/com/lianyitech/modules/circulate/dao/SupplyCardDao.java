package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.SupplyCard;

import java.util.List;

/**
 * 读者证补缺打印dao
 */
@MyBatisDao
public interface SupplyCardDao extends CrudDao<SupplyCard> {
    int insertFromReader(SupplyCard supplyCard);
    int deleteAll(SupplyCard supplyCard);
    SupplyCard getReader(SupplyCard supplyCard);
    Integer getCount(SupplyCard supplyCard);
    List<Reader> findReaderList(Reader reader);
}
