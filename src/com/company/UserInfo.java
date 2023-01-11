package com.company;

import java.io.Serializable;
import java.util.Map;

public class UserInfo implements Serializable {
    String login;
    Map<String, Integer> info;

    public UserInfo(String login, Map<String, Integer> info) {
        this.login = login;
        this.info = info;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "login='" + login + '\'' +
                ", info=" + info +
                '}';
    }
}
