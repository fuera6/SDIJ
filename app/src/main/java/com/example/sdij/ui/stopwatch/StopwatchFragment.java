package com.example.sdij.ui.stopwatch;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sdij.R;

public class StopwatchFragment extends Fragment {
    Chronometer chronometer;
    Button startButton, stopButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        chronometer = root.findViewById(R.id.chronometer);
        startButton = root.findViewById(R.id.start_btn);
        stopButton = root.findViewById(R.id.stop_btn);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                chronometer.setTextColor(Color.GRAY);
                startButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                chronometer.setTextColor(Color.BLACK);
                startButton.setText("재시작");
                stopButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }
}
