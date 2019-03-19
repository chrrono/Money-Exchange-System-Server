package com.money.exchange.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;


public class StateOfCashRegisterSerializer extends JsonSerializer<CurrentStateOfCurrencyInCashRegister> {

	@Override
	public void serialize(CurrentStateOfCurrencyInCashRegister cashState, JsonGenerator jsonGenerator , SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("currencyState",cashState.getAmountOfCurrency());
		jsonGenerator.writeNumberField("zloteState", cashState.getAmountOfZlotych());
		jsonGenerator.writeNumberField("averageRateExchange", cashState.getAverageRateOfExchange());
		jsonGenerator.writeStringField("currency", cashState.getCurrency().getAbbreviation());
		jsonGenerator.writeEndObject();
		
	}

	
}
