import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;


@ServerEndpoint("/chat")
//connects to chat.js when socket is opened
public class ChatServer {

    private static Set<Session> sessions = ConcurrentHashMap.newKeySet();
    //thread safe set of all sessions 
    @OnOpen
    public void onOpen(Session session) 
    {
        sessions.add(session);
        //self explanatory session is added to set when socket is opened
    }

    @OnMessage
    public void onMessage(String message, Session sender) throws IOException 
    {
        for(Session s : sessions) 
        {
            s.getBasicRemote().sendText(message);
            //loops through every connected user and sends them the message (includes sender themselves)
        }
    }

    @OnClose
    public void onClose(Session session) 
    {
        sessions.remove(session);
        //removes user from set
    }
}
