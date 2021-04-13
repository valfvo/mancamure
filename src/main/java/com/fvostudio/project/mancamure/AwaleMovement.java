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

        // ArrayList<Integer> newPits = new ArrayList<>(pits);
        // ArrayList<Integer> newBanks = new ArrayList<>(state.getBanks());
        AwaleBoardStateFactory.setPits(pits);
        AwaleBoardStateFactory.setBanks(state.getBanks());

        int playerBankIndex = state.getPlayerBankIndex();

        AwaleBoardStateFactory.setPit(startingPitIndex, 0);

        for (int i = destination; i > startingPitIndex; --i) {
            int realIndex = i % pitCount;

            if (realIndex == startingPitIndex) {
                continue;
            }

            AwaleBoardStateFactory.setPit(
                realIndex, AwaleBoardStateFactory.getPit(realIndex) + 1);
        }

        if (destinationIsOnOpponentSide) {
            for (
                int i = destination % pitCount;
                i >= 0
                 && i / playerPitCount != playerSide
                 && 2 <= AwaleBoardStateFactory.getPit(i)
                 && AwaleBoardStateFactory.getPit(i) <= 3;
                --i
            ) {
                int seedCount = AwaleBoardStateFactory.getPit(i);
                AwaleBoardStateFactory.setPit(i, 0);

                AwaleBoardStateFactory.setBank(playerBankIndex,
                    AwaleBoardStateFactory.getBank(playerBankIndex) + seedCount);
            }
        }

        AwaleBoardState resultingState = AwaleBoardStateFactory.getState(
            state.getBoard(),
            this,
            state.getUpperPlayer(),
            state.getOpponent()
        );

        return resultingState;
    }

    public int getStartingPitIndex() {
        return startingPitIndex;
    }
}
