package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class EndGame extends Command{
    String winner;

    public EndGame(String winner) {
        this.winner = winner;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.endGame(manager, winner);
        if(isCorrect){
            new Message("server", "End game success").send(manager.out);
        }else{
            new Message("server", "End game error").send(manager.out);
        }
    }
}
