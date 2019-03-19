package com.money.exchange.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.Currency;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.PartOfTransaction;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.Users;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.exceptions.InappropriatePathValueException;
import com.money.exchange.exceptions.ObjectNotExistException;
import com.money.exchange.exceptions.TransactionBadParametersException;
import com.money.exchange.repository.CurrencyRepository;
import com.money.exchange.repository.PartOfTransactionRepository;
import com.money.exchange.repository.TransactionRepository;
import com.money.exchange.repository.WorkPlaceRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class TransactionService{
	
//	private BigDecimal amountOfCurrency;
//	private BigDecimal amountOfZlotych;
//	private BigDecimal averageRateOfExchange;
	
	@Autowired
	private WorkPlaceRepository moneyExchangeOfficeRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private PartOfTransactionRepository transactionPartRepository;
	
//	@Autowired
//	private MessageToManagerController controller;
	
	@Autowired
	private MessageCreatorService messageService;
	
	CurrentStateOfCurrencyInCashRegister currencyStateRegister;
	
	List<String> validMoneyExchangeName;
	List<String> validCurrencyAbrreviation;
	
	BigDecimal tempNewTransactionZloteAmount;
	BigDecimal tempNewTransactionCurrencyAmount;
	
	public TransactionService() {
		super();
	}
	
	private boolean validatingPathVariables(String nameOfMoneyExchangeOffice, 
			String currencyAbbreviation) {
		if(validMoneyExchangeName == null) {
			validMoneyExchangeName = moneyExchangeOfficeRepository.findNamesByMoneyExchageOfficeRole();
		}
		
		if(validMoneyExchangeName.contains(nameOfMoneyExchangeOffice))
		{
			validCurrencyAbrreviation = currencyRepository.findCurrencyListOfParticularMoneyExchangeOffice(nameOfMoneyExchangeOffice);
			if(validCurrencyAbrreviation.contains(currencyAbbreviation))
				return true;
		}
		
		return false;
	}
	
	private boolean validationOfTransactionValues(Transaction transaction) {
		if(transaction.getAmountOfCurrency().equals(new BigDecimal(0.00).setScale(2)))
			return false;
		if(transaction.getAmountOfZlotych().equals(new BigDecimal(0.00).setScale(2)))
			return false;
		if(transaction.getRateOfExchange().equals(new BigDecimal(0.00).setScale(6)))
			return false;
			
		return true;
	}
	
	public CurrentStateOfCurrencyInCashRegister outputStateOfCurrencyInCashRegister(String nameOfMoneyExchangeOffice, 
			String currencyAbbreviation, Transaction newTransaction){
		
		if(serviceTransaction(nameOfMoneyExchangeOffice, currencyAbbreviation, newTransaction) == true) {
			newTransaction = transactionRepository.getTransactionWithIdAfterSave(currencyAbbreviation, nameOfMoneyExchangeOffice,
					newTransaction.getTime(), newTransaction.getAmountOfCurrency(), newTransaction.getType()).get(0);
			messageService.sendNotificationAboutNewTransaction(newTransaction);
			return currencyStateRegister;
		}
		return null;
	}
	
	public Transaction createBankTransaction(String nameOfMoneyExchangeOffice, String currencyAbbreviation, Transaction newBankTransaction) {
		
		if(!validatingPathVariables(nameOfMoneyExchangeOffice, currencyAbbreviation))
			throw new InappropriatePathValueException("Bad nameOfMoneyExchangeOffice or currencyAbbreviation");
		
		Currency currentCurrency = currencyRepository.findSingleByAbbreviation(currencyAbbreviation, nameOfMoneyExchangeOffice);
		
		if(newBankTransaction.getType().equals("SellToTheBank")) {
			currentCurrency.addTransaction(newBankTransaction);
			currencyRepository.save(currentCurrency);
			newBankTransaction = transactionRepository.getTransactionWithIdAfterSave(currencyAbbreviation, nameOfMoneyExchangeOffice,
					newBankTransaction.getTime(), newBankTransaction.getAmountOfCurrency(), newBankTransaction.getType()).get(0);

			messageService.sendBankTransactionMeesageToEmployee(newBankTransaction, nameOfMoneyExchangeOffice);
			return newBankTransaction;
		}
		else {
			throw new TransactionBadParametersException("Inappropriate type of transaction");
		}
	}
	
	
	public boolean serviceTransaction(String nameOfMoneyExchangeOffice, String currencyAbbreviation, Transaction newTransaction) 
		{
		
		if(!validatingPathVariables(nameOfMoneyExchangeOffice, currencyAbbreviation))
			throw new InappropriatePathValueException("Bad nameOfMoneyExchangeOffice or currencyAbbreviation");
		
		if(!validationOfTransactionValues(newTransaction))
			throw new TransactionBadParametersException("Inappropriate money values, couldn't be 0");
		
		this.tempNewTransactionCurrencyAmount = newTransaction.getAmountOfCurrency().setScale(2,RoundingMode.HALF_UP);
		
		Currency currentCurrency = currencyRepository.findSingleByAbbreviation(currencyAbbreviation, nameOfMoneyExchangeOffice);
		
		this.currencyStateRegister = currentCurrency.getCashRegisterStateForCurrency();
		List<Transaction> transactionInfluentOnCashRegister = transactionRepository
																.findTransactionOfParticularCurrencyAndWorkPlaceWhichIsNotAccounted(
																		currencyAbbreviation, nameOfMoneyExchangeOffice);
		
		currentCurrency.setTransactionList(transactionInfluentOnCashRegister);
		
		if(newTransaction.getType().equals("Buy")) {
			updatecurrencyStateRegisterAfterBuyTransaction(newTransaction, this.currencyStateRegister);
		}
		
		else if(newTransaction.getType().equals("Sell")) {
			updateStateAfterSellTransaction(transactionInfluentOnCashRegister, newTransaction);
		}
		else {
			throw new TransactionBadParametersException("Inappropriate type of transaction");
		}
		
		currentCurrency.addTransaction(newTransaction);
		currencyRepository.saveAndFlush(currentCurrency);
		transactionRepository.flush();
		return true;
	}
	
	
	
	public void updatecurrencyStateRegisterAfterBuyTransaction(Transaction newTransaction, CurrentStateOfCurrencyInCashRegister currencyStateRegister) {
		
		BigDecimal amountOfCurrency = currencyStateRegister.getAmountOfCurrency();
		BigDecimal amountOfZlotych = currencyStateRegister.getAmountOfZlotych();
		BigDecimal averageRateOfExchange = currencyStateRegister.getAverageRateOfExchange();
		
		amountOfCurrency = amountOfCurrency.add(newTransaction.getAmountOfCurrency());
		amountOfZlotych = amountOfZlotych.add(newTransaction.getAmountOfZlotych());
		averageRateOfExchange = amountOfZlotych.divide(amountOfCurrency, 6, RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP);	
		
		currencyStateRegister.setAmountOfCurrency(amountOfCurrency);
		currencyStateRegister.setAmountOfZlotych(amountOfZlotych);
		currencyStateRegister.setAverageRateOfExchange(averageRateOfExchange);
		
		newTransaction.setInAccountedProcess(true);
		newTransaction.setAccounted(false);
	}
	
	public void updateStateAfterSellTransaction(List<Transaction> transactions,
			Transaction newTransaction) 
	{
		BigDecimal amountOfCurrency = this.currencyStateRegister.getAmountOfCurrency();
		
		if(amountOfCurrency.compareTo(this.tempNewTransactionCurrencyAmount) < 0)
			throw new TransactionBadParametersException("Sell not executed because of limits in cash register");
		
		if(!validationOfTransactionValues(newTransaction))
			throw new TransactionBadParametersException("Inappropriate money values, couldn't be 0");
		
		ListIterator<Transaction> it = transactions.listIterator();
		
		Transaction firstTransaction = transactions.get(0);
//		PartOfTransaction partTransaction = firstTransaction.getTransactionPart();
		PartOfTransaction partTransaction = transactionPartRepository.findPartOfParticularTransactionByIdWhichIsNotAccounted(firstTransaction.getId());
		
		
		if( partTransaction != null && !partTransaction.isAccounted()) {
			it.next();
			accountFirstTransactionIfhasPartOfTransation(firstTransaction, partTransaction);
		}
		else {
			updateafterSellwithoutAnyPartTransaction(it);
			updatecurrencyStateRegisterAfterSellTransaction(newTransaction);
			return;
		}
		
		updateafterSellwithoutAnyPartTransaction(it);
		updatecurrencyStateRegisterAfterSellTransaction(newTransaction);
	}
	
	private void accountFirstTransactionIfhasPartOfTransation(Transaction firstTransaction, PartOfTransaction transactionPart)
	{
						
				BigDecimal transactionPartCurrency = returnBigDecimalfromPartOfTransactionValue(transactionPart, "currency_amount");
				BigDecimal transactionPartZlote = returnBigDecimalfromPartOfTransactionValue(transactionPart, "zloty_amount");
				
				BigDecimal firstTransactionRateExchange = returnBigDecimalfromTransactionValue(firstTransaction, "exchange_rate");
				
				BigDecimal tempCurrencyStateZlote = this.currencyStateRegister.getAmountOfZlotych();
				
				if(transactionPartCurrency.compareTo(this.tempNewTransactionCurrencyAmount) <= 0)
				{
					tempCurrencyStateZlote = tempCurrencyStateZlote.subtract(transactionPartZlote);
					this.tempNewTransactionCurrencyAmount = this.tempNewTransactionCurrencyAmount.subtract(transactionPartCurrency);
				
//					transactionPartRepository.delete(transactionPart);
//					transactionPart.setParentTransaction(null);
					
					
					transactionPart.setAccounted(true);
					firstTransaction.setAccounted(true);
					firstTransaction.setInAccountedProcess(false);
					
					//
					if(this.tempNewTransactionCurrencyAmount.equals(new BigDecimal(0.00).setScale(2))) {
						this.createPartOfTransaction(firstTransaction);
						firstTransaction.setAccounted(false);
						firstTransaction.setInAccountedProcess(true);
					}
					//
				}
				else
				{
					tempCurrencyStateZlote = tempCurrencyStateZlote.subtract(this.tempNewTransactionCurrencyAmount.multiply(firstTransactionRateExchange));
					transactionPartCurrency = transactionPartCurrency.subtract(this.tempNewTransactionCurrencyAmount);
					//transactionPartCurrency = transactionPartCurrency.subtract(tempNewTransactionCurrencyValue);
					transactionPart.setAccounted(true);
					
					PartOfTransaction newTransactionPart = new PartOfTransaction();
					newTransactionPart.setRestAmountOfCurrency(transactionPartCurrency);
					newTransactionPart.setRestAmountOfZlotych(transactionPartCurrency.multiply(firstTransactionRateExchange));
					newTransactionPart.setAccounted(false);
					firstTransaction.addTransactionPart(newTransactionPart);
					transactionPartRepository.save(newTransactionPart);
					
					this.tempNewTransactionCurrencyAmount = new BigDecimal(0.00).setScale(2);
//					transactionPartRepository.save(transactionPart);
				}
				transactionPartRepository.save(transactionPart);
				this.currencyStateRegister.setAmountOfZlotych(tempCurrencyStateZlote);
			
	}
	
	private void updateafterSellwithoutAnyPartTransaction(ListIterator<Transaction> it) {
		
		while(it.hasNext() && iteratingCondition(it)) {
			accountPastTransaction(it);
		}
		
		if(this.tempNewTransactionCurrencyAmount.equals(new BigDecimal(0.00).setScale(2))) {
//			transactionRepository.save(newTransaction);
			return;
		}
		
		if(it.hasNext()) {
			Transaction lastTransactionWithInfluenceOnCashRegister = it.next();
			createPartOfTransaction(lastTransactionWithInfluenceOnCashRegister);
		}
	}
	
	private boolean iteratingCondition(ListIterator<Transaction> it) {
		 
		if(it.next().getAmountOfCurrency().compareTo(this.tempNewTransactionCurrencyAmount) <= 0 ) {
			it.previous();
			return true;
		}
		it.previous();
		return false;
	}
	
	private void accountPastTransaction(ListIterator<Transaction> it) {
		Transaction tempPastTransaction = it.next();
//		transactionRepository.save(tempPastTransaction);
//		tempNewTransactionCurrencyValue = tempNewTransactionCurrencyValue.subtract(tempPastTransaction.getAmountOfCurrency());
		
		this.tempNewTransactionCurrencyAmount = this.tempNewTransactionCurrencyAmount.subtract(tempPastTransaction.getAmountOfCurrency());
		this.currencyStateRegister.setAmountOfZlotych((this.currencyStateRegister.getAmountOfZlotych())
										.subtract(tempPastTransaction.getAmountOfZlotych()));
		
		tempPastTransaction.setAccounted(true);
		tempPastTransaction.setInAccountedProcess(false);
		//
		if(this.tempNewTransactionCurrencyAmount.equals(new BigDecimal(0.00).setScale(2))) {
			this.createPartOfTransaction(tempPastTransaction);
			tempPastTransaction.setAccounted(false);
			tempPastTransaction.setInAccountedProcess(true);
		}
		//
		
	}
	
	private void createPartOfTransaction(Transaction lastTransactionWithInfluenceOnCashRegister) {
		
		//
		if(this.tempNewTransactionCurrencyAmount.equals(new BigDecimal(0.00).setScale(2))) {
			BigDecimal restZeroAmountOfCurrency = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
			BigDecimal restZeroAmountOfZlotych = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
			PartOfTransaction partOfTransaction = new PartOfTransaction(restZeroAmountOfCurrency, restZeroAmountOfZlotych,false);
			lastTransactionWithInfluenceOnCashRegister.addTransactionPart(partOfTransaction);
			transactionPartRepository.save(partOfTransaction);
			return;
		}
		//
		
//		this.currencyStateRegister.setAmountOfZlotych(this.tempNewTransactionCurrencyAmount.multiply(
//								createBigDecimalfromTransactionValue(lastTransactionWithInfluenceOnCashRegister,"exchange_rate")));
		
		BigDecimal restAmountOfCurrency =  returnBigDecimalfromTransactionValue(lastTransactionWithInfluenceOnCashRegister,"currency_amount")
												.subtract(this.tempNewTransactionCurrencyAmount)
												.setScale(2, RoundingMode.HALF_UP);
		BigDecimal restAmountOfZlotych = restAmountOfCurrency
												.multiply(returnBigDecimalfromTransactionValue(lastTransactionWithInfluenceOnCashRegister,"exchange_rate"))
												.setScale(2, RoundingMode.HALF_UP);
		
		this.currencyStateRegister.setAmountOfZlotych((this.currencyStateRegister.getAmountOfZlotych())
				.subtract(lastTransactionWithInfluenceOnCashRegister.getAmountOfZlotych().subtract(restAmountOfZlotych)));
		
		PartOfTransaction partOfTransaction = new PartOfTransaction(restAmountOfCurrency, restAmountOfZlotych,false);
		lastTransactionWithInfluenceOnCashRegister.addTransactionPart(partOfTransaction);
		transactionPartRepository.save(partOfTransaction);
//		transactionRepository.save(lastTransactionWithInfluenceOnCashRegister);
	}
	
	public void updatecurrencyStateRegisterAfterSellTransaction(Transaction newTransaction) {
		
		currencyStateRegister.setAmountOfCurrency(this.currencyStateRegister.getAmountOfCurrency().subtract(newTransaction.getAmountOfCurrency()));
		
		BigDecimal amountOfCurrency = this.currencyStateRegister.getAmountOfCurrency();
		BigDecimal amountOfZlotych = this.currencyStateRegister.getAmountOfZlotych();
		
		if(amountOfCurrency.equals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)))
			currencyStateRegister.setAverageRateOfExchange(new BigDecimal(0));
		else
			currencyStateRegister.setAverageRateOfExchange((
					amountOfZlotych.divide(amountOfCurrency, 6, RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP)));
		
	}

	private BigDecimal returnBigDecimalfromTransactionValue(Transaction transaction, String typeOfValue) {
		if(typeOfValue.toLowerCase().equals("currency_amount"))
			return transaction.getAmountOfCurrency();
		else if(typeOfValue.toLowerCase().equals("exchange_rate"))
			return transaction.getRateOfExchange();
		else if(typeOfValue.toLowerCase().equals("zloty_amount"))
			return transaction.getAmountOfZlotych();
		return null;
	}
	
	private BigDecimal returnBigDecimalfromPartOfTransactionValue(PartOfTransaction partOfTransaction, String typeOfValue) {
		if(typeOfValue.toLowerCase().equals("currency_amount"))
			return partOfTransaction.getRestAmountOfCurrency();
		else if(typeOfValue.toLowerCase().equals("zloty_amount"))
			return partOfTransaction.getRestAmountOfZlotych();
		return null;
	}
	
	

	public List<Transaction> getTodayTransactions(String nameOfMoneyExchangeOffice, String currencyAbbreviation) {
		
		if(!validatingPathVariables(nameOfMoneyExchangeOffice, currencyAbbreviation))
			return null;
		
		LocalDate today = LocalDate.now();
		List<Transaction> transactionList = transactionRepository.findTransactionOfParticularCurrencyAndWorkPlaceAllTransactionInSpecialDate(
																currencyAbbreviation, nameOfMoneyExchangeOffice, today);
		System.out.println("############################"+transactionList);
		return transactionList;
	}

	public List<Transaction> getAllTransactionOfParticulrWorkPlaceByDate(String nameOfMoneyExchangeOffice) {
		LocalDate date = LocalDate.now();
		return transactionRepository.findTransactionOfParticularWorkPlaceAllTransactionInSpecialDate(nameOfMoneyExchangeOffice, date);
	}
	
	public List<Transaction> getAllTransactionOfParticularWorkPlace(String currencyAbbreviation, String nameOfMoneyExchangeOffice) {
		return transactionRepository.findTransactionOfParticularCurrencyAndWorkPlace(currencyAbbreviation,nameOfMoneyExchangeOffice);
	}
	
	public List<Transaction> findAllSellToTheBankTransactionWhichNotAccounted(){
		return transactionRepository.findAllSellToTheBankTransactionWhichNotAccounted();
	}
	
	public List<Transaction> findAllSellToTheBankTransactionWhichNotAccountedFromParticularExchangeOffice(String nameOfMoneyExchangeOffice){
		return transactionRepository.findAllSellToTheBankTransactionWhichNotAccountedFromParticularExchangeOffice(nameOfMoneyExchangeOffice);
	}
	
	public void updateSellToTheBankTransaction(long id, String sender) {
		Transaction trToTheBank = transactionRepository.findTransactionByTheId(id);
		
		if(trToTheBank == null)
			throw new ObjectNotExistException("There is no bank Transaction with this Id");
		
		trToTheBank.setAccounted(true);
		trToTheBank.setInAccountedProcess(false);
		transactionRepository.deleteById(trToTheBank.getId());
		messageService.sendThatBankTransactionIsAccountedByEmployee(id, sender);		
	}

	//usuwanie transkacji
	public void deleteAllAccountedTransactions() {
		List<Transaction> transactionsToDelete = transactionRepository.findAllTransactionWhichIsAccounted();
		deleteTransactionFromList(transactionsToDelete);
	}
	
	public void deleteAllTransactionsFromAllWorkPlace() {
		List<WorkPlace> MoneyExchangeOfficeList = moneyExchangeOfficeRepository.findWorkPlaceListByMoneyExchageOfficeRole();
		for(WorkPlace exchangePoint : MoneyExchangeOfficeList) {
			String exchangePointName = exchangePoint.getName();
			List<String> currencyList = currencyRepository.findCurrencyListOfParticularMoneyExchangeOffice(exchangePointName);
			for(String currencyAbb : currencyList) {
				deleteTransactionByCurrencyAndMoneyExchangeName(currencyAbb,exchangePointName);
			}
		}
	}
	
	public void deleteTransactionByCurrencyAndMoneyExchangeName(String currencyAbrreviation, String moneyExchangeName) {
		List<Transaction> accountedTransactions = transactionRepository
				.findTransactionOfParticularCurrencyWhichIsAccounted(currencyAbrreviation, moneyExchangeName);
		List<Transaction> notAccountedTransactions = transactionRepository
				.findTransactionOfParticularCurrencyAndWorkPlaceWhichIsNotAccounted(currencyAbrreviation, moneyExchangeName);
		
		if(notAccountedTransactions.isEmpty()) {
			if(accountedTransactions.isEmpty())
				return;
			else
				deleteTransactionFromList(accountedTransactions);
		}
		else {
			long lastNotAccountedBuyTransactionId = notAccountedTransactions.get(0).getId();
			accountedTransactions = transactionRepository.findTransactionOfParticularCurrencyWhichIsAccountedLowerThenId(
					currencyAbrreviation, moneyExchangeName, lastNotAccountedBuyTransactionId);
			deleteTransactionFromList(accountedTransactions);
		}
	}
	
	private void deleteTransactionFromList(List<Transaction> transactionsToDelete) {
		int lastTransactionIndex = transactionsToDelete.size()-1;
		System.out.println("*****************************************************");
//		for( Transaction removeTr : transactionsToDelete)
//			System.out.println(removeTr);
//		System.out.println("*****************************************************");
		
		for( Transaction removeTr : transactionsToDelete) {
			List<PartOfTransaction> partTransactionToDelete = removeTr.getTransactionPart();
			if(!partTransactionToDelete.isEmpty()) {
				int lastPartIndex = partTransactionToDelete.size() - 1;
				PartOfTransaction removePartTr;
				while(lastPartIndex >= 0) {
					removePartTr = partTransactionToDelete.get(lastPartIndex);
					if(!removeTr.removeTransaction(removePartTr)) return;
//					if(!removeTr.removeTransactionFromCurrency()) return;
					transactionPartRepository.save(removePartTr);
					transactionPartRepository.delete(removePartTr);
					lastPartIndex--;
				}
			}
			if(!removeTr.removeTransactionFromCurrency()) return;
			transactionRepository.save(removeTr);
			transactionRepository.deleteById(removeTr.getId());
			transactionRepository.flush();
		}
	}
	
}

