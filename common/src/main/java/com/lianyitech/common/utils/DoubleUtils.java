package com.lianyitech.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/6/28.
 */
public class DoubleUtils {


    public static Double multi(Double d1 , Double d2){
        Double result = null;
        BigDecimal decimal1 = new BigDecimal(Double.toString(d1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(d2));
        return formatDouble(decimal1.multiply(decimal2)).doubleValue();
    }


    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static String formatDoubleToString(Double d) {
        DecimalFormat df   = new DecimalFormat("0.00");
        return df.format(formatDouble(d));
    }

    /**
     * 保留两位小数
     * @param bigDecimal
     * @return
     */
    public static BigDecimal formatDouble(BigDecimal bigDecimal){
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static double formatDouble(Double d){
        if(d==null) {
            return 0;
        }
        return formatDouble(new BigDecimal(d)).doubleValue();
    }

    public static void main(String[] args) {
//        String sql = "UPDATE catalog_book_directory t\n" +
//                "SET isbn = ?, title = ?, sub_title = ?, tied_title = ?, part_name = ?, part_num = ?, series_name = ?, author = ?, sub_author = ?, series_editor = ?, translator = ?, publishing_name = ?, publishing_address = ?, publishing_time = ?, librarsort_id = ?, librarsort_code = ?, price = ?, edition = ?, language = ?, measure = ?, page_no = ?, binding_form = ?, best_age = ?, attachment_note = ?, subject = ?, purpose = ?, content = ?, update_by = ?, update_date = ?, remarks = ?, taneji_no = ?\n" +
//                "WHERE id = ?\n" +
//                "\tAND del_flag = ?\n" +
//                "\tAND org_id = ?";
//        String params = "\"9787807469384\",\"经典智慧的宝库\",\"神奇故事\",null,null,null,null,\"毛立斌，云驰，郭雪霜编\",null,null,null,\"广西美术出版社\",null,\"2010-01\",null,\"I14\",13.8,null,\"chi\",\"23cm\",\"98页\",null,null,null,\"故事\",null,null,\"450125012034\",\"2017-07-07 09:29:02\",null,\"118\",\"9bcdaa6e2b4c46c28a12b3eb91124c5c\",\"0\",\"450125012034\"" ;
//        String[] tmp = params.split(",");
//        String[] t = sql.split("\\?");
//        StringBuilder res = new StringBuilder();
//        for (int i = 0 ; i < t.length ; i++) {
//            res.append(t[i]).append(tmp[i].replaceAll("\\\"","'"));
//        }
//        System.out.println(res.toString());
        Double d1 = 174D;
        Double d2 =50D;
        //System.out.println(java.text.DecimalFormat("#.00").);

        DecimalFormat df   = new DecimalFormat("0.00");
        System.out.println(df.format(d2));

    }
}
