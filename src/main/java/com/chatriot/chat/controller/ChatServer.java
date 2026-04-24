package com.chatriot.chat.controller;
//tells java where class is 

import java.time.LocalDateTime;
//input stream import
import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;

//imports hash map
import java.util.HashMap;

import jakarta.annotation.PostConstruct;
//imports array list
import java.util.ArrayList;
import java.io.IOException;

//get mapping for load
import org.springframework.web.bind.annotation.GetMapping;

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

// to import @controller
import org.springframework.stereotype.Controller;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

//need this specific array list to support websockets being multi threaded
import java.util.concurrent.CopyOnWriteArrayList;
//java classes import
import org.springframework.stereotype.Component;
//makes this a spring bean (object thats managed by spring framework)

//for time stamp formatting
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Controller
//makes it detect chatserver as a custom bean
public class ChatServer extends TextWebSocketHandler {
//parent is textwebsockethandler
    //set of all connected users + polymorphism in the wild
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    //stores details of rooms with strings being classIds
    private final Map<String, Classroom> classInfoMap = new ConcurrentHashMap<>();

    //not final bc im reassigining it later
    private List<Classroom> classInfo = new ArrayList<Classroom>();

    //collection of websocket sessions tied to classId
    private Map<String, Set<WebSocketSession>> roomOccupants = new ConcurrentHashMap<>();
    
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

    @GetMapping("/")
    //temporary reroute to show waiting page instead of index
    public String startPageMode()
    {
        // forward forces spring to look in static folder
        return "forward:/start.html";
    }

    //sets up the lists on startup
    @PostConstruct
    public void loadRooms() throws IOException {
        // only way this works with app.jar
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/classes.json");
        
        //type reference portion signifies every item in json should be turned into Classroom
        //curly brackets makes nameless class that inherits from typereference (helps preserve Classroom type in list)
        classInfo = objectMapper.readValue(is, new TypeReference<List<Classroom>>() {} );
        
        for(Classroom c: classInfo)
        {
            classInfoMap.put(c.getClassId(), c);

            //setting up rooms for potential students with specific keys
            for(Integer period: c.getPeriods())
            {
                String specialKey = c.getClassId() + "_" + period + "_" + c.getTeacher();
                roomOccupants.put(specialKey, ConcurrentHashMap.newKeySet());
                System.out.println("making key [" + specialKey + "]");
                System.out.println("--- VERSION 2.0 CHECK ---");
            }
        }
        
    }

    //overriding method from parent class (FOR SAFETY CHECKING PURPOSES)
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //adds user to current session
        //sessions.add(session);
        //System.out.println("User connected. Total: " + sessions.size());
        //gets last 50 messages from mongo
        //List<ChatMessage> history = messageRepository.findTop50ByOrderByTimestampAsc();

        /*for(ChatMessage m: history)
        {
            //sends string bc cant send full object to websockets
            String jsonMessage = objectMapper.writeValueAsString(m);
            //TextMessage is a wrapper required by spring boot
            session.sendMessage(new TextMessage(jsonMessage));
        }
        */
        
    }
    //runs when socket is opened

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //gets json from js code
        String payload = message.getPayload();

        //objectMapper.readValue(source, targetType);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        //checks if its a join message
        if("JOIN".equals(chatMessage.getType()))
        {
            String room = chatMessage.getClassId();
            String period = chatMessage.getPeriod();
            
            String specialKey = room + "_" + period + "_" + chatMessage.getTeacher();
            System.out.println("DEBUG: join message[" + specialKey + "]");
            if(roomOccupants.containsKey(specialKey))
            {
                //gets the room list and adds the session there
                //tags their session with the room label
                roomOccupants.get(specialKey).add(session);

                //sends back last 50 messages only for this room
                System.out.println("inside handle message");
                List<ChatMessage> history = messageRepository.findTop50ByClassIdAndPeriodOrderByTimestampAsc(room, period, chatMessage.getTeacher());
                for(ChatMessage m: history)
                {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(m)));
                }
                //session.sendMessage(new TextMessage(objectMapper.writeValueAsString(history)));
            }
        }
        else
        {   
            chatMessage.setTimestamp(LocalDateTime.now());
             //saves to mongoDB
            messageRepository.save(chatMessage); 

            //sends message without getting the previous ones.
            broadcastToRoom(chatMessage);
        }

        /*
        //System.out.println("Received: " + message.getPayload());
        for (WebSocketSession s: sessions) 
        {
            //check for matching classID here later
            if(s.isOpen())
            {
                //TextMessage wraps text so it can be sent
                //writeValueAsString converts java object back to json
                System.out.println("inside broadcaster");
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            }
        } 
        */
    }


    //private method so only called thru other class
    private void broadcastToRoom(ChatMessage m) throws Exception
    {
        String specialKey = m.getClassId() + "_" + m.getPeriod() + "_" + m.getTeacher();
    
        System.out.println("DEBUG: Searching for Key in broadcast: [" + specialKey + "]");
        Set<WebSocketSession> occupants = roomOccupants.get(specialKey);

        for(WebSocketSession s: occupants)
        {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsString(m)));
        }
        
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        
        for(Set<WebSocketSession> occupants: roomOccupants.values())
        {
            occupants.remove(session);
        }
        //sessions.remove(session);
        System.out.println("User left. Total: " + sessions.size() + " | Reason: " + status.getReason());
    }
    
    //closes session if error
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("ERROR OCCURRED:");
        exception.printStackTrace();
    }

}