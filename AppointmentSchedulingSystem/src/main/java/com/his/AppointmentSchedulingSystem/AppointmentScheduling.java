package com.his.AppointmentSchedulingSystem;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;
import com.his.AppointmentSchedulingSystem.view.AppointmentView;

public class AppointmentScheduling {
    public static void main(String[] args) {
        AppointmentView view = new AppointmentView();
        AppointmentController controller = new  AppointmentController(view);
        controller.viewAppointments();
        view.display();
    }
}