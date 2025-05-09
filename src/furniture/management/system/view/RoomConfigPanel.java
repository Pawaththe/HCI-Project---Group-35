package furniture.management.system.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomConfigPanel extends JPanel {
    public final JTextField widthField = new JTextField("20", 5); // Default width in meters
    public final JTextField lengthField = new JTextField("15", 5); // Default height in meters
    private final JComboBox<RoomShape> shapeBox = new JComboBox<>(RoomShape.values());
    public final JButton wallColorBtn = new JButton("Wall Color");
    public final JButton floorColorBtn = new JButton("Floor Color");

    private Color wallColor = new Color(240, 240, 245); // Light gray-blue
    private Color floorColor = new Color(210, 210, 210); // Light gray

    // Modern color scheme
    private final Color accentColor = new Color(70, 130, 180); // Steel blue
    private final Color panelBg = new Color(250, 250, 252);

    public RoomConfigPanel() {
        initUI();
        setupEventHandlers();

        // Initialize with default colors
        updateButtonColor(wallColorBtn, wallColor);
        updateButtonColor(floorColorBtn, floorColor);
    }

    private void initUI() {
        // Modern panel styling
        setBackground(panelBg);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "Room Configuration",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 16),
                        new Color(60, 60, 60)
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input fields
        styleTextField(widthField);
        styleTextField(lengthField);

        // Shape combo box
        styleComboBox(shapeBox);

        // Color buttons
        styleColorButton(wallColorBtn);
        styleColorButton(floorColorBtn);

        // Layout components
        gbc.gridx = 0; gbc.gridy = 0; add(createLabel("Width (m):"), gbc);
        gbc.gridx = 1; add(widthField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(createLabel("Length (m):"), gbc);
        gbc.gridx = 1; add(lengthField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(createLabel("Shape:"), gbc);
        gbc.gridx = 1; add(shapeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(wallColorBtn, gbc);
        gbc.gridx = 1; add(floorColorBtn, gbc);
    }

    public void setRoomWidth(float width) {
        widthField.setText(String.format("%.1f", width));
    }

    public void setRoomDepth(float depth) {
        lengthField.setText(String.format("%.1f", depth));
    }

    public void setRoomShape(RoomShape shape) {
        shapeBox.setSelectedItem(shape);
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        for (ActionListener listener : shapeBox.getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    public void setWallColor(Color color) {
        this.wallColor = color;
        updateButtonColor(wallColorBtn, color);
    }

    public void setFloorColor(Color color) {
        this.floorColor = color;
        updateButtonColor(floorColorBtn, color);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setBackground(Color.WHITE);
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return this;
            }
        });
    }

    private void styleColorButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setBackground(Color.WHITE);
    }

    private void setupEventHandlers() {
        wallColorBtn.addActionListener(this::chooseWallColor);
        floorColorBtn.addActionListener(this::chooseFloorColor);
    }

    private void chooseWallColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                this,
                "Choose Wall Color",
                wallColor
        );
        if (newColor != null) {
            wallColor = newColor;
            updateButtonColor(wallColorBtn, newColor);
        }
    }

    private void chooseFloorColor(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(
                this,
                "Choose Floor Color",
                floorColor
        );
        if (newColor != null) {
            floorColor = newColor;
            updateButtonColor(floorColorBtn, newColor);
        }
    }

    private void updateButtonColor(JButton button, Color color) {
        String buttonText = button == wallColorBtn ? "Wall Color" : "Floor Color";
        String hexColor = String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
        button.setText("<html><span style='color:" + hexColor + "'>■</span> " + buttonText + "</html>");
    }

    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public RoomShape getRoomShape() { return (RoomShape) shapeBox.getSelectedItem(); }
    public Color getWallColor() { return wallColor; }
    public Color getFloorColor() { return floorColor; }

    public float getRoomWidth() {
        try { return Float.parseFloat(widthField.getText()); }
        catch (NumberFormatException e) { return 20f; }
    }

    public float getRoomDepth() {
        try { return Float.parseFloat(lengthField.getText()); }
        catch (NumberFormatException e) { return 15f; }
    }

    public void addShapeChangeListener(Runnable listener) {
        shapeBox.addActionListener(e -> listener.run());
    }

    public enum RoomShape {
        RECTANGLE("Rectangle"),
        L_SHAPE("L-Shape"),
        U_SHAPE("U-Shape"),
        T_SHAPE("T-Shape"),
        SQUARE("Square"),
        CIRCULAR("Circular");

        private final String displayName;

        RoomShape(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}