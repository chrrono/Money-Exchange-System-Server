package com.money.exchange.entity;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.money.exchange.Controller.Serializers.CurrencySerializer;

@JsonSerialize(using = CurrencySerializer.class)
@Entity
public class Currency {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	private String abbreviation;
	private BigDecimal rateOfBuy;
	private BigDecimal rateOfSell;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="currency", cascade= {CascadeType.ALL})
	private List<Transaction> transactionList;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="currency", cascade= {CascadeType.ALL})
	private CurrentStateOfCurrencyInCashRegister cashRegisterStateForCurrency;
	
	@ManyToOne()
	@JoinColumn(name="money_exchange_office_id")
	private WorkPlace moneyExchangeOffice;
	
	public Currency() {
		super();
	}

	public Currency(String name, String abbreviation, BigDecimal rateOfBuy, BigDecimal rateOfSell) {
		super();
		this.name = name;
		this.abbreviation = abbreviation;
		this.rateOfBuy = rateOfBuy;
		this.rateOfSell = rateOfSell;
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
	
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public BigDecimal getRateOfBuy() {
		return rateOfBuy;
	}
	public void setRateOfBuy(BigDecimal rateOfBuy) {
		this.rateOfBuy = rateOfBuy;
	}
	
	public BigDecimal getRateOfSell() {
		return rateOfSell;
	}
	public void setRateOfSell(BigDecimal rateOfSell) {
		this.rateOfSell = rateOfSell;
	}
	
	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public void addTransaction(Transaction tr) {
		if(transactionList == null) {
			transactionList = new ArrayList<>();
		}
		transactionList.add(tr);
		tr.setCurrency(this);
	}
	
	public void setCashRegisterStateForCurrency(CurrentStateOfCurrencyInCashRegister cashRegisterStateForCurrency) {
		this.cashRegisterStateForCurrency = cashRegisterStateForCurrency;
		cashRegisterStateForCurrency.setCurrency(this);
	}
	
	public CurrentStateOfCurrencyInCashRegister getCashRegisterStateForCurrency() {
		return cashRegisterStateForCurrency;
	}

	public WorkPlace getMoneyExchangeOffice() {
		return moneyExchangeOffice;
	}

	public void setMoneyExchangeOffice(WorkPlace moneyExchangeOffice) {
		this.moneyExchangeOffice = moneyExchangeOffice;
	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", name=" + name + ", abbreviation=" + abbreviation + ", rateOfBuy=" + rateOfBuy
				+ ", rateOfSell=" + rateOfSell + "]";
	}
	
}
