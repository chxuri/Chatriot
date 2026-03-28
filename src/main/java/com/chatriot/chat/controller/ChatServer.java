package com.chatriot.chat.controller;
//tells java where class is 

import com.chatriot.chat.model.ChatMessage;
import com.chatriot.chat.repository.MessageRepository;

//java library that turns json content into a java object
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.*;
//imports websocket classes
import org.springframework.web.socket.handler.TextWebSocketHandler;
//base class 
import java.util.Set;

//need this specific array list to support websockets being multi threaded
import java.util.concurrent.CopyOnWriteArrayList;
//java classes import
import org.springframework.stereotype.Component;
//makes this a spring bean

@Component
//makes it detect chatserver as a custom bean
public class ChatServer extends TextWebSocketHandler {
//parent is textwebsockethandler
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList();
    //set of all connected users 
    
    //turns json into java objects
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final MessageRepository messageRepository;
    
    //constructor
    public ChatServer(MessageRepository mR)
    {
        messageRepository = mR;
    }

    //overriding method from parent class (FOR SAFETY CHECKING PURPOSES)
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("User connected: Total: " + sessions.size());
    }
    //runs when socket is opened

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //gets json from js code
        String payload = message.getPayload();

        //objectMapper.readValue(source, targetType);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage);

        //saves to mongoDB
        messageRepository.save(chatMessage); 

        //System.out.println("Received: " + message.getPayload());
        for (WebSocketSession s: sessions) 
        {
            //check for matching classID here later
            if(s.isOpen())
            {
                //TextMessage wraps text so it can be sent
                //writeValueAsString converts java object back to json
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            }
        } 
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("User left. Total: " + sessions.size() + " | Reason: " + status.getReason());
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("ERROR OCCURRED:");
        exception.printStackTrace();
    }
    //closes session
}