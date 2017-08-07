package com.lianyitech.modules.common;

import com.lianyitech.modules.analysis.entity.InputAnalysis;
import com.lianyitech.modules.report.entity.BookCirculte;
import com.lianyitech.modules.report.entity.BookReport;

import java.math.BigDecimal;
import java.util.Comparator;
//报表默认排序类
public class Compare {
    /**
     * 公共倒序
     */
    private static int common_Desc(Object o1,Object o2){
        if(o1 instanceof String && o2 instanceof String){//字符串类型的
            try {
                Double one = Double.parseDouble(o1.toString());
                Double two = Double.parseDouble(o2.toString());
                return -Double.compare(one, two);
            } catch (Exception e) {
                return 0;
            }
        } else if (o1 instanceof Number && o2 instanceof Number){
            return -new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString()));
        }
        return 0;
    }
    /**
     * BookReport藏书册数倒序排序
     */
    public static Comparator<BookReport> bookNum_Desc = (o1, o2) -> {
        if(o1!=null && o2!=null){
            if (o1.getOrgType().compareTo(o2.getOrgType())==0) {
                return common_Desc(o1.getBookNum() != null ? o1.getBookNum() : 0, o2.getBookNum() != null ? o2.getBookNum() : 0);
            }else{
                return o1.getOrgType().compareTo(o2.getOrgType());
            }
        }
        return 0;
    };
    /**
     * BookReport 生均册数倒序排序(生均册数这里是字符串的这里要转成double进行排序)
     */
    public static Comparator<BookReport> stuBookNum_Desc = (o1, o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getOrgType().compareTo(o2.getOrgType())==0) {
                return common_Desc(o1.getStuBookNum() != null ? o1.getStuBookNum() : "0", o2.getStuBookNum() != null ? o2.getStuBookNum() : "0");
            }else{
                return o1.getOrgType().compareTo(o2.getOrgType());
            }
        }
        return 0;
    };
    /**
     * BookCirculte stuAverRate生均借阅册数倒序（实体类是String型的转成double类型进行比较）
     */
    public static Comparator<BookCirculte> stuAverRate_Desc = (o1, o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getOrgType().compareTo(o2.getOrgType())==0) {
                return common_Desc(o1.getStuAverRate() != null ? o1.getStuAverRate() : "0", o2.getStuAverRate() != null ? o2.getStuAverRate() : "0");
            }else{
                return o1.getOrgType().compareTo(o2.getOrgType());
            }
        }
        return 0;
    };
    /**
     * BookCirculte averRate 借阅率倒序排序（实体类里面是string类型去掉%转成double进行比较）
     */
    public static Comparator<BookCirculte> averRate_Desc = (o1, o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getOrgType().compareTo(o2.getOrgType())==0) {
                return common_Desc(o1.getAverRate() != null ? o1.getStuAverRate().replace("%","") : "0", o2.getStuAverRate() != null ? o2.getStuAverRate().replace("%","") : "0");
            }else{
                return o1.getOrgType().compareTo(o2.getOrgType());
            }
        }
        return 0;
    };
    /**
     * BookCirculte circulationRate 流通率倒序排序（实体类里面是string类型去掉%转成double进行比较）
     */
    public static Comparator<BookCirculte> circulationRate_Desc = (o1, o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getOrgType().compareTo(o2.getOrgType())==0) {
                return common_Desc(o1.getCirculationRate() != null ? o1.getCirculationRate().replace("%","") : "0", o2.getCirculationRate() != null ? o2.getCirculationRate().replace("%","") : "0");
            }else{
                return o1.getOrgType().compareTo(o2.getOrgType());
            }
        }
        return 0;
    };
    /**
     * 未录入信息的学校倒序排序
     */
    public static Comparator<InputAnalysis> noInput_Desc = (o1, o2) -> {
        if(o1!=null && o2!=null){
            return common_Desc(o1.getNoInput() != null ? o1.getNoInput() : 0, o2.getNoInput() != null ? o2.getNoInput() : 0);
        }
        return 0;
    };

}
