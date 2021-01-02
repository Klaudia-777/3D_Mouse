package com.example.a3dmouse;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.round;

/*
MyCommunicationsActivity implements the actual actions performed
after establishing Bluetooth connection between phone and the computer.
 */
public class MyCommunicationsActivity extends CommunicationsActivity implements SensorEventListener {

    private TextView xCoordinateTextView;
    private TextView yCoordinateTextView;

    private TextView inputDistanceLabel;
    private TextView meterLabel;
    private EditText distancePlainText;

    private Sensor accelerometer;
    private Sensor magneticField;

    private SensorManager mSensorManager;

    private double angleAzimuthRemembered = 1;
    private double angleAzimuth = 1;
    private double anglePitchRemembered = 1;
    private double anglePitch = 1;

    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    private String textCoordinatesToSend;
    private long lastTime = 0;
    //TODO distance in meters
    private double distance = 0.0;
    static final float ALPHA_ACCELEROMETER = 0.03f;
    static final float ALPHA_MAGNETIC_FIELD = 0.03f;

    private Boolean distanceEnteredFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        inputDistanceLabel = findViewById(R.id.inputDistanceLabel);
        meterLabel = findViewById(R.id.meterLabel);
        distancePlainText = findViewById(R.id.distancePlainText);

        xCoordinateTextView = findViewById(R.id.xCoordinte);
        yCoordinateTextView = findViewById(R.id.yCoordinte);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        start();
    }

    public void start() {
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME);

    }

    public void stop() {
        // make sure to turn our sensor off when the activity is paused
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        long current = System.currentTimeMillis();
        double diff = (current - lastTime);

        if (diff > 0) {
            if (mBluetoothConnection.ismConnected()) {

                // filtrowanie nowo odczytanych wartości
                // trzykrotne - może wystarczy raz z odpowiednimi współczynnikami ALPHA
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerReading = lowPass(lowPass(lowPass(event.values, accelerometerReading, ALPHA_ACCELEROMETER), accelerometerReading, ALPHA_ACCELEROMETER), accelerometerReading, ALPHA_ACCELEROMETER);
                } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    magnetometerReading = lowPass(lowPass(lowPass(event.values, magnetometerReading, ALPHA_MAGNETIC_FIELD), magnetometerReading, ALPHA_MAGNETIC_FIELD), magnetometerReading, ALPHA_MAGNETIC_FIELD);
                }

                updateOrientationAngles();
                double deltaAzimuth;

                // obsłuzenie przypadku, kiedy poruszajac
                // telefonem w poziomie "przechodzimy" przez połnoc
                if (Math.abs(angleAzimuth - angleAzimuthRemembered) > 180) {
                    if (angleAzimuth > angleAzimuthRemembered) {
                        deltaAzimuth = (countDeltaComponent(angleAzimuth) + countDeltaComponent(angleAzimuthRemembered)) * (-1);
                    } else {
                        deltaAzimuth = countDeltaComponent(angleAzimuth) + countDeltaComponent(angleAzimuthRemembered);
                    }
                } else {
                    deltaAzimuth = angleAzimuth - angleAzimuthRemembered;
                }
                double deltaPitch = anglePitch - anglePitchRemembered;

                if (Math.abs(deltaAzimuth) < 0.5) {
                    deltaAzimuth = 0;
                }
                if (Math.abs(deltaPitch) < 0.5) {
                    deltaPitch = 0;
                }

                PhoneDistanceDelta delta = new PhoneDistanceDelta(deltaAzimuth, deltaPitch);
                onPhoneMovedGyroscope(delta);

                lastTime = current;
                angleAzimuthRemembered = angleAzimuth;
                anglePitchRemembered = anglePitch;

                if (distanceEnteredFlag) {
                    if (Math.abs(deltaAzimuth) > 0.5 || Math.abs(deltaPitch) > 0.5) {
                        sendViaBluetooth();
                    }
                }
            }
        }
    }

    // filtr dolnoprzepustowy
    // http://blog.thomnichols.org/2011/08/smoothing-sensor-data-with-a-low-pass-filter
    protected float[] lowPass(float[] input, float[] output, float ALPHA) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        float[] orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles);

        double degreesAzimuth = (Math.toDegrees((double) orientation[0]) + 360.0) % 360.0;
        angleAzimuth = round(degreesAzimuth * 100) / 100;

        double degreesPitch = (Math.toDegrees((double) orientation[1]) + 360.0) % 360.0;
        anglePitch = round(degreesPitch * 100) / 100;

    }

    private double countDeltaComponent(double angle) {
        if (angle < 180) {
            return angle;
        } else {
            return 360 - angle;
        }
    }

    private void sendViaBluetooth() {
        if (mBluetoothConnection.ismConnected()) {
            mBluetoothConnection.write(textCoordinatesToSend);
        }
    }

    private String prepareData(double x, double y, String directionHorizontal, String directionVertical) {
        double cursorHorizontal = Math.tan(Math.toRadians(x));
        double cursorVertical = Math.tan(Math.toRadians(y));
        String separator = ";";
        return cursorHorizontal + separator + directionHorizontal + separator + cursorVertical + separator + directionVertical + separator + distance;
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressLint("SetTextI18n")
    public void onPhoneMovedGyroscope(PhoneDistanceDelta phoneDistanceDelta) {
        double phonePositionDeltaX = phoneDistanceDelta.getX();
        double phonePositionDeltaY = phoneDistanceDelta.getY();
        String directionHorizontal;
        String directionVertical;
        String separator = ";";

        if (phonePositionDeltaX < 0) {
            directionHorizontal = "left";
        } else {
            directionHorizontal = "right";
        }
        if (phonePositionDeltaY < 0) {
            directionVertical = "up";
        } else {
            directionVertical = "down";
        }

        double distanceEntered = Double.parseDouble(distancePlainText.getText().toString());
        if (distanceEntered > 0) {
            // to bedzie do usuniecia, wyswietlanie pomocnicze
            xCoordinateTextView.setText(setPrecision(angleAzimuth) + separator + directionHorizontal);
            yCoordinateTextView.setText(setPrecision(anglePitch) + separator + directionVertical);

            distance = distanceEntered;
            textCoordinatesToSend = prepareData(phonePositionDeltaX, phonePositionDeltaY, directionHorizontal, directionVertical);
            distanceEnteredFlag = true;
        } else {
            distanceEnteredFlag = false;
        }

    }

    private String setPrecision(double doubleValue) {
        return String.format("%.2f", doubleValue);
    }

}
