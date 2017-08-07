package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.circulate.dao.SupplyCardDao;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.SupplyCard;
import com.lianyitech.modules.offline.utils.CustomException;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 读者证补缺服务层
 */
@Service
public class SupplyCardService extends CrudService<SupplyCardDao,SupplyCard> {
    @Autowired
    private SupplyCardDao supplyCardDao;
    public Page<SupplyCard> findPage(Page<SupplyCard> page,SupplyCard supplyCard){
        supplyCard.setCreateDate(new Date());
        supplyCard.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, supplyCard);
    }

    /**
     * 打印读者证
     * @param page
     * @param reader
     * @return
     */
    public Page<Reader> findReaderPage(Page<Reader> page,Reader reader){
        reader.setCreateDate(new Date());
        reader.setOrgId(UserUtils.getOrgId());
        reader.setPage(page);
        page.setList(dao.findReaderList(reader));
        return page;
    }

    /**
     * 验证
     * @param supplyCard
     * @return
     */
    public void check(SupplyCard supplyCard) throws CustomException{
        supplyCard.setCreateDate(new Date());
        supplyCard.setOrgId(UserUtils.getOrgId());
        SupplyCard card;
        if (StringUtils.isEmpty(supplyCard.getCard())) {
            throw new RuntimeException("请先输入查询条件！");
        } else {
            //验证读者证是否存在并且有效
            card = supplyCardDao.getReader(supplyCard);
        }
        if(card == null){
            throw new RuntimeException("该证号不存在!该姓名不存在!");
        }
        if(card.getStatus().equals("0")){
            int count = supplyCardDao.getCount(supplyCard);
            if(count>0){
                throw new CustomException("已经添加重复的读者证！");
            }
        }else{
            throw new RuntimeException("读者证不是有效状态!");
        }
    }
    public int saveSupplyCard(SupplyCard supplyCard){
        supplyCard.setCreateDate(new Date());
        supplyCard.setUpdateDate(new Date());
        supplyCard.setOrgId(UserUtils.getOrgId());
        supplyCard.setId(IdGen.uuid());
        return supplyCardDao.insertFromReader(supplyCard);
    }
    /**
     * 清空列表
     * @param supplyCard 传参实体
     * @throws Exception Exception
     */
    public void deleteAll(SupplyCard supplyCard) throws Exception {
        supplyCard.setOrgId(UserUtils.getOrgId());
        supplyCard.setCreateDate(new Date());
        supplyCard.setUpdateDate(new Date());
        supplyCardDao.deleteAll(supplyCard);
    }

}
