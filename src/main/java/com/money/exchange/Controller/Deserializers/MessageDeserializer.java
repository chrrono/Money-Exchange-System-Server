package com.money.exchange.Controller.Deserializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.money.exchange.entity.Message;
import com.money.exchange.entity.WorkPlace;
import com.money.exchange.repository.WorkPlaceRepository;

public class MessageDeserializer extends JsonDeserializer<Message>{

	@Autowired
    private static WorkPlaceRepository workPlaceRepsitory;

    public MessageDeserializer() { }

    @Autowired
    public MessageDeserializer(WorkPlaceRepository workPlaceRepsitory) {
        this.workPlaceRepsitory = workPlaceRepsitory;
    }
	
	@Override
	public Message deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		ObjectCodec objectCodec = parser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(parser);
        
        String type = jsonNode.get("type").asText();
        String content = jsonNode.get("content").asText();
        WorkPlace sender = workPlaceRepsitory.findWorkPlaceByName(jsonNode.get("sender").asText());
        WorkPlace receiver = workPlaceRepsitory.findWorkPlaceByName(jsonNode.get("receiver").asText());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(jsonNode.get("time").asText(),formatter);
		return new Message(type, content, sender, receiver, time);
	}

	
}



