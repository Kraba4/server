package com.company.command;

import com.company.UserManager;
import com.google.gson.Gson;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public abstract class Command implements Serializable {
    public boolean hasNext = false;
    public abstract void process(UserManager manager) throws IOException;
    public void send(OutputStreamWriter out) throws IOException {
        Gson gson = new Gson();

        for( Class cl : (new Reflections("com.company.command")).getSubTypesOf(Command.class)){

            if(cl.isInstance(this)){
                String json = gson.toJson(this);
                JsonCommand jsonCommand = new JsonCommand(cl.getSimpleName(), json);
                String json2 = gson.toJson(jsonCommand);
                System.out.println("send " + json2);
                out.write(json2 + "\n");
                out.flush();
                break;
            }
        }

    }
    public void send(OutputStreamWriter out, boolean hasNext) throws IOException {
        this.hasNext = hasNext;
        Gson gson = new Gson();

        for( Class cl : (new Reflections("com.company.command")).getSubTypesOf(Command.class)){

            if(cl.isInstance(this)){
                String json = gson.toJson(this);
                JsonCommand jsonCommand = new JsonCommand(cl.getSimpleName(), json);
                String json2 = gson.toJson(jsonCommand);
                System.out.println(json2);
                out.write(json2 + "\n");
                out.flush();
            }
        }
    }
}
