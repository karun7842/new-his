package com.his.AppointmentSchedulingSystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment {
@JsonProperty("patient")
	private Patient patient;

@JsonProperty("doctor")
	private Doctor doctor;

@JsonProperty("appointment_Date")
	private String appointmentDate;


@JsonProperty("appointment_time")
	private String appointmentTime;
@JsonProperty("Status")
private Status status;

	public Patient getPatient() {
		return patient;
	}

	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public Appointment(Patient patient, Doctor doctor, String appointmentDate, String department,
//			double consultationFee, String appointmentTime, Status status) {
//		super();
//		this.patient = patient;
//		this.doctor = doctor;
//		this.appointmentDate = appointmentDate;
//		this.department = department;
//		this.consultationFee = consultationFee;
//		this.appointmentTime = appointmentTime;
//		this.status = status;
//	}

	public Appointment(Patient patient, Doctor doctor, String appointmentDate, String appointmentTime) {
		super();
		this.patient = patient;
		this.doctor = doctor;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.status=Status.ACTIVE;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}


	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	@Override
	public String toString() {
		return "Appointment [patient=" + patient + ", doctor=" + doctor + ", appointmentDate=" + appointmentDate
				+ ", appointmentTime=" + appointmentTime + ", status=" + status + "]";
	}

	
	
}