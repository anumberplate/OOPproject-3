package com.courseanalytics.analysis;

import com.courseanalytics.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EngagementAnalysis {

    public static Map<String, String> classifyStudents() {
        Map<String, String> result = new HashMap<>();
        String sql = "SELECT student_id, SUM(hours_spent) as total_hours FROM studentactivity GROUP BY student_id";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double hours = rs.getDouble("total_hours");
                result.put(rs.getString("student_id"), hours >= 10 ? "Active" : "Less Active");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
