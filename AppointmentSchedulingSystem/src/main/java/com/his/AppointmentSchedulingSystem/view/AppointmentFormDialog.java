package com.his.AppointmentSchedulingSystem.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;
import com.his.AppointmentSchedulingSystem.model.Appointment;
import com.his.AppointmentSchedulingSystem.model.Doctor;
import com.his.AppointmentSchedulingSystem.model.Patient;
import com.toedter.calendar.JDateChooser;

public class AppointmentFormDialog extends JDialog {
	private JTextField mriIdTextField;
	private JButton searchButton;
	private JTextField patientNameField, patientPhoneField, patientEmailField;
	private JComboBox<String> departmentComboBox, specializationComboBox, doctorNameComboBox;
	private JDateChooser appointmentDateChooser;
	private JSpinner appointmentTimeSpinner;
	private JButton submitButton, cancelButton, scheduleAssistantButton;
	private Appointment appointment;
	private Patient selectedPatient;
	private Doctor selectedDoctor;
	private JPanel buttonPanel;

	private ArrayList<Doctor> doctors;
	private ArrayList<Patient> patients;
	private Set<String> specializationArray = new HashSet<>();
	private Set<String> doctorsSet = new HashSet<>();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

	public AppointmentFormDialog(Appointment appointment) {
		setTitle("Schedule Appointment");
		setModal(true);
		setSize(600, 500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout(10, 10));

		JPanel formPanel = new JPanel(new GridLayout(12, 2, 10, 10));
		mainPanel.add(formPanel, BorderLayout.CENTER);

		patients = AppointmentController.loadPatients();
		doctors = AppointmentController.loadDoctors();

		// MRI ID Field and Search Button
		formPanel.add(new JLabel("MRI ID:"));
		JPanel mriIdPanel = new JPanel(new BorderLayout());
		mriIdTextField = new JTextField();
		mriIdPanel.add(mriIdTextField, BorderLayout.CENTER);
		searchButton = new JButton(new ImageIcon(
				new ImageIcon("C:\\Users\\Milind P\\QuestWorkspace\\AppointmentSchedulingSystem\\images\\lens.png")
						.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new java.awt.Dimension(25, 25));
		searchButton.addActionListener(e -> searchPatient());
		mriIdPanel.add(searchButton, BorderLayout.EAST);
		formPanel.add(mriIdPanel);

		// Patient Information Fields
		formPanel.add(new JLabel("Patient Name:"));
		patientNameField = new JTextField();
		patientNameField.setEditable(false);
		formPanel.add(patientNameField);

		formPanel.add(new JLabel("Patient Phone:"));
		patientPhoneField = new JTextField();
		patientPhoneField.setEditable(false);
		formPanel.add(patientPhoneField);

		formPanel.add(new JLabel("Patient Email:"));
		patientEmailField = new JTextField();
		patientEmailField.setEditable(false);
		formPanel.add(patientEmailField);

		// Department, Specialization, and Doctor Fields
		formPanel.add(new JLabel("Department:"));
		for (Doctor doctor : doctors) {
			specializationArray.add(doctor.getDepartment());
		}
		departmentComboBox = new JComboBox<>(specializationArray.toArray(new String[0]));
		formPanel.add(departmentComboBox);

		formPanel.add(new JLabel("Specialization:"));
		specializationComboBox = new JComboBox<>();
		formPanel.add(specializationComboBox);

		formPanel.add(new JLabel("Doctor Name:"));
		doctorNameComboBox = new JComboBox<>();
		formPanel.add(doctorNameComboBox);

		// Appointment Date and Time
		formPanel.add(new JLabel("Appointment Date:"));
		appointmentDateChooser = new JDateChooser();
		formPanel.add(appointmentDateChooser);

		formPanel.add(new JLabel("Appointment Time:"));
		appointmentTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(appointmentTimeSpinner, "hh:mm a");
		appointmentTimeSpinner.setEditor(timeEditor);
		appointmentTimeSpinner.setValue(new Date());
		formPanel.add(appointmentTimeSpinner);
		formPanel.add(new JLabel());

		// Buttons Panel
		buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		scheduleAssistantButton = new JButton("Schedule Assistant");
		scheduleAssistantButton.addActionListener(e -> openScheduleAssistantWindow());
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerPanel.add(scheduleAssistantButton);

		JPanel secondPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");

		secondPanel.add(submitButton);
		secondPanel.add(cancelButton);

		buttonPanel.add(centerPanel);
		buttonPanel.add(secondPanel);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(mainPanel);

		submitButton.addActionListener(e -> submit());
		cancelButton.addActionListener(e -> cancel());

		departmentComboBox.addActionListener(e -> updateSpecializationComboBox());
		specializationComboBox.addActionListener(e -> updateDoctorComboBox());

		// Pre-fill the form if an appointment is provided
		if (appointment != null) {
			this.appointment = appointment;
			selectedPatient = appointment.getPatient();
			selectedDoctor = appointment.getDoctor();
			mriIdTextField.setText(String.valueOf(selectedPatient.getTokenNumber()));
			patientNameField.setText(selectedPatient.getName());
			patientPhoneField.setText(String.valueOf(selectedPatient.getPhoneNumber()));
			patientEmailField.setText(selectedPatient.getEmail());
			departmentComboBox.setSelectedItem(selectedDoctor.getDepartment());
			specializationComboBox.setSelectedItem(selectedDoctor.getSpecialization());
			doctorNameComboBox.setSelectedItem(selectedDoctor.getName());
			try {
				Date date = new SimpleDateFormat("dd LLLL yyyy").parse(appointment.getAppointmentDate());
				appointmentDateChooser.setDate(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Date time = new SimpleDateFormat("hh:mm a").parse(appointment.getAppointmentTime());
				appointmentTimeSpinner.setValue(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Disable MRI ID field and search button when rescheduling
			mriIdTextField.setEnabled(false);
			searchButton.setEnabled(false);
		}
	}

	private void openScheduleAssistantWindow() {
		JDialog scheduleAssistantDialog = new JDialog(this, "Schedule Assistant", true);
		scheduleAssistantDialog.setSize(400, 300);
		scheduleAssistantDialog.setLayout(new BorderLayout());
		scheduleAssistantDialog.setLocationRelativeTo(this);

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPanel.add(new JLabel("Schedule Assistant functionality goes here."));
		scheduleAssistantDialog.add(contentPanel, BorderLayout.CENTER);

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(e -> scheduleAssistantDialog.dispose());
		scheduleAssistantDialog.add(closeButton, BorderLayout.SOUTH);

		scheduleAssistantDialog.setVisible(true);
	}

	private void searchPatient() {
		try {
			int mrdID = Integer.parseInt(mriIdTextField.getText());
			for (Patient patient : patients) {
				if (patient.getTokenNumber() == mrdID) {
					patientNameField.setText(patient.getName());
					patientPhoneField.setText(String.valueOf(patient.getPhoneNumber()));
					patientEmailField.setText(patient.getEmail());
					selectedPatient = patient;
					break;
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid MRD ID", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateSpecializationComboBox() {
		if (departmentComboBox.getSelectedItem() != null) {
			specializationArray.clear();
			specializationComboBox.removeAllItems();
			for (Doctor doctor : doctors) {
				if (doctor.getDepartment().equals(departmentComboBox.getSelectedItem())) {
					specializationArray.add(doctor.getSpecialization());
				}
			}
			for (String specialization : specializationArray) {
				specializationComboBox.addItem(specialization);
			}
		}
	}

	private void updateDoctorComboBox() {
		if (specializationComboBox.getSelectedItem() != null) {
			doctorsSet.clear();
			doctorNameComboBox.removeAllItems();
			for (Doctor doctor : doctors) {
				if (doctor.getSpecialization().equals(specializationComboBox.getSelectedItem())) {
					doctorsSet.add(doctor.getName());
				}
			}
			for (String doctorName : doctorsSet) {
				doctorNameComboBox.addItem(doctorName);
			}
		}
	}

	private void submit() {
	    try {
	        String mriId = mriIdTextField.getText();
	        String patientName = patientNameField.getText();
	        String patientPhone = patientPhoneField.getText();
	        String patientEmail = patientEmailField.getText();
	        String department = (String) departmentComboBox.getSelectedItem();
	        String specialization = (String) specializationComboBox.getSelectedItem();
	        String doctorName = (String) doctorNameComboBox.getSelectedItem();
	        Date appointmentDate = appointmentDateChooser.getDate();
	        Date appointmentTime = (Date) appointmentTimeSpinner.getValue();

	        // Ensure selectedDoctor is set
	        selectedDoctor = null;
	        for (Doctor doctor : doctors) {
	            if (doctor.getName().equals(doctorName) &&
	                doctor.getDepartment().equals(department) &&
	                doctor.getSpecialization().equals(specialization)) {
	                selectedDoctor = doctor;
	                break;
	            }
	        }

	        if (selectedPatient == null || selectedDoctor == null || department.isEmpty() || specialization.isEmpty() || doctorName.isEmpty() || appointmentDate == null || appointmentTime == null) {
	            JOptionPane.showMessageDialog(this, "Please fill all the required fields", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        LocalDate localDate = appointmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        appointment = new Appointment(selectedPatient, selectedDoctor, localDate.format(formatter), new SimpleDateFormat("hh:mm a").format(appointmentTime));

	        dispose();
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	private void cancel() {
		appointment = null;
		dispose();
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public JTextField getMriIdTextField() {
		return mriIdTextField;
	}

	public JButton getSearchButton() {
		return searchButton;
	}
}
