package furniture.management.system.controller;

import javax.swing.*;
import furniture.management.system.view.Login;
import furniture.management.system.view.ShowRoomPanel;
import furniture.management.system.view.LoginSuccessListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize the Login panel with a listener for successful login
            new Login(new LoginSuccessListener() {
                @Override
                public void onLoginSuccess(String username) {
                    // On successful login, load the main application
                    JFrame frame = new JFrame("Furniture Matrix");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    ShowRoomPanel showroom = new ShowRoomPanel();
                    frame.getContentPane().add(showroom);

                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen with title bar
                    frame.setVisible(true);
                }
            });
        });
    }
}