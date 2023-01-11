package com.company.command;

import com.company.SessionInfo;
import com.company.UserInfo;
import com.company.UserManager;

import java.io.IOException;
import java.util.LinkedList;

public class LookSessionUser extends Command{

    public UserInfo user;
    public LookSessionUser() {}
    public LookSessionUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        LinkedList<UserInfo> users =  manager.server.lookSessionUsers(manager);
        for(UserInfo info : users){
            new LookSessionUser(info).send(manager.out, true);
        }
        new LookSessionUser().send(manager.out);
    }
}
