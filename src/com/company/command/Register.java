package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class Register extends Command {
    String login;
    String password;

    public Register(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.addNewUser(login, password);
        if(isCorrect){
            new Message("server", "Register success").send(manager.out, true);
            new Login(login, password).process(manager);
        }else{
            new Message("server", "Login is already used").send(manager.out);
        }
    }


}
