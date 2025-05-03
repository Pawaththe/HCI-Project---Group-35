package furniture.management.system.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoomConfigPanel extends JPanel {
    public final JTextField widthField = new JTextField("800", 5);
    public final JTextField heightField = new JTextField("600", 5);
    private final JComboBox<RoomShape> shapeBox = new JComboBox(RoomConfigPanel.RoomShape.values());
    public final JButton wallColorBtn = new JButton("Wall Color");
    public final JButton floorColorBtn = new JButton("Floor Color");
    private Color wallColor = new Color(240, 240, 245);
    private Color floorColor = new Color(210, 210, 210);
    private final Color accentColor = new Color(70, 130, 180);
    private final Color panelBg = new Color(250, 250, 252);

    public RoomConfigPanel() {
        this.initUI();
        this.setupEventHandlers();
        this.updateButtonColor(this.wallColorBtn, this.wallColor);
        this.updateButtonColor(this.floorColorBtn, this.floorColor);
    }

    private void initUI() {
        this.setBackground(this.panelBg);
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Room Configuration", 1, 2, new Font("Segoe UI", 1, 16), new Color(60, 60, 60)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = 21;
        gbc.fill = 2;
        this.styleTextField(this.widthField);
        this.styleTextField(this.heightField);
        this.styleComboBox(this.shapeBox);
        this.styleColorButton(this.wallColorBtn);
        this.styleColorButton(this.floorColorBtn);
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(this.createLabel("Width (px):"), gbc);
        gbc.gridx = 1;
        this.add(this.widthField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(this.createLabel("Height (px):"), gbc);
        gbc.gridx = 1;
        this.add(this.heightField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(this.createLabel("Shape:"), gbc);
        gbc.gridx = 1;
        this.add(this.shapeBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(this.wallColorBtn, gbc);
        gbc.gridx = 1;
        this.add(this.floorColorBtn, gbc);
    }

    public void setRoomWidth(float width) {
        this.widthField.setText(String.format("%.1f", width));
    }

    public void setRoomDepth(float depth) {
        this.heightField.setText(String.format("%.1f", depth));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", 0, 13));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", 0, 13));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        field.setBackground(Color.WHITE);
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", 0, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setFont(new Font("Segoe UI", 0, 13));
                return this;
            }
        });
    }

    private void styleColorButton(JButton button) {
        button.setFont(new Font("Segoe UI", 0, 13));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(2);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        button.setBackground(Color.WHITE);
    }

    private void setupEventHandlers() {
        this.wallColorBtn.addActionListener(this::chooseWallColor);
        this.floorColorBtn.addActionListener(this::chooseFloorColor);
    }

    private void chooseWallColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(this, "Choose Wall Color", this.wallColor);
        if (newColor != null) {
            this.wallColor = newColor;
            this.updateButtonColor(this.wallColorBtn, newColor);
        }

    }

    private void chooseFloorColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(this, "Choose Floor Color", this.floorColor);
        if (newColor != null) {
            this.floorColor = newColor;
            this.updateButtonColor(this.floorColorBtn, newColor);
        }

    }

    private void updateButtonColor(JButton button, Color color) {
        String buttonText = button == this.wallColorBtn ? "Wall Color" : "Floor Color";
        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        button.setText("<html><span style='color:" + hexColor + "'>&#9632;</span> " + buttonText + "</html>");
    }

    private Color getContrastColor(Color color) {
        double luminance = (0.299 * (double)color.getRed() + 0.587 * (double)color.getGreen() + 0.114 * (double)color.getBlue()) / 255.0;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public RoomShape getRoomShape() {
        return (RoomShape)this.shapeBox.getSelectedItem();
    }

    public Color getWallColor() {
        return this.wallColor;
    }

    public Color getFloorColor() {
        return this.floorColor;
    }

    public float getRoomWidth() {
        try {
            return Float.parseFloat(this.widthField.getText()) / 40.0F;
        } catch (NumberFormatException var2) {
            return 20.0F;
        }
    }

    public float getRoomDepth() {
        try {
            return Float.parseFloat(this.heightField.getText()) / 40.0F;
        } catch (NumberFormatException var2) {
            return 15.0F;
        }
    }

    public void addShapeChangeListener(Runnable listener) {
        this.shapeBox.addActionListener((e) -> {
            listener.run();
        });
    }

    public static enum RoomShape {
        RECTANGLE("Rectangle"),
        L_SHAPE("L-Shape"),
        U_SHAPE("U-Shape"),
        T_SHAPE("T-Shape"),
        SQUARE("Square"),
        CIRCULAR("Circular");

        private final String displayName;

        private RoomShape(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return this.displayName;
        }
    }
}
