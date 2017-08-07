/**
 *
 */
package com.lianyitech.modules.lib.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.circulate.web.SendIndustryHttp;
import com.lianyitech.modules.catalog.service.ImportTemplateService;
import com.lianyitech.modules.lib.dao.NoticeManageDao;
import com.lianyitech.modules.lib.entity.NoticeManage;
import com.lianyitech.modules.lib.entity.WaitingHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 公共通知Service
 *
 * @author zengzy
 * @version 2016-09-09
 */
@Service
public class NoticeManageService extends CrudService<NoticeManageDao, NoticeManage> {

    public NoticeManage get(String id) {
        return super.get(id);
    }

    public List<NoticeManage> findList(NoticeManage noticeManage) {
        return super.findList(noticeManage);
    }

    public Page<NoticeManage> findPage(Page<NoticeManage> page, NoticeManage noticeManage) {
        return super.findPage(page, noticeManage);
    }

    @Transactional
    public void save(NoticeManage noticeManage) {
        super.save(noticeManage);
        //sendIndustry(noticeManage);//暂时屏蔽。。为了演示
    }

    @Transactional
    public int delete(String ids) {
        return super.delete(ids);
    }

    private NoticeManage findIsTop() {
        return dao.findIsTop();
    }

    public void UpSite(NoticeManage noticeManage) {
        if (noticeManage.getIsTop() != null && noticeManage.getIsTop().equals("0")) {
            NoticeManage noticeManage1 = findIsTop();
            if (noticeManage1 != null) {
                noticeManage1.setIsTop("1");
                super.save(noticeManage1);
            }
        }
        super.save(noticeManage);

    }

    private HashMap<String, Object> sendIndustry(NoticeManage noticeManage) {
        HashMap<String, Object> result;

        WaitingHandle waitingHandle = new WaitingHandle();
        waitingHandle.setName(noticeManage.getTheme());               // 待办名称
        waitingHandle.setThemeDescribe(noticeManage.getContent());    // 主题描述
        waitingHandle.setOtherThemeDescribe("New");                      // 主题副描述
        waitingHandle.setWaitingType("0");                              // 类型  0公告通知   1待办事项
        waitingHandle.setUserType("1");                                  // 适用应用对象   0单管   1读者

        result = SendIndustryHttp.sendIndustryHandle(waitingHandle, noticeManage.getContent());

        return result;
    }
}