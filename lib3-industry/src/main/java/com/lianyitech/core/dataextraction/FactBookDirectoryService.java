package com.lianyitech.core.dataextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by wule on 2016/10/9.
 */
public class FactBookDirectoryService {
    private static Logger logger = LoggerFactory.getLogger(DimOrgService.class);

    public   static void insertFactBookDirectory(List<Map> list  )
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        PreparedStatement pre1 = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("--开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:libdw";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "libdw";// 用户名,系统默认的账户名
//            String password = "libdw";// 你安装时选设置的密码
            con = OracleConnection.getLibdwCon();// 获取连接
            System.out.println("--连接成功！");
            String sql = "insert into fact_book_directory  (ID,ORG_ID,CATEGORY_ID,DATE_ID,COPY_NUM,ADD_COPY_NUM, " +
                    "BOOK_DIRECTORY_ID,ISBN,AUTHOR,TITLE,PUBLISHING_NAME,PUBLISHING_TIME,PRICE,INSTOR_DATE,CREATE_DATE,ASSORTNUM)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate,?)";// 预编译语句，“？”代表参数
//System.out.println("insertfactCirculate----------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            // 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            int i=0;
            for(Map map :list) {
                i++;
                String id="";
                String librarsort_code =map.get("librarsort_code")==null ? "":  map.get("librarsort_code").toString();
                pre.setString(1, UUID.randomUUID().toString().trim().replaceAll("-", ""));
                pre.setString(2, map.get("org_id")==null ? "" : map.get("org_id").toString());
                if (librarsort_code != null && !librarsort_code.equals("")) {
                    String sql1 = "select id from DIM_BOOK_CATEGORY  where SMALL_CLASSES_CODE = ?  ";// 预编译语句，“？”代表参数
                    pre1 = con.prepareStatement(sql1);// 实例化预编译语句
                    pre1.setString(1, librarsort_code.substring(0,1));// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
                    result = pre1.executeQuery();// 执行查询，注意括号中不需要再加参数
                    if (result.next()) {
                         id = result.getString("id");
                    }
                    if (pre1 != null)
                        pre1.close();
                    if (result != null)
                        result.close();
                }
                pre.setString(3, id);
                pre.setString(3, map.get("DATE_ID")==null ? "" : map.get("DATE_ID").toString());
                pre.setString(4, map.get("log_type")==null ? "" : map.get("log_type").toString());
              //  System.out.println("result.getString):"+ map.get("create_date"));
                pre.setInt(5, map.get("copy_count")==null ? 0 :Integer.valueOf( map.get("copy_count").toString()));
                pre.setInt(6, map.get("new_count")==null ? 0 : Integer.valueOf( map.get("new_count").toString()));
                pre.setString(7, map.get("id")==null ? "" : map.get("id").toString());
                pre.setString(8, map.get("isbn")==null ? "" : map.get("isbn").toString());
                pre.setString(9, map.get("author")==null ? "" : map.get("author").toString());
                pre.setString(10, map.get("title")==null ? "" : map.get("title").toString());
                pre.setString(11, map.get("publishing_name")==null ? "" : map.get("publishing_name").toString());
                pre.setString(12, map.get("publishing_time")==null ? "" : map.get("publishing_time").toString());
                pre.setDouble(13, map.get("price")==null ? 0 : Double.valueOf(map.get("price").toString()));
                pre.setString(14, map.get("librarsort_code")==null ? "":  map.get("librarsort_code").toString());


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
                if (pre1 != null)
                    pre1.close();
                if (result != null)
                    result.close();
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
    public static List<Map> selectBookDirectory()
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
            String sql = "select cb.id as id ,cb.isbn,cb.org_id,cb.title,cb.author,cb.publishing_name,cb.publishing_time,cb.price,cb.librarsort_code,cc.copy_count,ccp.new_count  " +
                    "from catalog_book_directory cb  " +
                    "left join (select id,count(1) as copy_count,book_directory_id   from catalog_copy group by id,book_directory_id ) cc  on cc.book_directory_id =cb.id " +
                    "left join (select id,count(1) as new_count,book_directory_id,create_date from catalog_copy  group by id,book_directory_id,create_date  ) ccp " +
                    " on ccp.book_directory_id =cb.id and ccp.create_date>to_timestamp('2006-01-01 00:00:10.1','yyyy-mm-dd hh24:mi:ss.ff') ";// 预编译语句，“？”代表参数
           // System.out.println("------------------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
           // pre.setInt(1, 1);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<Map> list = new ArrayList();
            int i=0;
            int factCirculateCount = getBookDirectoryCount();
            while (result.next()) {
                i++;
                //System.out.println("书目:"  );
                Map map = new HashMap();
                map.put("id", result.getString("id"));
                map.put("isbn", result.getString("isbn"));
                map.put("org_id", result.getString("org_id"));
                map.put("title", result.getString("title"));
                map.put("author", result.getString("author"));
                map.put("publishing_name", result.getString("publishing_name"));
                map.put("title", result.getString("title"));
                map.put("price", result.getString("price"));

                map.put("librarsort_code", result.getString("librarsort_code"));
                map.put("copy_count", result.getString("copy_count"));
                map.put("new_count", result.getString("new_count"));
                list.add(map);

                if(factCirculateCount >= 2000 && 0 == i % 2000) {
                    //insert(list);
                    insertFactBookDirectory(list);
                    list.clear();
                  //  System.out.println("------------------------i"+i);
                }else if(factCirculateCount >= 2000 && i == factCirculateCount  ) {
                    //insert(list);
                    insertFactBookDirectory(list);
                    list.clear();
                   // System.out.println("------------------------i"+i);
                }
            }
            System.out.println("------------------------list"+list.size());
            if(factCirculateCount < 2000 && i == factCirculateCount){
                insertFactBookDirectory(list);
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

    public static int getBookDirectoryCount()
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
            String sql = "select count(*)  " +
                    "from catalog_book_directory cb " +
                    "left join (select id,count(1) as copy_count,book_directory_id   from catalog_copy group by id,book_directory_id ) cc  on cc.book_directory_id =cb.id " +
                    "left join (select id,count(1) as new_count,book_directory_id,create_date from catalog_copy  group by id,book_directory_id,create_date  ) ccp " +
                    " on ccp.book_directory_id =cb.id and ccp.create_date>to_timestamp('2006-01-01 00:00:10.1','yyyy-mm-dd hh24:mi:ss.ff') ";
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
       // System.out.println("A223" .substring(0,1));
    }
}
