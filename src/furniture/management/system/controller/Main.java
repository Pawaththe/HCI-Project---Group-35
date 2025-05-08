package furniture.management.system.controller;

import furniture.management.system.view.ShowRoomPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Furniture Matrix");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ShowRoomPanel showroom = new ShowRoomPanel();
            frame.getContentPane().add(showroom);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen with title bar
            frame.setVisible(true);
        });
    }
}
