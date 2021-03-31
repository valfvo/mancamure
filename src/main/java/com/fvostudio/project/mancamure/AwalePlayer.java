package com.fvostudio.project.mancamure;

import java.util.ArrayList;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Cell;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public abstract class AwalePlayer extends Player {
    @Override
    public Pair<Integer, Integer> getPlayableObject(OwnableElement element) {
        // element comes from getPlayableElements()
        assert(element instanceof Cell);
        Cell pit = (Cell) element;

        int seedCount = pit.getOwnableChildCount();
        Vector3 position = pit.getPosition();

        AwaleBoardState currState = (AwaleBoardState) getGame().getBoard().getState();

        return new Pair<>(seedCount, currState.getPitIndex(position));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getPlayableObjects(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardState state = (AwaleBoardState) boardState;

        ArrayList<Pair<Integer, Integer>> playableElements = new ArrayList<>();
        int pitIndex = state.getFirstPitIndex(this);

        for (int pit : state.getPlayerPits(this)) {
            playableElements.add(new Pair<>(pit, pitIndex));
            ++pitIndex;
        }

        return playableElements;
    }
}
