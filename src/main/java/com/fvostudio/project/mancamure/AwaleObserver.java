package com.fvostudio.project.mancamure;

import java.net.Socket;

import com.fvostudio.project.mancamure.gom.Observer;

public class AwaleObserver extends Observer {
    AwaleBoard.POV pov;

    public AwaleObserver(Socket socket, AwaleBoard.POV pov) {
        super(socket);
        this.pov = pov;
    }

    public AwaleBoard.POV getPOV() {
        return pov;
    }

    public void setPOV(AwaleBoard.POV pov) {
        this.pov = pov;
    }
}
