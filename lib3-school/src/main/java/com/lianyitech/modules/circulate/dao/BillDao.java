package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 操作单据管理DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface BillDao extends CrudDao<Bill> {

    /**
     * 创建图书单据
     * @param dto  dto
     */
    void create(CirculateDTO dto);

    /**
     * 创建期刊单据
     * @param dto  dto
     */
    void createPeri(CirculateDTO dto);

    /**
     * 创建借阅日志
     * @param dto  dto
     */
    void createlog(CirculateLogDTO dto);

    /**
     * 修改图书单据
     * @param dto  dto
     */
    void modify(CirculateDTO dto);

    /**
     * 修改期刊单据
     * @param dto  dto
     */
    void modifyPeri(CirculateDTO dto);

    void modifyBill(Bill bill);

    Map<String, Object> findBookByBarCode(Copy copy);

    //根据复本id查询归属的读者信息
    Map<String, Object> findReaderInfo(@Param( value= "copyId") String copyId,@Param( value= "orgId") String orgId);

    Map<String, Object> findCode(Bill bill);

    //根据若干条件查询最新的单据(条形码、读者卡、单据类型或者单据状态)
    Bill findLastByCon(CirculateDTO dto);

    /**
     * 查找预约/借的失效单据
     * @return list
     */
    List<CirculateDTO> findOverduePreBorrow();

    /**
     * 查找借出未还的超期单据
     * @return list
     */
    List<BillOverdueRecall> findRecallorOverdue();

    int updateRecallDate(BillOverdueRecall billOverdueRecall);

    /**
     * 查询有没有流通记录
     * @param reader reader
     * @return list
     */
    List<Bill> findCirculateLogByReader(Reader reader);

    /**
     * 根据条件查询是否存在流通
     * @param bill bill
     * @return list
     */
    List<Bill> findCirculateByReader(Bill bill);

    /**
     * 查询读者流通信息
     * @param readerCard readerCard
     * @return ReaderCirculateInfo
     */
    ReaderCirculateInfo findReaderCirculateInfo(ReaderCard readerCard);

    /**
     * 查询读者借的书的信息
     * @param reader  reader
     * @return list
     */
    List<ReaderUnionBook> findBorrowByBill(Reader reader);

    /**
     * 查询图书流通记录
     * @param dto dto
     * @return list
     */
    List<CirculateLogDTO> findBookCirculates(CirculateLogDTO dto);


	//查询读者借了几本书
    int countReaderBorrow(Reader reader);//传个读者ID
    //根据读者证找读者
    Reader findReaderByCard(ReaderCard readerCard);
	
    /**
     * 查询期刊流通记录
     * @param dto dto
     * @return list
     */
    List<CirculateLogDTO> findPeriCirculates(CirculateLogDTO dto);

    int countExceed(Bill bill);

    /**
     * 预约预借
     * @param clDto clDto
     * @return list
     */
    List<CirculateLogDTO> findOrderBorrow(CirculateLogDTO clDto);

    /**
     * 根据条形码查期刊复本
     * @param copy  copy
     * @return map
     */
    Map<String,Object> findPeriCopyByBarcode(Copy copy);

    /**
     * 已借未还
     * @param clDto clDto
     * @return list
     */
    List<CirculateLogDTO> listBorrowing(CirculateLogDTO clDto);

    void updateBillGroup(@Param("groupId")String groupId,@Param("readerId")String readerId,@Param("orgId")String orgId);

    /**
     * 取消合订期刊时条码是否流通过
     * @param map 参数
     * @return list
     */
    List<String> periBarcode(Map map);

    Integer borrowDay (@Param("orgId")String orgId,@Param("barcode")String barcode);

    Bill queryPastDay(String id);
}