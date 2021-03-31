package com.fvostudio.project.mancamure;

import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Minmax;
import com.fvostudio.project.mancamure.gom.Player;

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
        List<Integer> playerPits = state.getPlayerPits(getPlayer());
    
        // H1: keep as many seeds as possible in the leftmost pit of the player
        double h1 = playerPits.get(0);

        int playerSeedCount = 0;
        int nonEmptyPlayerPitCount = 0;

        for (int pit : playerPits) {
            if (pit > 0) {
                playerSeedCount += pit;
                ++nonEmptyPlayerPitCount;
            }
        }

        // H2: keep as many seeds as possible on the player's side
        double h2 = playerSeedCount;
        // H3: have as many moves as possible
        double h3 = nonEmptyPlayerPitCount;

        // H4: maximize the amount of seeds in the player's bank
        double h4 = state.getPlayerBank(getPlayer());
        // H6: minimize the amount of seeds in the opponent's bank
        double h6 = state.getOpponentBank(getPlayer());

        // H5: move the seeds from the closest pit to the opponent's side
        double h5 = 0.0;

        if (lastStateWherePlayerPlayed != null) {
            int rightmostNonEmptyPlayerPitPosition = 0;
            List<Integer> oldPits = lastStateWherePlayerPlayed.getPlayerPits();

            for (int i = oldPits.size() - 1; i >= 0; --i) {
                if (oldPits.get(i) > 0) {
                    rightmostNonEmptyPlayerPitPosition = i;
                    break;
                }
            }

            AwaleMovement lastPlayerMove =
                (AwaleMovement) lastStateWherePlayerPlayed.getLastMovement();

            int lastMoveIndex = 
                lastPlayerMove.getStartingPitIndex() % oldPits.size();

            boolean lastMoveWasRightmost = 
                lastMoveIndex == rightmostNonEmptyPlayerPitPosition;

            h5 = lastMoveWasRightmost ? 1.0 : 0.0;
        }

        return h1*w1 + h2*w2 + h3*w3 + h4*w4 + h5*w5 - h6*w6;
    }
}
