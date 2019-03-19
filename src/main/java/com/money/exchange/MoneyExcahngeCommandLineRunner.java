package com.money.exchange;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.money.exchange.entity.Currency;
import com.money.exchange.entity.CurrentStateOfCurrencyInCashRegister;
import com.money.exchange.entity.Role;
import com.money.exchange.entity.StaffMember;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.Users;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.CurrencyRepository;
import com.money.exchange.repository.PartOfTransactionRepository;
import com.money.exchange.repository.RoleRepository;
import com.money.exchange.repository.StaffMemberRepository;
import com.money.exchange.repository.TransactionRepository;
import com.money.exchange.repository.WorkPlaceRepository;
import com.money.exchange.service.CurrencyStateService;
import com.money.exchange.service.TransactionService;
import com.money.exchange.service.UpdateTransactionHistoryService;
import com.money.exchange.service.UserService;

@Component
public class MoneyExcahngeCommandLineRunner implements CommandLineRunner {
	
//public class MoneyExcahngeCommandLineRunner {
	
	@Autowired
	private TransactionService trService;
	@Autowired
	private UpdateTransactionHistoryService updateTransactionService;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private WorkPlaceRepository workPlaceRepository;
	@Autowired
	private StaffMemberRepository staffMemberRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private PartOfTransactionRepository transactionPartRepository;
	
	
	@Autowired
	private CurrencyStateService stateService;
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
//	@Override
	public void run(String... args) throws Exception {
		
		WorkPlace szubrytKantor = new WorkPlace("Szubryt","Nowy Sacz","Kantor");
		WorkPlace pocztaKantor = new WorkPlace("Poczta","Nowy Sacz","Kantor");
		WorkPlace krynicaKantor = new WorkPlace("Krynica","Nowy Sacz","Kantor");
		WorkPlace biuroMenadzera = new WorkPlace("Biuro","Nowy Sacz","BiuroMenadzera");
		
		workPlaceRepository.save(szubrytKantor);
		workPlaceRepository.save(pocztaKantor);
		workPlaceRepository.save(biuroMenadzera);
		workPlaceRepository.save(krynicaKantor);
		
		Currency EurCurrency = new Currency("Euro", "EUR", new BigDecimal(4.21), new BigDecimal(4.32));
		Currency UsdCurrency = new Currency("Us Dollar", "USD", new BigDecimal(3.71), new BigDecimal(3.72));
		Currency Eur1Currency = new Currency("Euro", "EUR", new BigDecimal(4.21), new BigDecimal(4.32));
		Currency Usd1Currency = new Currency("Us Dollar", "USD", new BigDecimal(3.71), new BigDecimal(3.72));
		Currency NokCurrency = new Currency("Korona Norweska", "NOK", new BigDecimal(0.425), new BigDecimal(0.444));
		Currency Nok1Currency = new Currency("Korona Norweska", "NOK", new BigDecimal(0.425), new BigDecimal(0.444));
		UsdCurrency.setRateOfBuy(new BigDecimal(4));
		
		Currency EurCurrencyKrynica = new Currency("Euro", "EUR", new BigDecimal(4.21), new BigDecimal(4.32));
//		Currency GbpCurrencyKrynica = new Currency("Funt Brytyjski", "GBP", new BigDecimal(4.52), new BigDecimal(4.72));
		Currency UsdCurrencyKrynica = new Currency("Us Dollar", "USD", new BigDecimal(4.21), new BigDecimal(4.32));
//		Currency ChfCurrencyKrynica = new Currency("Frank Szwajacrski", "CHF", new BigDecimal(3.61), new BigDecimal(3.82));
		Currency NokCurrencyKrynica = new Currency("Korona Norweska", "NOK", new BigDecimal(0.425), new BigDecimal(0.444));
		
		krynicaKantor.add(EurCurrencyKrynica);
//		krynicaKantor.add(GbpCurrencyKrynica);
//		krynicaKantor.add(ChfCurrencyKrynica);
		krynicaKantor.add(NokCurrencyKrynica);
		krynicaKantor.add(UsdCurrencyKrynica);
		
		szubrytKantor.add(EurCurrency);
		szubrytKantor.add(UsdCurrency);
		pocztaKantor.add(Eur1Currency);
		pocztaKantor.add(Usd1Currency);
		szubrytKantor.add(NokCurrency);
		pocztaKantor.add(Nok1Currency);
		
		workPlaceRepository.save(szubrytKantor);
		workPlaceRepository.save(pocztaKantor);
		workPlaceRepository.save(biuroMenadzera);
		workPlaceRepository.save(krynicaKantor);
		
		StaffMember member1 = new StaffMember("jakub", "Sulinka", "Emplyee");
		member1.setLogIn(true);
		StaffMember member2 = new StaffMember("jola", "Baduch", "Emplyee");
		member2.setLogIn(true);
		StaffMember member3 = new StaffMember("luki", "Ziomek", "Menager");
		member3.setLogIn(true);
		StaffMember member4 = new StaffMember("lukasz", "Góru", "Employee");
		member4.setLogIn(true);
		
		szubrytKantor.add(member1);
		pocztaKantor.add(member2);
		biuroMenadzera.add(member3);
		krynicaKantor.add(member4);
		
		
		staffMemberRepository.save(member2);
		staffMemberRepository.save(member1);
		staffMemberRepository.save(member3);
		staffMemberRepository.save(member4);

		
		Role role = new Role("EMPLOYEE");
		Role role2 = new Role("EMPLOYEE2");
		
		Role roleMenadzer = new Role("Menadzer".toUpperCase());
		List<Role> menadzerRoles = Arrays.asList(roleMenadzer);
		List<Role> roles = Arrays.asList(role);
		List<Role> roles2 = Arrays.asList(role2);
		roleRepository.save(role);
		roleRepository.save(role2);
		roleRepository.save(roleMenadzer);
		Users user = new Users("user", "user", true, roles, member1);
		userService.save(user);
		
		Users admin = new Users("menadzer", "menadzer", true, menadzerRoles, member3);
		userService.save(admin);
		
		Users userKrynica = new Users("krynica","krynica", true, roles2, member4);
		userService.save(userKrynica);
		
		
		
		CurrentStateOfCurrencyInCashRegister stateEUR = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateEUR1 = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateUSD = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateUSD1 = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateNOK = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateNOK1 = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		
		EurCurrency.setCashRegisterStateForCurrency(stateEUR);
		Eur1Currency.setCashRegisterStateForCurrency(stateEUR1);
		UsdCurrency.setCashRegisterStateForCurrency(stateUSD);
		Usd1Currency.setCashRegisterStateForCurrency(stateUSD1);
		NokCurrency.setCashRegisterStateForCurrency(stateNOK);
		Nok1Currency.setCashRegisterStateForCurrency(stateNOK1);
		
		currencyRepository.save(EurCurrency);
		currencyRepository.save(UsdCurrency);
		currencyRepository.save(Eur1Currency);
		currencyRepository.save(Usd1Currency);
		currencyRepository.save(NokCurrency);	
		currencyRepository.save(Nok1Currency);
		
		Transaction transaction1 = new Transaction("Buy",
				new BigDecimal(1200), 
				new BigDecimal(5040), 
				new BigDecimal(4.2),
				LocalDate.of(2018, Month.AUGUST, 25),
				LocalDateTime.of(2018, Month.AUGUST, 25, 12, 20));
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction2 = new Transaction("Sell",
				new BigDecimal(1000), 
				new BigDecimal(4300), 
				new BigDecimal(4.3),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction3 = new Transaction("Buy",
				new BigDecimal(2000), 
				new BigDecimal(8600), 
				new BigDecimal(4.3),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction4 = new Transaction("Sell",
				new BigDecimal(500), 
				new BigDecimal(2200), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction5 = new Transaction("Sell",
				new BigDecimal(1000), 
				new BigDecimal(4400), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
		trService.serviceTransaction("Poczta", "EUR", transaction1);
		trService.serviceTransaction("Poczta", "EUR", transaction2);
		trService.serviceTransaction("Poczta", "EUR", transaction3);
		trService.serviceTransaction("Poczta", "EUR", transaction4);
		trService.serviceTransaction("Poczta", "EUR", transaction5);
		
		Transaction transaction1a = new Transaction("Buy",
				new BigDecimal(400), 
				new BigDecimal(1688), 
				new BigDecimal(4.22),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction2a = new Transaction("Buy",
				new BigDecimal(400), 
				new BigDecimal(1696), 
				new BigDecimal(4.24),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction3a = new Transaction("Buy",
				new BigDecimal(285), 
				new BigDecimal(1199.85), 
				new BigDecimal(4.21),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction3b = new Transaction("Buy",
				new BigDecimal(115), 
				new BigDecimal(486.45), 
				new BigDecimal(4.23),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction4a = new Transaction("Sell",
				new BigDecimal(50), 
				new BigDecimal(215), 
				new BigDecimal(4.3),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction5a = new Transaction("Sell",
				new BigDecimal(1000), 
				new BigDecimal(4400), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction6a = new Transaction("SellToTheBank",
				new BigDecimal(100), 
				new BigDecimal(440), 
				new BigDecimal(4.40),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		trService.serviceTransaction("Szubryt", "EUR", transaction1a);
		trService.serviceTransaction("Szubryt", "EUR", transaction2a);
		trService.serviceTransaction("Szubryt", "EUR", transaction3a);
		trService.serviceTransaction("Szubryt", "EUR", transaction3b);
		trService.serviceTransaction("Szubryt", "EUR", transaction4a);
		trService.serviceTransaction("Szubryt", "EUR", transaction5a);
		//trService.createBankTransaction("Szubryt", "EUR", transaction6a);
		
		Transaction transactionNOK = new Transaction("Buy",
				new BigDecimal(1250), 
				new BigDecimal(551.25), 
				new BigDecimal(0.441),
				LocalDate.now(),
				LocalDateTime.now());
//		TimeUnit.SECONDS.sleep(1);
		
		trService.serviceTransaction("Szubryt", "NOK", transactionNOK); 
//		System.out.println();
//		for(Transaction tr : transactionRepository.findTransactionOfParticularCurrencyAndWorkPlaceAllBuyTransactionInSpecialDate(
//				"EUR","Kantor Szubryt",LocalDate.of(2018, Month.AUGUST, 25))) {
//			System.out.println(tr);
//			if(tr.getTransactionPart() != null)
//				System.out.println(tr.getTransactionPart());
//		}
//		System.out.println();
//		System.out.println();
//		System.out.println(currencyRepository.findSingleByAbbreviation("EUR", "Kantor Szubryt"));
//		System.out.println(workPlaceRepository.findWorkPlaceByName("Kantor Szubryt"));
//
//		EurCurrency.setTransactionList(transactionRepository.findTransactionOfParticularCurrencyAndWorkPlace("USD","Kantor Szubryt"));
//		EurCurrency.getTransactionList().get(0).setAmountOfCurrency(new BigDecimal(1234));
//		currencyRepository.save(EurCurrency);
	
		
		CurrentStateOfCurrencyInCashRegister stateEURKrynica = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateGBPKrynica = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateUSDKrynica = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister statCHFKrynica = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		CurrentStateOfCurrencyInCashRegister stateNOKKrynica = new CurrentStateOfCurrencyInCashRegister(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

		
		EurCurrencyKrynica.setCashRegisterStateForCurrency(stateEURKrynica);
//		GbpCurrencyKrynica.setCashRegisterStateForCurrency(stateGBPKrynica);
		UsdCurrencyKrynica.setCashRegisterStateForCurrency(stateUSDKrynica);
//		ChfCurrencyKrynica.setCashRegisterStateForCurrency(statCHFKrynica);
		NokCurrencyKrynica.setCashRegisterStateForCurrency(stateNOKKrynica);

		
		currencyRepository.save(EurCurrencyKrynica);
//		currencyRepository.save(GbpCurrencyKrynica);
		currencyRepository.save(UsdCurrencyKrynica);
//		currencyRepository.save(ChfCurrencyKrynica);
		currencyRepository.save(NokCurrencyKrynica);	

		
		Transaction transaction1k = new Transaction("Buy",
				new BigDecimal(1020), 
				new BigDecimal(4284), 
				new BigDecimal(4.2),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction7k = new Transaction("Sell",
				new BigDecimal(20), 
				new BigDecimal(88), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction8k = new Transaction("Sell",
				new BigDecimal(50), 
				new BigDecimal(220), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction4k = new Transaction("Buy",
				new BigDecimal(1000), 
				new BigDecimal(4350), 
				new BigDecimal(4.35),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction2k = new Transaction("Buy",
				new BigDecimal(1000), 
				new BigDecimal(4300), 
				new BigDecimal(4.3),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction3k = new Transaction("Buy",
				new BigDecimal(2000), 
				new BigDecimal(8500), 
				new BigDecimal(4.25),
				LocalDate.now(),
				LocalDateTime.now());
		
//		TimeUnit.SECONDS.sleep(1);
		
//		TimeUnit.SECONDS.sleep(1);
		
		Transaction transaction5k = new Transaction("Sell",
				new BigDecimal(4950), 
				new BigDecimal(21780), 
				new BigDecimal(4.4),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction6k = new Transaction("Buy",
				new BigDecimal(1000), 
				new BigDecimal(4200), 
				new BigDecimal(4.20),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction9k = new Transaction("SellToTheBank",
				new BigDecimal(50), 
				new BigDecimal(220), 
				new BigDecimal(4.40),
				LocalDate.now(),
				LocalDateTime.now());
		
		Transaction transaction10k = new Transaction("SellToTheBank",
				new BigDecimal(200), 
				new BigDecimal(880), 
				new BigDecimal(4.40),
				LocalDate.now(),
				LocalDateTime.now());

		trService.serviceTransaction("Krynica", "EUR", transaction1k);
		trService.serviceTransaction("Krynica", "EUR", transaction7k);
		trService.serviceTransaction("Krynica", "EUR", transaction8k);
		trService.serviceTransaction("Krynica", "EUR", transaction4k);
		trService.serviceTransaction("Krynica", "EUR", transaction2k);
		trService.serviceTransaction("Krynica", "EUR", transaction3k);
		trService.serviceTransaction("Krynica", "EUR", transaction5k);
		trService.serviceTransaction("Krynica", "EUR", transaction6k);
		
//		trService.deleteAllAccountedTransactions();
		
//		trService.deleteAllTransactionsFromAllWorkPlace();
//		trService.deleteTransactionByCurrencyAndMoneyExchangeName("EUR", "Krynica");
		//trService.createBankTransaction("Krynica", "EUR", transaction9k);
		//trService.createBankTransaction("Krynica", "EUR", transaction10k);
		
//		Transaction updatedTransaction = transactionRepository.findTransactionByTheId(Long.parseLong("54"));
//		updatedTransaction.setAmountOfCurrency(new BigDecimal(500));
//		updatedTransaction.setAmountOfZlotych(new BigDecimal(2200));
//		updatedTransaction.setRateOfExchange(new BigDecimal(4.4));
//		updateTransactionService.updateTransaction(updatedTransaction, "EUR", "Krynica");
		
//		Transaction updatedTransaction1 = transactionRepository.findTransactionByTheId(Long.parseLong("39"));
//		updatedTransaction1.setAmountOfCurrency(new BigDecimal(1000));
//		updatedTransaction1.setAmountOfZlotych(new BigDecimal(4400));
//		updatedTransaction1.setRateOfExchange(new BigDecimal(4.4));
//		updateTransactionService.updateTransaction(updatedTransaction1, "EUR", "Krynica");
//		
//		updatedTransaction1 = transactionRepository.findTransactionByTheId(Long.parseLong("56"));
//		updatedTransaction1.setAmountOfCurrency(new BigDecimal(4000));
//		updatedTransaction1.setAmountOfZlotych(new BigDecimal(17800));
//		updatedTransaction1.setRateOfExchange(new BigDecimal(4.45));
//		updateTransactionService.updateTransaction(updatedTransaction1, "EUR", "Krynica");
		
//		System.out.println("*******************"+stateService.getCurrencyStateInParticularCurrency("EUR"));
//		System.out.println("*******************"+userService.findWorkPlaceInfoAboutUser("user"));
//		System.out.println("*******************11"+transactionRepository.findSellToTheBankTransactionWhichNotAccountedByParticularCurrencyAndWorkPlace("EUR", "Krynica"));
//		System.out.println("*******************11");
//		for(Transaction tr : transactionRepository.findAllSellToTheBankTransactionWhichNotAccounted()) {
//			System.out.println(tr.toMessage());
//		}
		//		System.out.println("*******************22"+transactionPartRepository.findPartOfTransactionListOfParticularCurrencyAndWorkPlace("Krynica", "EUR"));
		
		
	}

}
