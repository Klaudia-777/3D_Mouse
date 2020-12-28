package com.example.a3dmouse;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

/*
MyCommunicationsActivity implements the actual actions performed
after establishing Bluetooth connection between phone and the computer.
 */
public class MyCommunicationsActivity extends CommunicationsActivity implements SensorEventListener {

    private TextView xCoordinateTextView;
    private TextView yCoordinateTextView;
    private Sensor mRotationVectorSensor;
    private SensorManager mSensorManager;

    private final float[] mRotationMatrix = new float[16];
    private double x = 1;
    private double y = 1;
    private double z = 1;

    private long lastTime = 0;
    //TODO distance in meters
    private long distance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        xCoordinateTextView = findViewById(R.id.xCoordinte);
        yCoordinateTextView = findViewById(R.id.yCoordinte);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //TODO: consider different sensor type
        mRotationVectorSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION);

        mRotationMatrix[0] = 1;
        mRotationMatrix[4] = 1;
        mRotationMatrix[8] = 1;
        mRotationMatrix[12] = 1;
        start();
    }

    public void start() {
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);

    }

    public void stop() {
        // make sure to turn our sensor off when the activity is paused
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        long current = System.currentTimeMillis();
        double diff = (current - lastTime) / 1000;

        if (diff > 0.5) {
            if (mBluetoothConnection.ismConnected()) {

                /*SensorManager.getRotationMatrixFromVector(
                        mRotationMatrix, event.values);
                float[] quaternion = new float[4];
                SensorManager.getQuaternionFromVector(quaternion, event.values);
                double currentX = mRotationMatrix[0] * x + mRotationMatrix[1] * y;
                double currentY = mRotationMatrix[4] * x + mRotationMatrix[5] * y;
                double currentZ = mRotationMatrix[8] * x + mRotationMatrix[9] * y + mRotationMatrix[10] * z;

                double div = Math.sqrt(1 - quaternion[0] * quaternion[0]);
                currentX = quaternion[1] / div;
                currentY = quaternion[2] / div;
                Math.toDegrees(Math.asin(div));

                int distance = 3780; //1m w pikselach
                PhoneDistanceDelta delta = new PhoneDistanceDelta(distance*currentX, distance*currentY);
                onPhoneMovedGyroscope(delta);
                x = currentX;
                y = currentY;
                z = currentZ;*/

                //TODO
                // plceholder - random cursor positions
                // count deltas according to sensor changes and distance
                // on the server side convert values from meters to pixels

                int xCoordinate = Math.abs(new Random().nextInt() % 1500);
                int yCoordinate = Math.abs(new Random().nextInt() % 1500);

                PhoneDistanceDelta delta = new PhoneDistanceDelta(distance * xCoordinate, distance * yCoordinate);
                onPhoneMovedGyroscope(delta);
                lastTime = current;
                sendViaBluetooth();
            }
        }
    }

    private void sendViaBluetooth() {
        String separator = ";";
        if (mBluetoothConnection.ismConnected()) {
            mBluetoothConnection.write(xCoordinateTextView.getText() + separator + yCoordinateTextView.getText());
        }
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onPhoneMovedGyroscope(PhoneDistanceDelta phoneDistanceDelta) {
        xCoordinateTextView.setText(setPrecision(phoneDistanceDelta.getX()));
        yCoordinateTextView.setText(setPrecision(phoneDistanceDelta.getY()));
    }

    private String setPrecision(double doubleValue) {
        return String.format("%.2f", doubleValue);
    }

}
