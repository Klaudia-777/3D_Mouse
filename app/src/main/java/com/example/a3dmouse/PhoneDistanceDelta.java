package com.example.a3dmouse;

public class PhoneDistanceDelta {
    private final double x;
    private final double y;

    public PhoneDistanceDelta(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
