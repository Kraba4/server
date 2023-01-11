package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class Chat extends Command{
    String text;

    public Chat(String text) {
        this.text = text;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        if(manager.user==null){
            new Message("server", "You must log in first").send(manager.out);
        }else{
            if(manager.connectedSession!=null){
                manager.server.userWriteInSessionChat(manager, text);
            }else{
                new Message("server", "You not in session").send(manager.out);
            }
        }
    }

}
