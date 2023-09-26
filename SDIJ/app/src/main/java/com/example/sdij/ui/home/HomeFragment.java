package com.example.sdij.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.sdij.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {
    TextClock textClock1;
    FloatingActionButton sdij_fab, suneung_fab, ebsi_fab, megastudy_fab, mimac_fab, etoos_fab;
    TextView note;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textClock1 = (TextClock) root.findViewById(R.id.textClock1);
        Typeface typeFace = ResourcesCompat.getFont(getContext(), R.font.bmjua_ttf);
        textClock1.setTypeface(typeFace);
        TextView note = (TextView) root.findViewById(R.id.note);

        sdij_fab = (FloatingActionButton) root.findViewById(R.id.sdij_fab);
        suneung_fab = (FloatingActionButton) root.findViewById(R.id.suneung_fab);
        ebsi_fab = (FloatingActionButton) root.findViewById(R.id.ebsi_fab);
        megastudy_fab = (FloatingActionButton) root.findViewById(R.id.megastudy_fab);
        mimac_fab = (FloatingActionButton) root.findViewById(R.id.mimac_fab);
        etoos_fab = (FloatingActionButton) root.findViewById(R.id.etoos_fab);

        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("note.txt");
            byte[] txt = new byte[3000];
            inFs.read(txt);
            String str = (new String(txt)).trim();
            inFs.close();
            note.setText(str);
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("note.txt", Context.MODE_PRIVATE);
                outFs.write("과제 없음".getBytes());
                outFs.close();
            } catch (IOException _e) {
            }
        }

        note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                View dialogView = (View) View.inflate(getActivity(), R.layout.my_task_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("과제 입력하기");
                dlg.setIcon(R.drawable.ic_menu_todolist);
                dlg.setView(dialogView);
                EditText textDlgEdt = (EditText) dialogView.findViewById(R.id.textDlgEdt);

                String task = "";
                try {
                    FileInputStream inFs = getActivity().getApplicationContext().openFileInput("note.txt");
                    byte[] txt = new byte[3000];
                    inFs.read(txt);
                    task = (new String(txt)).trim();
                    inFs.close();
                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "note.txt에서 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
                textDlgEdt.setText(task);

                dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note.setText(textDlgEdt.getText().toString().trim().length() == 0 ? "과제 없음" : textDlgEdt.getText().toString().trim());
                        try {
                            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("note.txt", Context.MODE_PRIVATE);
                            outFs.write(note.getText().toString().trim().getBytes());
                            outFs.close();
                            Toast.makeText(getActivity().getApplicationContext(), "저장됨", Toast.LENGTH_SHORT).show();
                        } catch (IOException _e) {
                        }
                    }
                });

                dlg.show();
                return true;
            }
        });

        sdij_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sdij.com/sdn/"));
                startActivity(mIntent);
            }
        });

        suneung_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.suneung.re.kr/"));
                startActivity(mIntent);
            }
        });

        ebsi_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ebsi.co.kr/"));
                startActivity(mIntent);
            }
        });

        megastudy_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.megastudy.net/"));
                startActivity(mIntent);
            }
        });

        mimac_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mimacstudy.com/"));
                startActivity(mIntent);
            }
        });

        etoos_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://go3.etoos.com/"));
                startActivity(mIntent);
            }
        });

        return root;
    }
}