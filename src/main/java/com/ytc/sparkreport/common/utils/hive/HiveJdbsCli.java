package com.ytc.sparkreport.common.utils.hive;

import java.sql.*;

import org.apache.log4j.Logger;

public class HiveJdbsCli {

    //Before
    private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive//192.168.68.160:10000/hive";

    //After
    private static  String user = "root";
    private static  String password = "123456";
    private static  String sql ="";
    private static ResultSet res;
    private static final Logger log = Logger.getLogger(HiveJdbsCli.class);

    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = getConn();
            stmt = conn.createStatement();

            //第1步: 存在先删除

            //第2步: 存在先删除

            //第3步: 存在先删除

            //第4步: 存在先删除

            //第5步: 存在先删除

            //第6步: 存在先删除

            //第7步: 存在先删除

            //第8步: 存在先删除
            countData(stmt,"people");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
            log.error("Connection Error!",e);
            System.exit(1);
        }finally {
            try{
                if (conn!=null){
                    conn.close();
                    conn=null;
                }
                if(stmt!=null){
                    stmt.close();
                    stmt = null;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 获取Hive连接
     * */
    private static Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * @Description: 统计
     * */
    private static void countData(Statement stmt, String tableName) throws SQLException{
        sql = "select count(1) from" + tableName;
        System.out.println("Running" + sql);
        res = stmt.executeQuery(sql);
        System.out.println("执行regular hive query运行结果:");
        while (res.next()){
            System.out.println("count---------------------->" + res.getString(1));
        }
    }

    /**
     * @Description: query
     * */
    private static void selectData(Statement stmt, String tableName) throws Exception{
        sql = "select * from" + tableName;
        res = stmt.executeQuery(sql);
        while (res.next()){
            System.out.println(res.getInt(1) + "\t" + res.getString(2));
        }
    }


}
