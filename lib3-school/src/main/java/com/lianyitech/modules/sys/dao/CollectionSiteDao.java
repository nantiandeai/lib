/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 馆藏地管理DAO接口
 * @author zengzy
 * @version 2016-09-02
 */
@MyBatisDao
public interface CollectionSiteDao extends CrudDao<CollectionSite> {


     int getName(CollectionSite collectionSite);

     int findCopy(CollectionSite collectionSite);

     String findIdByName(@Param("name")String name,@Param("orgId")String orgId);

     int insertCollectionSite(@Param("orgId")String orgId,@Param("tableName")String tableName);

     int updateStatus(CollectionSite collectionSite);

     int updateAll(CollectionSite collectionSite);
}