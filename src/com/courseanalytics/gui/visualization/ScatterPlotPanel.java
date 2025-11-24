package com.courseanalytics.gui.visualization;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScatterPlotPanel extends JPanel {

    private List<Double> hours;
    private List<Double> scores;

    private static final int PADDING = 70;
    private static final int POINT_SIZE = 8;
    private static final int TICK_COUNT = 10;

    public ScatterPlotPanel(List<Double> hours, List<Double> scores) {
        this.hours = hours;
        this.scores = scores;
        setBackground(new Color(18, 18, 22));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        double maxHours = hours.stream().max(Double::compare).orElse(1.0);
        double maxScore = scores.stream().max(Double::compare).orElse(1.0);

        // Axes
        g2.setColor(new Color(230, 230, 240));
        g2.drawLine(PADDING, height - PADDING, width - PADDING, height - PADDING); // X
        g2.drawLine(PADDING, PADDING, PADDING, height - PADDING); // Y

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Hours Studied vs Quiz Score", width / 2 - 110, 30);

        // Axis Labels
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Hours Studied", width / 2 - 30, height - 10);
        g2.drawString("Score", 10, PADDING - 10);

        // Grid + Axis Numbers
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(70, 70, 80));

        for (int i = 0; i <= TICK_COUNT; i++) {
            int x = PADDING + i * (width - 2 * PADDING) / TICK_COUNT;
            int y = height - PADDING - i * (height - 2 * PADDING) / TICK_COUNT;

            // Grid lines
            g2.drawLine(x, PADDING, x, height - PADDING);
            g2.drawLine(PADDING, y, width - PADDING, y);

            // X-axis numbers
            double xValue = (maxHours / TICK_COUNT) * i;
            g2.setColor(new Color(200, 200, 210));
            g2.drawString(String.format("%.1f", xValue), x - 10, height - PADDING + 15);

            // Y-axis numbers
            double yValue = (maxScore / TICK_COUNT) * i;
            g2.drawString(String.format("%.1f", yValue), PADDING - 40, y + 5);

            g2.setColor(new Color(70, 70, 80));
        }

        // Plot points
        g2.setColor(new Color(88, 166, 255));

        for (int i = 0; i < hours.size(); i++) {
            int x = PADDING + (int) ((hours.get(i) / maxHours) * (width - 2 * PADDING));
            int y = height - PADDING - (int) ((scores.get(i) / maxScore) * (height - 2 * PADDING));

            g2.fillOval(x - POINT_SIZE / 2, y - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
        }
    }
}
