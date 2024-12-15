package com.his.AppointmentSchedulingSystem.model;

import java.time.LocalDate;
import java.util.List;

public class DoctorSchedule {
private Doctor doctor;
private LocalDate date;
private List<String> availableSlots;
public DoctorSchedule(Doctor doctor, LocalDate date, List<String> availableSlots) {
	super();
	this.doctor = doctor;
	this.date = date;
	this.availableSlots = availableSlots;
}
public Doctor getDoctor() {
	return doctor;
}
public void setDoctor(Doctor doctor) {
	this.doctor = doctor;
}
public LocalDate getDate() {
	return date;
}
public void setDate(LocalDate date) {
	this.date = date;
}
public List<String> getAvailableSlots() {
	return availableSlots;
}
public void setAvailableSlots(List<String> availableSlots) {
	this.availableSlots = availableSlots;
}

}
