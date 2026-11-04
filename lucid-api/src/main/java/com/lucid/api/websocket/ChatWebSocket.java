package com.lucid.api.websocket;

import com.lucid.core.auth.JwtService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ws://localhost:8080/chat/games?token=...

@Component
@ServerEndpoint("/websocket/chat/{room-name}")
public class ChatWebSocket {

    private static final Map<String, Session> sessions = new HashMap<>();
    private static JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        ChatWebSocket.jwtService = jwtService;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("room-name") String roomName) throws IOException {
        System.out.println("Opening websocket connection");

        if (jwtService == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "JWT service not initialized"));
            return;
        }

        String token = extractToken(session);

        if (token == null || !jwtService.isTokenValid(token)) {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized websocket request"));
            return;
        }

        String username = jwtService.extractUsername(token);
        session.getUserProperties().put("username", username);
        System.out.println("Client connected to room: " + roomName + ", Session ID: " + session.getId() + ", User: " + username);
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            System.out.println("Received message from client " + session.getId() + ": " + message);
            for (Session s : sessions.values()) {
                if (!s.getId().equals(session.getId())) {  // don't send the message back to the sender
                    s.getBasicRemote().sendText(session.getId() + ": " + message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Client disconnected: " + session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void error(Session session, Throwable t) {
        System.out.println("Connection problems, runtime errors from message handlers, or conversion errors when decoding messages");
        t.printStackTrace();
    }

    private String extractToken(Session session) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        List<String> tokenValues = params.get("token");
        if (tokenValues == null || tokenValues.isEmpty()) {
            return null;
        }
        return tokenValues.get(0);
    }
}
