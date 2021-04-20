package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class AwaleBoardState implements BoardState {
    private boolean isFinalState;
    private AwaleBoard board;
    private AwaleMovement lastMovement;

    private Player upperPlayer;
    private Player currentPlayer;
    private ArrayList<Integer> pits;
    private ArrayList<Integer> banks;

    public AwaleBoardState(
        AwaleBoard board,
        ArrayList<Integer> pits,
        ArrayList<Integer> banks
    ) {
        this.board = board;
        this.lastMovement = (AwaleMovement) board.getGame().getLastMovement();
        this.upperPlayer = board.getGame().getPlayers().get(0);
        this.currentPlayer = board.getGame().getCurrentPlayer();
        this.pits = pits;
        this.banks = banks;
        this.isFinalState = board.getGame().isFinalState(this);
    }

    public AwaleBoardState(
        AwaleBoardState state,
        AwaleMovement lastMovement,
        Player currentPlayer,
        ArrayList<Integer> pits,
        ArrayList<Integer> banks
    ) {
        this.board = state.getBoard();
        this.lastMovement = lastMovement;
        this.upperPlayer = state.getUpperPlayer();
        this.currentPlayer = currentPlayer;
        this.pits = pits;
        this.banks = banks;
        this.isFinalState = board.getGame().isFinalState(this);
    }

    @Override
    public AwaleBoard getBoard() {
        return board;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public AwaleMovement getLastMovement() {
        return lastMovement;
    }

    @Override
    public List<BoardState> getNextStates() {
        List<BoardState> nextStates = new ArrayList<>();

        Awale game = (Awale) getBoard().getGame();
        AwalePlayer player = (AwalePlayer) getCurrentPlayer();
        ArrayList<Pair<Integer, Integer>> objects = player.getPlayableObjects(this);

        for (Pair<Integer, Integer> object : objects) {
            ArrayList<Movement> movements = game.getPossibleMovementsFrom(object);
            for (Movement movement : movements) {
                if (game.isLegalFrom(movement, this)) {
                    nextStates.add(movement.getResultingState(this));
                }
            }
        }

        return nextStates;
    }

    @Override
    public BoardState getPreviousState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinalState() {
        return isFinalState;
    }

    public Player getUpperPlayer() {
        return upperPlayer;
    }

    public Player getOpponent() {
        if (currentPlayer == upperPlayer) {
            return board.getGame().getPlayers().get(1);
        } else {
            return upperPlayer;
        }
    }

    public List<Integer> getPits() {
        return Collections.unmodifiableList(pits);
    }

    public int getPitIndex(Vector3 position) {
        int playerPitCount = pits.size() / 2;

        if (position.getY() == 0.0) {
            return (int) (playerPitCount - 1 - position.getX());
        } else {
            return (int) (playerPitCount + position.getX());
        }
    }

    public Vector3 getPitPosition(int index) {
        int playerPitCount = pits.size() / 2;

        if (index < playerPitCount) {
            return new Vector3(playerPitCount - 1 - index, 0.0, 0.0);
        } else {
            return new Vector3(index - playerPitCount, 1.0, 0.0);
        }
    }

    public Vector3 getPitPosition(int index, Player player) {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getPlayerPits() {
        return getPlayerPits(currentPlayer);
    }

    public List<Integer> getPlayerPits(Player player) {
        int start, end;

        if (player == upperPlayer) {
            start = 0;
            end = pits.size() / 2;
        } else {
            start = pits.size() / 2;
            end = pits.size();
        }

        return Collections.unmodifiableList(pits.subList(start, end));
    }

    public int getFirstPitIndex(Player player) {
        if (player == upperPlayer) {
            return 0;
        } else {
            return pits.size() / 2;
        }
    }

    public Vector3 getPlayerPitPosition(int i) {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getOpponentPits() {
        return getOpponentPits(currentPlayer);
    }

    public List<Integer> getOpponentPits(Player player) {
        int start, end;

        if (player == upperPlayer) {
            start = pits.size() / 2;
            end = pits.size();
        } else {
            start = 0;
            end = pits.size() / 2;
        }

        return Collections.unmodifiableList(pits.subList(start, end));
    }

    public Vector3 getOpponentPitPosition(int i) {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getBanks() {
        return Collections.unmodifiableList(banks);
    }

    public int getPlayerBank() {
        return getPlayerBank(currentPlayer);
    }

    public int getPlayerBank(Player player) {
        if (player == upperPlayer) {
            return banks.get(0);
        } else {
            return banks.get(1);
        }
    }

    public int getPlayerBankIndex() {
        if (currentPlayer == upperPlayer) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getOpponentBank(Player player) {
        if (player == upperPlayer) {
            return banks.get(1);
        } else {
            return banks.get(0);
        }
    }

    public byte[] getSerializedBoard() {
        int playerPitCount = pits.size() / 2;

        byte[] serializedBoard = 
            new byte[1 + pits.size() * 3 + banks.size() * 2];

        serializedBoard[0] = 0;  // +1: response type
        int offset = 1;

        for (int i = pits.size() - 1; i >= playerPitCount; --i) {
            Vector3 pitPosition = getPitPosition(i);
            serializedBoard[offset++] = (byte) (int) pitPosition.getX();
            serializedBoard[offset++] = (byte) (int) pitPosition.getY();
            serializedBoard[offset++] = (byte) (int) pits.get(i);
        }

        for (int i = 0; i < playerPitCount; ++i) {
            Vector3 pitPosition = getPitPosition(i);
            serializedBoard[offset++] = (byte) (int) pitPosition.getX();
            serializedBoard[offset++] = (byte) (int) pitPosition.getY();
            serializedBoard[offset++] = (byte) (int) pits.get(i);
        }

        serializedBoard[offset++] = 1;
        serializedBoard[offset++] = (byte) (int) banks.get(1);

        serializedBoard[offset++] = 0;
        serializedBoard[offset++] = (byte) (int) banks.get(0);

        return serializedBoard;
    }

    @Override
    public String toString() {
        String s = "";
        int playerPitCount = pits.size() / 2;

        s += banks.get(0);
        s += "\n";
        for (int i = playerPitCount - 1; i >= 0 ; --i) {
            Integer pit = pits.get(i);
            s += pit.toString() + " ";
        }

        s += "\n";

        for (int i = playerPitCount; i < pits.size() ; ++i) {
            Integer pit = pits.get(i);
            s += pit.toString() + " ";
        }

        s += "\n";
        s += banks.get(1);
        s += "\n\n";

        return s;
    }
}
