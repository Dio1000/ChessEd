package me.dariansandru.ui.gui.adminGUI;

import me.dariansandru.dbms.loggedUsers.LoggedAdmin;
import me.dariansandru.domain.Admin;
import me.dariansandru.utilities.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminMainPageGUI extends JFrame {

    private final JFrame frame = new JFrame("Admin Main Page");

    private final JButton organiseButton = new JButton("Organise");
    private final JButton loginButton = new JButton("Login");
    private final JButton registerButton = new JButton("Register");
    private final JButton exitButton = new JButton("Exit");

    private final JLabel loggedAdminLabel = new JLabel("You are not logged in yet!", SwingConstants.CENTER);

    private final Observer<Admin> loggedAdmin = new Observer<>();

    public AdminMainPageGUI() {
        LoggedAdmin.getLoggedAdmin().addObserver(loggedAdmin);
        loggedAdmin.addChangeListener(newAdmin -> {
            if (newAdmin == null) loggedAdminLabel.setText("You are not logged in yet!");
            else loggedAdminLabel.setText("Welcome " + newAdmin.getUsername() + "!");
        });
    }

    public Observer<Admin> getLoggedAdmin() {
        return loggedAdmin;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void drawGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400); 
        frame.setLayout(new BorderLayout());

        JLabel placeholderLabel = new JLabel("Chess Administration", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 24));
        placeholderLabel.setForeground(Color.BLUE);
        frame.add(placeholderLabel, BorderLayout.NORTH);

        loggedAdminLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loggedAdminLabel.setForeground(Color.DARK_GRAY);
        frame.add(loggedAdminLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleButton(organiseButton);
        styleButton(loginButton);
        styleButton(registerButton);
        styleButton(exitButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(organiseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(Color.LIGHT_GRAY);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void setLoginButtonAction(ActionListener action) {
        loginButton.addActionListener(action);
    }

    public void setRegisterButtonAction(ActionListener action) {
        registerButton.addActionListener(action);
    }

    public void setOrganiseButton(ActionListener action) {
        organiseButton.addActionListener(action);
    }

    public void setExitButtonAction(ActionListener action) {
        exitButton.addActionListener(action);
    }
}
