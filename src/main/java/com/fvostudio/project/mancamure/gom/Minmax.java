package com.fvostudio.project.mancamure.gom;

import java.util.List;
import java.util.Objects;

public abstract class Minmax implements Algorithm {
    @FunctionalInterface
    private interface FunctionTwo<TArgOne, TArgTwo, TReturn> {
        public TReturn apply(TArgOne argOne, TArgTwo argTwo);
    }

    private Board board;
    private int depth;
    private Player player;
    private boolean isAbPruningEnabled = false;
    private Movement bestMovement = null;

    public Minmax(Board board, int depth, Player player) {
        Objects.requireNonNull(player, "Can not instantiate Minmax with no player attached.");
        if (board.getGame() != player.getGame()) {
            throw new IllegalArgumentException(
                "The board and the player do not belong to the same game.");
        }

        this.board = board;
        this.depth = depth;
        this.player = player;
    }

    @Override
    public void execute() {
        bestMovement = null;

        BoardState currentState = board.getState();
        double bestValue = Double.NEGATIVE_INFINITY;

        FunctionTwo<BoardState, Integer, Double> minmax = isAbPruningEnabled()
            ? this::abMinmax
            : this::basicMinmax;

        for (BoardState state : currentState.getNextStates()) {
            beforeNextState(currentState, state, depth + 1);
            double value = minmax.apply(state, depth);

            if (value >= bestValue) {
                bestValue = value;
                bestMovement = state.getLastMovement();
            }
        }

        if (bestMovement == null) {
            throw new IllegalStateException("Minmax could not choose a movement");
        }
    }

    @Override
    public Object getReturnValue() {
        return bestMovement;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isAbPruningEnabled() {
        return isAbPruningEnabled;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setAbPruningEnabled(boolean isAbPruningEnabled) {
        this.isAbPruningEnabled = isAbPruningEnabled;
    }

    private double basicMinmax(BoardState state, int currentDepth) {
        if (currentDepth == 0) {
            return evaluate(state);
        }

        List<BoardState> nextStates = state.getNextStates();
        if (nextStates.isEmpty()) {
            return evaluate(state);
        }

        double bestValue;
        FunctionTwo<Double, Double, Double> compare;

        if (state.getCurrentPlayer() == player) {
            bestValue = Double.NEGATIVE_INFINITY;
            compare = Math::max;
        } else {
            bestValue = Double.POSITIVE_INFINITY;
            compare = Math::min;
        }

        for (BoardState nextState : nextStates) {
            beforeNextState(state, nextState, currentDepth);
            double value = basicMinmax(nextState, currentDepth - 1);
            bestValue = compare.apply(bestValue, value);
        }

        return bestValue;
    }

    private double abMinmax(BoardState state, int currentDepth) {
        return abMinmax(state, currentDepth,
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    private double abMinmax(BoardState state, int currentDepth, double a, double b) {
        if (currentDepth == 0) {
            return evaluate(state);
        }

        List<BoardState> nextStates = state.getNextStates();
        if (nextStates.isEmpty()) {
            return evaluate(state);
        }

        double bestValue;
        FunctionTwo<Double, Double, Double> compare;

        if (state.getCurrentPlayer() == player) {
            bestValue = Double.NEGATIVE_INFINITY;
            compare = Math::max;
        } else {
            bestValue = Double.POSITIVE_INFINITY;
            compare = Math::min;
        }

        beforeNextStates(state, currentDepth);

        for (BoardState nextState : nextStates) {
            beforeNextState(state, nextState, currentDepth);
            double value = abMinmax(nextState, currentDepth - 1, a, b);
            bestValue = compare.apply(bestValue, value);

            if (state.getCurrentPlayer() == player) {
                a  = compare.apply(a, bestValue);
            } else {
                b  = compare.apply(b, bestValue);
            }

            if (b <= a) {
                break;
            }
        }

        return bestValue;
    }

    public void beforeNextState(BoardState state, BoardState nextState,
                                int currentDepth) {}

    public void beforeNextStates(BoardState state, int currentDepth) {}

    public abstract double evaluate(BoardState state);
}
