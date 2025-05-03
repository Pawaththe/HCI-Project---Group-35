package furniture.management.system.controller;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Furniture Matrix");
            frame.setDefaultCloseOperation(3);
            ShowRoomPanel showroom = new ShowRoomPanel();
            frame.getContentPane().add(showroom);
            frame.pack();
            frame.setLocationRelativeTo((Component)null);
            frame.setVisible(true);
        });
    }
}