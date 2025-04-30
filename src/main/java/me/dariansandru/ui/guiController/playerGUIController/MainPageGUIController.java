package me.dariansandru.ui.guiController.playerGUIController;

import me.dariansandru.controller.ChessController;
import me.dariansandru.dbms.DBQuery;
import me.dariansandru.dbms.loggedUsers.LoggedPlayer;
import me.dariansandru.domain.Player;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;
import me.dariansandru.ui.gui.playerGUI.MainPageGUI;
import me.dariansandru.ui.gui.playerGUI.LoginPageGUI;
import me.dariansandru.ui.gui.playerGUI.RegisterPageGUI;
import me.dariansandru.ui.guiController.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainPageGUIController {

    private MainPageGUI mainPageGUI;

    public MainPageGUIController(MainPageGUI mainPageGUI) {
        this.mainPageGUI = mainPageGUI;
    }

    public void setMainPageGUI(MainPageGUI mainPageGUI) {
        this.mainPageGUI = mainPageGUI;
    }

    public void run() {
        mainPageGUI.drawGUI();

        mainPageGUI.setLoginButtonAction(e -> {
            if (mainPageGUI.getLoggedPlayer().getReference() != null) {
                JOptionPane.showMessageDialog(null, "You are already logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            LoginPageGUI loginPage = new LoginPageGUI();
            NavigationController.navigateTo(loginPage.getFrame());
        });

        mainPageGUI.setRegisterButtonAction(e -> {
            if (mainPageGUI.getLoggedPlayer().getReference() != null) {
                JOptionPane.showMessageDialog(null, "You are already logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            RegisterPageGUI registerPage = new RegisterPageGUI();
            NavigationController.navigateTo(registerPage.getFrame());
        });

        mainPageGUI.setPlayButtonAction(e -> {
            // TODO make playing without an account temporary possible
//            if (mainPageGUI.getLoggedPlayer().getReference() == null) {
//                JOptionPane.showMessageDialog(null, "You are not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }

            InputDevice inputDevice = new InputDevice();
            ChessGUIController guiController = getChessGUIController(inputDevice);
            guiController.run();
        });

        mainPageGUI.setStatsButtonAction(e -> {
            System.out.println(mainPageGUI.loggedPlayerName);
            List<String> playerData = DBQuery.getDataByUsername(mainPageGUI.loggedPlayerName);

            if (playerData.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Player data not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame statsFrame = new JFrame("Player Stats");
            statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            statsFrame.setSize(300, 200);
            statsFrame.setLayout(new BorderLayout());

            JPanel statsPanel = new JPanel();
            statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            String[] labels = {"Player ID", "Username", "Email", "Rating"};
            int[] indices = {0, 1, 2, 4};

            for (int i = 0; i < indices.length; i++) {
                JLabel label = new JLabel(labels[i] + ": " + playerData.get(indices[i]));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                statsPanel.add(label);
            }

            statsFrame.add(statsPanel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(event -> statsFrame.dispose());
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            statsFrame.add(buttonPanel, BorderLayout.SOUTH);

            statsFrame.setVisible(true);
        });

    }

    private static ChessGUIController getChessGUIController(InputDevice inputDevice) {
        OutputDevice outputDevice = new OutputDevice();

        Player p1 = new Player();
        Player p2 = new Player();

        ChessController chessController;
        try {
            chessController = new ChessController(p1, p2);
        } catch (InputException ex) {
            throw new RuntimeException(ex);
        }

        ChessConsoleUI chessConsoleUI = new ChessConsoleUI(inputDevice, outputDevice, chessController);
        ChessGUIController guiController = new ChessGUIController(chessConsoleUI);
        return guiController;
    }
}
