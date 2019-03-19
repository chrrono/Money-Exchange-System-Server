package com.money.exchange.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, FindSpecialTransactions {
	
	
	@Query("select t from Transaction t inner join t.currency as c inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 "
			+ "and t.type != 'SellToTheBank'")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlace(String currencyName, String moneyExchangeName);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.isAccounted=false "
			+ "and t.type != 'SellToTheBank' order by t.id asc")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlaceWhichIsNotAccounted(String currencyAbrreviation, String moneyExchangeName);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.type= 'Sell' ")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlaceAllSellTransaction(String currencyName, String moneyExchangeName);	
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.type = 'Buy' and t.date = ?3 order by t.time asc")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlaceAllBuyTransactionInSpecialDate(String currencyName, 
			String moneyExchangeName, LocalDate date);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.type = 'Sell' and t.date = ?3 order by t.time asc")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlaceAllSellTransactionInSpecialDate(String currencyName, 
			String moneyExchangeName, LocalDate date);
	
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.date = ?3 "
			+ "and t.type != 'SellToTheBank' order by t.time asc")
	List<Transaction> findTransactionOfParticularCurrencyAndWorkPlaceAllTransactionInSpecialDate(String currencyName, 
			String moneyExchangeName, LocalDate date);

	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where w.name=?1 and t.date = ?2 "
			+ "and t.type != 'SellToTheBank' order by t.id asc")
	List<Transaction> findTransactionOfParticularWorkPlaceAllTransactionInSpecialDate(String nameOfMoneyExchangeOffice,
			LocalDate date);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where w.name= :moneyExchangeName and  c.abbreviation= :currencyName and t.id >= :id "
			+ "and t.type != 'SellToTheBank' order by t.id asc")
	List<Transaction> findremovedTransactionsOfParticularWorkPlaceWhenUpdatedTransaction(@Param("currencyName") String currencyName, 
			@Param("moneyExchangeName") String moneyExchangeName, @Param("id") Long id);
	
	@Query("select t from Transaction t where t.id = ?1")
	Transaction findTransactionByTheId(long id);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation= :currencyName and w.name= :moneyExchangeName and t.type = 'Buy' "
			+ "and t.id > :startId and t.id < :endId order by t.id asc")
	List<Transaction> findTransactionsWhileUpdatingCurrencyState(@Param("currencyName") String currencyName, 
			@Param("moneyExchangeName") String moneyExchangeName, @Param("startId") long id1, @Param("endId") long id2);
	
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation= :currencyName and w.name= :moneyExchangeName and t.type = 'SellToTheBank' "
			+ "and t.isAccounted = 'false' order by t.time asc")
	List<Transaction> findSellToTheBankTransactionWhichNotAccountedByParticularCurrencyAndWorkPlace(@Param("currencyName") String currencyName, 
			@Param("moneyExchangeName") String moneyExchangeName);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where t.type = 'SellToTheBank' "
			+ "and t.isAccounted = 'false' order by t.id desc")
	List<Transaction> findAllSellToTheBankTransactionWhichNotAccounted();
	
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where t.type = 'SellToTheBank' "
			+ "and t.isAccounted = 'false' and w.name = :moneyExchangeName order by t.id asc")
	List<Transaction> findAllSellToTheBankTransactionWhichNotAccountedFromParticularExchangeOffice(@Param("moneyExchangeName") String moneyExchangeName);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation= :currencyName and w.name= :moneyExchangeName and t.time = :time and "
			+ "t.amountOfCurrency = :amountOfCurrency and t.type = :type order by t.id desc")
	List<Transaction> getTransactionWithIdAfterSave(@Param("currencyName") String currencyName, 
			@Param("moneyExchangeName") String moneyExchangeName,
			@Param("time") LocalDateTime time,
			@Param("amountOfCurrency") BigDecimal amountOfCurrency,
			@Param("type") String type);
	
	//usuwanie transkacji
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.isAccounted=true "
			+ "and t.type != 'SellToTheBank' order by t.id asc")
	List<Transaction> findTransactionOfParticularCurrencyWhichIsAccounted(String currencyAbrreviation, String moneyExchangeName);
	
	@Query("select t from Transaction t inner join t.currency as c "
			+ "inner join c.moneyExchangeOffice as w where c.abbreviation=?1 and w.name=?2 and t.isAccounted=true "
			+ "and t.type != 'SellToTheBank' and t.id < ?3 order by t.id asc")
	List<Transaction> findTransactionOfParticularCurrencyWhichIsAccountedLowerThenId(String currencyAbrreviation, String moneyExchangeName, long id);
	
	@Query("select t from Transaction t where t.isAccounted=true "
			+ "and t.type != 'SellToTheBank' order by t.id asc")
	List<Transaction> findAllTransactionWhichIsAccounted();
	
//	@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
//	User findUserByStatusAndNameNamedParams(
//	  @Param("status") Integer status, 
//	  @Param("name") String name);

	
}

