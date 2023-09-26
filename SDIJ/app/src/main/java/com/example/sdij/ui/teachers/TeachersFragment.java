package com.example.sdij.ui.teachers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sdij.R;

public class TeachersFragment extends Fragment {
    Button btnPrev1, btnNext1;
    ViewFlipper vFlipper1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teachers, container, false);

        btnPrev1 = (Button) root.findViewById(R.id.btnPrev1);
        btnNext1 = (Button) root.findViewById(R.id.btnNext1);
        vFlipper1 = (ViewFlipper) root.findViewById(R.id.viewFlipper1);

        btnPrev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper1.showPrevious();
            }
        });

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper1.showNext();
            }
        });

        return root;
    }
}