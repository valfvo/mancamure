package com.fvostudio.project.mancamure.gom;

import com.fvostudio.project.mancamure.gom.util.Vector3;

public class Cell extends OwnableElement {
    private Vector3 position;

    public Cell() {
        super();
        position = new Vector3();
    }

    public Cell(Vector3 position) {
        super();
        this.position = position;
    }

    public Vector3 getPosition() {
        return new Vector3(position);
    }
}
