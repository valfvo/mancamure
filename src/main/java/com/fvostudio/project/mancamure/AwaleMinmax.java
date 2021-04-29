package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Minmax;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Pair;

public class AwaleMinmax extends Minmax {
    private static final double w1 = 0.198649;
    private static final double w2 = 0.190084;
    private static final double w3 = 0.370793;
    private static final double w4 = 1.0;
    private static final double w5 = 0.418841;
    private static final double w6 = 0.565937;

    private ArrayList<Pair<AwaleBoardState, AwaleMovement>> playerData;

    private AwaleBoardState lastPlayerState;
    private AwaleMovement lastPlayerMovement;

    public AwaleMinmax(Board board, int depth, Player player) {
        super(board, depth, player);
    }

    @Override
    public void execute() {
        Pair<AwaleBoardState, AwaleMovement> pair = null;
        // + 2 : depth + 1 at the beginning and depth 0 at the end
        playerData = new ArrayList<>(Collections.nCopies(getDepth() + 2, pair));

        super.execute();
    }

    @Override
    public void beforeNextState(BoardState state, BoardState nextState,
                                int currentDepth)
    {
        if (state.getCurrentPlayer() == getPlayer()) {
            lastPlayerState = (AwaleBoardState) state;
            lastPlayerMovement = (AwaleMovement) nextState.getLastMovement();
        } else {  // current player is the opponent
            Pair<AwaleBoardState, AwaleMovement> lastPlayerData =
                playerData.get(currentDepth);

            lastPlayerState = lastPlayerData.first;
            lastPlayerMovement = lastPlayerData.second;
        }
    }

    @Override
    public void beforeNextStates(BoardState state, int currentDepth) {
        playerData.set(currentDepth, new Pair<>(lastPlayerState, lastPlayerMovement));
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
        if (h4 >= 25.0) {
            return Double.POSITIVE_INFINITY;
        }

        // H6: minimize the amount of seeds in the opponent's bank
        double h6 = state.getOpponentBank(getPlayer());
        if (h6 >= 25.0) {
            return Double.NEGATIVE_INFINITY;
        }

        int rightmostNonEmptyPlayerPitIndex = 0;
        List<Integer> oldPlayerPits = lastPlayerState.getPlayerPits();

        for (int i = oldPlayerPits.size() - 1; i >= 0; --i) {
            if (oldPlayerPits.get(i) > 0) {
                rightmostNonEmptyPlayerPitIndex = i;
                break;
            }
        }

        int lastMovementIndex = 
            lastPlayerMovement.getStartingPitIndex() % oldPlayerPits.size();

        boolean lastMoveWasRightmost = 
            lastMovementIndex == rightmostNonEmptyPlayerPitIndex;

        // H5: move the seeds from the closest pit to the opponent's side
        double h5 = lastMoveWasRightmost ? 1.0 : 0.0;

        return h1*w1 + h2*w2 + h3*w3 + h4*w4 + h5*w5 - h6*w6;
    }
}
