package com.fvostudio.project.mancamure.gom;

public class Cell extends OwnableElement {
    private Vector3 coordinate;

    public Cell() {
        super();
        coordinate = new Vector3();
    }

    public Cell(Vector3 coordinate) {
        super();
        this.coordinate = coordinate;
    }   
}
