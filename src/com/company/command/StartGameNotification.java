package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class StartGameNotification extends Command{
    String gameName;

    public StartGameNotification(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void process(UserManager manager) throws IOException {

    }
}
