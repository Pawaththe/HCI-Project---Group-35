package furniture.management.system.view;

import furniture.management.system.model.GLUtil.Material;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

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
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), "Furniture Configuration", 1, 2, new Font("Segoe UI SemiBold", 0, 16), new Color(80, 80, 80)));
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = 2;
        Font labelFont = new Font("Segoe UI", 0, 14);
        Font inputFont = new Font("Segoe UI", 0, 14);
        JLabel typeLabel = new JLabel("Furniture Type:");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(new Color(60, 60, 60));
        this.list = new JComboBox(new String[]{"DiningChair", "DiningTable", "ComputerDesk", "PantryCupboard", "TvStand", "Wardrobe", "CoffeeTable1", "CoffeeTable2", "CoffeeTable3", "Bed"});
        this.list.setFont(inputFont);
        this.list.setBackground(Color.WHITE);
        this.list.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        JLabel scaleLabel = new JLabel("Scale:");
        scaleLabel.setFont(labelFont);
        scaleLabel.setForeground(new Color(60, 60, 60));
        this.scaleField = new JTextField("1.0", 5);
        this.scaleField.setFont(inputFont);
        this.scaleField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        this.scaleField.setBackground(Color.WHITE);
        this.increaseSizeButton = this.createStyledButton("+");
        this.increaseSizeButton.setFont(new Font("Segoe UI", 0, 14));
        this.increaseSizeButton.setEnabled(false);
        this.decreaseSizeButton = this.createStyledButton("-");
        this.decreaseSizeButton.setFont(new Font("Segoe UI", 0, 14));
        this.decreaseSizeButton.setEnabled(false);
        this.currentScaleLabel = new JLabel("Current Scale: N/A");
        this.currentScaleLabel.setFont(labelFont);
        this.currentScaleLabel.setForeground(new Color(60, 60, 60));
        JLabel rotationLabel = new JLabel("Rotation (°):");
        rotationLabel.setFont(labelFont);
        rotationLabel.setForeground(new Color(60, 60, 60));
        this.rotationSlider = new JSlider(0, -180, 180, 0);
        this.rotationSlider.setFont(inputFont);
        this.rotationSlider.setBackground(new Color(250, 250, 250));
        this.rotationSlider.setMajorTickSpacing(90);
        this.rotationSlider.setMinorTickSpacing(15);
        this.rotationSlider.setPaintTicks(true);
        this.rotationSlider.setPaintLabels(true);
        this.rotationSlider.setEnabled(false);
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setFont(labelFont);
        colorLabel.setForeground(new Color(60, 60, 60));
        String[] colors = new String[]{"Brown Wood", "Tan Wood", "Orange Wood", "Dark Grey Metal", "Light Grey Metal", "Silver Metal", "Blue Plastic", "Magenta Plastic", "Cyan Plastic"};
        this.colorComboBox = new JComboBox(colors);
        this.colorComboBox.setFont(inputFont);
        this.colorComboBox.setBackground(Color.WHITE);
        this.colorComboBox.setEnabled(false);
        this.colorComboBox.addActionListener(this::updateColor);
        this.addBtn = this.createStyledButton("Add Furniture");
        this.deleteBtn = this.createStyledButton("Delete Selected");
        this.deleteBtn.setEnabled(false);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonPanel.add(this.addBtn);
        buttonPanel.add(this.deleteBtn);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = 22;
        this.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = 21;
        this.add(this.list, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.anchor = 22;
        this.add(scaleLabel, gbc);
        JPanel scalePanel = new JPanel(new FlowLayout(0, 5, 0));
        scalePanel.setBackground(new Color(250, 250, 250));
        scalePanel.add(this.scaleField);
        scalePanel.add(this.increaseSizeButton);
        scalePanel.add(this.decreaseSizeButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = 21;
        this.add(scalePanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.anchor = 22;
        this.add(new JLabel(" "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = 21;
        this.add(this.currentScaleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.anchor = 22;
        this.add(rotationLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = 21;
        this.add(this.rotationSlider, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.anchor = 22;
        this.add(colorLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = 21;
        this.add(this.colorComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = 10;
        this.add(buttonPanel, gbc);
        this.addBtn.addActionListener((e) -> {
            try {
                Float.parseFloat(this.scaleField.getText());
                onAdd.run();
            } catch (Exception var4) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for scale", "Input Error", 0);
            }

        });
        this.deleteBtn.addActionListener((e) -> {
            if (this.selectedItem != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this furniture?", "Confirm Deletion", 0);
                if (confirm == 0) {
                    onDelete.run();
                    this.setSelectedItem((RenderItem)null, (Runnable)null);
                }
            }

        });
        this.increaseSizeButton.addActionListener((e) -> {
            if (this.selectedItem != null) {
                this.selectedItem.scale = Math.min(5.0F, this.selectedItem.scale + 0.1F);
                this.updateCurrentScaleLabel();
                if (this.onChange != null) {
                    this.onChange.run();
                }
            }

        });
        this.decreaseSizeButton.addActionListener((e) -> {
            if (this.selectedItem != null) {
                this.selectedItem.scale = Math.max(0.1F, this.selectedItem.scale - 0.1F);
                this.updateCurrentScaleLabel();
                if (this.onChange != null) {
                    this.onChange.run();
                }
            }

        });
        this.rotationSlider.addChangeListener((e) -> {
            if (this.selectedItem != null) {
                this.selectedItem.rotationY = (float)this.rotationSlider.getValue();
                if (this.onChange != null) {
                    this.onChange.run();
                }
            }

        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (this.getModel().isPressed()) {
                    g2.setColor((new Color(0, 137, 123)).darker());
                } else if (this.getModel().isRollover()) {
                    g2.setColor(new Color(0, 137, 123));
                } else {
                    g2.setColor(new Color(0, 150, 136));
                }

                g2.fill(new RoundRectangle2D.Double(0.0, 0.0, (double)this.getWidth(), (double)this.getHeight(), 8.0, 8.0));
                g2.dispose();
                super.paintComponent(g);
            }

            protected void paintBorder(Graphics g) {
            }
        };
        button.setFont(new Font("Segoe UI SemiBold", 0, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(12));
        return button;
    }

    private void updateColor(ActionEvent e) {
        if (this.selectedItem != null) {
            String selectedColor = (String)this.colorComboBox.getSelectedItem();
            this.selectedItem.material = null;
            switch (selectedColor) {
                case "Brown Wood":
                    this.selectedItem.useWoodMaterial = true;
                    this.selectedItem.material = Material.WOOD;
                    this.selectedItem.color = new Color(153, 88, 0);
                    break;
                case "Tan Wood":
                    this.selectedItem.useWoodMaterial = true;
                    this.selectedItem.material = Material.WOOD;
                    this.selectedItem.color = new Color(196, 153, 79);
                    break;
                case "Orange Wood":
                    this.selectedItem.useWoodMaterial = true;
                    this.selectedItem.material = Material.WOOD;
                    this.selectedItem.color = new Color(250, 148, 59);
                    break;
                case "Dark Grey Metal":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.METAL;
                    this.selectedItem.color = new Color(107, 107, 107);
                    break;
                case "Light Grey Metal":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.METAL;
                    this.selectedItem.color = new Color(193, 214, 213);
                    break;
                case "Silver Metal":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.METAL;
                    this.selectedItem.color = new Color(245, 245, 245);
                    break;
                case "Blue Plastic":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.PLASTIC;
                    this.selectedItem.color = new Color(196, 15, 172);
                    break;
                case "Magenta Plastic":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.PLASTIC;
                    this.selectedItem.color = new Color(242, 205, 237);
                    break;
                case "Cyan Plastic":
                    this.selectedItem.useWoodMaterial = false;
                    this.selectedItem.material = Material.PLASTIC;
                    this.selectedItem.color = new Color(240, 228, 238);
            }

            if (this.onChange != null) {
                this.onChange.run();
            }
        }

    }

    public String getSelectedFurnitureType() {
        return (String)this.list.getSelectedItem();
    }

    public float getScale() {
        try {
            return Float.parseFloat(this.scaleField.getText());
        } catch (NumberFormatException var2) {
            return 1.0F;
        }
    }

    public void setSelectedItem(RenderItem item, Runnable onChange) {
        this.selectedItem = item;
        this.onChange = onChange;
        if (item != null) {
            this.rotationSlider.setValue((int)item.rotationY);
            this.rotationSlider.setEnabled(true);
            this.increaseSizeButton.setEnabled(true);
            this.decreaseSizeButton.setEnabled(true);
            this.colorComboBox.setEnabled(true);
            this.deleteBtn.setEnabled(true);
            this.updateCurrentScaleLabel();
            if (item.useWoodMaterial && item.material == Material.WOOD) {
                if (item.color.equals(new Color(153, 88, 0))) {
                    this.colorComboBox.setSelectedItem("Brown Wood");
                } else if (item.color.equals(new Color(196, 153, 79))) {
                    this.colorComboBox.setSelectedItem("Tan Wood");
                } else if (item.color.equals(new Color(250, 148, 59))) {
                    this.colorComboBox.setSelectedItem("Orange Wood");
                } else {
                    this.colorComboBox.setSelectedItem("Brown Wood");
                    item.color = new Color(153, 88, 0);
                    item.material = Material.WOOD;
                }
            } else if (item.material == Material.METAL) {
                if (item.color.equals(new Color(107, 107, 107))) {
                    this.colorComboBox.setSelectedItem("Dark Grey Metal");
                } else if (item.color.equals(new Color(193, 214, 213))) {
                    this.colorComboBox.setSelectedItem("Light Grey Metal");
                } else if (item.color.equals(new Color(245, 245, 245))) {
                    this.colorComboBox.setSelectedItem("Silver Metal");
                } else {
                    this.colorComboBox.setSelectedItem("Dark Grey Metal");
                    item.color = new Color(107, 107, 107);
                    item.material = Material.METAL;
                }
            } else if (item.material == Material.PLASTIC) {
                if (item.color.equals(new Color(196, 15, 172))) {
                    this.colorComboBox.setSelectedItem("Blue Plastic");
                } else if (item.color.equals(new Color(242, 205, 237))) {
                    this.colorComboBox.setSelectedItem("Magenta Plastic");
                } else if (item.color.equals(new Color(240, 228, 238))) {
                    this.colorComboBox.setSelectedItem("Cyan Plastic");
                } else {
                    this.colorComboBox.setSelectedItem("Blue Plastic");
                    item.color = new Color(196, 15, 172);
                    item.material = Material.PLASTIC;
                }
            } else {
                this.colorComboBox.setSelectedItem("Brown Wood");
                item.useWoodMaterial = true;
                item.material = Material.WOOD;
                item.color = new Color(153, 88, 0);
            }
        } else {
            this.rotationSlider.setValue(0);
            this.rotationSlider.setEnabled(false);
            this.increaseSizeButton.setEnabled(false);
            this.decreaseSizeButton.setEnabled(false);
            this.colorComboBox.setEnabled(false);
            this.deleteBtn.setEnabled(false);
            this.currentScaleLabel.setText("Current Scale: N/A");
        }

    }

    private void updateCurrentScaleLabel() {
        if (this.selectedItem != null) {
            this.currentScaleLabel.setText(String.format("Current Scale: %.1f", this.selectedItem.scale));
        }

    }
}
