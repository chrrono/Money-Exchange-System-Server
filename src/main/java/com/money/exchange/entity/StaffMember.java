package com.money.exchange.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="staff_member")
public class StaffMember {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	private String surname;
	private String role;
	private boolean isLogIn;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="work_place_id")
	private WorkPlace workPlace;
	
	public StaffMember(String name, String surname, String role) {
		super();
		this.name = name;
		this.surname = surname;
		this.role = role;
		this.isLogIn=false;
	}

	public StaffMember() {
		super();
	}
	
	public void setLogIn(boolean logIn) {
		this.isLogIn = logIn;
	}
	
	public boolean isLogIn() {
		return isLogIn;
	}

	public WorkPlace getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(WorkPlace workPlace) {
		this.workPlace = workPlace;
	}

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
