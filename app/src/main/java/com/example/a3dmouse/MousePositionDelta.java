package com.example.a3dmouse;

public class MousePositionDelta {
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;

    public MousePositionDelta(double deltaX, double deltaY, double deltaZ) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }
}
