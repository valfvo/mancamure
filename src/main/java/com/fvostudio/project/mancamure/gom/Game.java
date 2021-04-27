package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Game {
    public static final class Auth { private Auth() {} }
    private static final Auth auth = new Auth();

    private int roundCount = 0;

    private Board board;
    private Movement lastMovement;

    protected ArrayList<Player> players = new ArrayList<Player>();
    protected int currentPlayerIndex = -1;
    protected boolean hasStarted = false;
    protected boolean isFinished = true;

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex >= 0) {
            return players.get(currentPlayerIndex);
        } else {
            return null;
        }
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
        isFinished = false;
        currentPlayerIndex = -1;

        while (!isFinished) {
            ++roundCount;
            initializeNextRound();
            startNextRound();
            checkForGameEnd();
        }

        onEnd();
    }

    public void onStart() {}

    public void onMovement(Movement movement) {}

    public void onEnd() {}

    public void initializeNextRound() {
        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            ++currentPlayerIndex;

            if (currentPlayerIndex == 0) {
                onStart();
            }
        }
    }

    public void startNextRound() {
        Movement movement = null;
        do {
            movement = getCurrentPlayer().play();
        } while (!isLegal(movement));
        onMovement(movement);

        movement.apply(getBoard());
        lastMovement = movement;
    }

    public Movement getLastMovement() {
        return lastMovement;
    }

    public abstract ArrayList<Movement> getPossibleMovements(OwnableElement element);

    public abstract ArrayList<Movement> getPossibleMovementsFrom(Object obj);

    public abstract boolean isLegal(Movement movement);

    public abstract boolean isLegalFrom(Movement movement, BoardState state);

    public abstract void checkForGameEnd();

    public abstract boolean isFinalState(BoardState state);
}
