/**
 *
 */
package com.lianyitech.modules.circulate.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.dao.ReaderDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.circulate.entity.Group;
import com.lianyitech.modules.circulate.dao.GroupDao;

/**
 * 读者组织管理Service
 *
 * @author zengzy
 * @version 2016-09-19
 */
@Service
public class GroupService extends CrudService<GroupDao, Group> {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ReaderDao readerDao;
    @Autowired
    private ReaderCardDao readerCardDao;

    public Group get(String id) {
        return super.get(id);
    }

    public Page<Group> findPage(Page<Group> page, Group group) {
        //如果选择集体，传其类型为学生
        if(Objects.equals(group.getGroupType(),"3")){
            group.setGroupType("0");
        }
        group.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, group);
    }

    void batchSave(List<Group> list){
        dao.batchSave(list);
    }

    public void save(Group group) {

        String orgId = UserUtils.getOrgId();
        String id=group.getId();
        String groupType=group.getGroupType();
        String status="0";

        String[] names = group.getName().split(",");
        for (String name : names) {
            Group g = new Group();
            if(StringUtils.isNotEmpty(id)){
                g.setId(group.getId());
            }
            g.setName(name);
            g.setGroupType(groupType);
            g.setStatus(status);
            g.setOrgId(orgId);
            super.save(g);
        }
    }

    @Transactional
    public void saveSuper(Group group) {
        group.setOrgId(UserUtils.getOrgId());
        super.save(group);
    }

    @Transactional
    public int delete(String ids) {
        String[] idArr = ids.split(",");
        //只判断了流通记录，并没有判断是否登录条件
        for (String string : idArr) {
            Group group = new Group();
            group.setId(string);
            group.setOrgId(UserUtils.getOrgId());
            List<Reader> readerList = groupDao.findReaderByGroup(group);
            for (Reader reader : readerList) {
                List<Bill> billList = readerService.findCirculateLogByReader(reader);
                if (billList.size() != 0) {//有流通记录
                    return -1;
                }
                reader.setOrgId(UserUtils.getOrgId());
                ReaderCard readerCard = readerDao.findCardByReader(reader);
                if(readerCard.getDeposit()>0){
                    return -2;
                }
                readerService.deleteReader(reader.getId());//同时删除组织下面的读者

            }

        }
        return super.delete(ids);
    }

    /**
     * 判断组织是否唯一
     *
     * @param group 组织名字
     * @return 如果为真说明有同名，反之
     */
    public boolean isSameGroup(Group group) {
        group.setOrgId(UserUtils.getOrgId());
        return  groupDao.isSameGroup(group)>0;//groupDao.isSameGroup(group)>0 ? true : false;(你这代码简直就是画蛇添足)
    }

    public Group findCountByName(String groupName) {
        return dao.findCountByName(groupName,UserUtils.getOrgId());
    }


    //根据组织id得到该组织下面存在有借无还书的读者名称并且将组织名称带上（最多显示5个）
    private  List<Reader> getBorrowReaders(String groupIds){//目前改成in 查询
        //根据读者组织查询该组织下面是否存在有借无还的读者信息
        Reader r = new Reader();
        r.setGroupId(groupIds);
        r.setOrgId(UserUtils.getOrgId());
        return readerDao.findBorrowReader(r);
    }
    //根据读者集合返回名称
    private String return_msg(List<Reader> borrow_readers){
        StringBuilder msg = new StringBuilder();
        int num = 0;
        for (Reader reader : borrow_readers) {
             if(num>5){
                 return msg.toString();
             }
            msg.append(reader.getGroupName()+":"+reader.getName()+",");
            num++;
        }
        return msg.toString();
    }
    @Transactional
    public String logOutGroup(String groups) {
        String[] groupArr = groups.split(",");//一般情况下不会批量处理1000条，这里就不做此验证 （sql 用 in 语句不能超过1000）
        String in_gid ="'"+StringUtils.join(groupArr,"','")+"'";//'1','2'
        //第一步验证
        List<Reader> borrow_readers = this.getBorrowReaders(in_gid);
        if (borrow_readers != null && borrow_readers.size() > 0) {
            return this.return_msg(borrow_readers)+"借阅的图书未归还，注销失败";
        }
        //第二步：如果验证通过的情况下，需要将组织下面的读者进行注销
        Group group = new Group();
        group.setId(in_gid);
        group.setOrgId(UserUtils.getOrgId());
        List<Reader> reader_List = groupDao.findReaderByGroups(group);
        //第三步：这里需要调读者注销方法（如果存在预约预借的情况下还需要自动取消的）
        if(reader_List!=null && reader_List.size()>0){
            for (Reader reader : reader_List) {
                try {
                    reader.setUpdateDate(new Date());
                    readerService.logOutReader(reader);//注销(包括自动取消预约预借)
                } catch (Exception e) {
                    throw new RuntimeException("组织注销失败！");
                }
            }
        }
        //第四步：注销组织
        group.setUpdateDate(new Date());
        groupDao.logOutGroups(group);
        return "";
    }

}
