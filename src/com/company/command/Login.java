package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class Login extends Command {
    String login;
    String password;

    public Login(String login, String password) {
        hasNext = false;
        this.login = login;
        this.password = password;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.logIn(manager, login, password);
        //handler.user = handler.server.logIn(login, password);
        if(isCorrect){
            new Message("server", "Login success").send(manager.out);
        }else{
            new Message("server", "Wrong login or password").send(manager.out);
        }
    }


}
