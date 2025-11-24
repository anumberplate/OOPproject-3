package com.courseanalytics.data;

import com.courseanalytics.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActivityDAO {

    public static void insertActivity(CSVRecord r) {
        String sql = "INSERT IGNORE INTO studentactivity(activity_id, student_id, course_id, hours_spent, quiz_score, datee) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, r.activityId);
            ps.setString(2, r.studentId);
            ps.setString(3, r.courseId);
            ps.setDouble(4, r.hoursSpent);
            ps.setDouble(5, r.quizScore);
            ps.setString(6, r.date);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
