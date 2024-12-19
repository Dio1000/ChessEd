package me.dariansandru.ui.guiController.adminGUIController;

import me.dariansandru.dbms.DBQuery;
import me.dariansandru.dbms.DBUpdater;
import me.dariansandru.dbms.loggedUsers.LoggedAdmin;
import me.dariansandru.domain.Admin;
import me.dariansandru.ui.gui.adminGUI.AdminOrganiserPageGUI;
import me.dariansandru.utilities.observer.Observable;
import me.dariansandru.utilities.observer.Observer;

import javax.swing.*;

public class AdminOrganiserPageGUIController {
    private final AdminOrganiserPageGUI adminOrganiserPageGUI;
    private final Observer<Admin> loggedAdminObserver = new Observer<>();
    private String loggedAdmin = null;

    public AdminOrganiserPageGUIController(AdminOrganiserPageGUI adminOrganiserPageGUI) {
        this.adminOrganiserPageGUI = adminOrganiserPageGUI;

        loggedAdminObserver.addChangeListener(admin -> {
            loggedAdmin = admin.getUsername();
        });
    }

    public AdminOrganiserPageGUI getAdminOrganiserPageGUI() {
        return adminOrganiserPageGUI;
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            adminOrganiserPageGUI.drawGUI();

            adminOrganiserPageGUI.setBanPlayerButton(e -> handleBan());
            adminOrganiserPageGUI.setModifyRatingButton(e -> handleModifyRating());
            adminOrganiserPageGUI.setReportPlayerButton(e -> handleReportPlayer());
        });
    }

    boolean playerValidation(String player){
        if (player == null || player.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a player name!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }

        if (!DBQuery.playerExists(player)) {
            JOptionPane.showMessageDialog(null, "Player " + player + " does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }

        return false;
    }

    boolean adminPermissionValidation(String permission){
        if (permission == null || permission.isEmpty()) return false;
        return DBQuery.hasPermission(loggedAdmin, permission);
    }

    private void handleBan() {
        if (adminPermissionValidation("Ban Permission")){
            JOptionPane.showMessageDialog(null, "You do not have 'Ban Permission'!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String player = adminOrganiserPageGUI.getBanPlayerField().getText();
        if (playerValidation(player)) return;

        DBUpdater.deletePlayer(player);
        JOptionPane.showMessageDialog(null, "Player " + player + " has been banned!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleModifyRating() {
        if (adminPermissionValidation("Modify Rating Permission")){
            JOptionPane.showMessageDialog(null, "You do not have 'Modify Rating Permission'!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String player = adminOrganiserPageGUI.getModifyPlayerField().getText();
        int newRating = Integer.parseInt(adminOrganiserPageGUI.getModifyRatingField().getText());
        if (playerValidation(player)) return;

        DBUpdater.updateRating(player, newRating);
        JOptionPane.showMessageDialog(null, "Player " + player + "'s rating has been modified!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleReportPlayer() {
        if (adminPermissionValidation("Report Hacker Permission")){
            JOptionPane.showMessageDialog(null, "You do not have 'Report Hacker Permission'!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String player = adminOrganiserPageGUI.getReportPlayerField().getText();
        String report = adminOrganiserPageGUI.getReportReasonField().getText();
        if (playerValidation(player)) return;

        DBUpdater.insertReport(Integer.parseInt(DBQuery.getAdminDataByUsername(loggedAdmin).get(0)),
                Integer.parseInt(DBQuery.getDataByUsername(player).get(0)), report);
        JOptionPane.showMessageDialog(null, "Report for " + player + " has been submitted!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
