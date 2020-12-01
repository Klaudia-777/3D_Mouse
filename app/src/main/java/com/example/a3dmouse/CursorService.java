package com.example.a3dmouse;

/*
CursorService class is responsible for converting phone position
coordinates (reported by sensors) to the actual cursor position on the computer screen.
 */

class CursorService {
    private double distanceFromScreen;
    private double cursorXCoordinate;
    private double cursorYCoordinate;

    CursorService(double distanceFromScreen) {
        this.distanceFromScreen = distanceFromScreen;
    }

    double getCursorXCoordinate() {
        return cursorXCoordinate;
    }

    double getCursorYCoordinate() {
        return cursorYCoordinate;
    }

    //TODO:
    // how to get cursor up and down, to the left and to the right
    // movement from the rotational phone movement?
    // (probably scaling arising from the screen dimensions?)

    void setCursorMovementAngle(double angleYAxis, double angleXAxis) {
        cursorXCoordinate = distanceFromScreen * Math.tan(Math.toRadians(angleYAxis));
        cursorYCoordinate = distanceFromScreen * Math.tan(Math.toRadians(angleXAxis));
    }

    //TODO:
    // how to get cursor up and down, to the left and to the right
    // movement from the linear phone movement?
    // (probably scaling arising from the screen dimensions?)

    void setCursorMovementLinear(double x, double y) {
        cursorXCoordinate = x;
        cursorYCoordinate = y;
    }


}
