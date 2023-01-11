package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class LogOut extends Command{
    @Override
    public void process(UserManager manager) throws IOException {
        manager.server.userLogOut(manager);
        manager.isRunning = false;
    }
}
