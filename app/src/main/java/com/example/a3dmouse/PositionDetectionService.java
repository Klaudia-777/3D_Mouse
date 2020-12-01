package com.example.a3dmouse;

import android.content.Context;

/*
PositionDetectionService class handles the usage of sensors.
Also, while defining listeners for each sensor it provides
the implementation of onTranslation() methods.
 */
class PositionDetectionService {
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    private double last_x, last_y, last_z;
    private double last_azimuth, last_pitch, last_roll;

    private long lastUpdateOfAccelerometer;
    private long lastUpdateOfGyroscope;

    private static float SHAKE_THRESHOLD = 1.0f;

    PositionDetectionService(Context context, final PhonePositionChangeListener phonePositionChangeListener) {
        accelerometer = new Accelerometer(context);
        gyroscope = new Gyroscope(context);
        accelerometer.setListener(new Accelerometer.Listener() {

            @Override
            public void onTranslation(double tx, double ty, double tz) {
                long curTime = System.currentTimeMillis();
                double diffTime = (curTime - lastUpdateOfAccelerometer);
                if (diffTime > 1000) {
                    lastUpdateOfAccelerometer = curTime;

                    PhonePositionLinearDelta phonePositionLinearDelta = new PhonePositionLinearDelta((tx - last_x),
                            (ty - last_y),
                            (tz - last_z));

                    //TODO: is this needed?
                    double speed = Math.abs(phonePositionLinearDelta.getX() + phonePositionLinearDelta.getY() + phonePositionLinearDelta.getZ());

                    if (speed > SHAKE_THRESHOLD) {
                        phonePositionChangeListener.onPhoneMovedAccelerometer(phonePositionLinearDelta);
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

                    PhonePositionAnglesDelta phonePositionAnglesDelta = new PhonePositionAnglesDelta((azimuth - last_azimuth),
                            (pitch - last_pitch),
                            (roll - last_roll));

                    //TODO: is this needed?
                    double speed = Math.abs(phonePositionAnglesDelta.getAzimuth() + phonePositionAnglesDelta.getPitch() + phonePositionAnglesDelta.getRoll());

                    if (speed > SHAKE_THRESHOLD) {
                    phonePositionChangeListener.onPhoneMovedGyroscope(phonePositionAnglesDelta);
                    }

                    last_azimuth = azimuth;
                    last_pitch = pitch;
                    last_roll = roll;
                }
            }
        });
    }

    void unregisterListeners() {
        accelerometer.unregister();
        gyroscope.unregister();
    }

    void registerListeners() {
        accelerometer.register();
        gyroscope.register();
    }
}
