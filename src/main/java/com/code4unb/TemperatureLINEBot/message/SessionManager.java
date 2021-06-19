package com.code4unb.TemperatureLINEBot.message;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SessionManager {
    Map<String, Session> sessions;

    public SessionManager(){
        sessions = new HashMap<>();
    }

    public boolean hasSession(String line_id){
        return sessions.containsKey(line_id);
    }

    public boolean hasActiveSession(String line_id){
        return hasSession(line_id) && !sessions.get(line_id).isExpired();
    }

    public Optional<Session> findSession(String line_id){
        return Optional.ofNullable(sessions.get(line_id));
    }

    public Session findOrCreateSession(String line_id){
        Optional<Session> session = findSession(line_id);
        return session.orElseGet(Session::new);
    }

    public void createSession(String line_id){
        addSession(line_id,new Session());
    }

    public void addSession(String line_id,Session session){
        sessions.put(line_id,session);
    }

    public void removeSession(String line_id){
        sessions.remove(line_id);
    }
}
