package com.courseanalytics.gui.visualization;

import javax.swing.*;
import java.util.Map;

import com.courseanalytics.analysis.StatsHelper;
import com.courseanalytics.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VisualizationUtils {

    public static void showScatter() {
        List<Double> hours = new ArrayList<>();
        List<Double> scores = new ArrayList<>();
        String sql = "SELECT hours_spent, quiz_score FROM studentactivity";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                hours.add(rs.getDouble("hours_spent"));
                scores.add(rs.getDouble("quiz_score"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        JFrame f = new JFrame("Scatter Plot");
        f.add(new ScatterPlotPanel(hours, scores));
        f.setSize(600, 400);
        f.setVisible(true);
    }

    public static void showBarChart() {
        Map<String, Double> map = StatsHelper.averageQuizByCourse();
        JFrame f = new JFrame("Bar Chart");
        f.add(new BarChartPanel(map));
        f.setSize(600, 400);
        f.setVisible(true);
    }
}
