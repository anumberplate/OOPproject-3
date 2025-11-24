package com.courseanalytics.gui.visualization;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {

    private Map<String, Double> avgScores;

    public BarChartPanel(Map<String, Double> avgScores) {
        this.avgScores = avgScores;
        setBackground(new Color(18, 18, 22));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int padding = 60;
        int labelPadding = 40;

        int chartX = padding;
        int chartY = padding;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - padding - labelPadding;

        double maxValue = 0.0;
        for (double v : avgScores.values()) {
            if (v > maxValue) {
                maxValue = v;
            }
        }
        if (maxValue == 0) {
            return;
        }

        int n = avgScores.size();
        int barWidth = (int) (chartWidth / Math.max(n * 1.5, 1.0));
        int gap = barWidth / 2;

        // Axes
        g2.setColor(new Color(220, 220, 235));
        g2.drawLine(chartX, chartY, chartX, chartY + chartHeight); // Y axis
        g2.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight); // X axis

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String title = "Average Quiz Score by Course";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 25);

        // Axis label
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Average Score", 10, chartY - 10);

        // Bars
        int index = 0;
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        for (Map.Entry<String, Double> entry : avgScores.entrySet()) {
            String course = entry.getKey();
            double value = entry.getValue();

            double valueRatio = value / maxValue;
            int barHeight = (int) (valueRatio * chartHeight);

            int x = chartX + gap + index * (barWidth + gap);
            int y = chartY + chartHeight - barHeight;

            g2.setColor(new Color(255, 184, 77));
            g2.fillRect(x, y, barWidth, barHeight);

            g2.setColor(new Color(220, 220, 235));
            g2.drawRect(x, y, barWidth, barHeight);

            // Course label
            int labelWidth = g2.getFontMetrics().stringWidth(course);
            int labelX = x + (barWidth - labelWidth) / 2;
            int labelY = chartY + chartHeight + 15;
            g2.drawString(course, labelX, labelY);

            // Value label above bar
            String valueLabel = String.format("%.1f", value);
            int valueWidth = g2.getFontMetrics().stringWidth(valueLabel);
            int valueX = x + (barWidth - valueWidth) / 2;
            int valueY = y - 5;
            g2.drawString(valueLabel, valueX, valueY);

            index++;
        }
    }
}
