/**
 *
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.sys.dao.CollectionSiteDao;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 馆藏地管理Service
 *
 * @author zengzy
 * @version 2016-09-02
 */
@Service
public class CollectionSiteService extends CrudService<CollectionSiteDao, CollectionSite> {
    @Autowired
    private CopyDao copyDao;

    public void setDao(CollectionSiteDao dao) {
        this.dao = dao;
    }

    public CollectionSite get(String id) {
        return super.get(id);
    }

    public List<CollectionSite> findList(CollectionSite collectionSite) {
        collectionSite.setOrgId(UserUtils.getOrgId());
        return super.findList(collectionSite);
    }

    public Page<CollectionSite> findPage(Page<CollectionSite> page, CollectionSite collectionSite) {
        collectionSite.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, collectionSite);
    }

    @Transactional
    public void save(CollectionSite collectionSite) {
        if (StringUtils.isEmpty(collectionSite.getStockAttr())) {
            collectionSite.setStockAttr("1");
        }
        collectionSite.setOrgId(UserUtils.getOrgId());
        if ("0".equals(collectionSite.getStatus())) {//如果点击了默认，则先把以前的默认改咯
            collectionSite.setOrgId(UserUtils.getOrgId());
            dao.updateStatus(collectionSite);
        }
        super.save(collectionSite);
    }

    @Transactional
    public void delete(CollectionSite collectionSite) {
        super.delete(collectionSite);
    }

    /**
     * 批量修改
     *
     * @param collectionSite //根据传来的结合
     */
    @Transactional
    public String updatecollectionsite(CollectionSite collectionSite) {
        if (collectionSite != null) {
            String[] idList = collectionSite.getId().split(",");
            if("0".equals(collectionSite.getStockAttr())){//修改属性为不可外借
                for (String id : idList) {
                        //判断复本是否被借出
                        Copy copy = new Copy();
                        copy.setCollectionSiteId(id);
                        copy.setStatus(EnumLibStoreStatus.OUT_LIB.getValue());//借出
                        copy.setOrgId(UserUtils.getOrgId());
                        int count = copyDao.getCount(copy);
                        if (count > 0) {
                            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//直接下面抛出异常即可回滚
                            throw new RuntimeException("馆藏地中有图书未还，暂不能批量修改库存属性！");
                        }
                }
            }
            collectionSite.preUpdate();
            collectionSite.setIdList(idList);
            collectionSite.setOrgId(UserUtils.getOrgId());
            dao.updateAll(collectionSite);
        }
        return "";
    }

    /**
     * @param collectionSite 馆藏实体
     * @return // 返回馆藏地名
     */
    public int getName(CollectionSite collectionSite) {
        collectionSite.setOrgId(UserUtils.getOrgId());
        return dao.getName(collectionSite);
    }

    public Map deleteCopy(String ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] id = ids.split(",");
            for (String aa : id) {
                CollectionSite c = new CollectionSite();
                c.setId(aa);
                int count = dao.findCopy(c);
                if (count > 0) {
                    result.put("fail", "您所选的馆藏地中包含图书信息或者期刊信息，无法删除。");
                    break;
                }
            }
            if (!result.containsKey("fail")) {
                this.delete(ids);
                result.put("success", "删除成功!");
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            result.put("fail", "删除失败!");
        }
        return result;
    }
}