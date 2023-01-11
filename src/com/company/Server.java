package com.company;

import com.company.command.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Server implements UserManager.UserManagerDelegate{
    static boolean isRunning = true;
    static int session_id_counter = 0;
    LinkedList<User> users = new LinkedList<>();
    LinkedList<Session> sessions = new LinkedList<Session>();
    LinkedList<UserManager> activeUserManagers = new LinkedList<>();
    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(58197);
        while (isRunning) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("accept");
            UserManager activeUser = new UserManager(clientSocket, this);
            activeUser.start();
            activeUserManagers.add(activeUser);
//            clientSocket.close();
        }
    }
    @Override
    public boolean addNewUser(String login, String password){
        if(isNewLogin(login)){
            User user = new User(login, password);
            users.add(user);
            System.out.println(users);
            return true;
        }
        return false;
    }
    @Override
    public boolean logIn(UserManager userManager, String login, String password) throws IOException {
        for(User user : users){
            if(user.login.equals(login) && user.password.equals(password)){
                if(!isActiveUser(user)) {
                    if(userManager.user!=null){
                        disconnectUser(userManager);
                    }
                    userManager.user = user;
                    System.out.println("good");
                    return true;
                }
                System.out.println("already alive");
                return  false;

            }
        }
        System.out.println("incorrect login and password");
        return false;
    }
    @Override
    public boolean createSession(UserManager owner, int maxUsers, String password) throws IOException {
        if(!owner.hasActiveSession){
            if(owner.connectedSession!=null){
                disconnectUser(owner);
            }
            Session session = new Session(owner);
            session.id = session_id_counter++;
            session.maxUsers = maxUsers;
            session.password = password;
            session.game = "none";
            session.results.put(owner.user.login, new HashMap<>());
            session.results.get(owner.user.login).put("chess", 0);
            session.results.get(owner.user.login).put("tictac", 0);
            new LookSession(new SessionInfo(session.id,
                    owner.user.login,maxUsers, false, 1, session.game)).send(owner.out);
            sessions.add(session);
            owner.hasActiveSession = true;
            owner.connectedSession = session;
            System.out.println(session);
            return true;
        }
        return false;
    }

    private boolean isNewLogin(String login){
        for( User user : users){
            if(user.login.equals(login)){
                return false;
            }
        }
        return true;
    }
    @Override
    public int connectUserToSession(UserManager user, int sessionId, String password) throws IOException {
        for(Session session : sessions){
            if(session.id == sessionId){
                if(!session.users.contains(user) && session.users.size() < session.maxUsers
                        && (session.password.equals("") || password.equals(session.password))) {
                    if(user.connectedSession!=null){
                        disconnectUser(user);
                    }
                    for (UserManager user_x : session.users) {
                        new LookSessionUserNotification().send(user_x.out);
                    }
                    if(session.results.get(user.user.login)==null) {
                        session.results.put(user.user.login, new HashMap<>());
                        session.results.get(user.user.login).put("chess", 0);
                        session.results.get(user.user.login).put("tictac", 0);
                    }
                    session.users.add(user);
                    user.connectedSession = session;
                    serverWriteInSessionChat(user, "зашел в сессию");
                    return 0;
                }
                return 1;
            }
        }
        return 2;
    }
    @Override
    public void userWriteInSessionChat(UserManager user, String text) throws IOException {
        System.out.println("chat");
        user.connectedSession.chat.add(user.user.login + ": " + text);

        for(UserManager user_x : user.connectedSession.users){
            new UpdateChatNotification().send(user_x.out);
        }
    }
    @Override
    public void sendChatToUser(UserManager user) throws IOException {
        System.out.println(user.connectedSession.chat);
        new UpdateChat(user.connectedSession.chat).send(user.out);
    }
    @Override
    public String status(){
        return String.valueOf(users) + sessions;
    }

    @Override
    public LinkedList<SessionInfo> lookSession(UserManager manager, int sessionId) {
        LinkedList<SessionInfo> info = new LinkedList<>();
        if(sessionId==-1) {
            for (Session session : sessions) {
                info.add(new SessionInfo(session.id, session.owner.user.login, session.maxUsers, !session.password.equals(""),
                        session.users.size(), session.game));
            }
        }else{
            for (Session session : sessions) {
                if(session.id == sessionId) {
                    info.add(new SessionInfo(session.id, session.owner.user.login, session.maxUsers, !session.password.equals(""),
                            session.users.size(), session.game));
                }
            }
        }
        return info;

    }

    @Override
    public LinkedList<UserInfo> lookSessionUsers(UserManager manager) {
        LinkedList<UserInfo> info = new LinkedList<>();
        if(manager.connectedSession!=null) {
            for (UserManager userManager : manager.connectedSession.users) {
                info.add(new UserInfo(userManager.user.login,
                        manager.connectedSession.results.get(userManager.user.login)));
            }
        }
        return info;
    }
    @Override
    public boolean disconnectUser(UserManager user) throws IOException {
        if(user.connectedSession!=null){
            user.connectedSession.users.remove(user);
            if(user.connectedSession.owner.equals(user)){
                deleteSession(user.connectedSession);
            }else {
                if(user.connectedSession.users.contains(user.connectedSession.owner)) {
                    for (UserManager user_x : user.connectedSession.users) {
                        new LookSessionUserNotification().send(user_x.out);
                    }
                    serverWriteInSessionChat(user, "вышел из сессии");
                }
            }
            user.connectedSession = null;
            user.hasActiveSession = false;
            return true;
        }
        return false;
    }
    public void deleteSession(Session session) throws IOException {
        ArrayList<UserManager> temp = (ArrayList<UserManager>) session.users.clone();
        for(UserManager user : temp){
            new LookSessionNotification().send(user.out);
            disconnectUser(user);
        }
        sessions.remove(session);
    }
    public boolean isActiveUser(User user){
        for(UserManager manager : activeUserManagers){
            if(manager.user!=null && manager.user.equals(user)) return true;
        }
        return false;
    }

    @Override
    public void userLogOut(UserManager user) throws IOException {
        disconnectUser(user);
        activeUserManagers.remove(user);
    }

    @Override
    public void serverWriteInSessionChat(UserManager user, String text) throws IOException {
        user.connectedSession.chat.add("Пользователь " + user.user.login + " " + text);

        for(UserManager user_x : user.connectedSession.users){
            new UpdateChatNotification().send(user_x.out);
        }
    }
    @Override
    public boolean configureSession(UserManager manager, int maxUsers, String password, String gameName) throws IOException {
        if(manager.hasActiveSession && manager.connectedSession!=null){
            manager.connectedSession.password = password;
            manager.connectedSession.maxUsers = maxUsers;
            manager.connectedSession.game = gameName;
            for(UserManager user_x : manager.connectedSession.users){
                new LookSessionNotification(user_x.connectedSession.id).send(user_x.out);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean startGame(UserManager manager, String gameName) throws IOException {
        if(manager.hasActiveSession && manager.connectedSession!=null) {
            manager.connectedSession.gameStarted = true;
            manager.connectedSession.game = gameName;
            for(UserManager user_x : manager.connectedSession.users){
                if(!user_x.user.login.equals(manager.user.login)) {
                    new StartGameNotification(gameName).send(user_x.out);
                    if (manager.connectedSession.results.get(user_x.user.login).get(user_x.connectedSession.game) == null) {
                        manager.connectedSession.results.get(user_x.user.login).put(user_x.connectedSession.game, 0);
                    }
                }
            }
        }
        return false;
    }
    @Override
    public boolean endGame(UserManager manager, String winner) throws IOException {
        if( manager.connectedSession!=null) {
            manager.connectedSession.gameStarted = false;
            manager.connectedSession.results.get(winner).put(manager.connectedSession.game,
                    manager.connectedSession.results.get(winner).get(manager.connectedSession.game) + 1);
            for(UserManager user_x : manager.connectedSession.users){
                new EndGameNotification(winner).send(user_x.out);
            }
        }
        return false;
    }

    @Override
    public boolean sendGameData(UserManager manager, String dataJson, String type) throws IOException {
        if(manager.connectedSession!=null) {
            for(UserManager user_x : manager.connectedSession.users){

                    new SendGameData(dataJson, type).send(user_x.out);

            }
            return true;
        }
        return false;
    }
}
