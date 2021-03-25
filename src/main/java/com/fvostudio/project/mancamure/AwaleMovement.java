package com.fvostudio.project.mancamure;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class AwaleMovement implements Movement {
    int startingPit;
    Vector3 startingPitPosition;

    // public AwaleMovement(Pit pit) {
    //     this.pit = pit.getSeedCount();
    //     this.startingPitPosition = pit.getPosition();
    // }

    public AwaleMovement(int pit, Vector3 position) {
        this.startingPit = pit;
        this.startingPitPosition = position;
    }

    @Override
    public AwaleBoardState getResultingState(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardState state = (AwaleBoardState) boardState;
        // Board -> boardstate -> boardstate
        boolean isFinalState = Awale.isFinalState(state);

        return null;
    }

    public Vector3 getStartingPitPosition() {
        return startingPitPosition;
    }
}
