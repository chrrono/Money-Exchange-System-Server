package com.money.exchange.Controller.Serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.money.exchange.entity.Currency;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.WorkPlace;

public class WorkPlaceSerializer extends JsonSerializer<WorkPlace> {

	@Override
	public void serialize(WorkPlace workPlace, JsonGenerator jsonGenerator , SerializerProvider serializerProvider)
			throws IOException {	
		
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("name", workPlace.getName());
		jsonGenerator.writeStringField("location", workPlace.getLocation());
		jsonGenerator.writeStringField("role", workPlace.getRole());
		jsonGenerator.writeNumberField("id", workPlace.getId());
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		List<CurrentStateOfCurrencyInCashRegister> cashRegisterStateList = new ArrayList<CurrentStateOfCurrencyInCashRegister>();
		for(Currency currency : workPlace.getCurrencyList()) {
			for(Transaction transaction : currency.getTransactionList()) {
				transactions.add(transaction);
			}
			cashRegisterStateList.add(currency.getCashRegisterStateForCurrency());
		}
		
		jsonGenerator.writeObjectField("transactionsList", transactions);
		jsonGenerator.writeObjectField("cashRegisterStateList", cashRegisterStateList);
		jsonGenerator.writeEndObject();

	}
}
