package com.courseanalytics.gui;

import com.courseanalytics.analysis.StatsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class StatsViewer {

    public static void showStatsDialog(Frame parent) {
        Map<String, Double> hoursMap = StatsHelper.totalHoursPerStudent();
        Map<String, Double> avgMap = StatsHelper.averageQuizByCourse();
        double r = StatsHelper.correlationHoursVsScore();
        Map<String, Integer> dist = StatsHelper.engagementDistribution();
        int active = dist.getOrDefault("Active", 0);
        int less = dist.getOrDefault("Less Active", 0);

        boolean noHours = hoursMap.isEmpty();
        boolean noAvg = avgMap.isEmpty();
        boolean noEngagement = (active + less) == 0;

        if (noHours && noAvg && noEngagement) {
            String msg = "No data available yet (after upload). Please check your CSV contents.";
            JOptionPane.showMessageDialog(parent, msg, "Statistics", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        java.util.List<Map.Entry<String, Double>> hoursList = new java.util.ArrayList<>(hoursMap.entrySet());
        hoursList.sort(java.util.Map.Entry.comparingByKey());

        Object[][] tableData = new Object[hoursList.size()][2];
        for (int i = 0; i < hoursList.size(); i++) {
            Map.Entry<String, Double> e = hoursList.get(i);
            tableData[i][0] = e.getKey();
            tableData[i][1] = String.format("%.2f", e.getValue());
        }
        String[] columnNames = {"Student ID", "Total Hours"};

        JTable table = new JTable(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setRowHeight(22);
        table.setBackground(new Color(18, 18, 22));
        table.setForeground(new Color(230, 230, 240));
        table.getTableHeader().setBackground(new Color(30, 30, 36));
        table.getTableHeader().setForeground(new Color(235, 235, 245));
        table.setGridColor(new Color(60, 60, 70));
        table.setSelectionBackground(new Color(45, 45, 55));
        table.setSelectionForeground(new Color(230, 230, 240));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(420, 260));
        tableScroll.setBackground(new Color(18, 18, 22));
        tableScroll.getViewport().setBackground(new Color(18, 18, 22));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));

        StringBuilder summary = new StringBuilder();
        summary.append("Average quiz score per course\n");
        for (Map.Entry<String, Double> entry : avgMap.entrySet()) {
            summary.append("  ").append(entry.getKey())
                   .append(": ")
                   .append(String.format("%.2f", entry.getValue()))
                   .append("\n");
        }
        summary.append("\n");

        summary.append("Correlation (hours vs quiz score)\n");
        summary.append("  Value: ").append(String.format("%.3f", r)).append("\n\n");

        summary.append("Engagement distribution\n");
        summary.append("  Active: ").append(active).append("\n");
        summary.append("  Less Active: ").append(less).append("\n");

        JTextArea summaryArea = new JTextArea(summary.toString());
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(18, 18, 22));
        summaryArea.setForeground(new Color(230, 230, 240));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(new Color(24, 24, 28));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(30, 30, 36));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        leftPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(18, 18, 22));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        rightPanel.add(summaryArea, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.55);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setBackground(new Color(24, 24, 28));
        split.setDividerLocation(420);

        panel.add(split, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, panel, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
}
