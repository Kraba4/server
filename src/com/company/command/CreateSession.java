package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class CreateSession extends Command {
    int maxUsers;
    String password="";

    public CreateSession() {
        this.maxUsers = 2;
    }
    public CreateSession(int maxUsers) {
        this.maxUsers = maxUsers;
    }
    public CreateSession(int maxUsers, String password) {
        this.maxUsers = maxUsers;
        this.password = password;
    }
    public CreateSession(String password) {
        this.maxUsers = 2;
        this.password = password;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        if(manager.user==null){
            new Message("server", "You must log in first").send(manager.out);
        }else {
            boolean isCorrect = manager.server.createSession(manager, maxUsers, password);
            if (isCorrect) {
                new Message("server", "Session created success").send(manager.out);
            } else {
                new Message("server", "You already have session").send(manager.out);
            }
        }
    }
}
