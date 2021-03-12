package com.fvostudio.project.mancamure.gom;

public abstract class Minmax implements Algorithm {
    private int depth;
    private boolean isAbPruningEnabled;

    @Override
    public void execute() {

    }

    @Override
    public Object getReturnValue() {
        return null;
    }

    
    public int getDepth() {
        return depth;
    }

    public void setDepth(int newDepth) {
        this.depth = newDepth;
    }

    public boolean getIsAbPruningEnabled() {
        return isAbPruningEnabled;
    }

    public void setIsAbPruningEnabled(boolean isAbPruningEnabled) {
        this.isAbPruningEnabled=isAbPruningEnabled;
    }

    private double basicMinmax(BoardState state) {
        return 0.0;
    }

    private double abMinmax(BoardState state) {
        return 0.0;
    }

    public abstract double evaluate(BoardState state);
}
