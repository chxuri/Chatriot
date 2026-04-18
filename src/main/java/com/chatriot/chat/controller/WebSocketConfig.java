package com.chatriot.chat.controller;
//tells java where class is 

import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.context.annotation.Bean;
//debugging 

import org.springframework.context.annotation.Configuration;
//marks as spring config class
import org.springframework.web.socket.config.annotation.*;
//has socket config tools

@Configuration //contains config code
@EnableWebSocket //turns on socket support in spring boot
//annotionations ^^
public class WebSocketConfig implements WebSocketConfigurer 
{
    //implements = defines how websockets are configured
    private final ChatServer chatServer;
    
    public WebSocketConfig(ChatServer cS) 
    {
        chatServer = cS;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) 
    {
        registry.addHandler(chatServer, "/chat")
        //use chatserver to handle new connections
                .setAllowedOrigins("*")
                //allows connections from anywhere (otherwise CORS issue - block connection)
                .addInterceptors(new org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor());
               //protects against compression
    }


    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
    //for debugging
}