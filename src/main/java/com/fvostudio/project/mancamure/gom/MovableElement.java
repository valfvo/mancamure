package com.fvostudio.project.mancamure.gom;

import org.w3c.dom.DOMException;

public class MovableElement extends OwnableElement {
    public int moveTo(OwnableElement destination) {
        if (destination == null) {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                                   "The destination is null.");
        }

        if (destination == getOwnableParent()) {
            return 0;
        }

        destination.appendChild(this);

        return 1;
    }

    public int moveForward(int stepCount) {
        return moveForward(stepCount, Motion.LINEAR);
    }

    public int moveForward(int stepCount, Motion motion) {
        if (getOwnableParent() == null) {
            return 0;
        }

        OwnableElement parent = getOwnableParent().getNextOwnableSibling();
        if (parent == null
            && getOwnableParent().getPreviousOwnableSibling() == null
        ) {
            return 0;
        }

        int stepDone = 0;

        while (stepDone < stepCount || parent != null) {
            parent.appendChild(this);
            ++stepDone;
            parent = parent.getNextOwnableSibling();

            if (parent == null && motion == Motion.CIRCULAR) {
                Element grandParent = getParent().getParent();
                Element currentParent = grandParent.getFirstChild();

                while (!(currentParent instanceof OwnableElement)) {
                    currentParent = currentParent.getNextSibling();   
                }

                parent = (OwnableElement) currentParent;
            }
        }

        return stepDone;
    }

    public int moveBackward(int stepCount) {
        return moveBackward(stepCount, Motion.LINEAR);
    }

    public int moveBackward(int stepCount, Motion motion) {
        if (getOwnableParent() == null) {
            return 0;
        }

        OwnableElement parent = getOwnableParent().getPreviousOwnableSibling();
        if (parent == null
            && getOwnableParent().getNextOwnableSibling() == null
        ) {
            return 0;
        }

        int stepDone = 0;

        while (stepDone < stepCount || parent != null) {
            parent.appendChild(this);
            ++stepDone;
            parent = parent.getPreviousOwnableSibling();

            if (parent == null && motion == Motion.CIRCULAR) {
                Element grandParent = getParent().getParent();
                Element currentParent = grandParent.getLastChild();

                while (!(currentParent instanceof OwnableElement)) {
                    currentParent = currentParent.getPreviousSibling();   
                }

                parent = (OwnableElement) currentParent;
            }
        }

        return stepDone;
    }
}
