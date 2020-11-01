package com.example.a3dmouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PositionDetectionService implements SensorEventListener {
    private SensorManager senSensorManager;
    private MousePositionChangeListener mousePositionChangeListener;
    private Sensor senAccelerometer;
    private double last_x, last_y, last_z;
    private double lastUpdate;
    private static float SHAKE_THRESHOLD = 0.1f;

    public PositionDetectionService(Context context, MousePositionChangeListener mousePositionChangeListener) {
        this.mousePositionChangeListener = mousePositionChangeListener;
        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            long curTime = System.currentTimeMillis();

            double diffTime = (curTime - lastUpdate);
            if (diffTime > 1000) {
                lastUpdate = curTime;

                MousePositionDelta mousePositionDelta = new MousePositionDelta((x - last_x),
                        (y - last_y),
                        (z - last_z));
                double speed = Math.abs(mousePositionDelta.getDeltaX() + mousePositionDelta.getDeltaY() + mousePositionDelta.getDeltaZ());

                if (speed > SHAKE_THRESHOLD) {
                    mousePositionChangeListener.onMouseMoved(mousePositionDelta);
                }

                last_x = x;
                last_y = y;
                last_z = z;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("MY_APP", sensor.toString() + " - " + accuracy);
    }

    protected void unregisterListener() {
        senSensorManager.unregisterListener(this);
    }

    protected void registerListener() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

//    private SensorManager sensorManager;
//    private Sensor sensor;
//    private Double[] gravity = new Double[3];
//    private Double[] linear_acceleration = new Double[3];
//
//    public PositionDetectionService(Context context) {
//        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//    }
//
//    public void onSensorChanged(SensorEvent event) {
//        // In this example, alpha is calculated as t / (t + dT),
//        // where t is the low-pass filter's time-constant and
//        // dT is the event delivery rate.
//
//        final double alpha = 0.8;
//
//
//        // Isolate the force of gravity with the low-pass filter.
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//
//        // Remove the gravity contribution with the high-pass filter.
//        linear_acceleration[0] = event.values[0] - gravity[0];
//        linear_acceleration[1] = event.values[1] - gravity[1];
//        linear_acceleration[2] = event.values[2] - gravity[2];
//    }
}
