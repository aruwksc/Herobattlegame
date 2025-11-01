package org.example;

import org.example.facade.GameFacade;
import org.example.UI.GameUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFacade game = new GameFacade();
            GameUI gameUI = new GameUI(game);
            gameUI.setVisible(true);
        });
    }
}