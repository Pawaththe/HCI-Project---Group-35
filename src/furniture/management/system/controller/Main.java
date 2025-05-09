package furniture.management.system.controller;

import furniture.management.system.view.Login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize the Login panel with a listener for successful login
            new Login();
        });
    }
}