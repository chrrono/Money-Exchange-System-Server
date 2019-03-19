package com.money.exchange.Controller.Serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.money.exchange.entity.Currency;

public class CurrencySerializer extends JsonSerializer<Currency> {

	@Override
	public void serialize(Currency currency, JsonGenerator jsonGenerator , SerializerProvider serializerProvider)
			throws IOException {	
		
		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("id", currency.getId());
		jsonGenerator.writeStringField("name", currency.getName());		
		jsonGenerator.writeStringField("abbreviation", currency.getAbbreviation());
		jsonGenerator.writeObjectField("transactionList", currency.getTransactionList());
		jsonGenerator.writeObjectField("cashRegisterState", currency.getCashRegisterStateForCurrency());
		jsonGenerator.writeEndObject();
		
	}
}
