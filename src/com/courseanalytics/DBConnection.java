package com.courseanalytics;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;

    }

}
