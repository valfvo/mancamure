package com.fvostudio.project.mancamure.gom;

public interface Movement {
    void apply(Board board);
    BoardState getResultingState(BoardState boardState);
}
