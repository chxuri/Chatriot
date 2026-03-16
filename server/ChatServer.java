@ServerEndpoint("/chat")
public class ChatServer {

    private static Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session) 
    {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session sender) throws IOException 
    {
        for(Session s : sessions) 
        {
            s.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) 
    {
        sessions.remove(session);
    }
}

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
