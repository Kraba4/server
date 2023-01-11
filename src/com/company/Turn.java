package com.company;

import java.io.Serializable;

public class Turn implements Serializable {
    public int playerId;
    public Pos position;

    public Turn(int playerId, Pos position) {
        this.playerId = playerId;
        this.position = position;
    }
}
