package com.cristoffer85.States.StatesResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.awt.event.ActionListener;

public class StateDesign extends JPanel {
    protected static final Color BACKGROUND_COLOR = Color.ORANGE;
    protected static final int MIDDLE_PANEL_OFFSET = 36;
    protected static final Color BUTTON_COLOR = Color.ORANGE;
    protected Font menuButtonFontAndSize = loadCustomFont("/Retro-pixelfont.ttf", 44f);
    protected static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
    protected static final Font SWITCH_USER_FONTANDSIZE = new Font("Arial", Font.PLAIN, 12);
    protected static final int MENUBUTTON_VERTICAL_SPACING = 20;

    protected Image logoImage;
    private JLabel titleLabel;
    private JLabel logoLabel;
    private int originalLogoWidth;
    private int originalLogoHeight;
    private final double logoInitialScaleFactor = 1.8;

    public StateDesign() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentSizes();
            }
        });
    }

    protected JPanel createVerticalPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    protected JPanel createCombinedVerticalAndHorizontalPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    protected JButton regularMenuButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(menuButtonFontAndSize);
        button.setForeground(Color.BLACK);
        button.setBackground(BUTTON_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(5, 5, 1, 2));
        return button;
    }

    protected JButton switchUserButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(SWITCH_USER_FONTANDSIZE);
        button.setForeground(Color.BLACK);
        button.setBackground(BUTTON_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(5, 5, 1, 2));
        return button;
    }

    public Font loadCustomFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(path)).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return DEFAULT_FONT;
        }
    }

    protected void addLogo(JPanel logoPanel) {
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Logo.png"));
            logoImage = logoIcon.getImage();

            originalLogoWidth = logoImage.getWidth(null);
            originalLogoHeight = logoImage.getHeight(null);

            int initialWidth = (int) (originalLogoWidth * logoInitialScaleFactor);
            int initialHeight = (int) (originalLogoHeight * logoInitialScaleFactor);
            Image scaledImage = logoImage.getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));

            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            logoPanel.add(Box.createVerticalStrut(100));
            logoPanel.add(logoLabel);
            logoPanel.add(Box.createVerticalGlue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addTitle(JPanel panel, String title) {
        panel.add(Box.createVerticalGlue());
        titleLabel = new JLabel(title);
        titleLabel.setFont(menuButtonFontAndSize);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(MIDDLE_PANEL_OFFSET));
        panel.add(Box.createVerticalGlue());
        panel.setBackground(BACKGROUND_COLOR);
    }


    private void adjustComponentSizes() {
        Dimension size = getSize();
        float overallScaleFactor = Math.min(size.width / 1920f, size.height / 1080f);
        Font scaledFont = menuButtonFontAndSize.deriveFont(44f * overallScaleFactor);
    
        for (Component component : getComponents()) {
            if (component instanceof JButton) {
                component.setFont(scaledFont);
            } else if (component instanceof JPanel) {
                for (Component subComponent : ((JPanel) component).getComponents()) {
                    if (subComponent instanceof JButton) {
                        subComponent.setFont(scaledFont);
                    } else if (subComponent instanceof JLabel) {
                        JLabel label = (JLabel) subComponent;
                        if (label.getIcon() != null && label == logoLabel) {
                            // Calculate a new scale factor for the logo.
                            // For example, only allow the logo to shrink but not enlarge beyond its initial size:
                            double logoScale = overallScaleFactor < 1 ? overallScaleFactor : logoInitialScaleFactor;
                            int width = (int) (originalLogoWidth * logoScale);
                            int height = (int) (originalLogoHeight * logoScale);
                            Image scaledImage = logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                            label.setIcon(new ImageIcon(scaledImage));
                        } else {
                            label.setFont(scaledFont);
                        }
                    }
                }
            }
        }
        revalidate();
        repaint();
    }
    

}