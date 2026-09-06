package com.lucid.api.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

// ws://localhost:8080/chat/games

@Component
@ServerEndpoint("/chat/{room-name}")
public class ChatWebSocket {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("room-name") String roomName) {
        System.out.println("Client connected to room: " + roomName + ", Session ID: " + session.getId());
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

    @OnError
    public void error(Session session, Throwable t) {
        System.out.println("Connection problems, runtime errors from message handlers, or conversion errors when decoding messages");
        t.printStackTrace();
    }
}
