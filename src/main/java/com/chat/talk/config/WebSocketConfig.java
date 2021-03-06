package com.chat.talk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSocket server사용하려면 써야함
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private StompHandler stompHandler;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
		//sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 함
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//설정이므로 바꾸면 다른 곳도 다 바꿔줘야 함
        registry.setApplicationDestinationPrefixes("/chatapp");
        registry.enableSimpleBroker("/room");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
	    registration.interceptors(stompHandler);
	}
}