package com.chatriot.chat;
//tells java where class is 

import org.springframework.context.annotation.Configuration;
//marks as spring config class
import org.springframework.web.socket.config.annotation.*;
//has socket config tools

@Configuration //contains config code
@EnableWebSocket //turns on socket support in spring boot
//annotionations ^^
public class WebSocketConfig implements WebSocketConfigurer {
    //implements = defines how websockets are configured
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatServer(), "/chat")
        //use chatserver to handle new connections
                .setAllowedOrigins("*");
                //allows connections from anywhere (otherwise CORS issue - block connection)
    }
}