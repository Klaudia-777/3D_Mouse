package com.example.a3dmouse;

public class MousePositionLinearDelta {

    private final double x;
    private final double y;
    private final double z;

    public MousePositionLinearDelta(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getDeltaX() {
        return x;
    }

    public double getDeltaY() {
        return y;
    }

    public double getDeltaZ() {
        return z;
    }
}


