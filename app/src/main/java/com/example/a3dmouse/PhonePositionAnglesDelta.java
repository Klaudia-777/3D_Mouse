package com.example.a3dmouse;

/*
PhonePositionAnglesDelta class represents the changes of sensor rotational movement.
Azimuth is angle between the positive Y-axis and magnetic north and its range is between 0 and 360 degrees.
Positive Roll is defined when the phone starts by laying flat on a table and the positive Z-axis begins to tilt towards the positive X-axis.
Positive Pitch is defined when the phone starts by laying flat on a table and the positive Z-axis begins to tilt towards the positive Y-axis.
 */

class PhonePositionAnglesDelta {
    private final double azimuth;
    private final double pitch;
    private final double roll;

    PhonePositionAnglesDelta(double azimuth, double pitch, double roll) {
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }

    double getAzimuth() {
        return azimuth;
    }

    double getPitch() {
        return pitch;
    }

    double getRoll() {
        return roll;
    }
}
