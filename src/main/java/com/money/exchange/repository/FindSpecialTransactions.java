package com.money.exchange.repository;

import java.util.List;

import com.money.exchange.entity.Transaction;

public interface FindSpecialTransactions {
	
	public List<Transaction> findParticularMoneyExchangeAndCurrencyTransactions(String MoneyExchangeName,
			String CurrencyAbbreviation);

}
