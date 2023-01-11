package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class Message extends Command{

    String from;
    public String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }


    @Override
    public void process(UserManager manager) throws IOException {

    }
}
