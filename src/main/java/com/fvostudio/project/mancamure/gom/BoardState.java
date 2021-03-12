package com.fvostudio.project.mancamure.gom;

import java.util.List;

public interface BoardState {
    Board getBoard();

    Player getCurrentPlayer();

    List<BoardState> getStates();

    List<BoardState> getPreviousStates();
}
