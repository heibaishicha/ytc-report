package com.ytc.sparkreport.exercises;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ApiQueryTest {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args){
        try {
            Class.forName(driverName);
            Connection con = null;
            con = DriverManager.getConnection("jdbc:hive2://192.168.68.160:10000", "root", "123456");
            Statement stmt = con.createStatement();
            ResultSet res = null;
            String sql = "select * from people";
            System.out.println("Running: " + sql);
            res = stmt.executeQuery(sql);
            System.out.println("ok");
            while (res.next()) {
                System.out.println(res.getString(1) + "\t" + res.getString(2) + "\t" +  res.getString(3) + "\t" +  res.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }

    }
}