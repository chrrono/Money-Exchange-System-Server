package com.money.exchange.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.money.exchange.Controller.Deserializers.MessageDeserializer;
import com.money.exchange.Controller.Serializers.MessageSerializer;


@JsonSerialize(using = MessageSerializer.class)
@JsonDeserialize(using = MessageDeserializer.class)
@Entity
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long Id;
	
	private String type;
	private String content;
	private LocalDateTime time;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sender_Id")
	private WorkPlace sender;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="receiver_Id")
	private WorkPlace receiver;
	
	public Message() {
		super();
	}

	public Message(String typeOfMessage, String content, WorkPlace sender, WorkPlace receiver, LocalDateTime time) {
		super();

		this.type = typeOfMessage;
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
	}

	
	
	public long getId() {
		return Id;
	}



	public void setId(long id) {
		Id = id;
	}



	public String getTypeOfMessage() {
		return type;
	}



	public void setTypeOfMessage(String typeOfMessage) {
		this.type = typeOfMessage;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}

	

	public LocalDateTime getTime() {
		return time;
	}



	public void setTime(LocalDateTime time) {
		this.time = time;
	}



	public WorkPlace getSender() {
		return sender;
	}



	public void setSender(WorkPlace sender) {
		this.sender = sender;
	}



	public WorkPlace getReceiver() {
		return receiver;
	}



	public void setReceiver(WorkPlace receiver) {
		this.receiver = receiver;
	}



	@Override
	public String toString() {
		return "Message [Id=" + Id + ", typeOfMessage=" + type + ", content=" + content + ", sender=" + sender
				+ ", receiver=" + receiver + "]";
	}
	
	
	
}
