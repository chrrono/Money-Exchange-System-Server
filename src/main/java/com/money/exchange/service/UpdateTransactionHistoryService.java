package com.money.exchange.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.Currency;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.PartOfTransaction;
import com.money.exchange.entity.Transaction;
import com.money.exchange.repository.CurrencyRepository;
import com.money.exchange.repository.CurrentStateOfCurrencyInCashRegisterRepository;
import com.money.exchange.repository.PartOfTransactionRepository;
import com.money.exchange.repository.TransactionRepository;

@Service
public class UpdateTransactionHistoryService {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private PartOfTransactionRepository partOfTransactionRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private CurrentStateOfCurrencyInCashRegisterRepository currentStateOfCurrencyRepository;
	@Autowired
	private MessageCreatorService messageService;
	
	private List<Transaction> removedTransactionTemporaryList;
	private List<PartOfTransaction> removedPartOfTransactionTemporaryList;
	private CurrentStateOfCurrencyInCashRegister currencyStateRegister;
	
	
	
	public CurrentStateOfCurrencyInCashRegister updateTransaction(Transaction updatedTransaction, String currencyName, String moneyExchangeName) {
		
		long id = updatedTransaction.getId();
		
		setTransactionFiledToSpecificValues(updatedTransaction);
		initializeCurrencyStateAndRemovedTransactionAndPartTransactionList(currencyName, moneyExchangeName, id);
		
		int counterSellTransaction = 0;
		
		for(Transaction elTran : this.removedTransactionTemporaryList) {
			if(elTran.getType() == "Sell") {
				counterSellTransaction++;
			}
			setTransactionFiledToSpecificValues(elTran);
		}
				
		PartOfTransaction lastActivePart = this.removedPartOfTransactionTemporaryList.get(counterSellTransaction);
		deletePartTransactionAndTransactionWhichAreAfterUpdatedTransaction(counterSellTransaction);
		
		if(lastActivePart != null) {
			
			processLastActivePartOfTransaction(lastActivePart);
				
			long idStart = lastActivePart.getParentTransaction().getId();
			long idEnd = updatedTransaction.getId();
			List<Transaction> transactionsInfluentOnStateOfCurrency = transactionRepository.findTransactionsWhileUpdatingCurrencyState(
										currencyName, moneyExchangeName, idStart, idEnd);
			
			updateCurrentStateOfCashRegister(transactionsInfluentOnStateOfCurrency, lastActivePart);
		}
		
		
		transactionService.serviceTransaction(moneyExchangeName, currencyName, updatedTransaction);
		for(Transaction elTran : this.removedTransactionTemporaryList) {
			transactionService.serviceTransaction(moneyExchangeName, currencyName, elTran);
		}	
		
		this.messageService.sendNotificationAboutUpdateInParticularExchangeOffice(currencyName, moneyExchangeName);
		return this.currencyStateRegister;
	}
	
	private void setTransactionFiledToSpecificValues(Transaction updatedTransaction){
		if(updatedTransaction.getType().equals("Sell")) {
			updatedTransaction.setAccounted(true);
			updatedTransaction.setInAccountedProcess(false);
		}
		else {
			updatedTransaction.setAccounted(false);
			updatedTransaction.setInAccountedProcess(true);
		}
		updatedTransaction.removeTransactionPartList();
		updatedTransaction.setCurrency(null);
	}
	
	private void initializeCurrencyStateAndRemovedTransactionAndPartTransactionList(String currencyName,
			String moneyExchangeName, long id) {
		
		this.removedTransactionTemporaryList = transactionRepository.findremovedTransactionsOfParticularWorkPlaceWhenUpdatedTransaction(currencyName, moneyExchangeName, id);
		this.removedPartOfTransactionTemporaryList = this.partOfTransactionRepository
													.findPartOfTransactionListOfParticularCurrencyAndWorkPlace(moneyExchangeName, currencyName);
		
		Currency currentCurrency = currencyRepository.findSingleByAbbreviation(currencyName, moneyExchangeName);
		this.currencyStateRegister = currentCurrency.getCashRegisterStateForCurrency();
	}
	
	
	private void deletePartTransactionAndTransactionWhichAreAfterUpdatedTransaction(int howManyPartTransaction) {
		this.removedPartOfTransactionTemporaryList = this.removedPartOfTransactionTemporaryList.subList(0, howManyPartTransaction);
		
		for(PartOfTransaction elPart: this.removedPartOfTransactionTemporaryList){
			Transaction parentTransaction = elPart.getParentTransaction();
			if(parentTransaction.removeTransaction(elPart));
			transactionRepository.save(parentTransaction);
			partOfTransactionRepository.delete(elPart);
			
		}
		for(Transaction elTran : this.removedTransactionTemporaryList) {
			transactionRepository.deleteById(elTran.getId());
		}
		
		this.removedTransactionTemporaryList = this.removedTransactionTemporaryList.subList(1, this.removedTransactionTemporaryList.size());

	}
	
	private void processLastActivePartOfTransaction(PartOfTransaction lastActivePart) {
		lastActivePart.setAccounted(false);
		partOfTransactionRepository.save(lastActivePart);
		lastActivePart.getParentTransaction().setAccounted(false);
		lastActivePart.getParentTransaction().setInAccountedProcess(true);
		transactionRepository.save(lastActivePart.getParentTransaction());
	}
	
	public void updateCurrentStateOfCashRegister(List<Transaction> transactionsInfluentOnStateOfCurrency, PartOfTransaction lastActivePart) {
		this.currencyStateRegister.reset();
		this.currentStateOfCurrencyRepository.save(this.currencyStateRegister);
		
		if(lastActivePart != null) {
			updateCurrencyStateOfCashRegisterByPartOfTransaction(lastActivePart, this.currencyStateRegister);
			for(Transaction tr : transactionsInfluentOnStateOfCurrency) {
				this.transactionService.updatecurrencyStateRegisterAfterBuyTransaction(tr, this.currencyStateRegister);
				this.transactionRepository.save(tr);
				this.currentStateOfCurrencyRepository.save(this.currencyStateRegister);
			}

		}
	}
	
	public void updateCurrencyStateOfCashRegisterByPartOfTransaction(PartOfTransaction partOfTransaction, 
			CurrentStateOfCurrencyInCashRegister currencyStateRegister) {

		BigDecimal amountOfCurrency = currencyStateRegister.getAmountOfCurrency();
		BigDecimal amountOfZlotych = currencyStateRegister.getAmountOfZlotych();
		BigDecimal averageRateOfExchange = currencyStateRegister.getAverageRateOfExchange();
		
		amountOfCurrency = amountOfCurrency.add(partOfTransaction.getRestAmountOfCurrency());
		amountOfZlotych = amountOfZlotych.add(partOfTransaction.getRestAmountOfZlotych());
		if(amountOfCurrency.equals(new BigDecimal(0.00).setScale(2)))
		averageRateOfExchange = new BigDecimal("0.00").setScale(6, RoundingMode.HALF_UP);
		else
		averageRateOfExchange = amountOfZlotych.divide(amountOfCurrency, 6, RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP);	
		
		currencyStateRegister.setAmountOfCurrency(amountOfCurrency);
		currencyStateRegister.setAmountOfZlotych(amountOfZlotych);
		currencyStateRegister.setAverageRateOfExchange(averageRateOfExchange);
		
		this.currentStateOfCurrencyRepository.save(this.currencyStateRegister);
	}
	
}
