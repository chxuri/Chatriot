package com.chatriot.chat.controller;
//tells java where class is 

import com.fasterxml.jackson.core.type.TypeReference;

//imports hash map
import java.util.HashMap;

import jakarta.annotation.PostConstruct;
//imports array list
import java.util.ArrayList;
import java.io.IOException;

import com.chatriot.chat.model.ChatMessage;
import com.chatriot.chat.repository.MessageRepository;
import com.chatriot.chat.model.Classroom;

//java library that turns json content into a java object
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.*;
//imports websocket classes
import org.springframework.web.socket.handler.TextWebSocketHandler;
//base class 
import java.util.Set;
import java.util.List;
import java.io.File;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

//need this specific array list to support websockets being multi threaded
import java.util.concurrent.CopyOnWriteArrayList;
//java classes import
import org.springframework.stereotype.Component;
//makes this a spring bean (object thats managed by spring framework)

//for time stamp formatting
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
//makes it detect chatserver as a custom bean
public class ChatServer extends TextWebSocketHandler {
//parent is textwebsockethandler
    //set of all connected users + polymorphism in the wild
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final Map<String, Classroom> classInfoMap = new ConcurrentHashMap<>();

    //not final bc im reassigining it later
    private List<Classroom> classInfo = new ArrayList<Classroom>();

    private final Map<String, List<WebSocketSession>> roomOccupants = new ConcurrentHashMap<>();
    
    //turns json into java objects
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    //final makes sure app cant start without repo to give
    private final MessageRepository messageRepository;
    
    //constructor
    public ChatServer(MessageRepository mR)
    {
        messageRepository = mR;
        //using for timestamp formatting
        objectMapper.registerModule(new JavaTimeModule());
    }


    //sets up the lists on startup
    @PostConstruct
    public void loadRooms() throws IOException {
        File classroomFile = new File("src/main/resources/static/classes.json");
        //type reference portion signifies every item in json should be turned into Classroom
        //curly brackets makes nameless class that inherits from typereference (helps preserve Classroom type in list)
        classInfo = objectMapper.readValue(classroomFile, new TypeReference<List<Classroom>>() {} );
        
        for(Classroom c: classInfo)
        {
            classInfoMap.put(c.getClassId(), c);
            roomOccupants.put(c.getClassId(), new CopyOnWriteArrayList<>());
        }
    }

    //overriding method from parent class (FOR SAFETY CHECKING PURPOSES)
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //adds user to current session
        sessions.add(session);
        System.out.println("User connected. Total: " + sessions.size());
        //gets last 50 messages from mongo
        List<ChatMessage> history = messageRepository.findTop50ByOrderByTimestampAsc();

        for(ChatMessage m: history)
        {
            //sends string bc cant send full object to websockets
            String jsonMessage = objectMapper.writeValueAsString(m);
            //TextMessage is a wrapper required by spring boot
            session.sendMessage(new TextMessage(jsonMessage));
        }
        
    }
    //runs when socket is opened

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //gets json from js code
        String payload = message.getPayload();

        //objectMapper.readValue(source, targetType);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

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