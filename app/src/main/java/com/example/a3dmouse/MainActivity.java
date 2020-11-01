package com.example.a3dmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MousePositionChangeListener {
    private PositionDetectionService positionDetectionService;
    private TextView positionXTextView;
    private TextView positionYTextView;
    private TextView positionZTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        positionXTextView = findViewById(R.id.positionXTextView);
        positionYTextView = findViewById(R.id.positionYTextView);
        positionZTextView = findViewById(R.id.positionZTextView);
        positionDetectionService = new PositionDetectionService(getBaseContext(), this);
    }

    protected void onPause() {
        super.onPause();
        positionDetectionService.unregisterListener();
    }

    protected void onResume() {
        super.onResume();
        positionDetectionService.registerListener();
    }

    @Override
    public void onMouseMoved(MousePositionDelta mousePositionDelta) {
        positionXTextView.setText(setPrecision(mousePositionDelta.getDeltaX()));
        positionYTextView.setText(setPrecision(mousePositionDelta.getDeltaY()));
        positionZTextView.setText(setPrecision(mousePositionDelta.getDeltaZ()));
    }

    private String setPrecision(double doubleValue) {
        return String.format("%.2f", doubleValue);
    }
}
