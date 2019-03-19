package com.money.exchange.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.money.exchange.service.TransactionDeserializer;
import com.money.exchange.service.TransactionSerializer;

@JsonDeserialize(using = TransactionDeserializer.class)
@JsonSerialize(using = TransactionSerializer.class)
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String type;
	private BigDecimal amountOfCurrency;
	private BigDecimal amountOfZlotych;
	private BigDecimal rateOfExchange;
	private LocalDate date;
	private LocalDateTime time;
	/*if transaction of buing have influence for current state of cash register
	or transaction was accounted and there is no impact on cash register*/
	private boolean isAccounted;
	/*if part of transaction is accounted, but no whole transaction*/
	private boolean isInAccountedProcess;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="parentTransaction")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<PartOfTransaction> transactionPartList;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="currency_id")
	private Currency currency;
	
	public Transaction(long id, String type, BigDecimal amountOfCurrency, BigDecimal amountOfZlotych, BigDecimal rateOfExchange,
			LocalDate date, LocalDateTime time){
		super();
		this.id = id;
		this.type = type;
		this.amountOfCurrency = amountOfCurrency.setScale(2, RoundingMode.HALF_UP);
		this.amountOfZlotych = amountOfZlotych.setScale(2, RoundingMode.HALF_UP);
		this.rateOfExchange = rateOfExchange.setScale(6, RoundingMode.HALF_UP);
		this.date = date;
		this.time = time;
		setFieldsInConstructor();
	}
	
	public Transaction(String type, BigDecimal amountOfCurrency, BigDecimal amountOfZlotych, BigDecimal rateOfExchange,
			LocalDate date, LocalDateTime time){
		super();
		this.type = type;
		this.amountOfCurrency = amountOfCurrency.setScale(2, RoundingMode.HALF_UP);
		this.amountOfZlotych = amountOfZlotych.setScale(2, RoundingMode.HALF_UP);
		this.rateOfExchange = rateOfExchange.setScale(6, RoundingMode.HALF_UP);
		this.date = date;
		this.time = time;
		setFieldsInConstructor();
	}

	public Transaction() {
		super();
	}
	
	private void setFieldsInConstructor() {
		if(this.type.toUpperCase().equals("BUY")) {
			this.isAccounted = false;
			this.isInAccountedProcess = false;
		}
		else if(this.type.toUpperCase().equals("SELL")){
			this.isAccounted = true;
			this.isInAccountedProcess = false;
		}
		else if(this.type.toUpperCase().equals("SELLTOTHEBANK")){
			this.isAccounted = false;
			this.isInAccountedProcess = false;
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmountOfCurrency() {
		return amountOfCurrency;
	}

	public void setAmountOfCurrency(BigDecimal amountOfCurrency) {
		this.amountOfCurrency = amountOfCurrency.setScale(2, RoundingMode.HALF_UP);;
	}

	public BigDecimal getAmountOfZlotych() {
		return amountOfZlotych;
	}

	public void setAmountOfZlotych(BigDecimal amountOfZlotych) {
		this.amountOfZlotych = amountOfZlotych.setScale(2, RoundingMode.HALF_UP);;
	}

	public BigDecimal getRateOfExchange() {
		return rateOfExchange;
	}

	public void setRateOfExchange(BigDecimal rateOfExchange) {
		this.rateOfExchange = rateOfExchange.setScale(3, RoundingMode.HALF_UP);;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDateTime getTime() {
		return time;
	}
	
	public String getStringTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = this.getTime().format(formatter);
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public boolean isAccounted() {
		return isAccounted;
	}

	public void setAccounted(boolean isAccounted) {
		this.isAccounted = isAccounted;
	}

	public boolean isInAccountedProcess() {
		return isInAccountedProcess;
	}

	public void setInAccountedProcess(boolean isInAccountedProcess) {
		this.isInAccountedProcess = isInAccountedProcess;
	}

	public List<PartOfTransaction> getTransactionPart() {
		return transactionPartList;
	}

	public void addTransactionPart(PartOfTransaction transactionPart) {
		if(this.transactionPartList == null)
			this.transactionPartList = new ArrayList<>();
		this.transactionPartList.add(transactionPart);
		transactionPart.setParentTransaction(this);
	}
	
	public void removeTransactionPartList() {
		this.transactionPartList = null;
	}

	public boolean removeTransaction(PartOfTransaction partTr) {
		if(this.transactionPartList == null)
			return true;
		boolean result = this.transactionPartList.remove(partTr);
		partTr.setParentTransaction(null);
		return result;
	}
	
	public boolean removeTransactionFromCurrency() {
		boolean result = false;
		if(this.currency == null)
			return true;
		if(this.currency.getTransactionList().contains(this))
			result = this.currency.getTransactionList().remove(this);
		this.setCurrency(null);
		return true;
	}
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
		
	}
	
	
//
//
//	public String toMessageContent() {
//		
//		StringBuilder builder = new StringBuilder();
//		builder.append("Informacje o Tranzakcji:\n");
//		if(type.equals("Sell"))
//			builder.append("typ operacji : Sprzedaż\n");
//		else
//			builder.append("typ operacji : Sprzedaż\n");
//		builder.append("Waluta : "+currency.getAbbreviation()+"\n");
//		builder.append("Ilość Waluty : "+amountOfCurrency+"\n");
//		builder.append("Kurs : "+amountOfCurrency+"\n");
//		
//		return "Tranzakcja "+ type=" + type + ", amountOfCurrency=" + amountOfCurrency
//				+ ", amountOfZlotych=" + amountOfZlotych + ", rateOfExchange=" + rateOfExchange + ", date=" + date
//				+", isAccounted=" + isAccounted + ", isInAccountedProcess=" + isInAccountedProcess+"]";
//	}

	public String toMessage() {
		return "Transakcja Do Banku "+System.lineSeparator()
				+" waluta : "+this.getCurrency().getAbbreviation()+" ;\n"
				+" ilość waluty : "+this.getAmountOfCurrency()+" ;\n"
				+" średnia : "+this.getAmountOfCurrency()+" ;\n"
				+" ilosc zlotowek"+this.getAmountOfZlotych()+" ;\n";
		
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", type=" + type + ", amountOfCurrency=" + amountOfCurrency
				+ ", amountOfZlotych=" + amountOfZlotych + ", rateOfExchange=" + rateOfExchange + ", date=" + date
				+", isAccounted=" + isAccounted + ", isInAccountedProcess=" + isInAccountedProcess+"]";
	}
	
	
}
