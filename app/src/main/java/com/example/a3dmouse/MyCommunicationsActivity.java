package com.example.a3dmouse;

import android.os.Bundle;
import android.widget.TextView;

public class MyCommunicationsActivity extends CommunicationsActivity implements MousePositionChangeListener {
    private PositionDetectionService positionDetectionService;
    private MousePointerService mousePointerService;

    private double positionX;
    private double positionY;
    private double upDownAngle;

    private TextView xCoordinate;
    private TextView yCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xCoordinate = findViewById(R.id.xCoordinte);
        yCoordinate = findViewById(R.id.yCoordinte);

        positionX = 0;
        positionY = 0;
        upDownAngle = 0;

        positionDetectionService = new PositionDetectionService(getBaseContext(), this);
        mousePointerService = new MousePointerService(1);
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
    public void onMouseMovedAccelerometer(MousePositionLinearDelta mousePositionLinearDelta) {
        positionX += mousePositionLinearDelta.getDeltaX();
        positionY += mousePositionLinearDelta.getDeltaY();
        changeMouseCoordinatesLinear(positionX, positionY);
    }

    @Override
    public void onMouseMovedGyroscope(MousePositionAngleDelta mousePositionDelta) {
        positionX += mousePositionDelta.getDeltaY();
        upDownAngle += mousePositionDelta.getDeltaX();
        changeMouseCoordinatesAngles(positionX, upDownAngle);
    }

    private void changeMouseCoordinatesAngles(double xAngle, double yAngle) {
        mousePointerService.setMouseMovementAngle(xAngle, yAngle);
        xCoordinate.setText(setPrecision(mousePointerService.getMouseXCoordinate()));
        yCoordinate.setText(setPrecision(mousePointerService.getMouseYCoordinate()));
        sendViaBluetooth();
    }

    private void changeMouseCoordinatesLinear(double x, double y) {
        mousePointerService.setMouseMovementLinear(x, y);
        xCoordinate.setText(setPrecision(mousePointerService.getMouseXCoordinate()));
        yCoordinate.setText(setPrecision(mousePointerService.getMouseYCoordinate()));
        sendViaBluetooth();
    }

    private void sendViaBluetooth() {
        String separator = ";";
        for (byte bx : (xCoordinate.getText()+separator).getBytes()) {
            mBluetoothConnection.write(bx);
        }
        for (byte by : String.valueOf(yCoordinate.getText()).getBytes()) {
            mBluetoothConnection.write(by);
        }
    }

    private String setPrecision(double doubleValue) {
        return String.format("%.2f", doubleValue);
    }

//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}
