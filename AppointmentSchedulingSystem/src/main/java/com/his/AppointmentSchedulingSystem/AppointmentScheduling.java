package com.his.AppointmentSchedulingSystem;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;
import com.his.AppointmentSchedulingSystem.view.AppointmentView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AppointmentScheduling {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Nimbus look and feel
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

                AppointmentView view = new AppointmentView();
                AppointmentController controller = new AppointmentController(view);
                controller.viewAppointments();
                view.display();
            } catch (Exception e) {
                System.err.println("Failed to launch the application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
