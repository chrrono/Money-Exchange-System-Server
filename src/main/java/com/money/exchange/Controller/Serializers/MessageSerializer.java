package com.money.exchange.Controller.Serializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.money.exchange.entity.Message;

public class MessageSerializer extends JsonSerializer<Message> {

	@Override
	public void serialize(Message newMessage, JsonGenerator jsonGenerator , SerializerProvider serializerProvider)
			throws IOException {	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = newMessage.getTime().format(formatter).toString();
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("time", time);
		jsonGenerator.writeStringField("sender", newMessage.getSender().getName());
		jsonGenerator.writeStringField("receiver", newMessage.getReceiver().getName());
		jsonGenerator.writeStringField("type", newMessage.getTypeOfMessage());
		jsonGenerator.writeStringField("content", newMessage.getContent());
		jsonGenerator.writeEndObject();

	}

	
}
