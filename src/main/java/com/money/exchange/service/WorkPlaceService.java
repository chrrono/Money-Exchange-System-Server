package com.money.exchange.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.Currency;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.WorkPlaceRepository;

@Service
public class WorkPlaceService {

	@Autowired
	private WorkPlaceRepository workPlaceRepository;
	
	public List<WorkPlace> getListOfWorkPlace() {
		List<WorkPlace> workPlaceList = workPlaceRepository.findWorkPlaceListByMoneyExchageOfficeRole();
		for(WorkPlace workPlace : workPlaceList) {
			for(Currency currency : workPlace.getCurrencyList())
				for(Iterator<Transaction> it = currency.getTransactionList().iterator(); it.hasNext();) {
					Transaction tr = it.next();
					if(tr.getType().equals("SellToTheBank")) {
						it.remove();
					}
				}
		}
		return workPlaceList;
	}
}
