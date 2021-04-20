package com.fvostudio.project.mancamure;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.Cell;
import com.fvostudio.project.mancamure.gom.Game;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.Observable;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.Player;
import com.fvostudio.project.mancamure.gom.util.Pair;
import com.fvostudio.project.mancamure.gom.util.Vector3;

public class Awale extends Game implements Observable {
    private ArrayList<Socket> observerSockets = new ArrayList<>();

    // private Socket clientSocket;

    // public Awale() {
    //     this.observerSockets = 
    //     // this.args = args;
    //     // this.clientSocket = clientSocket;
    // }

    // @Override
    // public void run() {
    //     AwaleBoard board = new AwaleBoard();
    //     setBoard(board);

    //     boolean isAbPruningEnabled = Boolean.valueOf(args.get(2));

    //     HumanPlayer playerOne = new HumanPlayer(this.clientSocket);
    //     add(playerOne);

    //     // AIPlayer playerOne = new AIPlayer();
    //     // add(playerOne);
    //     // int depthOne = Integer.valueOf(args.get(0));
    //     // AwaleMinmax minmaxOne = new AwaleMinmax(board, depthOne, playerOne);
    //     // minmaxOne.setAbPruningEnabled(isAbPruningEnabled);
    //     // playerOne.setAlgorithm(minmaxOne);

    //     AIPlayer playerTwo = new AIPlayer();
    //     add(playerTwo);
    //     int depthTwo = Integer.valueOf(args.get(1));
    //     AwaleMinmax minmaxTwo = new AwaleMinmax(board, depthTwo, playerTwo);
    //     minmaxTwo.setAbPruningEnabled(isAbPruningEnabled);
    //     playerTwo.setAlgorithm(minmaxTwo);

    //     start();
    // }

    @Override
    public List<Socket> getObservers() {
        return Collections.unmodifiableList(observerSockets);
    }

    @Override
    public void addObserver(Socket observerSocket) {
        this.observerSockets.add(observerSocket);
    }

    @Override
    public void addObservers(List<Socket> observerSockets) {
        this.observerSockets.addAll(observerSockets);
    }

    public void notifyObserversOfBoardUpdate() {
        AwaleBoardState currentState = (AwaleBoardState) getBoard().getState();
        notifyObservers(currentState.getSerializedBoard());
    }

    public void notifyObserversOfGameEnd() {
        byte[] serializedGameEnd = {2, 0};
        notifyObservers(serializedGameEnd);
    }

    @Override
    public void add(Player player) {
        super.add(player);

        AwaleBoard currentBoard = (AwaleBoard) getBoard();
        int columnCount = currentBoard.getColumnCount();
        int y = players.size() == 1 ? 0 : 1;  // upper or lower

        for (int x = 0; x < columnCount; ++x) {
            Cell cell = currentBoard.getCell(new Vector3(x, y, 0));
            player.obtain(cell);
        }

        player.obtain(currentBoard.getBanks().get(y));
    }

    @Override
    public void onStart() {
        notifyObserversOfBoardUpdate();
    }

    @Override
    public void onEnd() {
        notifyObserversOfGameEnd();
    }

    @Override
    public void startNextRound() {
        super.startNextRound();
        notifyObserversOfBoardUpdate();
    }

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
        return isLegalFrom(movement, getBoard().getState());
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
        isFinished = getBoard().getState().isFinalState();
    }

    @Override
    public boolean isFinalState(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardState state = (AwaleBoardState) boardState;

        // at least 25 seeds captured
        List<Integer> banks = state.getBanks();
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

            return !isOpponentFeedable;
        }

        return false;
    }
}
