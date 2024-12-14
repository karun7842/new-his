package com.his.AppointmentSchedulingSystem;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;
import com.his.AppointmentSchedulingSystem.view.AppointmentView;

import javax.swing.*;

public class AppointmentScheduling {
    public static void main(String[] args) {
        // Set Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not applied. Falling back to default.");
        }

        // Launch Application
        SwingUtilities.invokeLater(() -> {
            AppointmentView view = new AppointmentView();
            AppointmentController controller = new AppointmentController(view);
            controller.viewAppointments();
            view.display();
        });
    }
}
