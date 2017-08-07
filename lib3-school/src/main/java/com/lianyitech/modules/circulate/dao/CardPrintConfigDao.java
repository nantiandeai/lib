/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.CardPrintConfig;
import com.lianyitech.modules.circulate.entity.Group;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 读者证打印设置DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface CardPrintConfigDao extends CrudDao<CardPrintConfig> {

}