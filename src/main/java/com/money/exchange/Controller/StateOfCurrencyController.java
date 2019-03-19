package com.money.exchange.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.exceptions.InappropriatePathValueException;
import com.money.exchange.service.CurrencyStateService;

@RestController
@RequestMapping("/currencyState")
public class StateOfCurrencyController {

	@Autowired
	private CurrencyStateService stateService;
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@ResponseBody
	@RequestMapping(value="/currency/{abbreviation}", method=RequestMethod.GET)
	public List<CurrentStateOfCurrencyInCashRegister> getCurrentStateOfParticularCurrency(@PathVariable("abbreviation") String abbrreviation) 
	{
		return stateService.getCurrencyStateInParticularCurrency(abbrreviation);
	}
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@ResponseBody
	@RequestMapping(value="/moneyExchangeOffice/{name}", method=RequestMethod.GET)
	public List<CurrentStateOfCurrencyInCashRegister> getCurrentStateOfParticularMoneyExchangeOffice(@PathVariable String name) 
	{
		List<CurrentStateOfCurrencyInCashRegister> stateList = stateService.getCurrencyStateInParticularMoneyExchangeOffice(name);
		if(stateList == null)
			throw new InappropriatePathValueException("Bad nameOfMoneyExchangeOffice");
		return stateList;
	}

	@ResponseBody
	@RequestMapping(value="/moneyExchangeOffice/{name}/currency/{abbreviation}", method=RequestMethod.GET)
	public CurrentStateOfCurrencyInCashRegister getCurrentStateOfParticularMoneyExchangeOfficeAndParticularCurrency(@PathVariable String name,
			@PathVariable("abbreviation") String abbreviation) 
	{
		CurrentStateOfCurrencyInCashRegister state = stateService.getCurrencyStateInParticularMoneyExchangeOfficeAndParticularCurrency(name, abbreviation);
		if(state == null)
			throw new InappropriatePathValueException("Bad nameOfMoneyExchangeOffice or currencyAbbreviation");
		return state;
	}
}
