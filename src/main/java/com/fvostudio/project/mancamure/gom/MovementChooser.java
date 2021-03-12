package com.fvostudio.project.mancamure.gom;

public interface MovementChooser {
    Algorithm getAlgorithm();
    void setAlgorithm(Algorithm algorithm);
    Movement chooseMovement();
}
