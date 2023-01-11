package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class StartGame extends Command{
    String gameName;

    public StartGame(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.startGame(manager, gameName);
        if(isCorrect){
            new Message("server", "Start game success").send(manager.out);
        }else{
            new Message("server", "Start game error").send(manager.out);
        }
    }
}
