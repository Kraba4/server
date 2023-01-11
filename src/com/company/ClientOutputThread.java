package com.company;

import com.company.command.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientOutputThread extends Thread{
    Socket socket;
    BufferedReader reader;
    OutputStreamWriter writer;
    Scanner scanner;
    public ClientOutputThread(Socket socket, Scanner scanner, BufferedReader reader,
                               OutputStreamWriter writer ) {
        this.reader = reader;
        this.writer = writer;
        this.socket = socket;
        this.scanner = scanner;
    }
    @Override
    public void run() {
        super.run();


            try {
                while(true) {
                    String commandText = scanner.nextLine();
                    Command command = stringToCommand(commandText);
                    if (command == null) {
                        System.out.println("Не верная команда");
                        continue;
                    }
                    stringToCommand(commandText).send(writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    private static Command stringToCommand(String command) throws IOException {
        String[] params = command.split("@");
        switch (params[0]){
            case "LOGIN":
                if(params.length==3) {
                    return new Login(params[1], params[2]);
                }else return null;
            case "REGISTER":
                if(params.length==3) {
                    return new Register(params[1], params[2]);
                }else return null;
            case "CREATE":
                if(params.length==3) {
                    return new CreateSession(Integer.parseInt(params[1]), params[2]);
                }else if(params.length==2){
                    return new CreateSession(Integer.parseInt(params[1]));
                }else if(params.length==1){
                    return new CreateSession();
                }
            case "CONNECT":
                if(params.length==3){
                    return new ConnectToSession(Integer.parseInt(params[1]), params[2]);
                }else if(params.length==2){
                    return new ConnectToSession(Integer.parseInt(params[1]));
                }else return null;
            case "CHAT":
                return new Chat(params[1]);
            case "UPDATE_CHAT":
                return new UpdateChat();
            case "STATUS":
                return new ServerStatus();
            case "LOOK":
                if(params.length==1) {
                    return new LookSession();
                }else if(params.length==2){
                    return new LookSession(Integer.parseInt(params[1]));
                }else return null;
            case "LOOK_USER":
                return new LookSessionUser();
            case "DISCONNECT":
                return new Disconnect();
            case "CONFIGURE":
                return new ConfigureSession(Integer.parseInt(params[1]), params[2], params[3]);
            default:
                return null;
        }
    }
}
