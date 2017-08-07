/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.circulate.entity.Group;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 读者管理DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface ReaderDao extends CrudDao<Reader> {
    Reader findByCard(Map<String,String> map);
    List<Reader> findList(Reader reader);
    int logOutReaderCard(Reader reader);//注销读者证
    int regainReaderCard(Reader reader);//恢复
    Group findGroupByReader(Reader reader);//根据读者查询其组织
    ReaderCard findCardByReader(Reader reader);//根据读者找读者证
    int checkCard(Reader reader);//读者证是否唯一
    int deleteReader(Reader reader);
    List <Reader> findBorrowReader(Reader reader);
    void batchSave(List<Reader> list);
    Reader queryByReaderPlatfrom(Reader reader);

    void createTableTemp(String tableName);
    void dropTableTemp(String tableName);
    void batchSaveTemp(@Param("list")List<Reader> list, @Param("tableName")String tableName);
    void insertNotExistsGroup(@Param("tableName")String tableName);
    void updateGroupInfoTemp(@Param("tableName")String tableName);
    void checkGroupInfoTemp(@Param("tableName")String tableName);
    void checkReaderCardTemp(@Param("orgId")String orgId, @Param("tableName")String tableName);
    void insertNotExistsReaderCard(@Param("tableName")String tableName);
    int insertReaderFromTemp(@Param("updateDate") Date updateDate, @Param("tableName")String tableName);
    List <Reader> findErrorListTemp(@Param("tableName")String tableName);
    int updateTypeGroup(Reader reader);
    int updateTempByItself(@Param("tableName")String tableName);
    Map<String,Object> findReaderInfoByCard(@Param("card")String card,@Param("orgId")String orgId);
    void updateCardImg(Reader reader);
}