package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Game {
    private Board board;
    private Player currentPlayer;
    private ArrayList<Player> players = new ArrayList<Player>();

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public  List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public void start() { //peut-être a mettre abstract
        
    }

    public void startNextRound() {
        int indexCurrentPlayer = players.indexOf(currentPlayer);
        if (indexCurrentPlayer == players.size() - 1)
            indexCurrentPlayer = 0;
        else
            ++indexCurrentPlayer;

        currentPlayer = players.get(indexCurrentPlayer);
        currentPlayer.play();
    }

    public abstract ArrayList<Movement> getPossibleMovement(OwnableElement element);

    public abstract boolean isLegal(Movement movement);

    public abstract boolean isLegalFrom(Movement movement, BoardState state);

    public abstract void checkForGameEnd();
}
