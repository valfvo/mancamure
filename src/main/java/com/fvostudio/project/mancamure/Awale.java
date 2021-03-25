package com.fvostudio.project.mancamure;

import java.util.ArrayList;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Game;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class Awale extends Game {
    @Override
    public ArrayList<Movement> getPossibleMovements(OwnableElement element) {
        return null;
    }

    /**
     * @param obj integer that represents the number of seeds in the pit + its position
     */
    @Override
    public ArrayList<Movement> getPossibleMovementsFrom(Object obj) {
        if (!(obj instanceof Pair<?, ?>)) {
            throw new IllegalArgumentException(
                "The object is not a Pair<Integer, Vector3>.");
        }

        Pair<?, ?> pair = (Pair<?, ?>) obj;

        if (!(pair.first instanceof Integer) || !(pair.second instanceof Vector3)) {
            throw new IllegalArgumentException(
                "The object is not a Pair<Integer, Vector3>.");
        }

        int pit = (Integer) pair.first;
        Vector3 position = (Vector3) pair.second;

        ArrayList<Movement> possibleMovements = new ArrayList<>();
        if (pit > 0) {
            possibleMovements.add(new AwaleMovement(pit, position));
        }

        return possibleMovements;
    }

    @Override
    public boolean isLegal(Movement movement) {
        return false;
    }

    @Override
    public boolean isLegalFrom(Movement movement, BoardState state) {
        assert(movement instanceof AwaleMovement && state instanceof AwaleBoardState);
        AwaleBoardState resultingState = (AwaleBoardState) movement.getResultingState(state);

        if (resultingState.isFinalState()) {
            return false;
        }

        int opponentSeedCount = 0;
        ArrayList<Integer> pits = resultingState.getPits();

        int start, end;
        if (state.getCurrentPlayer() == getCurrentPlayer()) {
            start = 0;
            end = pits.size() / 2;
        } else {
            start = pits.size() / 2;
            end = pits.size();
        }

        for (int i = start; opponentSeedCount == 0 && i < end; ++i) {
            opponentSeedCount += pits.get(i);
        }

        return opponentSeedCount != 0;
    }

    @Override
    public void checkForGameEnd() {
    }

    public static boolean isFinalState(AwaleBoardState state) {
        // Au moins 25 graines de capturées
        ArrayList<Integer> banks = state.getBanks();
        if (banks.get(0) >= 25 || banks.get(1) >= 25) {
            return true;
        }

        // <= 3 graines sur la plateau
        ArrayList<Integer> pits = state.getPits();
        int pitCount = pits.size();
        int playerPitCount = pitCount / 2;

        int playerSeedCount = 0;
        int opponentSeedCount = 0;

        for (int i = 0; i < playerPitCount; ++i) {
            opponentSeedCount += pits.get(i);
        }

        for (int i = playerPitCount; i < pitCount; ++i) {
            playerSeedCount += pits.get(i);
        }

        if (playerSeedCount + opponentSeedCount <= 3) {
            return true;
        }

        // Impossible de nourir un adversaire affamé
        if (opponentSeedCount <= 0) {
            boolean isOpponentFeedable = false;

            for (int i = playerPitCount; !isOpponentFeedable && i < pitCount; ++i) {
                isOpponentFeedable = pits.get(i) >= (pitCount - i);
            }

            return isOpponentFeedable;
        }

        return false;
    }
}
