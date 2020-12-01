package com.example.a3dmouse;

/*
PhonePositionLinearDelta class represents the change of position of the phone in 3D space (x,y,z).
 */
class PhonePositionLinearDelta {

    private final double x;
    private final double y;
    private final double z;

    PhonePositionLinearDelta(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getZ() {
        return z;
    }
}


