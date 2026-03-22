import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        //SpringApplication is a springboot class that sets up app, starts server
        //.run activates websocket endpoint
        //ChatApplication.class uses ChatApplication as main config (finds @ServerEndpoint)
        //args passes command line arguments through

    }
}
