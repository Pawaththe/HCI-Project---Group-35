package furniture.management.system.view;

import furniture.management.system.model.GLUtil;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import static javax.swing.BorderFactory.createEmptyBorder;

public class FurnitureConfigPanel extends JPanel {
    private final JComboBox<String> list;
    private final JTextField scaleField;
    private final JButton addBtn;
    private final JButton deleteBtn;
    private final JSlider rotationSlider;
    private final JButton increaseSizeButton;
    private final JButton decreaseSizeButton;
    private final JLabel currentScaleLabel;
    private final JComboBox<String> colorComboBox;
    private RenderItem selectedItem;
    private Runnable onChange;
    private Runnable onDelete;

    public FurnitureConfigPanel(Runnable onAdd, Runnable onDelete) {
        this.onDelete = onDelete;

        setBorder(BorderFactory.createTitledBorder(
                createEmptyBorder(15, 15, 15, 15),
                "Furniture Configuration",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI SemiBold", Font.PLAIN, 16),
                new Color(80, 80, 80)
        ));

        setLayout(new GridBagLayout());
        setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Furniture Type
        JLabel typeLabel = new JLabel("Furniture Type:");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(new Color(60, 60, 60));

        list = new JComboBox<>(new String[]{
                "DiningChair", "DiningTable", "ComputerDesk",
                "PantryCupboard", "TvStand", "Wardrobe",
                "CoffeeTable1", "CoffeeTable2", "CoffeeTable3", "Bed"
        });
        list.setFont(inputFont);
        list.setBackground(Color.WHITE);
        list.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                createEmptyBorder(5, 8, 5, 8)
        ));

        // Scale Controls
        JLabel scaleLabel = new JLabel("Scale:");
        scaleLabel.setFont(labelFont);
        scaleLabel.setForeground(new Color(60, 60, 60));

        scaleField = new JTextField("1.0", 5);
        scaleField.setFont(inputFont);
        scaleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                createEmptyBorder(5, 8, 5, 8)
        ));
        scaleField.setBackground(Color.WHITE);

        increaseSizeButton = createStyledButton("+");
        increaseSizeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        increaseSizeButton.setEnabled(false);

        decreaseSizeButton = createStyledButton("-");
        decreaseSizeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        decreaseSizeButton.setEnabled(false);

        currentScaleLabel = new JLabel("Current Scale: N/A");
        currentScaleLabel.setFont(labelFont);
        currentScaleLabel.setForeground(new Color(60, 60, 60));

        // Rotation Slider
        JLabel rotationLabel = new JLabel("Rotation (°):");
        rotationLabel.setFont(labelFont);
        rotationLabel.setForeground(new Color(60, 60, 60));

        rotationSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        rotationSlider.setFont(inputFont);
        rotationSlider.setBackground(new Color(250, 250, 250));
        rotationSlider.setMajorTickSpacing(90);
        rotationSlider.setMinorTickSpacing(15);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setPaintLabels(true);
        rotationSlider.setEnabled(false);

        // Color Selection
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setFont(labelFont);
        colorLabel.setForeground(new Color(60, 60, 60));

        String[] colors = {
                "Brown Wood", "Tan Wood", "Orange Wood",
                "Dark Grey Metal", "Light Grey Metal", "Silver Metal",
                "Blue Plastic", "Magenta Plastic", "Cyan Plastic"
        };
        colorComboBox = new JComboBox<>(colors);
        colorComboBox.setFont(inputFont);
        colorComboBox.setBackground(Color.WHITE);

        colorComboBox.setEnabled(false);
        colorComboBox.addActionListener(this::updateColor);

        // Action Buttons
        addBtn = createStyledButton("Add Furniture");
        deleteBtn = createStyledButton("Delete Selected");
        deleteBtn.setEnabled(false);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.setBorder(createEmptyBorder(0, 0, 0, 0));
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);

        // Layout components
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        add(typeLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        add(list, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        add(scaleLabel, gbc);

        JPanel scalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        scalePanel.setBackground(new Color(250, 250, 250));
        scalePanel.add(scaleField);
        scalePanel.add(increaseSizeButton);
        scalePanel.add(decreaseSizeButton);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        add(scalePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(" "), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        add(currentScaleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        add(rotationLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        add(rotationSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        add(colorLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        add(colorComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Event Listeners
        addBtn.addActionListener(e -> {
            try {
                Float.parseFloat(scaleField.getText());
                onAdd.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number for scale",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            if (selectedItem != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this furniture?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    onDelete.run();
                    setSelectedItem(null, null);
                }
            }
        });

        increaseSizeButton.addActionListener(e -> {
            if (selectedItem != null) {
                selectedItem.scale = Math.min(5.0f, selectedItem.scale + 0.1f);
                updateCurrentScaleLabel();
                if (onChange != null) onChange.run();
            }
        });

        decreaseSizeButton.addActionListener(e -> {
            if (selectedItem != null) {
                selectedItem.scale = Math.max(0.1f, selectedItem.scale - 0.1f);
                updateCurrentScaleLabel();
                if (onChange != null) onChange.run();
            }
        });

        rotationSlider.addChangeListener(e -> {
            if (selectedItem != null) {
                selectedItem.rotationY = rotationSlider.getValue();
                if (onChange != null) onChange.run();
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 137, 123).darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 137, 123));
                } else {
                    g2.setColor(new Color(0, 150, 136));
                }
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setFont(new Font("Segoe UI SemiBold", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void updateColor(ActionEvent e) {
        if (selectedItem != null) {
            String selectedColor = (String) colorComboBox.getSelectedItem();
            selectedItem.material = null;
            switch (selectedColor) {
                case "Brown Wood":
                    selectedItem.useWoodMaterial = true;
                    selectedItem.material = GLUtil.Material.WOOD;
                    selectedItem.color = new Color(153, 88, 0);
                    break;
                case "Tan Wood":
                    selectedItem.useWoodMaterial = true;
                    selectedItem.material = GLUtil.Material.WOOD;
                    selectedItem.color = new Color(196, 153, 79);
                    break;
                case "Orange Wood":
                    selectedItem.useWoodMaterial = true;
                    selectedItem.material = GLUtil.Material.WOOD;
                    selectedItem.color = new Color(250, 148, 59);
                    break;
                case "Dark Grey Metal":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.METAL;
                    selectedItem.color = new Color(107, 107, 107);
                    break;
                case "Light Grey Metal":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.METAL;
                    selectedItem.color = new Color(193, 214, 213);
                    break;
                case "Silver Metal":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.METAL;
                    selectedItem.color = new Color(245, 245, 245);
                    break;
                case "Blue Plastic":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.PLASTIC;
                    selectedItem.color = new Color(196, 15, 172);
                    break;
                case "Magenta Plastic":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.PLASTIC;
                    selectedItem.color = new Color(242, 205, 237);
                    break;
                case "Cyan Plastic":
                    selectedItem.useWoodMaterial = false;
                    selectedItem.material = GLUtil.Material.PLASTIC;
                    selectedItem.color = new Color(240, 228, 238);
                    break;
            }
            if (onChange != null) onChange.run();
        }
    }

    public String getSelectedFurnitureType() {
        return (String) list.getSelectedItem();
    }

    public float getScale() {
        try {
            return Float.parseFloat(scaleField.getText());
        } catch (NumberFormatException e) {
            return 1.0f;
        }
    }

    public void setSelectedItem(RenderItem item, Runnable onChange) {
        this.selectedItem = item;
        this.onChange = onChange;
        if (item != null) {
            rotationSlider.setValue((int) item.rotationY);
            rotationSlider.setEnabled(true);
            increaseSizeButton.setEnabled(true);
            decreaseSizeButton.setEnabled(true);
            colorComboBox.setEnabled(true);
            deleteBtn.setEnabled(true);
            updateCurrentScaleLabel();

            if (item.useWoodMaterial && item.material == GLUtil.Material.WOOD) {
                if (item.color.equals(new Color(153, 88, 0))) {
                    colorComboBox.setSelectedItem("Brown Wood");
                } else if (item.color.equals(new Color(196, 153, 79))) {
                    colorComboBox.setSelectedItem("Tan Wood");
                } else if (item.color.equals(new Color(250, 148, 59))) {
                    colorComboBox.setSelectedItem("Orange Wood");
                } else {
                    colorComboBox.setSelectedItem("Brown Wood");
                    item.color = new Color(153, 88, 0);
                    item.material = GLUtil.Material.WOOD;
                }
            } else if (item.material == GLUtil.Material.METAL) {
                if (item.color.equals(new Color(107, 107, 107))) {
                    colorComboBox.setSelectedItem("Dark Grey Metal");
                } else if (item.color.equals(new Color(193, 214, 213))) {
                    colorComboBox.setSelectedItem("Light Grey Metal");
                } else if (item.color.equals(new Color(245, 245, 245))) {
                    colorComboBox.setSelectedItem("Silver Metal");
                } else {
                    colorComboBox.setSelectedItem("Dark Grey Metal");
                    item.color = new Color(107, 107, 107);
                    item.material = GLUtil.Material.METAL;
                }
            } else if (item.material == GLUtil.Material.PLASTIC) {
                if (item.color.equals(new Color(196, 15, 172))) {
                    colorComboBox.setSelectedItem("Blue Plastic");
                } else if (item.color.equals(new Color(242, 205, 237))) {
                    colorComboBox.setSelectedItem("Magenta Plastic");
                } else if (item.color.equals(new Color(240, 228, 238))) {
                    colorComboBox.setSelectedItem("Cyan Plastic");
                } else {
                    colorComboBox.setSelectedItem("Blue Plastic");
                    item.color = new Color(196, 15, 172);
                    item.material = GLUtil.Material.PLASTIC;
                }
            } else {
                colorComboBox.setSelectedItem("Brown Wood");
                item.useWoodMaterial = true;
                item.material = GLUtil.Material.WOOD;
                item.color = new Color(153, 88, 0);
            }
        } else {
            rotationSlider.setValue(0);
            rotationSlider.setEnabled(false);
            increaseSizeButton.setEnabled(false);
            decreaseSizeButton.setEnabled(false);
            colorComboBox.setEnabled(false);
            deleteBtn.setEnabled(false);
            currentScaleLabel.setText("Current Scale: N/A");
        }
    }

    private void updateCurrentScaleLabel() {
        if (selectedItem != null) {
            currentScaleLabel.setText(String.format("Current Scale: %.1f", selectedItem.scale));
        }
    }
}