package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Movement;

public class AwaleMovement implements Movement {
    private int startingPitIndex;

    // public AwaleMovement(Pit pit) {
    //     this.pit = pit.getSeedCount();
    //     this.startingPitPosition = pit.getPosition();
    // }

    public AwaleMovement(int startingPitIndex) {
        this.startingPitIndex = startingPitIndex;
    }

    @Override
    public void apply(Board board) {
        board.changeState(getResultingState(board.getState()));
    }

    @Override
    public AwaleBoardState getResultingState(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);

        AwaleBoardState state = (AwaleBoardState) boardState;        
        List<Integer> pits = state.getPits();
        int pitCount = pits.size();
        int playerPitCount = pitCount / 2;

        int sowedSeedCount = pits.get(startingPitIndex);
        int loopCount = sowedSeedCount / pitCount;
        int playerSide = startingPitIndex / playerPitCount;

        // cumulative index of the destination pit
        int destination = (startingPitIndex + sowedSeedCount + loopCount);
        boolean destinationIsOnOpponentSide = 
            (destination % pitCount) / playerPitCount != playerSide;

        ArrayList<Integer> newPits = new ArrayList<>(pits);
        ArrayList<Integer> newBanks = new ArrayList<>(state.getBanks());
        int playerBankIndex = state.getPlayerBankIndex();

        newPits.set(startingPitIndex, 0);

        for (int i = destination; i > startingPitIndex; --i) {
            int realIndex = i % pitCount;

            if (realIndex == startingPitIndex) {
                continue;
            }

            newPits.set(realIndex, newPits.get(realIndex) + 1);
        }

        if (destinationIsOnOpponentSide) {
            for (
                int i = destination % pitCount;
                (i >= 0
                  && i / playerPitCount != playerSide)
                  && 2 <= newPits.get(i) && newPits.get(i) <= 3;
                --i
            ) {
                int seedCount = newPits.get(i);
                newPits.set(i, 0);
                newBanks.set(playerBankIndex, newBanks.get(playerBankIndex) + seedCount);
            }
        }

        AwaleBoardState resultingState =
            new AwaleBoardState(state, this, state.getOpponent(), newPits, newBanks);

        return resultingState;
    }

    public int getStartingPitIndex() {
        return startingPitIndex;
    }
}
