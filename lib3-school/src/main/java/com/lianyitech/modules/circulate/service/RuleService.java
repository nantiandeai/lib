package com.lianyitech.modules.circulate.service;


import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.dao.ReaderDao;
import com.lianyitech.modules.circulate.dao.RuleDao;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.Rule;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 借阅规则管理Service
 *
 * @author zengzy
 * @version 2016-09-07
 */
@Service

public class RuleService extends CrudService<RuleDao, Rule> {
    @Autowired
    private RuleDao ruleDao;

    @Autowired
    ReaderDao readerDao ;

    public Page<Rule> findPage(Page<Rule> page, Rule rule) {
        rule.setOrgId(UserUtils.getOrgId());
        List<Rule> list = new ArrayList<>();
        List<Rule> defaultList = ruleDao.defaultRule(new Rule());
        for (Rule rule1 : defaultList) {
            rule1.setOrgId(UserUtils.getOrgId());
            rule.setReaderType(rule1.getReaderType());
            Rule r = ruleDao.orgRule(rule);
            if (r==null){
                list.add(rule1);
            }else{
                list.add(r);
            }
        }
        page.setList(list);
        return page;
    }

    /**
     * 根据读者证号查询借阅规则
     * @param readerId
     * @return
     */
    public Rule findRuleByReader(String readerId) {
        Rule rule = null;
        Reader paramsReader = new Reader();
        paramsReader.setId(readerId);
        paramsReader.setOrgId(UserUtils.getOrgId());
        Reader reader = readerDao.get(paramsReader);
        Rule paramRule = new Rule();
        paramRule.setReaderType(reader.getReaderType());
        paramRule.setOrgId(reader.getOrgId());
        rule = ruleDao.orgRule(paramRule);
        if(rule==null) {
            rule = ruleDao.defaultRule(paramRule).get(0);
        }
        return rule;
    }

    @Transactional
    public void save(Rule rule) {
        if (ruleDao.orgRule(rule)==null){
            rule.preInsert();
            ruleDao.insert(rule);
        }else{
            rule.preUpdate();
            ruleDao.update(rule);
        }
    }

    HashMap<String, Object> findRuleOperateCountByCard(CirculateDTO dto) {
        dto.setRuleId(dao.findRuleIdByCard(dto.getOrgId(),dto.getCard()));
        return dao.findRuleOperateCountByCard(dto);
    }

    public Integer shortDay (Copy copy) {
        return dao.shortDay(copy.getOrgId(),copy.getCard());
    }

}