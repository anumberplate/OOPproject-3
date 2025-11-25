package com.courseanalytics.gui.visualization;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SimpleBarChartPanel extends JPanel {

    private Map<String, Double> avgScores;

    public SimpleBarChartPanel(Map<String, Double> avgScores) {
        this.avgScores = avgScores;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;

        // Find max value
        double maxValue = 0;
        for (double v : avgScores.values()) {
            if (v > maxValue) maxValue = v;
        }
        if (maxValue == 0) return;

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, padding, padding, height - padding); // Y axis
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X axis

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String title = "Average Quiz Score by Course";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 20);

        // Y-axis label
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Average Score", 10, padding - 10);

        // X-axis label
        FontMetrics fm = g2.getFontMetrics();
        String xAxisLabel = "Course";
        int xAxisLabelWidth = fm.stringWidth(xAxisLabel);
        g2.drawString(xAxisLabel, width / 2 - xAxisLabelWidth / 2, height - padding + 35);

        // Draw bars
        int barCount = avgScores.size();
        int barWidth = chartWidth / (barCount * 2);
        int gap = barWidth;

        int index = 0;
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (Map.Entry<String, Double> entry : avgScores.entrySet()) {
            String course = entry.getKey();
            double value = entry.getValue();

            // Calculate bar dimensions
            double valueRatio = value / maxValue;
            int barHeight = (int) (valueRatio * chartHeight);
            int x = padding + gap + index * (barWidth + gap);
            int y = height - padding - barHeight;

            // Draw bar
            g2.setColor(new Color(70, 130, 180)); // Steel blue
            g2.fillRect(x, y, barWidth, barHeight);

            // Draw bar border
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);

            // Draw course label
            int labelWidth = g2.getFontMetrics().stringWidth(course);
            int labelX = x + (barWidth - labelWidth) / 2;
            g2.drawString(course, labelX, height - padding + 15);

            // Draw value label
            String valueLabel = String.format("%.1f", value);
            int valueWidth = g2.getFontMetrics().stringWidth(valueLabel);
            int valueX = x + (barWidth - valueWidth) / 2;
            g2.drawString(valueLabel, valueX, y - 5);

            index++;
        }

        // Y-axis labels
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            double value = (maxValue / 5) * i;
            int y = height - padding - (i * chartHeight / 5);
            g2.drawString(String.format("%.0f", value), padding - 30, y + 5);
        }
    }
}
