package com.money.exchange.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.repository.CurrentStateOfCurrencyInCashRegisterRepository;

@Service
public class CurrencyStateService {

	@Autowired
	private CurrentStateOfCurrencyInCashRegisterRepository stateRepository;
	
	public List<CurrentStateOfCurrencyInCashRegister> getCurrencyStateInParticularMoneyExchangeOffice(String moneyExchangeOfficeName){
		return stateRepository.getCurrencyStateInParticularMoneyExchangeOffice(moneyExchangeOfficeName);
	}
	
	
	public List<CurrentStateOfCurrencyInCashRegister> getCurrencyStateInParticularCurrency(String abbreviation){
		return stateRepository.getCurrencyStateInParticularCurrency(abbreviation);
	}
	
	public CurrentStateOfCurrencyInCashRegister getCurrencyStateInParticularMoneyExchangeOfficeAndParticularCurrency(
			String moneyExchangeOfficeName, String abbreviation) {
		return stateRepository.getCurrencyStateInParticularMoneyExchangeOfficeAndParticularCurrency(moneyExchangeOfficeName, abbreviation);
	}
}
