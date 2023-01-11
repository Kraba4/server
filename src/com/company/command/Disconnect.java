package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class Disconnect extends Command {
    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.disconnectUser(manager);
        if(isCorrect){
            new Message("server", "Disconnect success").send(manager.out);
        }else{
            new Message("server", "Disconnect wrong").send(manager.out);
        }
    }
}
