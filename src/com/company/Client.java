package com.company;

import com.company.command.*;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket clientSocket = new Socket("127.0.0.1", 58888);

        OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        writer.writeObject(new Login("AFA", "FAF"));
//        Command command = (Command) reader.readObject();
        Scanner scanner = new Scanner(System.in);
        ClientOutputThread clientOutputThread = new ClientOutputThread(clientSocket, scanner, reader, writer);
        ClientInputThread clientInputThread = new ClientInputThread(clientSocket, reader, writer);
//        System.out.println(((Message)command).text);
        clientOutputThread.start();
        clientInputThread.start();
        clientInputThread.join();
    }

}
