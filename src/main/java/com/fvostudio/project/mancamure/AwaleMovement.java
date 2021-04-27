package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.util.Vector3;

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
        // + loopCount : the starting pit must remain empty after the move
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
                i >= 0
                 && i / playerPitCount != playerSide
                 && 2 <= newPits.get(i)
                 && newPits.get(i) <= 3;
                --i
            ) {
                int seedCount = newPits.get(i);
                newPits.set(i, 0);

                newBanks.set(playerBankIndex,
                    newBanks.get(playerBankIndex) + seedCount);
            }
        }

        AwaleBoardState resultingState =
            new AwaleBoardState(state, this, state.getOpponent(), newPits, newBanks);

        return resultingState;
    }

    public byte[] getSerializationFrom(AwaleBoardState state) {
        List<Integer> pits = state.getPits();
        int pitCount = pits.size();
        int playerPitCount = pitCount / 2;

        Vector3 startingPitPosition = state.getPitPosition(startingPitIndex);
        int startingPitSeedCount = pits.get(startingPitIndex);

        // 2: type, size
        // 3: startingPit { x y seedCount }
        // 2 * seedCount: destination { x y }
        // 6 * 3: opponentPits { x y bank }
        int serializiationSize = 2 + 3 + 2 * startingPitSeedCount + 3 * playerPitCount;
        byte[] serializedMovement = new byte[serializiationSize];
        serializedMovement[0] = 1;
        serializedMovement[1] = (byte) serializiationSize;

        int offset = 2;
        serializedMovement[offset++] = (byte) (int) startingPitPosition.getX();
        serializedMovement[offset++] = (byte) (int) startingPitPosition.getY();
        serializedMovement[offset++] = (byte) startingPitSeedCount;

        int sowedSeedCount = pits.get(startingPitIndex);
        int loopCount = sowedSeedCount / pitCount;
        int playerSide = startingPitIndex / playerPitCount;

        // cumulative index of the destination pit
        // + loopCount : the starting pit must remain empty after the move
        int destination = (startingPitIndex + sowedSeedCount + loopCount);
        boolean destinationIsOnOpponentSide = 
            (destination % pitCount) / playerPitCount != playerSide;

        ArrayList<Integer> newPits = new ArrayList<>(pits);
        ArrayList<Integer> newBanks = new ArrayList<>(state.getBanks());

        int playerBankIndex = state.getPlayerBankIndex();

        newPits.set(startingPitIndex, 0);

        for (int i = startingPitIndex + 1; i <= destination; ++i) {
            int realIndex = i % pitCount;

            if (realIndex == startingPitIndex) {
                continue;
            }

            newPits.set(realIndex, newPits.get(realIndex) + 1);
            Vector3 pitPosition = state.getPitPosition(realIndex);
            serializedMovement[offset++] = (byte) (int) pitPosition.getX();
            serializedMovement[offset++] = (byte) (int) pitPosition.getY();
        }

        int seedToCollectOffset = 0;

        for (
            int i = state.getFirstPitIndex(state.getOpponent()) + playerPitCount - 1;
            i >= 0 && i / playerPitCount != playerSide;
            --i
        ) {
            int seedCount = newPits.get(i);

            Vector3 pitPosition = state.getPitPosition(i);
            serializedMovement[offset++] = (byte) (int) pitPosition.getX();
            serializedMovement[offset++] = (byte) (int) pitPosition.getY();

            if (i == destination % pitCount) {
                seedToCollectOffset = offset;
            }

            serializedMovement[offset++] = -1;
        }

        if (destinationIsOnOpponentSide) {
            for (
                int i = destination % pitCount;
                i >= 0
                 && i / playerPitCount != playerSide
                 && 2 <= newPits.get(i)
                 && newPits.get(i) <= 3;
                --i
            ) {
                serializedMovement[seedToCollectOffset] = (byte) playerBankIndex;
                seedToCollectOffset += 3;
            }
        }

        return serializedMovement;
    }

    public int getStartingPitIndex() {
        return startingPitIndex;
    }
}
