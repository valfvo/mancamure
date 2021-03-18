package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.Player;

public class AwaleBoardState implements BoardState {

    @Override
    public Board getBoard() {
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

    /* [U6, U5, U4, U3, U2, U1,
     *  L1, L2, L3, L4, L5, L6]
     * 
     * U : Upper, L : Lower
     */
    public ArrayList<Integer> getPits() {
        return null;
    }

    /* [left, right] */
    public ArrayList<Integer> getBanks() {
        return null;
    }
}
