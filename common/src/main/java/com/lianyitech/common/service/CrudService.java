/**
 *
 */
package com.lianyitech.common.service;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.AbstractDataEntity;
import com.lianyitech.common.persistence.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service基类
 *
 * @version 2014-05-16
 */

public abstract class CrudService<D extends CrudDao<T>, T extends AbstractDataEntity<T>>{
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public T get(String id) {
        return dao.get(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    /**
     * 查询分页数据
     *
     * @param page   分页对象
     * @param entity
     * @return
     */
    public Page<T> findPage(Page<T> page, T entity) {
        entity.setPage(page);
        page.setList(dao.findList(entity));
        return page;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     */
    @Transactional
    public void save(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            dao.insert(entity);
        } else {
            entity.preUpdate();
            dao.update(entity);
        }
    }

    /**
     * 删除数据
     *
     * @param entity
     */
    @Transactional
    public void delete(T entity) {
        dao.delete(entity);
    }


    /**
     * 批量删除 多个id逗号隔开
     *
     * @param ids
     */
    @Transactional
    public int delete(String ids) {
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("DEL_FLAG_DELETE", "1");
        map.put("updateDate",new Date());
        return dao.delete(map);
    }

    public static void main(String[] args) {
        String a = "abc123";
        StringBuilder abc  = new StringBuilder();
        abc.append(a);
        abc.append("|");
        System.out.println("abc="+abc.toString());
        System.out.println("abc.substring(abc.length())="+abc.substring(abc.length()));
        System.out.println("abc.substring(0,abc.length()-1)="+abc.substring(0,abc.length()-1));
    }

}
