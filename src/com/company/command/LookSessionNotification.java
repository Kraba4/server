package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class LookSessionNotification extends Command{
    public int sessionId;

    public LookSessionNotification() {
    }

    public LookSessionNotification(int sessionId){
        this.sessionId = sessionId;
    }
    @Override
    public void process(UserManager manager) throws IOException {

    }
}
