/**
 * 
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.BookDirectoryForImport;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.sys.entity.CollectionSite;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 书目管理DAO接口
 * @author zengzy
 * @version 2016-08-26
 */
@MyBatisDao
public interface BookDirectoryDao extends CrudDao<BookDirectory> {
    /**
     * 更新数据
     * @param entity
     * @return
     */
    public int getCountByCon(BookDirectory entity);

    int getCount(BookDirectory entity);

    /*
    *根据分类号等条件得到查询最大的种次号
    **/
    public int getMaxTanejiNo(BookDirectory entity);
    /*
    根据分类号和机构id等条件查询已有的种次号
    * */
    public List<String> getTaneJINo(BookDirectory entity);
    /*
    * 根据id修改马克数据
    * */
    public void updateMarc(BookDirectory entity);
    /*
    * 根据书目条件查询书目复本数
    * */
    public int getBookNumber(BookDirectory entity);

    /**
     * 根据馆藏地名称查询出馆藏地id
     * @param collectionSiteName 馆藏地名称
     * @return 馆藏地对象
     * DSF加上orgId
     */
    public CollectionSite findSite(String collectionSiteName,String orgId);


    public CollectionSite findSiteByObj(BookDirectory bookDirectory);


    public void batchinsert(List<BookDirectory> list);

    public void batchInsertCopyList(List<Copy> list);


    BookDirectory findId(BookDirectory bookDirectory);

    public List<BookDirectory> queryAllDirectory(String orgId);
    List<BookDirectory> getDirectoryByCopyIds(Map<String,Object> map);
    void batchSaveTemp(@Param("list")List<BookDirectoryForImport> list,@Param("tableName")String tableName);

    void batchSaveCopyTemp(@Param("list")List<Copy> list,@Param("tableName")String tableName);

    void dropTempTable(String tableName);
    void createBookTemp(String tableName);
    void createCopyTemp(String tableName);
    /**
     * 根据临时表自身过滤重复的条形码
     * @param tableName 临时表名称
     * @return int 修改条数
     */
    int updateTempByItself(@Param("tableName")String tableName);

    /**
     * *根据其他表过来重复的条形码
     * @param orgId 机构id
     * @param tableName 临时表名称
     * @return int 修改条数
     */
    int updateTempByOther(@Param("orgId")String orgId,@Param("tableName")String tableName);
    int insertBook(@Param("orgId")String orgId,@Param("tableName")String tableName);
    void updateTempBatch(@Param("orgId")String orgId,@Param("tableName")String tableName);
    void updateTempSite(@Param("orgId")String orgId,@Param("tableName")String tableName);
    void updateBookTemp(@Param("orgId")String orgId,@Param("tableName")String tableName);
    void updateBookTempByRule(@Param("orgId")String orgId,@Param("tableName")String tableName);
    int insertCopy(@Param("orgId")String orgId, @Param("updateDate") Date updateDate, @Param("copyTemp")String copyTemp, @Param("bookTemp")String bookTemp);
    List<BookDirectoryForImport> findErrorListTemp(@Param("bookTemp")String bookTemp, @Param("copyTemp")String copyTemp);

    /**
     * 修改(书目对应的复本所有的条形码验证不通过的)书目为不匹配
     * @param copyTemp 馆藏临时表
     * @param bookTemp 书目临时表
     */
    void updateNotMatchBookTemp(@Param("copyTemp")String copyTemp,@Param("bookTemp")String bookTemp);

    BookDirectory getBookDirectory(BookDirectory bookDirectory);
}