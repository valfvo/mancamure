package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Game;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class AwaleBoardState implements BoardState {
    Board board;
    private ArrayList<Integer> pits = new ArrayList<>();
    private ArrayList<Integer> banks = new ArrayList<>();
    boolean isFinalState;

    public AwaleBoardState(Board refBoard,
                           ArrayList<Integer> pits, ArrayList<Integer> banks
    ) {
        this.board = refBoard;

        for (Integer pit : pits) {
            this.pits.add(pit);
        }

        for (Integer bank : banks) {
            this.banks.add(bank);
        }

        this.isFinalState = board.getGame().isFinalStateFrom(this);
    }

    @Override
    public Board getBoard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game getGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getCurrentPlayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Movement getLastMovement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BoardState> getNextStates() {
        throw new UnsupportedOperationException();
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
        return getBoard().getGame().getPlayers().get(0);
    }

    /* [U6, U5, U4, U3, U2, U1,
     *  L1, L2, L3, L4, L5, L6]
     * 
     * U : Upper, L : Lower
     */
    public ArrayList<Integer> getPits() {
        throw new UnsupportedOperationException();
    }

    public int getPitIndex(Vector3 position) {
        // int index = pos.getY() == 1.0
        //     ? (int) (pos.getX() + 6)
        //     : (int) (5 - pos.getX());
        throw new UnsupportedOperationException();
    }

    public Vector3 getPitPosition(int index, Player player) {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getPlayerPits() {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getPlayerPits(Player player) {
        Player upperPlayer = getUpperPlayer();
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
        throw new UnsupportedOperationException();
    }

    public Vector3 getPlayerPitPosition(int i) {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getOpponentPits() {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getOpponentPits(Player player) {
        Player upperPlayer = getUpperPlayer();
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

    public ArrayList<Integer> getBanks() {
        throw new UnsupportedOperationException();
    }

    public int getPlayerBank() {
        throw new UnsupportedOperationException();
    }

    public int getPlayerBank(Player player) {
        throw new UnsupportedOperationException();
    }

    public int getPlayerBankIndex() {
        throw new UnsupportedOperationException();
    }

    public int getOpponentBank(Player player) {
        throw new UnsupportedOperationException();
    }
}
