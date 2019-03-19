package com.money.exchange.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.Transaction;

public class TransactionSerializer extends JsonSerializer<Transaction> {

	@Override
	public void serialize(Transaction newTransaction, JsonGenerator jsonGenerator , SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("id", newTransaction.getId());
		jsonGenerator.writeStringField("type", newTransaction.getType());
		jsonGenerator.writeNumberField("amountOfCurrency",newTransaction.getAmountOfCurrency());
		jsonGenerator.writeNumberField("amountOfZlotych", newTransaction.getAmountOfZlotych());
		jsonGenerator.writeNumberField("rateOfExchange", newTransaction.getRateOfExchange());
		jsonGenerator.writeStringField("currency", newTransaction.getCurrency().getAbbreviation());
		jsonGenerator.writeStringField("moneyExchangeOffice", newTransaction.getCurrency().getMoneyExchangeOffice().getName());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		jsonGenerator.writeObjectField("time", newTransaction.getTime().format(formatter));
		jsonGenerator.writeObjectField("date", newTransaction.getDate().toString());
		
		jsonGenerator.writeEndObject();
		
	}
	

}
