package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Owner {
    public static final long PLAYABLE_ELEMENT = 0b1;

    private String name;
    private Game game;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }

    public List<OwnableElement> getPlayableElements() {
        List<OwnableElement> playableElements = new ArrayList<OwnableElement>();

        List<OwnableElement> ownedElements = getOwnedElements();
        for (OwnableElement element : ownedElements) {
            if (element.belongsTo(PLAYABLE_ELEMENT)) {
                playableElements.add(element);
            }
        }

        return playableElements;
    }

    public void loseBoardElements() {
        if (getGame() != null) {
            return;
        }

        Board board = getGame().getBoard();
        if (board == null) {
            return;
        }

        for (OwnableElement owned : getOwnedElements()) {
            for (Owner owner : owned.getOwners()) {
                if (owner == board) {
                    lose(owned);
                }
            }
        }
    }

    public abstract Movement play();

    public abstract ArrayList<OwnableElement> getPlayableElementsFrom(BoardState state);
}
