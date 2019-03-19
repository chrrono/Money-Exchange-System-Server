package com.money.exchange.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.money.exchange.service.StateOfCashRegisterSerializer;


@Entity
@JsonSerialize(using=StateOfCashRegisterSerializer.class)
public class CurrentStateOfCurrencyInCashRegister {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private BigDecimal amountOfCurrency;
	private BigDecimal amountOfZlotych;
	
	@Column(precision = 8, scale = 6)
	@Type(type = "big_decimal")
	private BigDecimal averageRateOfExchange;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="currency_id")
	private Currency currency;
	
	public CurrentStateOfCurrencyInCashRegister() {
		super();
		amountOfCurrency = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		amountOfZlotych = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		averageRateOfExchange = new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
	}
	
	public CurrentStateOfCurrencyInCashRegister(BigDecimal amountOfCurrency, BigDecimal amountOfZlotych,
			BigDecimal averageRateOfExchange) {
		super();
		this.amountOfCurrency = amountOfCurrency.setScale(2, RoundingMode.HALF_UP);
		this.amountOfZlotych = amountOfZlotych.setScale(2, RoundingMode.HALF_UP);
		this.averageRateOfExchange = averageRateOfExchange.setScale(6, RoundingMode.HALF_UP);
	}
	
	public void reset() {
		amountOfCurrency = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		amountOfZlotych = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		averageRateOfExchange = new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
	}

	public BigDecimal getAmountOfCurrency() {
		return amountOfCurrency;
	}

	public void setAmountOfCurrency(BigDecimal amountOfCurrency) {
		this.amountOfCurrency = amountOfCurrency;
	}

	public BigDecimal getAmountOfZlotych() {
		return amountOfZlotych;
	}

	public void setAmountOfZlotych(BigDecimal amountOfZlotych) {
		this.amountOfZlotych = amountOfZlotych;
	}

	public BigDecimal getAverageRateOfExchange() {
		return averageRateOfExchange;
	}

	public void setAverageRateOfExchange(BigDecimal averageRateOfExchange) {
		this.averageRateOfExchange = averageRateOfExchange;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "CurrentStateOfCurrencyInCashRegister [id=" + id + ", amountOfCurrency=" + amountOfCurrency
				+ ", amountOfZlotych=" + amountOfZlotych + ", averageRateOfExchange=" + averageRateOfExchange +"]";
		
	}
	
	
	
	
}
