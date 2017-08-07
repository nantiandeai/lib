/**
 *
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.Batch;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.LibraryCopy;
import com.lianyitech.modules.sys.entity.CollectionSite;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 馆藏复本管理DAO接口
 * @author zengzy
 * @version 2016-08-26
 */
@MyBatisDao
public interface CopyDao extends CrudDao<Copy> {

    List<Copy> findCopyByBatch(Batch batch);

    List<LibraryCopy> findLibraryCopyList(LibraryCopy lc);

    /**
     * 绑定批次下拉框用
     * @param type
     * @param orgId
     * @return
     */
    List<Batch> getAllBatchNo(@Param("type") String type, @Param("orgId") String orgId);

    /**
     * 绑定馆藏地下拉框
     * @param sdf
     * @return
     */
    List<CollectionSite> getAllSiteName(Map<String,String> sdf);

    List<Copy> findAllList(Copy copy);

    int getCount(Copy copy);

    /**
     * 根据id修改复本馆藏地
     * @param lc 复本实体类
     * @return int 修改结果条数
     */
    int updateCopySiteByIds(Copy lc);
    /**
     * 一键修改本机构下所选条件的的复本馆藏地
     */
    int updateCopySiteByCon(Copy lc);
    /**
     * 新书通报查询
     * @param copy 复本信息
     * @return list 复本集合
     */
    List<Copy> newbookReportList(Copy copy);

    /**
     * 丢失，剔旧，报废清单查询
     * @param copy 复本信息
     * @return list 复本集合
     */
    List<Copy> findScrapList(Copy copy);

    List<Copy> findListByPage(Copy copy);

	/**
     * 根据条形码获取单个对象
     * @param copy 副本信息（需要跟机构关联代码修改过）
     * @return copy 副本信息
     */
    Copy findByBarCode(Copy copy);

    /**
     * 根据条形码获取复本状态等信息
     * @param orgId
     * @param barcode
     * @return
     */
    Map<String,String> findStatusByBarCode(@Param( value= "orgId") String orgId, @Param(value = "barcode") String barcode);

    void updateByBillType(Map<String, Object> param);

    /**
     * 判断复本信息下是否有流通记录
     * @param map 复本ids
     * @return 记录条数
     */
    int checkBillByCopy(Map map);

    //判断复本是否已借出
    String findStatusAndStockById(LibraryCopy libraryCopy);

    //条码置换
    int updateBarcode(LibraryCopy libraryCopy);

    //根据条码查出当条复本信息
    List<LibraryCopy> getCopy(@Param("oldBarcode") String oldBarcode,@Param("orgId") String orgId);

    //丢失返还，修改状态为在馆
    int returnLossCopy(Copy lc);

    /**
     * 条码查缺
     * @param libraryCopy 复本对象
     * @return list
     */
    List<String> checkBarcode(LibraryCopy libraryCopy);

    /**
     * 根据条码查询复本信息（查缺打印）
     * @param copy 复本实体
     * @return copy
     */
    Copy getCopyByBarcode(Copy copy);

    

   //看书目信息是否被删除
   int countBookDirectory(LibraryCopy libraryCopy);

    /**
     * 查询图书相关信息(流通模块查询书目信息)
     * @param orgId
     * @param barcode
     * @return
     */
    Map<String, Object> findBookByBarCode(@Param( value= "orgId") String orgId, @Param(value = "barcode") String barcode);

    /**
     * 根据书目id获得其下复本信息的条形码集合
     * @param bookDirectory
     * @return
     */
    List<String> listBarcode(BookDirectory bookDirectory);
}