package com.company;

import com.company.command.*;
import com.google.gson.Gson;
import org.reflections.Reflections;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ClientInputThread extends Thread{
    Socket socket;
    BufferedReader reader;
    OutputStreamWriter writer;
    public ClientInputThread(Socket socket, BufferedReader reader,
    OutputStreamWriter writer ) {
        this.reader = reader;
        this.writer = writer;
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
            try {
                Gson gson = new Gson();
                while(true) {

                    String dataJson = (String)reader.readLine();
                    Command command = takeCommand(dataJson, gson);

                    if (command instanceof Message) {
                        System.out.println(((Message) command).text);
                    }
                    if (command instanceof UpdateChatNotification) {
                        new UpdateChat().send(writer);
                    }
                    if (command instanceof LookSession) {
                        LinkedList<SessionInfo> sessionInfos = new LinkedList<>();
                        sessionInfos.add(((LookSession) command).sessionInfo);
                        while (command.hasNext) {
                            dataJson = (String)reader.readLine();
                            command = takeCommand(dataJson, gson);
                            sessionInfos.add(((LookSession) command).sessionInfo);
                        }
                        for (SessionInfo info : sessionInfos) {
                            System.out.println(info);
                        }
                    }
                    if(command instanceof LookSessionUser){
                        LinkedList<UserInfo> loginsInfo = new LinkedList<>();
                        loginsInfo.add(((LookSessionUser) command).user);
                        while (command.hasNext) {
                            dataJson = (String)reader.readLine();
                            command = takeCommand(dataJson, gson);
                            loginsInfo.add(((LookSessionUser) command).user);
                        }
                        for (UserInfo info : loginsInfo) {
                            System.out.println(info);
                        }
                    }
                    if(command instanceof LookSessionNotification){
                        new LookSession(((LookSessionNotification)command).sessionId).send(writer);
                    }
                    if(command instanceof LookSessionUserNotification){
                        new LookSessionUser().send(writer);
                    }
                    if(command instanceof  StartGameNotification){
                        System.out.println("Game is starting...");
                    }
                    if(command instanceof  EndGameNotification){
                        System.out.println("Game is ending...");
                    }
                    if(command instanceof SendGameData){

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public Command takeCommand(String dataJson, Gson gson){
            Object data = null;
            JsonCommand jsonCommand = gson.fromJson(dataJson, JsonCommand.class);
            for( Class cl : (new Reflections("com.company.command")).getSubTypesOf(Command.class)){
                if(cl.getSimpleName().equals(jsonCommand.type)){
                    data = gson.fromJson(jsonCommand.json, cl);
                    break;
                }
            }
            return (Command) data;
        }
    }

