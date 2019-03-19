package com.money.exchange.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.money.exchange.entity.Transaction;

@Repository
public class TransactionRepositoryImpl implements FindSpecialTransactions{

	@PersistenceContext
	private EntityManager em;
	
	public List<Transaction> findParticularMoneyExchangeAndCurrencyTransactions(String MoneyExchangeName,
			String CurrencyAbbreviation){
//		String query1 = "select t from Transaction t inner join WorkPlace w inner join Currency c"+
//			"where w.name = :MoneyExchangeName and c.name = :CurrencyAbbreviation";
//		String query = "select t from transaction t";
//		Query queryTransactionList = em.createNamedQuery(query,Transaction.class);
//		queryTransactionList.setParameter("MoneyExchangeName", MoneyExchangeName);
//		queryTransactionList.setParameter("CurrencyAbbreviation", CurrencyAbbreviation);
//		List<Transaction> transactions = queryTransactionList.getResultList();
//		return transactions;
//		System.out.println("========================================><");
		return null;
	}
	
}
