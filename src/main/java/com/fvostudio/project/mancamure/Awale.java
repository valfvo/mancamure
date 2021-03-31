package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Game;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.util.Pair;

public class Awale extends Game {
    @Override
    public ArrayList<Movement> getPossibleMovements(OwnableElement element) {
        // element.belongsTo(PLAYABLE_ELEMENT)
        // AwaleBoardState state = (AwaleBoardState) getBoard().getState();

        // vector3 = element.getPosition
        // integer = element.getPit

        // class Game2 extends Game implements GameFrom { ... }

        // Pair<Integer, Vector3> pair = null;

        // return getPossibleMovementsFrom(pair);
        throw new UnsupportedOperationException();
    }

    /**
     * @param obj pair<Integer, Vector3> that represents the number of seeds in
     *  the pit + its position
     */
    @Override
    public ArrayList<Movement> getPossibleMovementsFrom(Object obj) {
        if (!(obj instanceof Pair<?, ?>)) {
            throw new IllegalArgumentException(
                "The object is not a Pair<Integer, Integer>.");
        }

        Pair<?, ?> pair = (Pair<?, ?>) obj;

        if (!(pair.first instanceof Integer) || !(pair.second instanceof Integer)) {
            throw new IllegalArgumentException(
                "The object is not a Pair<Integer, Integer>.");
        }

        int pit = (Integer) pair.first;
        int index = (Integer) pair.second;

        ArrayList<Movement> possibleMovements = new ArrayList<>();
        if (pit > 0) {
            possibleMovements.add(new AwaleMovement(index));
        }

        return possibleMovements;
    }

    @Override
    public boolean isLegal(Movement movement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLegalFrom(Movement movement, BoardState state) {
        assert(movement instanceof AwaleMovement && state instanceof AwaleBoardState);

        if (state.isFinalState()) {
            return false;
        }

        AwaleBoardState resultingState =
            (AwaleBoardState) movement.getResultingState(state);

        List<Integer> resultingOpponentPits =
            resultingState.getOpponentPits(state.getCurrentPlayer());

        // can't starve the opponent
        for (int pit : resultingOpponentPits) {
            if (pit > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void checkForGameEnd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinalStateFrom(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardState state = (AwaleBoardState) boardState;

        // at least 25 seeds captured
        ArrayList<Integer> banks = state.getBanks();
        if (banks.get(0) >= 25 || banks.get(1) >= 25) {
            return true;
        }

        // 3 seeds or less on the board
        // ArrayList<Integer> pits = state.getPits();
        List<Integer> playerPits = state.getPlayerPits();
        List<Integer> opponentPits = state.getOpponentPits();
        // int pitCount = pits.size();
        // int playerPitCount = pitCount / 2;

        int playerSeedCount = 0;
        int opponentSeedCount = 0;

        for (int pit : opponentPits) {
            opponentSeedCount += pit;
            if (opponentSeedCount > 3) {
                break;
            }
        }

        for (int pit : playerPits) {
            playerSeedCount += pit;
            if (playerSeedCount > 3) {
                break;
            }
        }

        if (playerSeedCount == 0 || playerSeedCount + opponentSeedCount <= 3) {
            return true;
        }

        // a starved opponent can not be fed
        if (opponentSeedCount <= 0) {
            boolean isOpponentFeedable = false;
            int maxIndex = playerPits.size() - 1;

            for (int i = maxIndex; !isOpponentFeedable && i >= 0; --i) {
                isOpponentFeedable = playerPits.get(i) > maxIndex - i;
            }

            return isOpponentFeedable;
        }

        return false;
    }
}
