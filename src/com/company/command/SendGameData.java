package com.company.command;

import com.company.UserManager;

import java.io.IOException;

public class SendGameData extends Command{
    String type;
    String dataJson;

    public SendGameData(String dataJson, String type) {
        this.dataJson = dataJson;
        this.type = type;
    }

    @Override
    public void process(UserManager manager) throws IOException {
        boolean isCorrect = manager.server.sendGameData(manager, dataJson, type);
        if(isCorrect){
            new Message("server", "Data success").send(manager.out);
        }else{
            new Message("server", "Data error").send(manager.out);
        }
    }
}
