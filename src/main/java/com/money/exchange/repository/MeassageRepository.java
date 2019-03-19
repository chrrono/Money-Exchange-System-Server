package com.money.exchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.Message;
import com.money.exchange.entity.Transaction;

public interface MeassageRepository extends JpaRepository<Message,Long> {

//	@Query("select t from Transaction t inner join t.currency as c inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2")
//	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlace(String currencyName, String moneyExchangeName);
	
	@Query("select m from Message m inner join m.receiver as wr inner join m.sender as ws where wr.name = :moneyExchangeOfficeName or "
			+ "ws.name = :moneyExchangeOfficeName order by m.Id desc")
	List<Message> findAllMessageReceivedFromParticularExchangeOffice(@Param("moneyExchangeOfficeName") String moneyExchangeOfficeName);
	

}
