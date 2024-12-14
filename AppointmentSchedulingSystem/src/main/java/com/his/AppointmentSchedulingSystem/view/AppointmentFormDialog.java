

package com.his.AppointmentSchedulingSystem.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;
import com.his.AppointmentSchedulingSystem.model.Appointment;
import com.his.AppointmentSchedulingSystem.model.Doctor;
import com.his.AppointmentSchedulingSystem.model.Patient;
import com.toedter.calendar.JDateChooser;

public class AppointmentFormDialog extends JDialog {
    private JComboBox<Patient> mriIdComboBox;
    JTextField editor;
    private JComboBox<String> departmentComboBox, doctorNameComboBox, availableSlotsComboBox, specializationComboBox;
    private JTextField patientNameField, patientPhoneField, patientEmailField;
    private JDateChooser appointmentDateChooser;
    private JSpinner appointmentTimeSpinner;
    private JButton submitButton, cancelButton,scheduleAssistantButton ;
    private Appointment appointment;
    private Patient selectedPatient;
    private Doctor selectedDoctor;
    private JPanel buttonPanel;

    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private Set<String> specializationArray = new HashSet<>();
    private Set<String> doctorsSet = new HashSet<>();
    private DefaultComboBoxModel<String> specialComboBoxModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> doctorComboBoxModel = new DefaultComboBoxModel<>();
    private Set<String> departments = new HashSet<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

    public AppointmentFormDialog() {
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

        formPanel.add(new JLabel("MRI ID:"));
        mriIdComboBox = new JComboBox<>(patients.toArray(new Patient[0]));
        mriIdComboBox.setEditable(true);
        editor = (JTextField) mriIdComboBox.getEditor().getEditorComponent();

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	System.out.println("Hi");
                filterPatients();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterPatients();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterPatients();
            }

            private void filterPatients() {
                String filterText = editor.getText();
                int num = Integer.parseInt(filterText);
                DefaultComboBoxModel<Patient> filteredModel = new DefaultComboBoxModel<>();
                for (Patient patient : patients) {
                    if (patient.getTokenNumber()==num) {
                        filteredModel.addElement(patient);
                    }
                }
                mriIdComboBox.setModel(filteredModel);
                editor.setText(filterText); // Preserve the user's input
                mriIdComboBox.setPopupVisible(true);
            }
        });

        formPanel.add(mriIdComboBox);
        mriIdComboBox.setUI(new BasicComboBoxUI() {

			@Override
			protected JButton createArrowButton() {
				// TODO Auto-generated method stub
				
				ImageIcon startIcon = new ImageIcon("C:\\Users\\2021617\\Downloads\\image2.png");
				Image scaledStartImage = startIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
				ImageIcon scaledStartIcon = new ImageIcon(scaledStartImage);
				
				
				JButton arrowButton = new JButton(scaledStartIcon);
				arrowButton.setBorder(BorderFactory.createEmptyBorder());
				arrowButton.setContentAreaFilled(false);
				return arrowButton;
			}
        	
        });
        

        mriIdComboBox.setEditable(true);
        formPanel.add(mriIdComboBox);

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
        formPanel.add(new JSeparator());

        formPanel.add(new JLabel("Department:"));
        for (Doctor doctor : doctors) {
            departments.add(doctor.getDepartment());
        }
        departmentComboBox = new JComboBox<>(departments.toArray(new String[0]));
        formPanel.add(departmentComboBox);

        formPanel.add(new JLabel("Specialization:"));
        specializationComboBox = new JComboBox<>();
        formPanel.add(specializationComboBox);

        formPanel.add(new JLabel("Doctor Name:"));
        doctorNameComboBox = new JComboBox<>();
        formPanel.add(doctorNameComboBox);
        
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

//formPanel.add(scheduleAssistantButton);

//        formPanel.add(new JLabel("Schedule Assistant:"));
//        availableSlotsComboBox = new JComboBox<>(new String[] { "9:00 AM", "10:00 AM", "11:00 AM" });
//        formPanel.add(availableSlotsComboBox);

        buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        //scheduleAssistantButton=new JButton("Schedule Assistant");
        scheduleAssistantButton=new JButton("Schedule Assistant");
        scheduleAssistantButton.addActionListener(e->openScheduleAssistantWindow());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(scheduleAssistantButton);
        
        JPanel secondPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        
        secondPanel.add(submitButton);
        secondPanel.add(cancelButton);
        
//        
        //buttonPanel.add(scheduleAssistantButton);
        buttonPanel.add(centerPanel);
        buttonPanel.add(secondPanel);
//        buttonPanel.add(submitButton);
//        buttonPanel.add(cancelButton);
        
        

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> cancel());
        

        mriIdComboBox.addActionListener(e -> updatePatientFields());
        departmentComboBox.addActionListener(e -> updateSpecializationComboBox());
        specializationComboBox.addActionListener(e -> updateDoctorComboBox());
        doctorNameComboBox.addActionListener(e-> doctorSelected());
       
        
    }

    
    	private void openScheduleAssistantWindow() {
    	    JDialog scheduleAssistantDialog = new JDialog(this, "Schedule Assistant", true);
    	    scheduleAssistantDialog.setSize(400, 300);
    	    scheduleAssistantDialog.setLayout(new BorderLayout());
    	    scheduleAssistantDialog.setLocationRelativeTo(this);

    	    // Add content to the new window
    	    JPanel contentPanel = new JPanel();
    	    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	    contentPanel.add(new JLabel("Schedule Assistant functionality goes here."));
    	    scheduleAssistantDialog.add(contentPanel, BorderLayout.CENTER);

    	    // Close button for the dialog
    	    JButton closeButton = new JButton("Close");
    	    closeButton.addActionListener(e -> scheduleAssistantDialog.dispose());
    	    scheduleAssistantDialog.add(closeButton, BorderLayout.SOUTH);

    	    scheduleAssistantDialog.setVisible(true);
    	}
    

    private void updatePatientFields() {
        if (mriIdComboBox.getSelectedItem() != null) {
            Patient patient = patients.get(mriIdComboBox.getSelectedIndex());
            selectedPatient=patient;
            patientNameField.setText(patient.getName());
            patientEmailField.setText(patient.getEmail());
            patientPhoneField.setText(String.valueOf(patient.getPhoneNumber()));
        }
    }

    private void updateSpecializationComboBox() {
        if (departmentComboBox.getSelectedItem() != null) {
            specializationArray.clear();
            specialComboBoxModel.removeAllElements();
            for (Doctor doctor : doctors) {
                if (doctor.getDepartment().equals(departmentComboBox.getSelectedItem())) {
                    specializationArray.add(doctor.getSpecialization());
                }
            }
            specializationArray.forEach(specialComboBoxModel::addElement);
            specializationComboBox.setModel(specialComboBoxModel);
        }
    }

    private void updateDoctorComboBox() {
        if (specializationComboBox.getSelectedItem() != null) {
            doctorsSet.clear();
            doctorComboBoxModel.removeAllElements();
            for (Doctor doctor : doctors) {
                if (doctor.getSpecialization().equals(specializationComboBox.getSelectedItem())) {
                    doctorsSet.add(doctor.getName());
                }
            }
            doctorsSet.forEach(doctorComboBoxModel::addElement);
            doctorNameComboBox.setModel(doctorComboBoxModel);
        }
    }
    private void doctorSelected() {
        if (doctorNameComboBox.getSelectedItem() != null) {
        	for (Doctor doctor : doctors) {
                if (doctor.getName().equals(doctorNameComboBox.getSelectedItem())) {
                    selectedDoctor=doctor;
                    System.out.println(doctor);
                    return;
                }
            }
        }
    }
    

    private void submit() {
        try {
            String mriId = mriIdComboBox.getSelectedItem().toString();
            String patientName = patientNameField.getText();
            String patientPhone = patientPhoneField.getText();
            String patientEmail = patientEmailField.getText();
            String department = departmentComboBox.getSelectedItem().toString();
            String doctorName = doctorNameComboBox.getSelectedItem().toString();
            String availableSlot = availableSlotsComboBox.getSelectedItem().toString();
            Date appointmentDate = appointmentDateChooser.getDate();
            Date appointmentTime = (Date) appointmentTimeSpinner.getValue();
            
//            LocalDate appointmentDate=appointmentDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            Date appointmentTime=(Date)appointmentTimeSpinner.getValue();

            if (selectedPatient==null||selectedDoctor==null||appointmentDate==null||appointmentTime==null) {
            	return;
            }
            

           LocalDate localDate = appointmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Doctor doctor = new Doctor();
            doctor.setName(doctorName);

            Patient patient = new Patient();
            patient.setName(patientName);
            patient.setPhoneNumber(Long.parseLong(patientPhone));
            patient.setEmail(patientEmail);
            
            appointment = new Appointment(selectedPatient, selectedDoctor, appointmentDate.toInstant().atZone(ZoneId.systemDefault()).format(formatter), availableSlot);
//            System.out.println(patient);
//            System.out.println(doctor);
//            appointment = new Appointment(patient, doctor, doctorName, department, doctor.getConsultationFee(), availableSlot, Status.ACTIVE);
            
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
    
}
