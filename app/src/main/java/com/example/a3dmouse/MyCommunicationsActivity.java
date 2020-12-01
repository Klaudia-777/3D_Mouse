package com.example.a3dmouse;

import android.os.Bundle;
import android.widget.TextView;

/*
MyCommunicationsActivity implements the actual actions performed
after establishing Bluetooth connection between phone and the computer.
 */
public class MyCommunicationsActivity extends CommunicationsActivity implements PhonePositionChangeListener {
    private PositionDetectionService positionDetectionService;
    private CursorService cursorService;

    private double positionX;
    private double positionY;
    private double upDownAngle;
    private double leftRightAngle;

    private TextView xCoordinateTextView;
    private TextView yCoordinateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* The main 'communication activity' view displaying cursor x, y coordinates.
           These values are temporarily displayed on the phone screen
           to enable easy checks if they are sent to the bluetooth server correctly.
        */
        setContentView(R.layout.activity_main);
        xCoordinateTextView = findViewById(R.id.xCoordinte);
        yCoordinateTextView = findViewById(R.id.yCoordinte);

        positionX = 0;
        positionY = 0;
        upDownAngle = 0;
        leftRightAngle = 0;

        positionDetectionService = new PositionDetectionService(getBaseContext(), this);
        cursorService = new CursorService(1);
    }

    protected void onPause() {
        super.onPause();
        positionDetectionService.unregisterListeners();
    }

    protected void onResume() {
        super.onResume();
        positionDetectionService.registerListeners();
    }

    @Override
    public void onPhoneMovedAccelerometer(PhonePositionLinearDelta phonePositionLinearDelta) {
        positionX += phonePositionLinearDelta.getX();
        positionY += phonePositionLinearDelta.getY();
        changeCursorCoordinatesLinear(positionX, positionY);
    }

    @Override
    public void onPhoneMovedGyroscope(PhonePositionAnglesDelta phonePositionAnglesDelta) {
        leftRightAngle += phonePositionAnglesDelta.getAzimuth();
        upDownAngle += phonePositionAnglesDelta.getPitch();
        changeCursorCoordinatesAngles(leftRightAngle, upDownAngle);
    }

    private void changeCursorCoordinatesAngles(double xAngle, double yAngle) {
        cursorService.setCursorMovementAngle(xAngle, yAngle);
        xCoordinateTextView.setText(setPrecision(cursorService.getCursorXCoordinate()));
        yCoordinateTextView.setText(setPrecision(cursorService.getCursorYCoordinate()));
        sendViaBluetooth();
    }

    private void changeCursorCoordinatesLinear(double x, double y) {
        cursorService.setCursorMovementLinear(x, y);
        xCoordinateTextView.setText(setPrecision(cursorService.getCursorXCoordinate()));
        yCoordinateTextView.setText(setPrecision(cursorService.getCursorYCoordinate()));
        sendViaBluetooth();
    }

    private void sendViaBluetooth() {
        String separator = ";";
        for (byte bx : (xCoordinateTextView.getText()+separator).getBytes()) {
            mBluetoothConnection.write(bx);
        }
        for (byte by : String.valueOf(yCoordinateTextView.getText()).getBytes()) {
            mBluetoothConnection.write(by);
        }
    }

    private String setPrecision(double doubleValue) {
        return String.format("%.2f", doubleValue);
    }

}
