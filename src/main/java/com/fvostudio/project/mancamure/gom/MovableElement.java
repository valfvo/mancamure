package com.fvostudio.project.mancamure.gom;

public class MovableElement extends OwnableElement {
    public boolean moveTo(OwnableElement destination) {
        return false;
    }

    public boolean moveForward(int stepCount) {
        return moveForward(stepCount, Motion.LINEAR);
    }

    public boolean moveForward(int stepCount, Motion motion) {
        /*
         * BoardAwale
         * 
         * // Player
         * ownedElements = [MovableElement, MovableElement, ...]
         * 
         * seed = MovableElement
         * [board, player1] = seed.getOwners() -> seed.getOwnableParent.getOwners()
         * seed.moveForward(1)
         * 
         * 
         * 
         * [board, player2] = seed.getOwners()
         */
        // getOwnableParent().getNextOwnableSibling().appendChild(this);

        return false;
    }

    public boolean moveBackward(int stepCount) {
        return moveBackward(stepCount, Motion.LINEAR);
    }

    public boolean moveBackward(int stepCount, Motion motion) {
        return false;
    }
}
