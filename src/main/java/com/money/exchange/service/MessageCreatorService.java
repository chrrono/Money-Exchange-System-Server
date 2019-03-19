package com.money.exchange.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.money.exchange.entity.Message;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.Users;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.MeassageRepository;
import com.money.exchange.repository.WorkPlaceRepository;

@Service
public class MessageCreatorService {

	@Autowired
	MeassageRepository messageRepository;
	
	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private WorkPlaceRepository workPlaceRepsitory;
	
	
	public void sendNotificationAboutNewTransaction(Transaction transaction) {
		messageTemplate.convertAndSend("/manager/queue", transaction);
	}
	
	public void saveMassage(Message message) {
		messageRepository.save(message);
	}
	
	public void sendBankTransactionMeesageToEmployee(Transaction bankTransaction, String nameOfMoneyExchangeOffice) {
		
		
		List<Users> destinationList = userService.findUserNameByWorkPlace(nameOfMoneyExchangeOffice);
		for(Users u : destinationList)
			messageTemplate.convertAndSendToUser(u.getUsername(),"/employee/queue", bankTransaction);
	}
	
	public void sendThatBankTransactionIsAccountedByEmployee(long id, String senderUsername) {
		String typeOfMessage = "BankTransaction";
		String content = "Wykonano Transakcje Do Banku o id: "+id;
		WorkPlace sender = userService.findWorkPlaceInfoAboutUser(senderUsername);
		WorkPlace receiver = workPlaceRepsitory.findWorkPlaceByName("Biuro");
		
		LocalDateTime time = LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		LocalDateTime time = now
		
		Message message = new Message(typeOfMessage, content, sender, receiver, time);
		messageRepository.save(message);
		messageTemplate.convertAndSend("/manager/queue", message);
	}
	
	public void sendNotificationAboutUpdateInParticularExchangeOffice(String currencyName, String moneyExchangeName) {
		String typeOfMessage = "Update";
		String content = currencyName;
		WorkPlace sender = workPlaceRepsitory.findWorkPlaceByName(moneyExchangeName);
		WorkPlace receiver = workPlaceRepsitory.findWorkPlaceByName("Biuro");
		
		LocalDateTime time = LocalDateTime.now();
		
		Message message = new Message(typeOfMessage, content, sender, receiver, time);
		messageRepository.save(message);
		messageTemplate.convertAndSend("/manager/queue", message);
	}
	
	public List<Message> getAllMessageReceivedFromParticularExchangeOffice(String moneyExchangeOfficeName){
		return messageRepository.findAllMessageReceivedFromParticularExchangeOffice(moneyExchangeOfficeName);
	}
}
