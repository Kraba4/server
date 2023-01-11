package com.company;

import java.io.Serializable;

public class SessionInfo implements Serializable {
    int sessionId;
    String ownerLogin;
    int maxUsers;
    boolean hasPassword;
    int nUsers;
    String gameName;
    public SessionInfo(int sessionId, String ownerLogin, int maxUsers, boolean hasPassword, int nUsers, String gameName) {
        this.sessionId = sessionId;
        this.ownerLogin = ownerLogin;
        this.maxUsers = maxUsers;
        this.hasPassword = hasPassword;
        this.nUsers = nUsers;
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return "SessionInfo{" +
                "sessionId=" + sessionId +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", maxUsers=" + maxUsers +
                ", hasPassword=" + hasPassword +
                ", nUsers=" + nUsers +
                '}';
    }
}
