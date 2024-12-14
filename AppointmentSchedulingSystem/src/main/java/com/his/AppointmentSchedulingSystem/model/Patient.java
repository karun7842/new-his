package com.his.AppointmentSchedulingSystem.model;

import java.time.LocalDate;
import java.util.ArrayList;

import com.his.AppointmentSchedulingSystem.controller.AppointmentController;

public class Patient {
	static private int tokenCounter;
    private String name;
	private LocalDate dob;
    private int age;
    private String address;
    private String email;
    private String bloodGroup;
    private long phoneNumber;
    private int mrdID;
    public Patient() {
    	
    }
 
   
    public Patient(String name, LocalDate dob, int age, String address, String email, String bloodGroup, long phoneNumber) {
        this.name = name;
        this.dob = dob;
        this.age = age;
        this.address = address;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
        this.mrdID=++tokenCounter;
    }
 
    public String getName() {
    	return name;
    	}
    public LocalDate getDob() {
    	return dob;
    	}
    public int getAge() {
    	return age;
    	}
    public String getAddress() {
    	return address;
    	}
    public String getEmail() {
    	return email;
    	}
    public String getBloodGroup() {
    	return bloodGroup;
    	}
    public long getPhoneNumber() {
    	return phoneNumber;
    	}
    public int getTokenNumber() {
    	return mrdID;
    }
    public void setTokenNumber(int tokenNumber) {
    	this.mrdID=tokenNumber;
    }
    public void setName(String name) {
		this.name = name;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
    

    
	public static int getTokenCounter() {
		return tokenCounter;
	}


	public static void setTokenCounter(int tokenCounter) {
		Patient.tokenCounter = tokenCounter;
	}


	@Override
	public String toString() {
		return new Integer(mrdID).toString();
	}


    
}