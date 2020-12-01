package com.example.a3dmouse;

/*
The interface declaring methods for actions performed
in case of both phone movement types.
 */
public interface PhonePositionChangeListener {
    void onPhoneMovedAccelerometer(PhonePositionLinearDelta phonePositionLinearDelta);
    void onPhoneMovedGyroscope(PhonePositionAnglesDelta mousePositionDelta);
}
