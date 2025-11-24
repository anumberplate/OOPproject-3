package com.courseanalytics.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginDialog extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean succeeded;
    private Properties users;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(320, 180);
        setLocationRelativeTo(parent);
        setResizable(false);

        loadUsers();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dark theme colors
        panel.setBackground(new Color(24, 24, 28));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(new Color(230, 230, 240));
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(new Color(230, 230, 240));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        usernameField.setBackground(new Color(18, 18, 22));
        usernameField.setForeground(new Color(230, 230, 240));
        usernameField.setCaretColor(new Color(230, 230, 240));
        passwordField.setBackground(new Color(18, 18, 22));
        passwordField.setForeground(new Color(230, 230, 240));
        passwordField.setCaretColor(new Color(230, 230, 240));

        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        loginButton.setBackground(new Color(45, 45, 55));
        loginButton.setForeground(new Color(230, 230, 240));
        cancelButton.setBackground(new Color(45, 45, 55));
        cancelButton.setForeground(new Color(230, 230, 240));

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(24, 24, 28));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        loginButton.addActionListener(e -> login());
        cancelButton.addActionListener(e -> cancel());

        getRootPane().setDefaultButton(loginButton);
    }

    private void loadUsers() {
        users = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("users.properties")) {
            if (input == null) {
                JOptionPane.showMessageDialog(this,
                    "users.properties not found in classpath.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            users.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to load users.properties.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void login() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        if (authenticate(username, password)) {
            succeeded = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private void cancel() {
        succeeded = false;
        dispose();
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    private boolean authenticate(String username, String password) {
        String expectedPassword = users.getProperty(username);
        return expectedPassword != null && expectedPassword.equals(password);
    }
}
