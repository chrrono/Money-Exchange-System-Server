package com.money.exchange.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.Transaction;
import com.money.exchange.exceptions.TransactionBadParametersException;
import com.money.exchange.service.TransactionService;
import com.money.exchange.service.UpdateTransactionHistoryService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	
	
	@Autowired
	TransactionService transactonService;
	
	@Autowired
	UpdateTransactionHistoryService updateService;

//	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value="/newTransaction/{nameOfMoneyExchangeOffice}/{currencyAbbreviation}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public CurrentStateOfCurrencyInCashRegister executeTransaction(@PathVariable String nameOfMoneyExchangeOffice,
																   @PathVariable String currencyAbbreviation,
																   @RequestBody Transaction newTransaction)
//																	throws TransactionBadParametersException
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
		System.out.println("===>"+nameOfMoneyExchangeOffice);
		System.out.println("===>"+currencyAbbreviation);
		System.out.println();
		
		CurrentStateOfCurrencyInCashRegister state = transactonService.outputStateOfCurrencyInCashRegister(nameOfMoneyExchangeOffice, 
																			currencyAbbreviation, newTransaction);
		if( state != null)
			return state;
		else 
			return null;
			//throw new TransactionBadParametersException("Przekroczony Limit, nie mozna wykonac transakcji sprzedaży");
	}
	
	@RequestMapping(value="/newBankTransaction/{nameOfMoneyExchangeOffice}/{currencyAbbreviation}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Transaction createBankTransaction(@PathVariable String nameOfMoneyExchangeOffice,
																   @PathVariable String currencyAbbreviation,
																   @RequestBody Transaction newBankTransaction)
	{
		newBankTransaction = transactonService.createBankTransaction(nameOfMoneyExchangeOffice, 
																			currencyAbbreviation, newBankTransaction);
		if( newBankTransaction != null)
			return newBankTransaction;
		return null;
	}
	
	@RequestMapping(value="editTransaction/{nameOfMoneyExchangeOffice}/{currencyAbbreviation}/Id/{id}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public CurrentStateOfCurrencyInCashRegister editTransaction(@PathVariable String nameOfMoneyExchangeOffice,
																@PathVariable String currencyAbbreviation,
																@PathVariable long id,
																@RequestBody Transaction newTransaction)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
		System.out.println();
		
		CurrentStateOfCurrencyInCashRegister state = updateService.updateTransaction(newTransaction, currencyAbbreviation, nameOfMoneyExchangeOffice);
		
		if( state != null)
			return state;
		return null;
	}
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/today/{nameOfMoneyExchangeOffice}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Transaction> getListOfTodayTransactions(@PathVariable String nameOfMoneyExchangeOffice){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
		List<Transaction> transactions = transactonService.getAllTransactionOfParticulrWorkPlaceByDate(nameOfMoneyExchangeOffice);
		return transactions;
	}
	
	@RequestMapping(value = "/all/{nameOfMoneyExchangeOffice}/currency/{abbreviation}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Transaction> getListOfAllTransactions(@PathVariable String nameOfMoneyExchangeOffice, @PathVariable String abbreviation){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
		List<Transaction> transactions = transactonService.getAllTransactionOfParticularWorkPlace(abbreviation, nameOfMoneyExchangeOffice);
		return transactions;
	}
	
	@RequestMapping(value="/sellToTheBank/{id}/update", method=RequestMethod.POST)
	public void accountSellToTheBankTransaction(@PathVariable long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		transactonService.updateSellToTheBankTransaction(id,currentPrincipalName);
	}
	
	@RequestMapping(value="/sellToTheBank/create/{nameOfMoneyExchangeOffice}/{abbreviation}", method=RequestMethod.POST)
	public Transaction accountSellToTheBankTransaction(@PathVariable String nameOfMoneyExchangeOffice, @PathVariable String abbreviation,
			 										@RequestBody Transaction newBankTransaction) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Transaction transactionWithId = transactonService.createBankTransaction(nameOfMoneyExchangeOffice, abbreviation, newBankTransaction);
		return transactionWithId;
	}
	
	@RequestMapping(value = "/sellToTheBank/All", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Transaction> getListOfsellToTheBankTransactions(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
		List<Transaction> bankTransactions = transactonService.findAllSellToTheBankTransactionWhichNotAccounted();
		return bankTransactions;
	}
	
	@RequestMapping(value = "/sellToTheBank/All/{nameOfMoneyExchangeOffice}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Transaction> getListOfsellToTheBankTransactionsToParticularExchangeOffice(@PathVariable String nameOfMoneyExchangeOffice){
		
		List<Transaction> bankTransactions = transactonService.findAllSellToTheBankTransactionWhichNotAccountedFromParticularExchangeOffice(
																														nameOfMoneyExchangeOffice);
		return bankTransactions;
	}
	
	@RequestMapping(value = "/delete/accountedTransactions", method=RequestMethod.POST)
	public boolean deleteAllAccountedTransactions() {
		transactonService.deleteAllTransactionsFromAllWorkPlace();
		return true;
	}
	
}
