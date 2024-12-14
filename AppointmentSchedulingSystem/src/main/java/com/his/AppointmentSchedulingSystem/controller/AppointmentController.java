package com.his.AppointmentSchedulingSystem.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.his.AppointmentSchedulingSystem.model.Appointment;
import com.his.AppointmentSchedulingSystem.model.Doctor;
import com.his.AppointmentSchedulingSystem.model.Patient;
import com.his.AppointmentSchedulingSystem.model.Status;
import com.his.AppointmentSchedulingSystem.view.AppointmentFormDialog;
import com.his.AppointmentSchedulingSystem.view.AppointmentView;

public class AppointmentController {
    private List<Appointment> appointments;
    private AppointmentView view;
    private List<Appointment> filteredAppointments;
    String filePath = "appointments.json";
    int mrdFilter = 0;
    static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public AppointmentController(AppointmentView view) {
        this.view = view;
        loadAppointments();

        view.getScheduleButton().addActionListener(e -> scheduleAppointment());
        view.getRescheduleButton().addActionListener(e -> rescheduleAppointment());
        view.getCancelButton().addActionListener(e -> cancelAppointment());
        view.getResetButton().addActionListener(e -> resetFilters());

        view.getFilterMrdField().addActionListener(e -> applyFilters());
        view.getFilterDoctorField().addActionListener(e -> applyFilters());
        view.getFilterSpecialityField().addActionListener(e -> applyFilters());
    }

    public void viewAppointments() {
        loadAppointments();
        updateTable(appointments);
    }

    private void loadAppointments() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                appointments = mapper.readValue(file, new TypeReference<List<Appointment>>() {
                });
            } catch (IOException e) {
                appointments = new ArrayList<Appointment>();
            }
        }
    }

    private void saveAppointments() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), appointments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTable(List<Appointment> listToDisplay) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);

        for (Appointment a : listToDisplay) {
            Doctor doctor = a.getDoctor();
            String doctorName = (doctor != null) ? doctor.getName() : "Unknown";
            double consultationFee = (doctor != null) ? doctor.getConsultationFee() : 0.0;
            String department = (doctor != null) ? doctor.getDepartment() : "Unknown";
            String specialization = (doctor != null) ? doctor.getSpecialization() : "Unknown";

            model.addRow(new Object[]{
                a.getPatient().getName(),
                a.getPatient().getTokenNumber(),
                a.getPatient().getPhoneNumber(),
                doctorName,
                consultationFee,
                department,
                specialization,
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getStatus()
            });
        }
        model.fireTableDataChanged();
    }


    private void applyFilters() {
        if (!view.getFilterMrdField().getText().isEmpty()) {
            mrdFilter = Integer.parseInt(view.getFilterMrdField().getText());
        }

        String doctorFilter = view.getFilterDoctorField().getText();
        String specialityFilter = view.getFilterSpecialityField().getText();

        filteredAppointments = appointments.stream()
                .filter(a -> (mrdFilter == 0 || a.getPatient().getTokenNumber() == mrdFilter)
                        && (doctorFilter.isEmpty() || a.getDoctor().getName().equalsIgnoreCase(doctorFilter))
                        && (specialityFilter.isEmpty()
                        || a.getDoctor().getSpecialization().equalsIgnoreCase(specialityFilter)))
                .collect(Collectors.toList());

        updateTable(filteredAppointments);
    }

    private void resetFilters() {
        view.getFilterMrdField().setText("");
        view.getFilterDoctorField().setText("");
        view.getFilterSpecialityField().setText("");
        updateTable(appointments);
    }

    private void scheduleAppointment() {
        AppointmentFormDialog formDialog = new AppointmentFormDialog(null);
        formDialog.setVisible(true);
        Appointment newAppointment = formDialog.getAppointment();
        if (newAppointment != null) {
            appointments.add(newAppointment);
            saveAppointments();
            updateTable(appointments);
        }
    }

    private void rescheduleAppointment() {
        int selectedRow = view.getAppointmentTable().getSelectedRow();
        if (selectedRow != -1) {
            Appointment appointmentToReschedule = appointments.get(selectedRow);
            AppointmentFormDialog formDialog = new AppointmentFormDialog(appointmentToReschedule);
            formDialog.getMriIdTextField().setEnabled(false);
            formDialog.getSearchButton().setEnabled(false);
            formDialog.setVisible(true);
            Appointment rescheduledAppointment = formDialog.getAppointment();
            if (rescheduledAppointment != null) {
                appointments.set(selectedRow, rescheduledAppointment);
                saveAppointments();
                updateTable(appointments);
            }
        } else {
            JOptionPane.showMessageDialog(view.getAppointmentTable(), "Please select an appointment to reschedule.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cancelAppointment() {
        int selectedRow = view.getAppointmentTable().getSelectedRow();
        if (selectedRow != -1) {
            Appointment appointment = appointments.get(selectedRow);
            appointment.setStatus(Status.CANCELED);
            saveAppointments();
            updateTable(appointments);
        }
    }

    public static ArrayList<Patient> loadPatients() {
        File file = new File("patient.json");
        if (file.exists()) {
            try {
                return mapper.readValue(file, new TypeReference<ArrayList<Patient>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<Patient>();
    }

    public static ArrayList<Doctor> loadDoctors() {
        File file = new File("Doctor.json");
        if (file.exists()) {
            try {
                return mapper.readValue(file, new TypeReference<ArrayList<Doctor>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<Doctor>();
    }
}
