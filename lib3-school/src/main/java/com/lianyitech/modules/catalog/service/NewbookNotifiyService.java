/**
 *
 */
package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.circulate.web.SendIndustryHttp;
import com.lianyitech.modules.catalog.dao.NewbookNotifiyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.NewbookNotifiy;
import com.lianyitech.modules.catalog.entity.NewbookNotifiyDetail;
import com.lianyitech.modules.lib.entity.WaitingHandle;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 新书通报管理Service
 *
 * @author zengzy
 * @version 2016-08-26
 */
@Service
public class NewbookNotifiyService extends CrudService<NewbookNotifiyDao, NewbookNotifiy> {


    @Autowired
    private NewbookNotifiyDao dao;

    public List<NewbookNotifiy> findList(NewbookNotifiy newbookNotifiy) {
        newbookNotifiy.setOrgId(UserUtils.getOrgId());
        return super.findList(newbookNotifiy);
    }

    public Page<NewbookNotifiy> findPage(Page<NewbookNotifiy> page, NewbookNotifiy newbookNotifiy) {
        newbookNotifiy.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, newbookNotifiy);
    }

    public Page<NewbookNotifiy> findNewbookList(Page<NewbookNotifiy> page, NewbookNotifiy newbookNotifiy) {
        newbookNotifiy.setOrgId(UserUtils.getOrgId());
        newbookNotifiy.setPage(page);
        page.setList(dao.findNewbookList(newbookNotifiy));
        return page;
    }

    public List<NewbookNotifiy> exportNewBooksReports(NewbookNotifiy newbookNotifiy) {
        newbookNotifiy.setOrgId(UserUtils.getOrgId());
        List<NewbookNotifiy> list = dao.findNewbookList(newbookNotifiy);
        return list;
    }

    public int countByName (NewbookNotifiy newbookNotifiy){
         newbookNotifiy.setOrgId(UserUtils.getOrgId());
        return dao.countByName(newbookNotifiy);
    }

    @Transactional
    public void save(NewbookNotifiy newbookNotifiy) {
        newbookNotifiy.setOrgId(UserUtils.getOrgId());
        super.save(newbookNotifiy);
    }

    @Transactional
    public void delete(NewbookNotifiy newbookNotifiy) {
        super.delete(newbookNotifiy);
    }

    @Transactional
    public void deletes(String[] str) {
        if (str != null) {
            for (String a : str) {
                super.delete(dao.get(a));
            }
        }
    }

    @Transactional
    public void addNewBooksToReports(String[] str, String id) {
        try {
            if (str != null) {
                for (String a : str) {
                    NewbookNotifiyDetail newbookNotifiyDetail = new NewbookNotifiyDetail();
                    newbookNotifiyDetail.preInsert();
                    newbookNotifiyDetail.setCopyId(a);
                    newbookNotifiyDetail.setNewbookNotifiyId(id);
                    dao.insertNewbook(newbookNotifiyDetail);
                }
            }

//            Integer code = (Integer)sendIndustryHandle(id).get("code");  //暂时屏蔽，勿删
//            if ( ( code!=null )&&( code==0 ) ){
                //更新主表状态
                NewbookNotifiy newbookNotifiy = new NewbookNotifiy();
                newbookNotifiy.setId(id);
                newbookNotifiy.setStatus("1");
                dao.updateStatus(newbookNotifiy);
//            }

        } catch (Exception e) {
            logger.error("操作失败",e);
        }
    }

    public void preProcessing(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMdd");
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + t.format(new Date()) + ".xlsx");
        response.setBufferSize(2048);
    }

    private String getNewBookId(Copy copy) {
        String str = dao.getNewBookId(copy);
        return str;
    }

    /**
     * 发送新书通报到行管
     * @param id 新书通报的ID
     **/
    private HashMap<String, Object> sendIndustryHandle(String id) {
        HashMap<String, Object> result;

        NewbookNotifiy newbookNotifiy = dao.get(id);

        if (newbookNotifiy!=null){
            List<NewbookNotifiy> detailList = dao.findNewbookList(newbookNotifiy);

            WaitingHandle waitingHandle = new WaitingHandle();
            waitingHandle.setName(newbookNotifiy.getName());                 // 待办名称
            waitingHandle.setThemeDescribe(newbookNotifiy.getName());        // 主题描述
            waitingHandle.setOtherThemeDescribe("New");                      // 主题副描述
            waitingHandle.setWaitingType("0");                               // 类型  0公告通知   1待办事项
            waitingHandle.setUserType("1");                                  // 适用应用对象   0单管   1读者
            result = SendIndustryHttp.sendIndustryHandle(waitingHandle, detailList);
        }else{
            result = new HashMap<String, Object>(){{put("code", -1);put("desc", "没有找到该新书通报的记录！");}};
        }

        return result;
    }

}