package furniture.management.system.view;

import furniture.management.system.model.CoffeeTableManager;
import furniture.management.system.model.ComputerDeskManager;
import furniture.management.system.model.DiningChairManager;
import furniture.management.system.model.DiningTableManager;
import furniture.management.system.model.PantryCupboardManager;
import furniture.management.system.model.TvStandManager;
import furniture.management.system.model.WardrobeManager;
import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private CardLayout cardLayout;
    private Color backgroundColor = new Color(240, 240, 245);
    private Color menuColor = new Color(250, 250, 252);
    private Color accentColor = new Color(70, 130, 180);

    public Menu(String username) {
        super("Furniture Matrix" + username);
        initializeUI(username);
    }

    private void initializeUI(String username) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        setUndecorated(true); // Remove window decorations
        gd.setFullScreenWindow(this); // Set to full-screen exclusive mode

        getContentPane().setBackground(backgroundColor);

        JMenuBar menuBar = createModernMenuBar(username);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(createPanelWithBorder(new ShowRoomPanel()), "dashboardPanel");
        add(createPanelWithBorder(new CoffeeTableManager.CoffeeTableStartUpPanel()), "CoffeeTableStartUpPanel");
        add(createPanelWithBorder(new CoffeeTableManager.CoffeeTableDisplay(true)), "CoffeeTableDisplay");
        add(createPanelWithBorder(new CoffeeTableManager.CoffeeTable2DPanel()), "CoffeeTableDisplay2d");
        add(createPanelWithBorder(new ComputerDeskManager.ComputerDeskStartUpPanel()), "ComputerDeskStartUpPanel");
        add(createPanelWithBorder(new ComputerDeskManager.ComputerDeskDisplay(true)), "ComputerDeskDisplay");
        add(createPanelWithBorder(new ComputerDeskManager.ComputerDesk2DPanel()), "ComputerDeskDisplay2d");
        add(createPanelWithBorder(new DiningChairManager.DiningChairStartUpPanel()), "DiningChairStartUpPanel");
        add(createPanelWithBorder(new DiningChairManager.DiningChairDisplay(true)), "DiningChairDisplay");
        add(createPanelWithBorder(new DiningChairManager.DiningChair2DPanel()), "DiningChairDisplay2d");
        add(createPanelWithBorder(new DiningTableManager.DiningTableStartUpPanel()), "DiningTableStartUpPanel");
        add(createPanelWithBorder(new DiningTableManager.DiningTableDisplay(true)), "DiningTableDisplay");
        add(createPanelWithBorder(new DiningTableManager.DiningTable2DPanel()), "DiningTableDisplay2d");
        add(createPanelWithBorder(new PantryCupboardManager.PantryCupboardStartUpPanel()), "PantryCupboardStartUpPanel");
        add(createPanelWithBorder(new PantryCupboardManager.PantryCupboardDisplay(true)), "PantryCupboardDisplay");
        add(createPanelWithBorder(new PantryCupboardManager.PantryCupboard2DPanel()), "PantryCupboardDisplay2d");
        add(createPanelWithBorder(new TvStandManager.TvStandStartUpPanel()), "TvStandStartUpPanel");
        add(createPanelWithBorder(new TvStandManager.TvStandDisplay(true)), "TvStandDisplay");
        add(createPanelWithBorder(new TvStandManager.TvStand2DPanel()), "TvStandDisplay2d");
        add(createPanelWithBorder(new WardrobeManager.WardrobeStartUpPanel()), "WardrobeStartUpPanel");
        add(createPanelWithBorder(new WardrobeManager.WardrobeDisplay(true)), "WardrobeDisplay");
        add(createPanelWithBorder(new WardrobeManager.Wardrobe2DPanel()), "WardrobeDisplay2d");
        add(createPanelWithBorder(new SavedPanelsPanel()), "savedDesignPanel");

        applyModernStyles();
    }

    private JMenuBar createModernMenuBar(String username) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(menuColor);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.setPreferredSize(new Dimension(getWidth(), 60));

        JMenu menu = createIconMenu("Menu", "icons/Menu.png", 30);
        addMenuItems(menu);

        JMenu userMenu = createIconMenu(username, "icons/user.png", 26);
        addUserMenuItems(userMenu);

        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        menuBar.add(menu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userMenu);

        return menuBar;
    }

    private JMenu createIconMenu(String text, String iconPath, int iconHeight) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menu.setBackground(menuColor);

        ImageIcon icon = createImageIcon(iconPath, text + " Icon");
        if (icon != null) {
            menu.setIcon(new ImageIcon(icon.getImage().getScaledInstance(-1, iconHeight, Image.SCALE_SMOOTH)));
        }

        return menu;
    }

    private void addMenuItems(JMenu menu) {
        String[] items = {
                "Dashboard", "DiningChair", "DiningTable", "ComputerDesk",
                "Bed", "Cupboard", "TvStand", "PantryCupboard",
                "Wardrobe", "CoffeeTable1", "CoffeeTable2", "CoffeeTable3", "SavedDesigns"
        };

        for (String item : items) {
            JMenuItem menuItem = createModernMenuItem(item);
            menuItem.addActionListener(e -> cardLayout.show(getContentPane(), getPanelName(item)));
            menu.add(menuItem);
        }
    }

    private JMenuItem createModernMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(menuColor);
        item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        item.setIconTextGap(10);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(230, 240, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(menuColor);
            }
        });

        return item;
    }

    private String getPanelName(String menuText) {
        switch (menuText) {
            case "Dashboard":
                return "dashboardPanel";
            case "CoffeeTable1":
            case "CoffeeTable2":
            case "CoffeeTable3":
                return "CoffeeTableStartUpPanel";
            case "ComputerDesk":
                return "ComputerDeskStartUpPanel";
            case "DiningChair":
                return "DiningChairStartUpPanel";
            case "DiningTable":
                return "DiningTableStartUpPanel";
            case "PantryCupboard":
                return "PantryCupboardStartUpPanel";
            case "TvStand":
                return "TvStandStartUpPanel";
            case "Wardrobe":
                return "WardrobeStartUpPanel";
            case "Bed":
                return "BedStartUpPanel";
            case "SavedDesigns":
                return "savedDesignPanel";
            default:
                return menuText + "Panel";
        }
    }

    private void addUserMenuItems(JMenu userMenu) {
        JMenuItem userNameItem = new JMenuItem("Logged in as: " + userMenu.getText());
        userNameItem.setEnabled(false);
        userNameItem.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JMenuItem signOutItem = createModernMenuItem("Sign Out");
        signOutItem.addActionListener(e -> System.exit(0));

        userMenu.add(userNameItem);
        userMenu.addSeparator();
        userMenu.add(signOutItem);
    }

    private JPanel createPanelWithBorder(JComponent panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private void applyModernStyles() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource("/" + path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        }
        System.err.println("Couldn't find file: " + path);
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu("matrix");
            menu.setVisible(true);
            // Ensure the frame is set to full screen after being visible
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(menu);
        });
    }
}