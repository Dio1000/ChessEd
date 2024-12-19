package me.dariansandru.ui.gui.adminGUI;

import me.dariansandru.ui.guiController.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminOrganiserPageGUI extends JFrame {
    private JFrame frame;
    private JTextField banPlayerField;
    private JTextField modifyPlayerField;
    private JTextField modifyRatingField;
    private JTextField reportPlayerField;
    private JTextField reportReasonField;
    private JButton banPlayerButton;
    private JButton modifyRatingButton;
    private JButton reportPlayerButton;
    private JButton backButton;

    public JFrame getFrame() {
        return frame;
    }

    public AdminOrganiserPageGUI() {
        drawGUI();
    }

    public void drawGUI() {
        frame = new JFrame("Admin Organiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 400);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Admin Organiser", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Ban player:", SwingConstants.RIGHT));
        banPlayerField = new JTextField();
        mainPanel.add(banPlayerField);

        mainPanel.add(new JLabel("Modify rating of", SwingConstants.RIGHT));
        modifyPlayerField = new JTextField();
        mainPanel.add(modifyPlayerField);

        mainPanel.add(new JLabel("to:", SwingConstants.RIGHT));
        modifyRatingField = new JTextField();
        mainPanel.add(modifyRatingField);

        mainPanel.add(new JLabel("Report player", SwingConstants.RIGHT));
        reportPlayerField = new JTextField();
        mainPanel.add(reportPlayerField);

        mainPanel.add(new JLabel("for:", SwingConstants.RIGHT));
        reportReasonField = new JTextField();
        mainPanel.add(reportReasonField);

        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        banPlayerButton = new JButton("Ban Player");
        modifyRatingButton = new JButton("Modify Rating");
        reportPlayerButton = new JButton("Report Player");
        backButton = new JButton("Back");

        buttonsPanel.add(banPlayerButton);
        buttonsPanel.add(modifyRatingButton);
        buttonsPanel.add(reportPlayerButton);
        buttonsPanel.add(backButton);

        frame.add(buttonsPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> NavigationController.navigateToAdminMainPage());

        frame.setVisible(true);
    }

    public void setBanPlayerButton(ActionListener actionListener) {
        banPlayerButton.addActionListener(actionListener);
    }

    public void setModifyRatingButton(ActionListener actionListener) {
        modifyRatingButton.addActionListener(actionListener);
    }

    public void setReportPlayerButton(ActionListener actionListener) {
        reportPlayerButton.addActionListener(actionListener);
    }

    public JTextField getBanPlayerField() {
        return banPlayerField;
    }

    public JTextField getModifyPlayerField() {
        return modifyPlayerField;
    }

    public JTextField getModifyRatingField() {
        return modifyRatingField;
    }

    public JTextField getReportPlayerField() {
        return reportPlayerField;
    }

    public JTextField getReportReasonField() {
        return reportReasonField;
    }

}
