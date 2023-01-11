package com.company;

import com.company.command.Command;
import com.company.command.JsonCommand;
import com.google.gson.Gson;
import org.reflections.Reflections;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class UserManager extends Thread{
    public User user;
    Socket socket;
    public UserManagerDelegate server;
    public boolean isRunning;
    public BufferedReader in;
    public OutputStreamWriter out;
    boolean hasActiveSession;
    public Session connectedSession;
    public UserManager(Socket socket, UserManagerDelegate server){
        this.socket = socket;
        this.server = server;
        isRunning = true;
    }
    @Override
    public void run() {
        super.run();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new OutputStreamWriter(socket.getOutputStream());
            Gson gson = new Gson();
            while (isRunning) {
                String dataJson = (String)in.readLine();
                Object data = null;
                System.out.println(dataJson);
                JsonCommand jsonCommand = gson.fromJson(dataJson, JsonCommand.class);
                for( Class cl : (new Reflections("com.company.command")).getSubTypesOf(Command.class)){

                    try {
                        if (cl.getSimpleName().equals(jsonCommand.type)) {
                            System.out.println(cl.getSimpleName());
                            data = gson.fromJson(jsonCommand.json, cl);
                            break;
                        }
                    }
                    catch (NullPointerException nu) {
                        server.userLogOut(this);
                        nu.printStackTrace();
                        System.out.println(user + " logout");
                        isRunning = false;
                        break;
                    }
                }
                System.out.println((Command)data);
                ((Command)data).process(this);
            }
        }
        catch (SocketException e) {
            try {
                server.userLogOut(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(user + " logout");
        }
        catch (IOException e) {
            try {
                server.userLogOut(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.out.println(user + " logout");
        }
        try {
            server.userLogOut(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface UserManagerDelegate{
        boolean addNewUser(String login, String password);
        boolean logIn(UserManager userManager, String login, String password) throws IOException;
        boolean createSession(UserManager owner, int maxUsers, String password) throws IOException;
        int connectUserToSession(UserManager user, int sessionId, String password) throws IOException;
        void userWriteInSessionChat(UserManager user, String text) throws IOException;
        void serverWriteInSessionChat(UserManager user, String text) throws IOException;
        void sendChatToUser(UserManager user) throws IOException;
        String status();
        LinkedList<SessionInfo> lookSession(UserManager manager, int sessionId);
        LinkedList<UserInfo> lookSessionUsers(UserManager manager);
        boolean disconnectUser(UserManager user) throws IOException;
        void userLogOut(UserManager user) throws IOException;
        boolean configureSession( UserManager manager, int maxUsers, String password, String gameName) throws IOException;
        boolean startGame(UserManager manager, String gameName) throws IOException;
        boolean endGame(UserManager manager, String winnner) throws IOException;
        boolean sendGameData(UserManager manager, String dataJson, String type) throws IOException;
    }
}
