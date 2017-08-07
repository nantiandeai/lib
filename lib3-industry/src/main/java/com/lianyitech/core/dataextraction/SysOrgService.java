package com.lianyitech.core.dataextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by wule on 2016/10/9.
 */
public class SysOrgService {
    private static Logger logger = LoggerFactory.getLogger(SysOrgService.class);

    public   static void insertSysOrg(List<Map> list  )
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
            String sql = "insert into SYS_ORG values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";// 预编译语句，“？”代表参数
//System.out.println("insertfactCirculate----------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            // 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            int i=0;
            for(Map map :list) {
                i++;
                pre.setString(1,map.get("ID")==null ? "" : map.get("ID").toString());
                pre.setString(2, map.get("PARENT_ID")==null ? "" : map.get("PARENT_ID").toString());
                pre.setString(3, map.get("PARENT_IDS")==null ? "" : map.get("PARENT_IDS").toString());
                pre.setString(4, map.get("NAME")==null ? "" : map.get("NAME").toString());
              //  System.out.println("result.getString):"+ map.get("create_date"));
                pre.setInt(5, map.get("SORT")==null ? 0:Integer.valueOf( map.get("SORT").toString()));
                pre.setString(6, map.get("PROVINCE")==null ? "" : map.get("PROVINCE").toString());
                pre.setString(7, map.get("CITY")==null ? "" : map.get("CITY").toString());
                pre.setString(8, map.get("COUNTY")==null ? "" : map.get("COUNTY").toString());
                pre.setString(9, map.get("TYPE")==null ? "" : map.get("TYPE").toString());
                pre.setString(10, map.get("ADDRESS")==null ? "" : map.get("ADDRESS").toString());
                pre.setString(11, map.get("ZIP_CODE")==null ? "" : map.get("ZIP_CODE").toString());
                pre.setString(12, map.get("MASTER")==null ? "" : map.get("MASTER").toString());
                pre.setDouble(13, map.get("PHONE")==null ? 0 : Double.valueOf(map.get("PHONE").toString()));
                pre.setString(14, map.get("FAX")==null ? "" : map.get("FAX").toString());
                pre.setString(15, map.get("EMAIL")==null ? "" : map.get("EMAIL").toString());
                pre.setString(16, map.get("PRIMARY_PERSON")==null ? "" : map.get("PRIMARY_PERSON").toString());
                pre.setString(17, map.get("DEPUTY_PERSON")==null ? "" : map.get("DEPUTY_PERSON").toString());
                pre.setString(18, map.get("CREATE_BY")==null ? "" : map.get("CREATE_BY").toString());
                pre.setTimestamp(19, map.get("CREATE_DATE")==null ? null : Timestamp.valueOf(map.get("CREATE_DATE").toString()));
                pre.setString(20, map.get("UPDATE_BY")==null ? "" : map.get("UPDATE_BY").toString());
                pre.setTimestamp(21, map.get("UPDATE_DATE")==null ? null : Timestamp.valueOf(map.get("UPDATE_DATE").toString()));
                pre.setString(22, map.get("REMARKS")==null ? "" : map.get("REMARKS").toString());
                pre.setString(23, map.get("DEL_FLAG")==null ? "" : map.get("DEL_FLAG").toString());
                pre.setString(24, map.get("SHOOLTYPE")==null ? "" : map.get("SHOOLTYPE").toString());
                pre.setString(25, map.get("SHOOLNATURE")==null ? "" : map.get("SHOOLNATURE").toString());


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
    public static List<Map> selectSysOrg()
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lytsys ";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "portal";// 用户名,系统默认的账户名
//            String password = "123456";// 你安装时选设置的密码
            con = OracleConnection.getLytsysCon();// 获取连接
            System.out.println("连接成功！");
            String sql = "select * from SYS_ORG t ";// 预编译语句，“？”代表参数
           // System.out.println("------------------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
           // pre.setInt(1, 1);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<Map> list = new ArrayList();
            int i=0;
            int factCirculateCount = getSysOrgCount();
            while (result.next()) {
                i++;
                //System.out.println("书目:"  );
                Map map = new HashMap();
                map.put("ID", result.getString("ID"));
                map.put("PARENT_ID", result.getString("PARENT_ID"));
                map.put("PARENT_IDS", result.getString("PARENT_IDS"));
                map.put("NAME", result.getString("NAME"));
                map.put("SORT", result.getString("SORT"));
                map.put("PROVINCE", result.getString("PROVINCE"));
                map.put("CITY", result.getString("CITY"));
                map.put("COUNTY", result.getString("COUNTY"));

                map.put("TYPE", result.getString("TYPE"));
                map.put("ADDRESS", result.getString("ADDRESS"));
                map.put("ZIP_CODE", result.getString("ZIP_CODE"));
                map.put("MASTER", result.getString("MASTER"));
                map.put("PHONE", result.getString("PHONE"));
                map.put("FAX", result.getString("FAX"));
                map.put("EMAIL", result.getString("EMAIL"));
                map.put("PRIMARY_PERSON", result.getString("PRIMARY_PERSON"));
                map.put("DEPUTY_PERSON", result.getString("DEPUTY_PERSON"));
                map.put("CREATE_BY", result.getString("CREATE_BY"));
                map.put("CREATE_DATE", result.getTimestamp("CREATE_DATE"));
                map.put("UPDATE_BY", result.getString("UPDATE_BY"));
                map.put("UPDATE_DATE", result.getTimestamp("UPDATE_DATE"));
                map.put("REMARKS", result.getString("REMARKS"));
                map.put("DEL_FLAG", result.getString("DEL_FLAG"));
                map.put("SHOOLTYPE", result.getString("SHOOLTYPE"));
                map.put("SHOOLNATURE", result.getString("SHOOLNATURE"));

                list.add(map);

                if(factCirculateCount >= 2000 && 0 == i % 2000) {
                    //insert(list);
                    insertSysOrg(list);
                    list.clear();
                  //  System.out.println("------------------------i"+i);
                }else if(factCirculateCount >= 2000 && i == factCirculateCount  ) {
                    //insert(list);
                    insertSysOrg(list);
                    list.clear();
                   // System.out.println("------------------------i"+i);
                }
            }
            if(factCirculateCount < 2000 && i == factCirculateCount){
                insertSysOrg(list);
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

    public static int getSysOrgCount()
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
//            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
//            System.out.println("开始尝试连接数据库！");
//            String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lytsys ";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
//            String user = "portal";// 用户名,系统默认的账户名
//            String password = "123456";// 你安装时选设置的密码
            con = OracleConnection.getLytsysCon();// 获取连接
            System.out.println("连接成功！");
            String sql = "select count(*) from SYS_ORG t ";// 预编译语句，“？”代表参数
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
       // selectfactCirculate();
       // selectSysOrg();
    }
}
