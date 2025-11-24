package com.courseanalytics.analysis;

import com.courseanalytics.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class StatsHelper {

    public static Map<String, Double> averageQuizByCourse() {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT course_id, AVG(quiz_score) as avg_score FROM studentactivity GROUP BY course_id";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("course_id"), rs.getDouble("avg_score"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static double correlationHoursVsScore() {
        String sql = "SELECT hours_spent, quiz_score FROM studentactivity";
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        int n = 0;

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double x = rs.getDouble("hours_spent");
                double y = rs.getDouble("quiz_score");
                sumX += x;
                sumY += y;
                sumXY += x * y;
                sumX2 += x * x;
                sumY2 += y * y;
                n++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX*sumX)*(n*sumY2 - sumY*sumY));
        return denominator == 0 ? 0 : numerator / denominator;
    }

    public static Map<String, Double> totalHoursPerStudent() {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT student_id, SUM(hours_spent) AS total_hours FROM studentactivity GROUP BY student_id";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("student_id"), rs.getDouble("total_hours"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Integer> engagementDistribution() {
        Map<String, Integer> dist = new HashMap<>();
        dist.put("Active", 0);
        dist.put("Less Active", 0);

        String sql = "SELECT student_id, SUM(hours_spent) AS total_hours FROM studentactivity GROUP BY student_id";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double hours = rs.getDouble("total_hours");
                String label = hours >= 10 ? "Active" : "Less Active";
                dist.put(label, dist.get(label) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dist;
    }
}
