package com.example.a3dmouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/*
The Gyroscope class is responsible for registering the phone rotational coordinates in 3D space.
The name may suggest the usage of GYROSCOPE sensor but its only
for the better understanding of what it actually does.
 */
class Gyroscope {

    public interface Listener {

        // This method is responsible for registering deltas
        // between the subsequent captured phone rotational
        // coordinates (azimuth, pitch and roll).
        // --> implemented in PositionDetectionService class.
        void onTranslation(double rx, double ry, double rz);
    }

    private Listener listener;

    void setListener(Listener listener) {
        this.listener = listener;
    }

    private SensorManager sensorManager;
    private List<Sensor> sensors;
    private SensorEventListener sensorEventListener;

    // Gravity rotational data
    private float[] gravity;
    // Magnetic rotational data
    private float[] magnetic; //for magnetic rotational data
    private float[] accels = new float[3];
    private float[] mags = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;

    Gyroscope(Context context) {
        sensorManager = (SensorManager) context.getSystemService((Context.SENSOR_SERVICE));
        sensors =  new ArrayList<>();
        sensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        mags = event.values.clone();
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        accels = event.values.clone();
                        break;
                }

                if (mags != null && accels != null) {
                    gravity = new float[9];
                    magnetic = new float[9];
                    SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                    float[] outGravity = new float[9];
                    SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
                    SensorManager.getOrientation(outGravity, values);

                    azimuth = values[0] * 57.2957795f;
                    pitch =values[1] * 57.2957795f;
                    roll = values[2] * 57.2957795f;
                    mags = null;
                    accels = null;
                    listener.onTranslation(azimuth, pitch, roll);
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    void register() {
        sensors.forEach(sensor -> {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        });
    }

    void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
