package com.money.exchange.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.money.exchange.entity.Message;
import com.money.exchange.entity.Transaction;
import com.money.exchange.entity.Users;
import com.money.exchange.repository.WorkPlaceRepository;
import com.money.exchange.service.MessageCreatorService;
import com.money.exchange.service.UserService;

@Controller
public class MessageToManagerController {

	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	@Autowired
	private MessageCreatorService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WorkPlaceRepository workPlaceRepository;
		
//	@MessageMapping("/send/message")
//	public void onReceivedMeassage(String message) {
//		this.messageTemplate.convertAndSend("/chat", message+LocalDateTime.now());
//		this.messageTemplate.
//		//messageTemplate.convertAndSend("/chat", tr);
//	}
	
//	@CrossOrigin(origins = "http://localhost:4201")
	@MessageMapping("/send/toManager")
	@SendTo("/manager/queue")
	public Message onReceivedMeassageFromEmployee(Message message) {
		messageService.saveMassage(message);
		return message;
	}
	
//	@CrossOrigin(origins = "http://localhost:4201")
//	@MessageMapping("/send/toManager")
//	@SendTo("/manager/queue")
//	public Message onReceivedMeassageFromEmployee(Message message) {
//		Message m = message;
//		messageService.saveMassage(m);
//		return m;
//		//messageTemplate.convertAndSend("/chat", tr);
//	}

//	@CrossOrigin(origins = "http://localhost:4200")
	@MessageMapping("/send/toEmployee")
//	@SendTo("/employee/queue")
	public Message onReceivedMeassageFromManager(Message message) {
		List<Users> destinationList = userService.findUserNameByWorkPlace(message.getReceiver().getName());
		for(Users u : destinationList)
			messageTemplate.convertAndSendToUser(u.getUsername(),"/employee/queue", message);
		messageService.saveMassage(message);
		return message;
		//messageTemplate.convertAndSend("/chat", tr);
	}
	
//	@MessageMapping("/sendTransaction/toEmployee")
////	@SendTo("/employee/queue")
//	public Message onReceivedTransactionFromManager(Transaction trToTheBank) {
//		List<Users> destinationList = userService.findUserNameByWorkPlace(message.getReceiver().getName());
//		for(Users u : destinationList)
//			messageTemplate.convertAndSendToUser(u.getUsername(),"/employee/queue", message);
//		messageService.saveMassage(message);
//		return message;
//		//messageTemplate.convertAndSend("/chat", tr);
//	}
}
