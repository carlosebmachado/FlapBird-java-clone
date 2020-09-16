package me.carlosmachado.systems;

import javax.swing.JOptionPane;

public class ErrorMessage {

    public static void print(Exception e) {
        JOptionPane.showMessageDialog(null,
                "An error occurred.\nMessage: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
