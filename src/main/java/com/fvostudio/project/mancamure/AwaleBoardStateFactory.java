package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Stack;

import com.fvostudio.project.mancamure.gom.Player;

public class AwaleBoardStateFactory {
    private Stack<AwaleBoardState> stack = new Stack<>();

    // public AwaleBoardState makeState() {
    //     throw new UnsupportedOperationException();
    // }

    public AwaleBoardState getState(
        AwaleBoardState state,
        AwaleMovement lastMovement,
        Player currentPlayer,
        ArrayList<Integer> pits,
        ArrayList<Integer> banks
    ) {
        if (stack.isEmpty()) {
            return new AwaleBoardState(state, lastMovement, currentPlayer, pits, banks);
        } else {
            AwaleBoardState newState = stack.pop();
            newState.reset(state, lastMovement, currentPlayer, pits, banks);
            return newState;
        }
    }

    public void recycle(AwaleBoardState state) {
        stack.push(state);
    }
}
