package com.courseanalytics.data;

import com.courseanalytics.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDAO {

    public static void insertStudent(String studentId, String firstName, String lastName, int age) {
        String sqlCheck = "SELECT student_id FROM students WHERE student_id=?";
        String sqlInsert = "INSERT INTO students(student_id, first_name, last_name, age) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlCheck);
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) { // Only insert if not exists
                ps = conn.prepareStatement(sqlInsert);
                ps.setString(1, studentId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setInt(4, age);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
