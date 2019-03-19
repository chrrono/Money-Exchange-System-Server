package com.money.exchange.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.catalina.User;

@Entity
@Table(name="users")
public class Users {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Id;
	private String username;
	private String password;
	private boolean active;
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns =
			@JoinColumn(name = "user_id"), inverseJoinColumns = 
			@JoinColumn(name="role_id"))
	private List<Role> roles;
	
	@OneToOne
	@JoinColumn(name="staff_member_id")
	private StaffMember employee;
	
	private Users() {
		super();
	}
	
	public Users(Users user) {
		this.username = user.username;
		this.password = user.password;
		this.active = user.active;
		this.roles = user.roles;
		this.employee = user.employee;
	}

	public Users(String username, String password, boolean active, List<Role> roles, StaffMember employee) {
		
		this.username = username;
		this.password = password;
		this.active = active;
		this.roles = roles;
		this.employee = employee;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public int getActive() {
//		return active;
//	}
//
//	public void setActive(int active) {
//		this.active = active;
//	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public StaffMember getEmployee() {
		return employee;
	}

	public void setEmployee(StaffMember employee) {
		this.employee = employee;
	}
	
	public boolean isActive() {
		return active;
	}


}
