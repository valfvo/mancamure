package com.fvostudio.project.mancamure.gom;

public class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Vector3)) {
            return false;
        }

        Vector3 other = (Vector3) o;

        return getX() == other.getX()
            && getY() == other.getY()
            && getZ() == other.getZ();
    }
}
