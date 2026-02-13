package com.saihoz.task_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar simple broker para /topic
        config.enableSimpleBroker("/topic");

        // Prefijo para los mensajes que envía el cliente
        config.setApplicationDestinationPrefixes("/app");

        config.setUserDestinationPrefix("/user");

        // Opciones para usar RabbitMQ (producción):
        // config.enableStompBrokerRelay("/topic")
        //     .setRelayHost("localhost")
        //     .setRelayPort(61613)
        //     .setClientLogin("guest")
        //     .setClientPasscode("guest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");

        // Para producción, agregar más orígenes permitidos
    }
}
