package com.example.sdij.ui.curriculum;

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

public class CurriculumFragment extends Fragment {
    Button btnPrev2, btnNext2;
    ViewFlipper vFlipper2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_curriculum, container, false);

        btnPrev2 = (Button) root.findViewById(R.id.btnPrev2);
        btnNext2 = (Button) root.findViewById(R.id.btnNext2);
        vFlipper2 = (ViewFlipper) root.findViewById(R.id.viewFlipper2);

        btnPrev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper2.showPrevious();
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFlipper2.showNext();
            }
        });

        return root;
    }
}