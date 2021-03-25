package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public abstract class AwalePlayer extends Player {
    @Override
    public ArrayList<Pair<Integer, Vector3>> getPlayableElementsFrom(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardState state = (AwaleBoardState) boardState;

        List<Integer> pits = state.getPits();
        int pitCount = pits.size();
        int playerPitCount = pitCount / 2;

        ArrayList<Pair<Integer, Vector3>> playableElements = new ArrayList<>();

        if (state.getCurrentPlayer() == this) {
            for (int i = playerPitCount; i < pitCount; ++i) {
                int pit = pits.get(i);
                Vector3 position = new Vector3(i % playerPitCount, 1, 0);

                playableElements.add(new Pair<Integer, Vector3>(pit, position));
            }
        } else {
            for (int i = 0; i < playerPitCount; ++i) {
                int pit = pits.get(i);
                Vector3 position = new Vector3(i, 0, 0);

                playableElements.add(new Pair<Integer, Vector3>(pit, position));
            }
        }

        return playableElements;
    }
}
