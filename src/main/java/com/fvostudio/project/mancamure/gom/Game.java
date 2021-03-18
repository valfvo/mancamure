package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Game {
    public static final class Auth { private Auth() {} }
    private static final Auth auth = new Auth();

    private Board board;
    private Player currentPlayer;
    private ArrayList<Player> players = new ArrayList<Player>();
    private boolean hasStarted = false;

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setBoard(Board board) {
        if (hasStarted()) {
            throw new IllegalStateException("Can not set a board for a game already started.");
        }
        if (board.getGame() != null) {
            throw new IllegalStateException("Can not set a board belonging to another game.");
        }

        this.board = board;
        board.setGame(auth, this);
    }

    public void add(Player player) {
        if (player.getGame() == this) {
            throw new IllegalArgumentException("The player is already playing the game.");
        }

        players.add(player);
        player.setGame(auth, this);
    }

    public void remove(Player player) {
        if (player.getGame() != this) {
            throw new IllegalArgumentException("The player is not playing the game.");
        }

        player.loseBoardElements();

        players.remove(player);
        player.setGame(auth, null);
    }

    public void start() { 
        if (hasStarted()) {
            throw new IllegalStateException("Can not start a game already started.");
        }
        if (players.size() == 0) {
            throw new IllegalStateException("Can not start a game without players.");
        }
        if (getBoard() == null) {
            throw new IllegalStateException("Can not start a game without board.");
        }

        hasStarted = true;
        startNextRound();
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
