package com.courseanalytics;

import com.courseanalytics.controller.AppController;
import com.courseanalytics.gui.LoginDialog;
import com.courseanalytics.gui.MainWindow;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            startApplication();
        });
    }

    private static void startApplication() {
        // Show login dialog first
        LoginDialog loginDialog = new LoginDialog(null);
        loginDialog.setVisible(true);

        if (!loginDialog.isSucceeded()) {
            System.out.println("Login cancelled. Exiting.");
            return;
        }

        // If login succeeded, launch main window
        MainWindow mainWin = new MainWindow();
        mainWin.setVisible(true);
    }
}
