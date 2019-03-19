package com.money.exchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;

public interface CurrentStateOfCurrencyInCashRegisterRepository extends JpaRepository<CurrentStateOfCurrencyInCashRegister, Long> {

	
	@Query("select s from CurrentStateOfCurrencyInCashRegister s inner join s.currency as c inner join c.moneyExchangeOffice as m "
			+ "where m.name = :name")
	public List<CurrentStateOfCurrencyInCashRegister> getCurrencyStateInParticularMoneyExchangeOffice(@Param("name") String moneyExchangeOfficeName);
	
	@Query("select s from CurrentStateOfCurrencyInCashRegister s inner join s.currency as c where c.abbreviation = :abbreviation")
	public List<CurrentStateOfCurrencyInCashRegister> getCurrencyStateInParticularCurrency(@Param("abbreviation") String abbreviation);
	
	@Query("select s from CurrentStateOfCurrencyInCashRegister s inner join s.currency as c inner join c.moneyExchangeOffice as m "
			+ "where m.name = :name and c.abbreviation = :abbreviation")
	public CurrentStateOfCurrencyInCashRegister getCurrencyStateInParticularMoneyExchangeOfficeAndParticularCurrency(
			@Param("name") String moneyExchangeOfficeName, @Param("abbreviation") String abbreviation);
}
