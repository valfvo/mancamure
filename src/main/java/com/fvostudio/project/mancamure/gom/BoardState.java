package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;

public abstract class BoardState {
    private Board board;
    private Player currentPlayer;

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public abstract ArrayList<BoardState> getStates();

    public abstract ArrayList<BoardState> getPreviousStates();
}
