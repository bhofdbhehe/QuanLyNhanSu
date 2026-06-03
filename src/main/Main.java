package main;

import gui.LoginGUI;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
            
            UIManager.put("Button.background", new Color(41, 128, 185));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            
            UIManager.put("Button.border", BorderFactory.createEmptyBorder(6, 20, 6, 20));
            
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("Button.focusPainted", false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}