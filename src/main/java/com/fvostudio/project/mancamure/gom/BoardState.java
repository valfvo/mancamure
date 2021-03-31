package com.fvostudio.project.mancamure.gom;

import java.util.List;

public interface BoardState {
    Board getBoard();

    Game getGame();

    Player getCurrentPlayer();

    Movement getLastMovement();

    List<BoardState> getNextStates();

    BoardState getPreviousState();

    boolean isFinalState();
}
