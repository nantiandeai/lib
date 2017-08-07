/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 读者证管理DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface ReaderCardDao extends CrudDao<ReaderCard> {

    int updateCardStatus(Map map);//注销时更新status
    ReaderCard findByReaderId(Reader reader);//更具读者ID查找读者证
    ReaderCard findByCard(ReaderCard readerCard);
    int findCountByCard(Reader reader);//公用读者证是否唯一
    int findCountByNewCard(Reader reader);//读者换证新证号是否唯一
    int deleteReaderCard(ReaderCard readerCard);
    int updateStatusToLoss(ReaderCard readerCard);//改变读者证的状态挂失
    int updateStatusRemoveLoss(ReaderCard readerCard);//改变读者证的状态解挂
    int updateInvalidLog(ReaderCard readerCard);//将旧读者证的预约预借记录自动失效

    ReaderCard findReaderCardByCard(ReaderCard readerCard);
    List<String> findAll(Map<String,Object> param);
    void batchSave(List<ReaderCard> list);
    List<Reader> findRenewalPage(Reader reader);  //查找换证记录

    int updateStatus(CirculateDTO dto);
    List<String> findBarCode(Map<String,Object> param);
    int updateEndDate(ReaderCard readerCard);
    /**
     * 修改读者证押金
     * @param card
     * @param orgId
     * @param amount
     * @param updateDate
     * @return
     */
    int updateDeposit(@Param("card") String card, @Param("orgId") String orgId, @Param("amount") Double amount, @Param("updateDate") Date updateDate);

    /**
     * 暂只定时任务用到
     * 查询所有读者证过期但状态还是有效的数据
     * @return list
     */
    List<ReaderCard> findAllOverdueReaderCard();

    /**
     * 暂只定时任务用到
     * 修改读者证过期状态
     */
    void updateReaderCardOverdueStatus(ReaderCard readerCard);


}