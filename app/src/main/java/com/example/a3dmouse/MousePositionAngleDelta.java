package com.example.a3dmouse;

public class MousePositionAngleDelta {
    private final double azimuth;
    private final double pitch;
    private final double roll;

    public MousePositionAngleDelta(double azimuth, double pitch, double roll) {
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getDeltaX() {
        return azimuth;
    }

    public double getDeltaY() {
        return pitch;
    }

    public double getDeltaZ() {
        return roll;
    }
}
