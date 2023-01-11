package com.company.command;

import com.company.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class UpdateChat extends Command {
    LinkedList<String> chat;

    public UpdateChat(LinkedList<String> chat) {
        this.chat = chat;
    }

    public UpdateChat() {
    }

    @Override
    public void process(UserManager manager) throws IOException {
        if(manager.connectedSession!=null){
            manager.server.sendChatToUser(manager);
        } else{
            new Message("server", "END_CHAT").send(manager.out);
        }
    }
}
