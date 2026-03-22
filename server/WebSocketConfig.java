import org.springframework.context.annotation.Bean;
//tells spring to create and manage
import org.springframework.context.annotation.Configuration;
//marks as config class
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//enables sockets

@Configuration
public class WebSocketConfig {
    @Bean
    //create and manage object
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
        //looks for ServerEndpoint(chat) and starts
    }
}