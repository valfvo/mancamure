package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Minmax;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.Vector3;

public class AwaleMinmax extends Minmax {
    private static final double w1 = 0.198649;
    private static final double w2 = 0.190084;
    private static final double w3 = 0.370793;
    private static final double w4 = 1.0;
    private static final double w5 = 0.418841;
    private static final double w6 = 0.565937;

    private AwaleBoardState lastStateWherePlayerPlayed = null;

    public AwaleMinmax(Board board, int depth, Player player) {
        super(board, depth, player);
    }

    @Override
    public void beforeNextState(BoardState state, int currentDepth) {
        if (state.getCurrentPlayer() == getPlayer()) {
            lastStateWherePlayerPlayed = (AwaleBoardState) state;
        }
    }

    @Override
    public double evaluate(BoardState boardState) {
        AwaleBoardState state = (AwaleBoardState) boardState;
        boolean isCurrentPlayerAnOpponent = state.getCurrentPlayer() != getPlayer();

        ArrayList<Integer> pits = state.getPits();
        int playerPitCount = pits.size() / 2;

        double h1 = isCurrentPlayerAnOpponent
            ? pits.get(playerPitCount - 1)
            : pits.get(playerPitCount);

        int playerSeedCount = 0;
        int nonEmptyPlayerPitCount = 0;
    
        List<Integer> playerPits = isCurrentPlayerAnOpponent
            ? pits.subList(0, playerPitCount)
            : pits.subList(playerPitCount, pits.size());

        for (int pit : playerPits) {
            if (pit > 0) {
                playerSeedCount += pit;
                ++nonEmptyPlayerPitCount;
            }
        }

        double h2 = playerSeedCount;
        double h3 = nonEmptyPlayerPitCount;

        ArrayList<Integer> banks = state.getBanks();
        double h4, h6;  // playerBank, opponentBank
        if (isCurrentPlayerAnOpponent) {
            h4 = banks.get(0);
            h6 = banks.get(1);
        } else {
            h4 = banks.get(1);
            h6 = banks.get(0);
        }

        double h5 = 0.0;

        if (lastStateWherePlayerPlayed != null) {
            Vector3 rightmostNonEmptyPlayerPitPosition = null;

            ArrayList<Integer> oldPits = lastStateWherePlayerPlayed.getPits();

            for (int i = oldPits.size(); i >= playerPitCount; --i) {
                if (oldPits.get(i) > 0) {
                    int x = i - playerPitCount;
                    rightmostNonEmptyPlayerPitPosition = new Vector3(x, 1, 0);
                    break;
                }
            }

            AwaleMovement lastPlayerMove =
                (AwaleMovement) lastStateWherePlayerPlayed.getLastMovement();

            boolean lastMoveWasRightmost = lastPlayerMove
                .getStartingPitPosition()
                .equals(rightmostNonEmptyPlayerPitPosition);

            h5 = lastMoveWasRightmost ? 1.0 : 0.0;
        }

        return h1*w1 + h2*w2 + h3*w3 + h4*w4 + h5*w5 - h6*w6;
    }
}
