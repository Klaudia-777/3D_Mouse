package com.example.a3dmouse;

class MousePointerService {
    private double distanceFromScreen;
    private double mouseXCoordinate;
    private double mouseYCoordinate;

    MousePointerService(double distanceFromScreen) {
        this.distanceFromScreen = distanceFromScreen;
    }

    public double getMouseXCoordinate() {
        return mouseXCoordinate;
    }

    public double getMouseYCoordinate() {
        return mouseYCoordinate;
    }

    void setMouseMovementAngle(double angleYAxis, double angleXAxis) {
        mouseXCoordinate = distanceFromScreen * Math.tan(Math.toRadians(angleYAxis));
        mouseYCoordinate = distanceFromScreen * Math.tan(Math.toRadians(angleXAxis));
    }

    void setMouseMovementLinear(double x, double y) {
        mouseXCoordinate = x;
        mouseYCoordinate = y;
    }


}
