/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.Group;
import com.lianyitech.modules.circulate.entity.Reader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 读者组织管理DAO接口
 * @author zengzy
 * @version 2016-09-19
 */
@MyBatisDao
public interface GroupDao extends CrudDao<Group> {

    int isSameGroup(Group group);//判断组织是否唯一

    Group findCountByName(@Param(value = "groupName") String groupName,@Param(value = "orgId") String orgId);

    List<Reader> findReaderByGroup(Group group);
    //根据组织ids 查询所有读者
    List<Reader> findReaderByGroups(Group group);
    int logOutGroup(Group group);//注销组织
    int logOutGroups(Group group);//批量注销注销组织
    List<Group> findAllGroup();
    void batchSave(List<Group> list);
}