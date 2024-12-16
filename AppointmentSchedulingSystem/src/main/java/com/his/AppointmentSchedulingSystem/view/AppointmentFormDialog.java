package com.his.AppointmentSchedulingSystem.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.lang.ModuleLayer.Controller;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
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
	List<String> timeSlots;

	public AppointmentFormDialog(Appointment appointment) {
		setTitle("Schedule Appointment");
		setModal(true);
		setSize(700, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));
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
		searchButton = new JButton(new ImageIcon(new ImageIcon("C:\\Users\\2021617\\Downloads\\image2.png").getImage()
				.getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
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

		formPanel.add(new JSeparator());
		formPanel.add(new JLabel());

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
		scheduleAssistantDialog.setSize(600, 500);
		scheduleAssistantDialog.setLayout(new BorderLayout());
		scheduleAssistantDialog.setLocationRelativeTo(this);

		JPanel slotsPanel = new JPanel();
		slotsPanel.setLayout(new GridLayout(0, 4, 10, 10));
		slotsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		timeSlots = AppointmentController.addScheduleList(selectedDoctor,
				appointmentDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).format(formatter));
		for (String slot : timeSlots) {
			JButton slotButton = new JButton(slot);
			slotButton.addActionListener(e -> {
				appointmentTimeSpinner.setValue(ParseTime(slot));
				scheduleAssistantDialog.dispose();
			});
			slotsPanel.add(slotButton);
		}
		scheduleAssistantDialog.add(slotsPanel, BorderLayout.CENTER);

		JButton closeButton = new JButton("close");
		closeButton.addActionListener(e -> scheduleAssistantDialog.dispose());
		scheduleAssistantDialog.add(closeButton, BorderLayout.SOUTH);
		scheduleAssistantDialog.setVisible(true);

	}
//	private ArrayList<String> generateTimeSlots(){
//		ArrayList<String>slots=new ArrayList<>();
//		int startHour=9;
//		int endHour=17;
//		int interval=15;
//		for(int hour=startHour;hour<=endHour;hour++) {
//			for(int minute=0;minute<60;minute+=interval) {
//				String time=String.format("%02d:%02d %s",(hour>12?hour-12:hour),minute,(hour>=12?"PM":"AM"));
//				slots.add(time);
//			}
//		}
//		return slots;
//	}

	private Date ParseTime(String time) {
		try {
			return new SimpleDateFormat("hh:mm a").parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
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

	public void submit() {
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
			for (Doctor doctor : doctors) {
				if (doctor.getName().equals(doctorName) && doctor.getDepartment().equals(department)
						&& doctor.getSpecialization().equals(specialization)) {
					selectedDoctor = doctor;
					break;
				}
			}

			if (selectedPatient == null || selectedDoctor == null || department.isEmpty() || specialization.isEmpty()
					|| doctorName.isEmpty() || appointmentDate == null || appointmentTime == null) {
				JOptionPane.showMessageDialog(this, "Please fill all the required fields", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			LocalDate localDate = appointmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String time = new SimpleDateFormat("hh:mm a").format(appointmentTime);
			appointment = new Appointment(selectedPatient, selectedDoctor, localDate.format(formatter), time);
			AppointmentController.addScheduleList(selectedDoctor,localDate.format(formatter));
			AppointmentController.removeScheduleList(selectedDoctor, localDate.format(formatter));
			AppointmentController.writeDoctorSchedule();
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
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
