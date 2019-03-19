package com.money.exchange.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.money.exchange.entity.Message;
import com.money.exchange.entity.Transaction;
import com.money.exchange.service.MessageCreatorService;

@RestController
@RequestMapping("/messages")
public class MessageRestController {
	
	@Autowired
	private MessageCreatorService messageService;

//	@RequestMapping(value = "/all/{nameOfMoneyExchangeOffice}/currency/{abbreviation}", method=RequestMethod.GET, produces="application/json")
//	@ResponseBody
//	public List<Transaction> getListOfAllTransactions(@PathVariable String nameOfMoneyExchangeOffice, @PathVariable String abbreviation){
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String currentPrincipalName = authentication.getName();
//		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$"+currentPrincipalName);
//		List<Transaction> transactions = transactonService.getAllTransactionOfParticularWorkPlace(abbreviation, nameOfMoneyExchangeOffice);
//		return transactions;
//	}
	
	@RequestMapping(value = "/All/{nameOfMoneyExchangeOffice}", method=RequestMethod.GET, produces="application/json")
	public List<Message> getListOfAllMessages(@PathVariable String nameOfMoneyExchangeOffice){
		
		List<Message> messages = messageService.getAllMessageReceivedFromParticularExchangeOffice(nameOfMoneyExchangeOffice);
		return messages;
	}
}
