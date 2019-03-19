package com.money.exchange.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	
	List<Currency> findByAbbreviation(String abb);
	
	@Query("select c from Currency c inner join c.moneyExchangeOffice as ex where ex.name = :name and c.abbreviation = :abbre")
	Currency findSingleByAbbreviation(@Param("abbre") String abb, @Param("name") String name); 
	
	@Query("select c.abbreviation from Currency c inner join c.moneyExchangeOffice as ex where ex.name = :name")
	List<String> findCurrencyListOfParticularMoneyExchangeOffice(@Param("name") String moneyExchangeOfficeName);
	
}
