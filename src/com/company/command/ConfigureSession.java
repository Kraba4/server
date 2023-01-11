package com.company.command;

import com.company.SessionInfo;
import com.company.UserManager;

import java.io.IOException;

public class ConfigureSession extends Command {
    public int maxUsers;
    public String password;
    public String gameName;
    public ConfigureSession(int maxUsers, String password, String gameName) {
        this.maxUsers = maxUsers;
        this.password = password;
        this.gameName = gameName;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.configureSession(manager, maxUsers, password, gameName);
        if(isCorrect){
            new Message("server", "Configure success").send(manager.out);
        }else{
            new Message("server", "Configure error").send(manager.out);
        }
    }
}
