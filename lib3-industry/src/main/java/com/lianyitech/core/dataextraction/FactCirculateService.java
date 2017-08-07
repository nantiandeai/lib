package com.lianyitech.core.dataextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by wule on 2016/10/9.
 */
public class FactCirculateService {
    private static Logger logger = LoggerFactory.getLogger(FactCirculateService.class);

    public   static void insertfactCirculate(List<Map> list  )
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("--开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:libdw";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "libdw";// 用户名,系统默认的账户名
//            String password = "libdw";// 你安装时选设置的密码
            con = OracleConnection.getLibdwCon();// 获取连接
            System.out.println("--连接成功！");
            String sql = "insert into fact_circulate (ID,ORG_ID,DATE_ID,OP_TYPE_ID,OP_TIME,BAR_CODE, " +
                    "BOOK_DIRECTORY_ID,ISBN,AUTHOR,TITLE,PUBLISHING_NAME,PUBLISHING_TIME,PRICE,READER_ID,NAME,EMAIL,PHONE,CERT_TYPE,CERT_NUM,READER_TYPE,CREATE_DATE)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";// 预编译语句，“？”代表参数
//System.out.println("insertfactCirculate----------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            // 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            int i=0;
            for(Map map :list) {
                i++;
                pre.setString(1, UUID.randomUUID().toString().trim().replaceAll("-", ""));
                pre.setString(2, map.get("org_id")==null ? "" : map.get("org_id").toString());
                pre.setString(3, map.get("DATE_ID")==null ? "" : map.get("DATE_ID").toString());
                pre.setString(4, map.get("log_type")==null ? "" : map.get("log_type").toString());
              //  System.out.println("result.getString):"+ map.get("create_date"));
                pre.setTimestamp(5, map.get("create_date")==null ? null:Timestamp.valueOf( map.get("create_date").toString()));
                pre.setString(6, map.get("bar_code")==null ? "" : map.get("bar_code").toString());
                pre.setString(7, map.get("book_dir_id")==null ? "" : map.get("book_dir_id").toString());
                pre.setString(8, map.get("isbn")==null ? "" : map.get("isbn").toString());
                pre.setString(9, map.get("author")==null ? "" : map.get("author").toString());
                pre.setString(10, map.get("title")==null ? "" : map.get("title").toString());
                pre.setString(11, map.get("publishing_name")==null ? "" : map.get("publishing_name").toString());
                pre.setString(12, map.get("publishing_time")==null ? "" : map.get("publishing_time").toString());
                pre.setDouble(13, map.get("price")==null ? 0 : Double.valueOf(map.get("price").toString()));
                pre.setString(14, map.get("read_id")==null ? "" : map.get("read_id").toString());
                pre.setString(15, map.get("name")==null ? "" : map.get("name").toString());
                pre.setString(16, map.get("email")==null ? "" : map.get("email").toString());
                pre.setString(17, map.get("phone")==null ? "" : map.get("phone").toString());
                pre.setString(18, map.get("cert_type")==null ? "" : map.get("cert_type").toString());
                pre.setString(19, map.get("cert_num")==null ? "" : map.get("cert_num").toString());
                pre.setString(20, map.get("reader_type")==null ? "" : map.get("reader_type").toString());


               pre.addBatch();
//                if (0 == i % 1)
//                {
//
//                }
            }
            System.out.println("==>>i:" + i);
            pre.executeBatch();
            // pre.setString(7, map.get("PAGESUM").toString());
            System.out.println("--插入成功！");
            con.commit();
        }
        catch (Exception e)
        {
            logger.error("操作失败",e);
        }
        finally
        {
            try
            {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("--数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
    }
    public static List<Map> selectfactCirculate()
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lib3school";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "lib3school";// 用户名,系统默认的账户名
//            String password = "lib3school";// 你安装时选设置的密码
            con = OracleConnection.getLib3schoolCon();// 获取连接
            System.out.println("连接成功！");
            String sql = "select c.org_id,cl.log_type,cl.create_date,cc.bar_code,cbd.id as book_dir_id,c.isbn,c.title,cbd.author,cbd.publishing_name,cbd.publishing_time,cbd.price " +
                    ",cr.id as read_id,cr.name,cr.email,cr.phone,cr.cert_type,cr.cert_num,cr.reader_type from circulate_log cl " +
                    "left join CIRCULATE_BILL c on c.id =cl.bill_id " +
                    "left join catalog_copy cc on cc.id =c.copy_id " +
                    "left join catalog_book_directory cbd on cbd.id=cc.book_directory_id " +
                    "left join circulate_reader cr on cr.id = c.reader_id " +
                    "left join circulate_card cc1 on cc1.reader_id = cr.id where cc.status=0 ";// 预编译语句，“？”代表参数
           // System.out.println("------------------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
           // pre.setInt(1, 1);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<Map> list = new ArrayList();
            int i=0;
            int factCirculateCount = getfactCirculateCount();
            while (result.next()) {
                i++;
                //System.out.println("书目:"  );
                Map map = new HashMap();
                map.put("org_id", result.getString("org_id"));
                map.put("log_type", result.getString("log_type"));
                map.put("create_date", result.getTimestamp("create_date"));
                map.put("bar_code", result.getString("bar_code"));
                map.put("book_dir_id", result.getString("book_dir_id"));
                map.put("isbn", result.getString("isbn"));
                map.put("title", result.getString("title"));
                map.put("author", result.getString("author"));

                map.put("publishing_name", result.getString("publishing_name"));
                map.put("publishing_time", result.getString("publishing_time"));
                map.put("price", result.getString("price"));
                map.put("read_id", result.getString("read_id"));
                map.put("name", result.getString("name"));
                map.put("email", result.getString("email"));
                map.put("phone", result.getString("phone"));
                map.put("cert_type", result.getString("cert_type"));
                map.put("cert_num", result.getString("cert_num"));
                map.put("reader_type", result.getString("reader_type"));
                list.add(map);

                if(factCirculateCount >= 2000 && 0 == i % 2000) {
                    //insert(list);
                    insertfactCirculate(list);
                    list.clear();
                  //  System.out.println("------------------------i"+i);
                }else if(factCirculateCount >= 2000 && i == factCirculateCount  ) {
                    //insert(list);
                    insertfactCirculate(list);
                    list.clear();
                   // System.out.println("------------------------i"+i);
                }
            }
            if(factCirculateCount < 2000 && i == factCirculateCount){
                insertfactCirculate(list);
            }
            // 当结果集不为空时
            return  list;
        }
        catch (Exception e)
        {
            logger.error("操作失败",e);
        }
        finally
        {
            try
            {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
        return null;
    }

    public static int getfactCirculateCount()
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lib3school";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "lib3school";// 用户名,系统默认的账户名
//            String password = "lib3school";// 你安装时选设置的密码
            con = OracleConnection.getLib3schoolCon();// 获取连接
            System.out.println("连接成功！");
            String sql = "select  count(1) from circulate_log cl " +
                    "left join CIRCULATE_BILL c on c.id =cl.bill_id " +
                    "left join catalog_copy cc on cc.id =c.copy_id " +
                    "left join catalog_book_directory cbd on cbd.id=cc.book_directory_id " +
                    "left join circulate_reader cr on cr.id = c.reader_id " +
                    "left join circulate_card cc1 on cc1.reader_id = cr.id where cc.status=0 ";// 预编译语句，“？”代表参数
            //System.out.println("------------------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            // pre.setInt(1, 1);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<Map> list = new ArrayList();
            if (result.next()) {
               return  result.getInt(1);
            }

            // 当结果集不为空时
            return  0;
        }
        catch (Exception e)
        {
           logger.error("操作失败",e);
        }
        finally
        {
            try
            {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        //selectfactCirculate();
    }
}
