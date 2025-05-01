package me.dariansandru.ui.gui.playerGUI;

import me.dariansandru.dbms.loggedUsers.LoggedPlayer;
import me.dariansandru.domain.Player;
import me.dariansandru.utilities.observer.Observable;
import me.dariansandru.utilities.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainPageGUI extends JFrame {
    private JFrame frame;
    private JButton loginButton;
    private JButton registerButton;
    private JButton playHumanButton;
    private JButton playAIButton;
    private JButton statsButton;
    private JLabel messageLabel;
    private final Observer<Player> loggedPlayer = new Observer<>();
    public String loggedPlayerName;

    public Observer<Player> getLoggedPlayer() {
        return loggedPlayer;
    }

    public JFrame getFrame() {
        return frame;
    }

    public MainPageGUI() {
        LoggedPlayer.getLoggedPlayer().addObserver(loggedPlayer);
        loggedPlayer.addChangeListener(newPlayer -> {
            if (newPlayer == null) {
                messageLabel.setText("You are not logged in!");
            } else {
                loggedPlayerName = newPlayer.getUsername();
                messageLabel.setText("Welcome " + loggedPlayerName + "!");
            }
        });
    }

    public void drawGUI() {
        frame = new JFrame("Chess Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel placeholderLabel = new JLabel("ChessEd", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(placeholderLabel, BorderLayout.NORTH);

        if (messageLabel == null) {
            messageLabel = new JLabel("You are not logged in!", SwingConstants.CENTER);
        }

        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playHumanButton = new JButton("Play vs Human");
        playAIButton = new JButton("Play vs AI");
        statsButton = new JButton("Stats");
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        playHumanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(playHumanButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(playAIButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(statsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(registerButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void setLoginButtonAction(ActionListener action) {
        loginButton.addActionListener(action);
    }

    public void setRegisterButtonAction(ActionListener action) {
        registerButton.addActionListener(action);
    }

    public void setPlayHumanButtonAction(ActionListener action) {
        playHumanButton.addActionListener(action);
    }

    public void setPlayAIButtonAction(ActionListener action) {
        playAIButton.addActionListener(action);
    }

    public void setStatsButtonAction(ActionListener action) {
        statsButton.addActionListener(action);
    }
}