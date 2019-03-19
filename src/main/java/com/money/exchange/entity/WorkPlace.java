package com.money.exchange.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.money.exchange.Controller.Serializers.WorkPlaceSerializer;

@JsonSerialize(using = WorkPlaceSerializer.class)
@Entity
public class WorkPlace {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	private String location;
	private String role;
	
	@OneToMany(mappedBy="workPlace")
	private List<StaffMember> listOfEmployees;
	
	@OneToMany(mappedBy="moneyExchangeOffice")
	private List<Currency> currencyList;
	
//	@OneToMany(mappedBy="workPlace")
//	private List<Transaction> transactionList;
	
	public WorkPlace() {
		super();
	}

	public WorkPlace(String name, String location, String Role) {
		super();
		this.name = name;
		this.location = location;
		this.role = Role;
	}
	
	public void add(StaffMember member) {
		if(listOfEmployees == null) {
			listOfEmployees = new ArrayList<>();
		}
		listOfEmployees.add(member);
		member.setWorkPlace(this);
	}
	
	public List<StaffMember> getListOfEmployees(){
		return listOfEmployees;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		role = role;
	}

	public List<Currency> getCurrencyList(){
		return currencyList;
	}

	public void setCurrencyList(List<Currency> currencyList) {
		if(currencyList == null) {
			currencyList = new ArrayList<>();
		}
		this.currencyList = currencyList;
	}
	
	public void add(Currency currency) {
		if(currencyList == null) {
			currencyList = new ArrayList<>();
		}
		currencyList.add(currency);
		currency.setMoneyExchangeOffice(this);
	}

	@Override
	public String toString() {
		return "CurrencyExchangeOffice [id=" + id + ", name=" + name + ", location=" + location + "]";
	}
	
	
}
