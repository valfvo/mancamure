package com.fvostudio.project.mancamure.gom;

public class MovableElement extends OwnableElement {
    public boolean moveTo(OwnableElement destination) {
        return false;
    }

    public boolean moveForward(int stepCount) {
        return false;
    }

    public boolean moveBackward(int stepCount) {
        return false;
    }
}
