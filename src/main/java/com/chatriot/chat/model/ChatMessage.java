//helps for future referencing
package com.chatriot.chat.model;

public class chatMessage {
    private String sender;
    private String content;
    private String classId;

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
}