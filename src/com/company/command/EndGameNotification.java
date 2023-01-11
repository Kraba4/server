package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class EndGameNotification extends Command{
    String winner;

    public EndGameNotification(String winner) {
        this.winner = winner;
    }

    @Override
    public void process(UserManager manager) throws IOException {

    }
}
