package LiveChat.config;

// A registry for configuring message broker options. : 스프링의 메시징 지원을 구성하는데 사용
import org.springframework.messaging.simp.config.MessageBrokerRegistry;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@EnableWebSocketMessageBroker
@Configuration
public class SpringConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메시지 받을 때 ( = 메시지 구독 요청 url)
        registry.enableSimpleBroker("/sub");

        // 메시지 보낼 때 ( = 메시지 발행 요청 url)
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
