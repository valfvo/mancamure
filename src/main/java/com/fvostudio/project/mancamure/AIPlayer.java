package com.fvostudio.project.mancamure;

import com.fvostudio.project.mancamure.gom.Algorithm;
import com.fvostudio.project.mancamure.gom.Movement;
import com.fvostudio.project.mancamure.gom.MovementChooser;

public class AIPlayer extends AwalePlayer implements MovementChooser {
    private Algorithm algorithm;

    public AIPlayer() {
        this.algorithm = null;
    }

    public AIPlayer(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Movement chooseMovement() {
        if (algorithm == null) {
            throw new IllegalStateException(
                "Can not choose a movement without any algorithm attached.");
        }

        algorithm.execute();
        Movement bestMovement = (Movement) algorithm.getReturnValue();

        return bestMovement;
    }

    @Override
    public Movement play() {
        return chooseMovement();
    }

}
