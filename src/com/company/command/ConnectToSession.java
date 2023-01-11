package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class ConnectToSession extends Command {
    int sessionId;
    String password = "";
    public ConnectToSession(int sessionId) {
        this.sessionId = sessionId;
    }
    public ConnectToSession(int sessionId, String password) {
        this.sessionId = sessionId;
        this.password = password;
    }
    @Override
    public void process(UserManager manager) throws IOException {
        if(manager.user==null){
            new Message("server", "You must log in first").send(manager.out);
        }else {
            int result = manager.server.connectUserToSession(manager, sessionId, password);
            if (result == 0) {
                new Message("server", "Connection success").send(manager.out);

            }
            if (result == 1) {
                new Message("server", "Access denied").send(manager.out);
            }
            if (result == 2) {
                new Message("server", "Id is not correct").send(manager.out);
            }
        }
    }
}
