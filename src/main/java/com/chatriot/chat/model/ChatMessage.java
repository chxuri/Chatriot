//helps for future referencing
package com.chatriot.chat.model;
//date & time library
import java.time.LocalDateTime;

public class ChatMessage {
    private String sender;
    private String content;
    private String classId;
    //timestamp!
    private LocalDateTime timestamp;
    private String type;

    //default
    public ChatMessage()
    {
        timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public String getSender()
    {
        return sender;
    }

    public String getContent()
    {
        return content;
    }

    public String getClassId()
    {
        return classId;
    }

    public String getType()
    {
        return type;
    }

    public void setSender(String s)
    {
        sender = s;
    }
    
    public void setContent(String c)
    {
        content = c;
    }
    
    public void setClassId(String cI)
    {
        classId = cI;
    }

    public void setTimestamp(LocalDateTime t)
    {
        timestamp = t;
    }
    
    public void setType(String t)
    {
        type = t;
    }
}