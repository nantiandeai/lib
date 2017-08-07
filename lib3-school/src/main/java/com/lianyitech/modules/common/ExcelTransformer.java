package com.lianyitech.modules.common;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.entity.CirculateLogDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.report.entity.CollectBookStatistics;

import java.util.List;

/**
 * Created by zengyuanmei on 2016/11/12
 */
public class ExcelTransformer {
    public Integer listIndex(List<Object> list , Object o){
       int index = list.indexOf(o);
        return index;
    }

    public static String isSite(CollectBookStatistics y){
        if("".equals(y.getSiteName())&& y.getSiteName()==null) {
            return "-";
        }else {
            return y.getSiteName();
        }
    }
    /**
     * 出版社日期格式转换(目前只考虑几种（.\/年月）)
     */
    public static String switch_date(Object publishingTime,int num){
        if (publishingTime != null && !"".equals(publishingTime)) {
            String regex = "[\\.年月日\\\\/]+";
            String result = publishingTime.toString().replaceAll(regex,"-");
            if (result.length() > 4 && !result.contains("-")) {
                if(result.length()>=6){
                    result = result.substring(0, 4) + "-" + result.substring(4, 6)+"-"+result.substring(6,result.length());
                }else {
                    result = result.substring(0, 4) + "-" + result.substring(4, result.length());
                }
            }
            String[] arr = result.split("-");
            if(arr.length>1 && arr[1].length()<2){
                arr[1] = "0"+arr[1];
            }
            if(arr.length>2 && arr[2].length()<2){
                arr[2] = "0"+arr[2];
            }
            result = StringUtils.join(arr,"-");
            if(result.length()<num){//年的情况（2009年、2009）
                if(StringUtils.endsWith(result,"-")){
                    result = result+"01";
                }else{
                    result = result+"-01";
                }
                result = result.substring(0,result.length());
            }else if(result.length()>=num){
                result = result.substring(0,num);
            }
            return result;
        }
        return "";
    }
     public static String readerState(Reader y){
        if("0".equals(y.getStatus())) {
            return "有效";
        } else if("1".equals(y.getStatus())){
            return "无效";
        } else if("2".equals(y.getStatus())){
            return "挂失";
        }else {
            return "其他";
        }
    }

    public static String readerType(String readerType){
        if("0".equals(readerType)) {
            return "学生";
        } else if("1".equals(readerType)){
            return "老师";
        }else {
            return "其他";
        }
    }

    public static String isStained(Copy y){
        if("污损".equals(y.getIsStained())) {
            return "污损";
        }else {
            return "-";
        }
    }

    public static  String pastDayName(CirculateLogDTO clDto) {
        if (clDto.getPastDay()<=0) {
            return "未超期";
        }else {
            return "超期";
        }
    }

    public Integer pastDay(CirculateLogDTO clDto) {
       return (clDto.getPastDay()!=null&&clDto.getPastDay()>0)?clDto.getPastDay():0;
    }
}
