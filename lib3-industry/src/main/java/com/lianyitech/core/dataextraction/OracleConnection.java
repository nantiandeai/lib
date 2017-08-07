package com.lianyitech.core.dataextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by wule on 2016/10/9.
 */
public class OracleConnection {
    private static Logger logger = LoggerFactory.getLogger(OracleConnection.class);
    private static Connection con1 = null;// 创建一个数据库连接
    private static Connection con2 = null;// 创建一个数据库连接
    private static Connection con3 = null;// 创建一个数据库连接

    public   static Connection getLibdwCon(  )
    {
        try
        {
            if(con1 == null) {
                Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
                System.out.println("--开始尝试连接数据库！");
                String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:libdw";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
                String user = "libdw";// 用户名,系统默认的账户名
                String password = "libdw";// 你安装时选设置的密码
                con1 = DriverManager.getConnection(url, user, password);// 获取连接
            }

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
                if (con1 != null)
                    con1.close();
                System.out.println("--数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
        return con1;
    }
    public static Connection getLib3schoolCon()
    {
        try
        {
            if(con2 == null) {
                Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
                System.out.println("开始尝试连接数据库！");
                String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lib3school";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
                String user = "lib3school";// 用户名,系统默认的账户名
                String password = "lib3school";// 你安装时选设置的密码
                con2 = DriverManager.getConnection(url, user, password);// 获取连接
            }

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
                if (con2 != null)
                    con2.close();
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
        return con2;
    }

    public static Connection getLytsysCon()
    {
        try
        {
            if(con3 == null) {
                Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
                System.out.println("开始尝试连接数据库！");
                String url = "jdbc:oracle:" + "thin:@192.168.2.199:1521:lytsys ";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
                String user = "portal";// 用户名,系统默认的账户名
                String password = "123456";// 你安装时选设置的密码
                con3 = DriverManager.getConnection(url, user, password);// 获取连接
                System.out.println("连接成功！");
            }

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
                if (con3 != null)
                    con3.close();
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e)
            {
                logger.error("操作失败",e);
            }
        }
        return con3;
    }
    public static void main(String[] args) throws Exception {

    }
}
