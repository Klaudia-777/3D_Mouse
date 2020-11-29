package com.example.a3dmouse;

import android.content.Context;

public class PositionDetectionService {
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    private double last_x, last_y, last_z;
    private double last_azimuth, last_pitch, last_roll;
    private long lastUpdateOfAccelerometer;
    private long lastUpdateOfGyroscope;
    private static float SHAKE_THRESHOLD = 1.0f;

    public PositionDetectionService(Context context, final MousePositionChangeListener mousePositionChangeListener) {
        accelerometer = new Accelerometer(context);
        gyroscope = new Gyroscope(context);
        accelerometer.setListener(new Accelerometer.Listener() {

            @Override
            public void onTranslation(double tx, double ty, double tz) {
                long curTime = System.currentTimeMillis();
                double diffTime = (curTime - lastUpdateOfAccelerometer);
                if (diffTime > 1000) {
                    lastUpdateOfAccelerometer = curTime;

                    MousePositionLinearDelta mousePositionDelta = new MousePositionLinearDelta((tx - last_x),
                            (ty - last_y),
                            (tz - last_z));
                    double speed = Math.abs(mousePositionDelta.getDeltaX() + mousePositionDelta.getDeltaY() + mousePositionDelta.getDeltaZ());

                    if (speed > SHAKE_THRESHOLD) {
                        mousePositionChangeListener.onMouseMovedAccelerometer(mousePositionDelta);
                    }

                    last_x = tx;
                    last_y = ty;
                    last_z = tz;

                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {

            @Override
            public void onTranslation(double azimuth, double pitch, double roll) {
                long curTime = System.currentTimeMillis();
                double diffTime = (curTime - lastUpdateOfGyroscope);
                if (diffTime > 1000) {
                    lastUpdateOfGyroscope = curTime;

                    MousePositionAngleDelta mousePositionDelta = new MousePositionAngleDelta((azimuth - last_azimuth),
                            (pitch - last_pitch),
                            (roll - last_roll));
                    double speed = Math.abs(mousePositionDelta.getDeltaX() + mousePositionDelta.getDeltaY() + mousePositionDelta.getDeltaZ());

                    if (speed > SHAKE_THRESHOLD) {
                    mousePositionChangeListener.onMouseMovedGyroscope(mousePositionDelta);
                    }

                    last_azimuth = azimuth;
                    last_pitch = pitch;
                    last_roll = roll;
                }
            }
        });
    }

    public void unregisterListeners() {
        accelerometer.unregister();
        gyroscope.unregister();
    }

    public void registerListeners() {
        accelerometer.register();
        gyroscope.register();
    }
}
