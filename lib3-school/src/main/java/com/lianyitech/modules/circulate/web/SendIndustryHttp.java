package com.lianyitech.modules.circulate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.JsonMapper;
import com.lianyitech.modules.lib.entity.WaitingHandle;

import java.util.HashMap;

/**
 * send http 行管代办接口
 * @author jordan jiang
 * @version 2016/9/26
 */
public class SendIndustryHttp {

    public static HashMap<String, Object> sendIndustryHandle(WaitingHandle waitingHandle,Object dealData){
        HashMap<String, Object> result;

        ObjectMapper mapper = JsonMapper.getInstance();
        String jsonData;
        try {
            jsonData = mapper.writeValueAsString(dealData);
        } catch (final Exception e) {
            jsonData = null;
            new HashMap<String, Object>(){{put("code", -1);put("desc", e.getMessage());}};
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("name",waitingHandle.getName());		                         // 待办名称
        param.put("themeDescribe",waitingHandle.getThemeDescribe());		     // 主题描述
        param.put("otherThemeDescribe",waitingHandle.getOtherThemeDescribe());   // 主题副描述
        param.put("waitingType",waitingHandle.getWaitingType());	             // 类型  0公告通知   1待办事项
        param.put("userType",waitingHandle.getUserType());		                 // 适用应用对象   0单管   1读者
        param.put("dealData",jsonData);

        String industryUrl = Global.getConfig("industry.url");
        result = HttpSender.sendForm(industryUrl + "/api/lib/waitingHandle",param);

        return result;
    }
}
