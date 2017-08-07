package com.lianyitech.core.dataextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * Created by wule on 2016/10/9.
 */
public class DimOrgService {
    private static Logger logger = LoggerFactory.getLogger(DimOrgService.class);

    public   static void insertDimOrg(List<Map> list  )
    {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        PreparedStatement pre1 = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创
        try
        {
            con = OracleConnection.getLibdwCon();// 获取连接
            String sql = "insert into DIM_ORG values(?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,null)";// 预编译语句，“？”代表参数
//System.out.println("insertfactCirculate----------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            // 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            int i=0;
            for(Map map :list) {
                i++;
                String org_id =map.get("org_id") == null ? null : map.get("org_id").toString();
                String name="" ,county_name="",county_code="",city_name="",city_code="",province_name="",province_code="",county_type="",city_type="",province_type="";
                String county_name_="",county_code_="",city_name_="",city_code_="",province_name_="",province_code_="";
                if (org_id != null && !org_id.equals("")) {

                    String sql1 = "select s.id,s.name,s1.name as county_name,s1.id as county_code,s1.type as county_type ,s2.name as city_name,s2.id as city_code,s2.type as city_type ,s3.name as province_name,s3.id as province_code,s3.type as province_type  from sys_org s " +
                            "left join sys_org s1 on s1.id=s.parent_id " +
                            "left join sys_org s2 on s2.id=s1.parent_id  " +
                            "left join sys_org s3 on s3.id=s2.parent_id  " +
                            "where s.id=?   ";// 预编译语句，“？”代表参数
                    pre1 = con.prepareStatement(sql1);// 实例化预编译语句
                    pre1.setString(1, org_id );// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
                    result = pre1.executeQuery();// 执行查询，注意括号中不需要再加参数
                    if (result.next()) {
                        name = result.getString("name");
                        county_name = result.getString("county_name");
                        county_code = result.getString("county_code");
                        city_name = result.getString("city_name");
                        city_code = result.getString("city_code");
                        province_name = result.getString("province_name");
                        province_code = result.getString("province_code");
                        county_type = result.getString("county_type");
                        city_type = result.getString("city_type");
                        province_type = result.getString("province_type");
                    }

                    if(null != county_type)
                    if(!county_type.equals("") && county_type.equals("1")){
                        province_name_=county_name;
                        province_code_=county_code;
                    }else  if(!county_type.equals("") && county_type.equals("2")){
                        city_name_=county_name;
                        city_code_=county_code;
                    }else  if(!county_type.equals("") && (county_type.equals("3")|| county_type.equals("4"))){
                        county_name_=county_name;
                        county_code_=county_code;
                    }
                    if(null != city_type)
                    if(!city_type.equals("") && city_type.equals("1")){
                        province_name_=city_name;
                        province_code_=city_code;
                    }else  if(!city_type.equals("") && city_type.equals("2")){
                        city_name_=city_name;
                        city_code_=city_code;
                    }else  if(!city_type.equals("") && (city_type.equals("3")|| city_type.equals("4"))){
                        county_name_=city_code;
                        county_code_=city_code;
                    }
                    if(null != province_type)
                    if(!province_type.equals("") && province_type.equals("1")){
                        province_name_=province_name;
                        province_code_=province_code;
                    }else  if(!province_type.equals("") && province_type.equals("2")){
                        city_name_=province_name;
                        city_code_=province_code;
                    }else  if(!province_type.equals("") && (province_type.equals("3")|| province_type.equals("4"))){
                        county_name_=province_name;
                        county_code_=province_code;
                    }

                    pre.setString(1, UUID.randomUUID().toString().trim().replaceAll("-", ""));
                    pre.setString(2, map.get("org_id")==null ? "" : map.get("org_id").toString());
                    pre.setString(3,name);
                    pre.setString(4,province_code_);
                    pre.setString(5,province_name_ );
                    pre.setString(6, city_code_);
                    pre.setString(7, city_name_);
                    pre.setString(8,county_code_ );
                    pre.setString(9,county_name_);
                    pre.setInt(10,map.get("student_num") == null ? 0 :Integer.valueOf( map.get("student_num").toString()));
                    pre.setInt(11,map.get("teacher_num")  == null ? 0 :Integer.valueOf( map.get("teacher_num").toString()));
                    pre.setInt(12,map.get("other_num") == null ? 0 :Integer.valueOf(  map.get("other_num").toString()));
                    pre.setString(13,null);
                    pre.addBatch();

                    if (pre1 != null)
                        pre1.close();
                    if (result != null)
                        result.close();
                }

               // pre.setDouble(13, map.get("PHONE")==null ? 0 : Double.valueOf(map.get("PHONE").toString()));



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
    public static List<Map> selectDimOrg()
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
            con =OracleConnection.getLib3schoolCon();// 获取连接
            System.out.println("连接成功！");
            String sql = "select  c.org_id,c1.teacher_num,c2.student_num,c3.other_num from   (select org_id from circulate_reader  group by org_id ) c " +
                    "left join (select count(1) as teacher_num ,org_id,reader_type from circulate_reader  group by org_id,reader_type ) c1 on c1.org_id=c.org_id and c1.reader_type=0 " +
                    "left join (select count(1) as student_num ,org_id,reader_type from circulate_reader  group by org_id,reader_type ) c2 on c2.org_id=c.org_id and c2.reader_type=1 " +
                    "left join (select count(1) as other_num ,org_id,reader_type from circulate_reader  group by org_id,reader_type ) c3 on c3.org_id=c.org_id and c3.reader_type=2   ";// 预编译语句，“？”代表参数
            System.out.println("------------------------sql:"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
           // pre.setInt(1, 1);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<Map> list = new ArrayList();
            int i=0;
            int factCirculateCount = getDimOrgCount();
            while (result.next()) {
                i++;
                //System.out.println("书目:"  );
                Map map = new HashMap();
                map.put("org_id", result.getString("org_id"));
                map.put("teacher_num", result.getString("teacher_num"));
                map.put("student_num", result.getString("student_num"));
                map.put("other_num", result.getString("other_num"));

                list.add(map);

                if(factCirculateCount >= 2000 && 0 == i % 2000) {
                    //insert(list);
                    insertDimOrg(list);
                    list.clear();
                  //  System.out.println("------------------------i"+i);
                }else if(factCirculateCount >= 2000 && i == factCirculateCount  ) {
                    //insert(list);
                    insertDimOrg(list);
                    list.clear();
                   // System.out.println("------------------------i"+i);
                }
            }
            if(factCirculateCount < 2000 && i == factCirculateCount){
                insertDimOrg(list);
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

    public static int getDimOrgCount()
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
            String sql = "select  count(1) from   (select org_id from CIRCULATE_READER  group by org_id ) c " +
                    "left join (select count(1) as teacher_num ,org_id,READER_TYPE from CIRCULATE_READER   group by org_id,READER_TYPE ) c1 on c1.org_id=c.org_id and c1.READER_TYPE=0 " +
                    "left join (select count(1) as student_num ,org_id,READER_TYPE from CIRCULATE_READER  group by org_id,READER_TYPE ) c2 on c2.org_id=c.org_id and c2.READER_TYPE=1 " +
                    "left join (select count(1) as other_num ,org_id,READER_TYPE from CIRCULATE_READER   group by org_id,READER_TYPE ) c3 on c3.org_id=c.org_id and c3.READER_TYPE=2   ";// 预编译语句，“？”代表参数            //System.out.println("------------------------sql:"+sql);
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
    public static boolean fileExists(String plainFilePath){
        File file=new File(plainFilePath);
        if(!file.exists())  {
            return false;
        }  else{

            return true;
        }
    }




    public static Clob oracleStr2Clob(String str, Clob lob) throws Exception {
        Method methodToInvoke = lob.getClass().getMethod(
                "getCharacterOutputStream", (Class[]) null);
        Writer writer = (Writer) methodToInvoke.invoke(lob, (Object[]) null);
        writer.write(str);
        writer.close();
        return lob;
    }
    public static Object createOracleLob(Connection conn, String lobClassName)
            throws Exception {
        Class lobClass = conn.getClass().getClassLoader().loadClass(
                lobClassName);
        final Integer DURATION_SESSION = new Integer(lobClass.getField(
                "DURATION_SESSION").getInt(null));
        final Integer MODE_READWRITE = new Integer(lobClass.getField(
                "MODE_READWRITE").getInt(null));
        Method createTemporary = lobClass.getMethod("createTemporary",
                new Class[] { Connection.class, boolean.class, int.class });
        Object lob = createTemporary.invoke(null, new Object[] { conn, false,
                DURATION_SESSION });
        Method open = lobClass.getMethod("open", new Class[] { int.class });
        open.invoke(lob, new Object[] { MODE_READWRITE });
        return lob;
    }
    public static void main(String[] args) throws Exception {
       // selectfactCirculate();
        //selectDimOrg();
    }
}
