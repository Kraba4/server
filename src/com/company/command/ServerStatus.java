package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class ServerStatus extends Command {
    @Override
    public void process(UserManager manager) throws IOException {
        new Message("server", manager.server.status()).send(manager.out);
    }
}
