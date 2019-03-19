package com.money.exchange.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PartOfTransaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private BigDecimal restAmountOfCurrency;
	private BigDecimal restAmountOfZlotych;
	private boolean isAccounted;
	
	@ManyToOne()
	@JoinColumn(name="transaction_id")
	private Transaction parentTransaction;

	
	
	public PartOfTransaction() {
		super();
	}

	public PartOfTransaction(BigDecimal restAmountOfCurrency, BigDecimal restAmountOfZlotych, boolean isAccounted) {
		super();
		this.restAmountOfCurrency = restAmountOfCurrency;
		this.restAmountOfZlotych = restAmountOfZlotych;
		this.isAccounted = isAccounted;
	}

	public long getId() {
		return id;
	}

	public BigDecimal getRestAmountOfCurrency() {
		return restAmountOfCurrency;
	}

	public void setRestAmountOfCurrency(BigDecimal restAmountOfCurrency) {
		this.restAmountOfCurrency = restAmountOfCurrency;
	}

	public BigDecimal getRestAmountOfZlotych() {
		return restAmountOfZlotych;
	}

	public void setRestAmountOfZlotych(BigDecimal restAmountOfZlotych) {
		this.restAmountOfZlotych = restAmountOfZlotych;
	}

	public Transaction getParentTransaction() {
		return parentTransaction;
	}

	public void setParentTransaction(Transaction parentTransaction) {
		this.parentTransaction = parentTransaction;
	}
	
	public boolean isAccounted() {
		return isAccounted;
	}

	public void setAccounted(boolean isAccounted) {
		this.isAccounted = isAccounted;
	}

	@Override
	public String toString() {
		return "PartOfTransaction [id=" + id + ", restAmountOfCurrency=" + restAmountOfCurrency
				+ ", restAmountOfZlotych=" + restAmountOfZlotych;
	}
	
}
