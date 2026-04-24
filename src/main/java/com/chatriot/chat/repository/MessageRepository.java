package com.chatriot.chat.repository;

import com.chatriot.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository //indicates this class is used as a data access object
//interface allows spring's mongoDB methods to automatically be implemeneted
public interface MessageRepository extends MongoRepository<ChatMessage, String> 
{
    //<ChatMessage, String> -> stores chatMessage objects with IDs that are Strings
    //finds by class period
    List<ChatMessage> findByClassIdAndPeriod(String classId, String period, String teacher);
    //finds the top messages by derived query methods
    //scans name of method and parses it to make db query
    List<ChatMessage> findTop50ByClassIdAndPeriodOrderByTimestampAsc(String classId, String period, String teacher);

    //List<ChatMessage> findByClassIdOrderByTimestampAsc(String classId);
}