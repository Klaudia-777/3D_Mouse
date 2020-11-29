package com.example.a3dmouse;

public interface MousePositionChangeListener {
    void onMouseMovedAccelerometer(MousePositionLinearDelta mousePositionLinearDelta);
    void onMouseMovedGyroscope(MousePositionAngleDelta mousePositionDelta);
}
