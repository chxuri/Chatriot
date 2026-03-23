package com.chatriot.chat;
//tells java where class is 

import org.springframework.web.socket.*;
//imports websocket classes
import org.springframework.web.socket.handler.TextWebSocketHandler;
//base class 
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
//java classes import
import org.springframework.stereotype.Component;
//makes this a spring bean

@Component
//makes it detect chatserver as a custom bean
public class ChatServer extends TextWebSocketHandler {
//parent is textwebsockethandler
    private static Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    //set of all connected users 
    @Override
    //overriding method from parent class (also protects from bugs yay)
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("User connected: Total: " + sessions.size());
    }
    //runs when socket is opened

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //protected -> accesible only by children 
            System.out.println("Received: " + message.getPayload());
            for (WebSocketSession s: sessions) 
            {
                if(s.isOpen())
                {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
                //s.sendMessage(new TextMessage(message.getPayload()));
                //TextMessage wraps text so it can be sent
            } 
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Disconnected! Reason: " + status.getReason());
        System.out.println("Code: " + status.getCode());
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("ERROR OCCURRED:");
        exception.printStackTrace();
    }
    //closes session
}