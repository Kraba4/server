package com.company.command;

import com.company.SessionInfo;
import com.company.UserManager;

import java.io.IOException;
import java.util.LinkedList;

public class LookSession extends Command{
    public SessionInfo sessionInfo;
    public int sessionId = -1;
    public LookSession() {
    }
    public LookSession(SessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

    public LookSession(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        LinkedList<SessionInfo> sessions = manager.server.lookSession(manager, sessionId);
        System.out.println(sessions.size());
        for(SessionInfo session : sessions){
            new LookSession(session).send(manager.out, true);
        }
        new LookSession().send(manager.out);

    }
}
