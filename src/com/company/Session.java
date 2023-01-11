package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Session {
    UserManager owner;
    ArrayList<UserManager> users;
    Map<String, Map<String, Integer>> results;
    LinkedList<String> chat;
    String game;
    boolean gameStarted;
    int id;
    int maxUsers=4;
    String password="";
    public Session(UserManager owner){
        this.owner = owner;
        users = new ArrayList<>();
        results = new HashMap<>();
        users.add(owner);
        chat = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Session{" +
                "owner=" + owner +
                ", users=" + users +
                ", results=" + results +
                ", chat=" + chat +
                ", game='" + game + '\'' +
                ", gameStarted=" + gameStarted +
                ", id=" + id +
                ", maxUsers=" + maxUsers +
                ", password='" + password + '\'' +
                '}';
    }
}
