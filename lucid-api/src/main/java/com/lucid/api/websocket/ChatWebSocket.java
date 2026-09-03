package com.lucid.api.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

// ws://localhost:8080/chat

@Component
@ServerEndpoint("/chat")
public class ChatWebSocket {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            System.out.println("Received message from client " + session.getId() + ": " + message);
            session.getBasicRemote().sendText("Echo: " + message);  // sending response back to the client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Client disconnected: " + session.getId());
    }
}
