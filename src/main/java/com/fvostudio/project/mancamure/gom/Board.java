package com.fvostudio.project.mancamure.gom;

import java.util.Objects;

import com.fvostudio.project.mancamure.gom.util.Vector3;

public abstract class Board extends Owner {
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game.Auth auth, Game game) {
        Objects.requireNonNull(auth);
        this.game = game;
    }

    public abstract Cell getCell(Vector3 coordinate);

    public abstract BoardState getState();

    public abstract void changeState(BoardState state);
}
