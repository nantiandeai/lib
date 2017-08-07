/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Rule;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 借阅规则管理DAO接口
 * @author zengzy
 * @version 2016-09-07
 */
@MyBatisDao
public interface RuleDao extends CrudDao<Rule> {

    HashMap<String,Object> findRuleOperateCountByCard(CirculateDTO dto);

    //默认借阅规则
    List<Rule> defaultRule(Rule rule);

    //机构借阅规则
    Rule orgRule(Rule rule);

    /**
     * 根据机构id，读者证号查询对应的借阅规则id,没配置的返回默认借阅规则(org_id=null)
     * @param orgId
     * @param card
     * @return
     */
    String findRuleIdByCard(@Param(value="orgId")String orgId, @Param(value="card") String card);

    Integer shortDay(@Param(value="orgId")String orgId, @Param(value="barcode") String barcode);
}