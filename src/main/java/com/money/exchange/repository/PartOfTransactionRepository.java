package com.money.exchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.money.exchange.entity.PartOfTransaction;

public interface PartOfTransactionRepository extends JpaRepository<PartOfTransaction, Long> {
	
	@Query("select p from PartOfTransaction p inner join p.parentTransaction as t where t.id = :id and p.isAccounted = false")
	PartOfTransaction findPartOfParticularTransactionByIdWhichIsNotAccounted(@Param("id") long id);
	
	@Query("select p from WorkPlace w inner join w.currencyList as c inner join c.transactionList as t inner join t.transactionPartList as p "
			+ "where c.abbreviation = :abbreviation and w.name = :name order by p.id desc")
	List<PartOfTransaction> findPartOfTransactionListOfParticularCurrencyAndWorkPlace(@Param("name") String WorkPlaceName,
																					  @Param("abbreviation") String CurrencyAbrreviation);
} 
