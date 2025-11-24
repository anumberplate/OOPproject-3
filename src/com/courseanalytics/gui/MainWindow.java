package com.courseanalytics.gui;

import com.courseanalytics.controller.AppController;
import com.courseanalytics.gui.visualization.VisualizationUtils;
import com.courseanalytics.util.MessageUtils;
import com.courseanalytics.gui.StatsViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.Collections;

public class MainWindow extends JFrame {

    private AppController controller = new AppController();
    private JTextArea log;
    private boolean hasUploadedData = false;

    private JButton upload;
    private JButton scatter;
    private JButton bar;
    private JButton stats;

    public MainWindow() {
        setTitle("Online Course Analytics");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(24, 24, 28));

        JLabel titleLabel = new JLabel("Online Course Participation Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(235, 235, 245));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        initButtons();

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(30, 30, 36));
        left.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 8));
        left.setPreferredSize(new Dimension(220, getHeight()));

        left.add(upload);
        left.add(Box.createVerticalStrut(10));
        left.add(scatter);
        left.add(Box.createVerticalStrut(10));
        left.add(bar);
        left.add(Box.createVerticalStrut(10));
        left.add(stats);
        left.add(Box.createVerticalGlue());

        log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font("Consolas", Font.PLAIN, 12));
        log.setBackground(new Color(18, 18, 22));
        log.setForeground(new Color(230, 230, 240));
        log.setCaretColor(new Color(230, 230, 240));
        log.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createEmptyBorder(16, 8, 16, 16));
        right.setBackground(new Color(24, 24, 28));
        right.add(new JScrollPane(log), BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }

    private void initButtons() {
        upload = new JButton("Upload CSV");
        scatter = new JButton("Scatter Plot");
        bar = new JButton("Bar Chart");
        stats = new JButton("Show Stats");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension buttonSize = new Dimension(180, 32);

        JButton[] buttons = {upload, scatter, bar, stats};
        for (JButton b : buttons) {
            b.setFont(buttonFont);
            b.setMaximumSize(buttonSize);
            b.setPreferredSize(buttonSize);
            b.setFocusPainted(false);
            b.setBackground(new Color(45, 45, 55));
            b.setForeground(new Color(235, 235, 245));
            b.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 80)));
        }

        upload.addActionListener(e -> uploadCSV());
        scatter.addActionListener(e -> {
            if (hasUploadedData) {
                VisualizationUtils.showScatter();
            } else {
                JFrame f = new JFrame("Scatter Plot");
                f.add(new com.courseanalytics.gui.visualization.ScatterPlotPanel(Collections.emptyList(), Collections.emptyList()));
                f.setSize(600, 400);
                f.setVisible(true);
            }
        });
        bar.addActionListener(e -> {
            if (hasUploadedData) {
                VisualizationUtils.showBarChart();
            } else {
                JFrame f = new JFrame("Bar Chart");
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setSize(600, 400);
                f.setLocationRelativeTo(this);

                JPanel placeholder = new JPanel(new BorderLayout());
                placeholder.setBackground(new Color(24, 24, 28));

                JLabel icon = new JLabel("📊", SwingConstants.CENTER);
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                icon.setForeground(new Color(70, 70, 80));

                JLabel msg = new JLabel("No data uploaded yet.", SwingConstants.CENTER);
                msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                msg.setForeground(new Color(130, 130, 140));

                placeholder.add(icon, BorderLayout.CENTER);
                placeholder.add(msg, BorderLayout.SOUTH);
                placeholder.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

                f.add(placeholder);
                f.setVisible(true);
            }
        });
        stats.addActionListener(e -> showStats());
    }

    private void uploadCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            if (files.length == 0) return;

            JDialog loading = new JDialog(this, "Uploading...", true);
            JLabel label = new JLabel("Preparing upload...", JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            loading.add(label);
            loading.setSize(340, 120);
            loading.setLocationRelativeTo(this);
            loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            SwingWorker<Void, String> worker = new SwingWorker<>() {
                private int current = 0;
                private final int total = files.length;
                private String baseText = "";

                @Override
                protected Void doInBackground() throws Exception {
                    for (File f : files) {
                        current++;
                        baseText = "Uploading (" + current + "/" + total + "): " + f.getName();
                        publish("status|" + baseText);
                        controller.uploadCSV(f);
                        publish("log|Uploaded: " + f.getName() + "\n");
                        // Small pause so each file is visible in the UI
                        Thread.sleep(400);
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    for (String chunk : chunks) {
                        String[] parts = chunk.split("\\|", 2);
                        if (parts[0].equals("status")) {
                            baseText = parts[1];
                            label.setText(baseText);
                        } else if (parts[0].equals("log")) {
                            log.append(parts[1]);
                        }
                    }
                }

                @Override
                protected void done() {
                    loading.dispose();
                    hasUploadedData = true;
                }
            };

            Timer ellipsisTimer = new Timer(500, e -> {
                String txt = label.getText();
                if (txt.endsWith("...")) {
                    label.setText(txt.replace("...", "."));
                } else if (txt.endsWith("..")) {
                    label.setText(txt.replace("..", "..."));
                } else if (txt.endsWith(".")) {
                    label.setText(txt.replace(".", ".."));
                } else {
                    label.setText(txt + ".");
                }
            });
            ellipsisTimer.start();

            worker.execute();
            loading.setVisible(true);

            ellipsisTimer.stop();
        }
    }

    private void showStats() {
        if (!hasUploadedData) {
            String msg = "No data available in this session. Please upload CSV files to see statistics.";
            JOptionPane.showMessageDialog(this, msg, "Statistics", JOptionPane.INFORMATION_MESSAGE);
            log.append(msg + "\n");
            return;
        }
        StatsViewer.showStatsDialog(this);
    }
}
