package com.money.exchange.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.money.exchange.entity.Currency;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.CurrencyRepository;



public class TransactionDeserializer extends JsonDeserializer<Transaction> {

	@Override
	public Transaction deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		ObjectCodec objectCodec = parser.getCodec();
        JsonNode jasonNode = objectCodec.readTree(parser);
        
        String type = jasonNode.get("type").asText();
        long id = jasonNode.get("id").asLong();
        BigDecimal amountOfCurrency = new BigDecimal(jasonNode.get("amountOfCurrency").asDouble());
        BigDecimal amountOfZlotych = new BigDecimal(jasonNode.get("amountOfZlotych").asDouble());
        BigDecimal rateOfExchange = new BigDecimal(jasonNode.get("rateOfExchange").asDouble());
//        LocalDate transactionDate = LocalDate.parse(jasonNode.get("date").asText());
//        
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime transactionTime = LocalDateTime.parse(jasonNode.get("time").asText(), formatter);
        
        Transaction newTransaction = new Transaction(id, type, amountOfCurrency, amountOfZlotych, rateOfExchange, LocalDate.now(), LocalDateTime.now().withNano(0));
//        
//        String moneyExchangeName = jasonNode.get("moneyExchangeName").asText();
//        String currencyAbbreviation = jasonNode.get("currencyAbbreviation").asText();
//        System.out.println("===========<<<<<"+moneyExchangeName+", "+currencyAbbreviation);
        
		return newTransaction;
	}

	
}
