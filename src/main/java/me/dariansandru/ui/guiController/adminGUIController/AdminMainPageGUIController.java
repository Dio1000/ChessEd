package me.dariansandru.ui.guiController.adminGUIController;

import me.dariansandru.ui.gui.adminGUI.AdminLoginPageGUI;
import me.dariansandru.ui.gui.adminGUI.AdminMainPageGUI;
import me.dariansandru.ui.gui.adminGUI.AdminOrganiserPageGUI;
import me.dariansandru.ui.gui.adminGUI.AdminRegisterPageGUI;
import me.dariansandru.ui.guiController.NavigationController;

import javax.swing.*;

public class AdminMainPageGUIController {
    private AdminMainPageGUI adminMainPageGUI;

    public AdminMainPageGUIController(AdminMainPageGUI adminMainPageGUI) {
        this.adminMainPageGUI = adminMainPageGUI;
    }

    public void setAdminMainPageGUI(AdminMainPageGUI adminMainPageGUI) {
        this.adminMainPageGUI = adminMainPageGUI;
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            adminMainPageGUI.drawGUI();

            adminMainPageGUI.setLoginButtonAction(e -> {
                if (adminMainPageGUI.getLoggedAdmin().getReference() != null) {
                    JOptionPane.showMessageDialog(null, "You are already logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                AdminLoginPageGUI adminLoginPageGUI = new AdminLoginPageGUI();
                NavigationController.navigateTo(adminLoginPageGUI.getFrame());
            });

            adminMainPageGUI.setRegisterButtonAction(e -> {
                if (adminMainPageGUI.getLoggedAdmin().getReference() != null) {
                    JOptionPane.showMessageDialog(null, "You are already logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                AdminRegisterPageGUI adminRegisterPageGUI = new AdminRegisterPageGUI();
                NavigationController.navigateTo(adminRegisterPageGUI.getFrame());
            });

            adminMainPageGUI.setOrganiseButton(e -> {
                if (adminMainPageGUI.getLoggedAdmin().getReference() == null) {
                    JOptionPane.showMessageDialog(null, "Please log in before accessing this page!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                AdminOrganiserPageGUI adminOrganiserPageGUI = new AdminOrganiserPageGUI();
                AdminOrganiserPageGUIController adminOrganiserPageGUIController = new AdminOrganiserPageGUIController(adminOrganiserPageGUI);
                NavigationController.navigateToAdminOrganiserPage(adminOrganiserPageGUIController);
            });

            adminMainPageGUI.setExitButtonAction(e -> {
                adminMainPageGUI.dispose();
            });

            NavigationController.setCurrentFrame(adminMainPageGUI);
        });
    }

}
