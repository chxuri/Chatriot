//helps for future referencing
package com.chatriot.chat.model;
//date & time library
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ChatMessage {
    //handled by server
    private String id;
    //stuff i wrote
    private String sender;
    private String content;
    private String classId;
    private String period;
    //timestamp!
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

    public String getId()
    {
        return id;
    }

    public String getPeriod()
    {
        return period;
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

    public void setPeriod(String p)
    {
        period = p;
    }

    public void setId(String i)
    {
        id = i;
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