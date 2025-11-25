package com.courseanalytics;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Properties props = new Properties();
            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    System.err.println("db.properties not found in classpath");
                    return null;
                }
                props.load(input);
            }

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
