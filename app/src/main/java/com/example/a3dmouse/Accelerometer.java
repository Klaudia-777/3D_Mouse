package com.example.a3dmouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/*
The Accelerometer class is responsible for registering
the phone linear coordinates in 3D space.
 */
class Accelerometer {

    public interface Listener {

        // This method is responsible for registering deltas
        // between the subsequent captured phone coordinates.
        // --> implemented in PositionDetectionService class.
        void onTranslation(double tx, double ty, double tz);
    }

    private Listener listener;

    void setListener(Listener listener) {
        this.listener = listener;
    }

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService((Context.SENSOR_SERVICE));
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (listener != null) {
                    listener.onTranslation(event.values[0], event.values[1], event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    void register() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
