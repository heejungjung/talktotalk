package com.chat.talk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chat.talk.model.Message;
import com.chat.talk.services.RoomListService;

@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private RoomListService roomListService;
    
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	//web socket connected
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}

	//web socket disconnected
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        if (username != null) {
            logger.info("User Disconnected: " + username);

            Message chatMessage = new Message();
            chatMessage.setMessageType(Message.MessageType.LEAVE);
            chatMessage.setSender(username);
            roomListService.Leave(roomId);

            messagingTemplate.convertAndSend("/room/"+roomId, chatMessage);
        }
    }
}