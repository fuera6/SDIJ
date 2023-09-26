package com.example.sdij.ui.timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sdij.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TimerFragment extends Fragment {
    TextView countdownText;
    Button startButton, stopButton, cancelButton;
    EditText hourText, minText, secondText;
    CountDownTimer countDownTimer;
    boolean timerRunning, firstState;
    long time=0;
    long tempTime=0;
    FrameLayout setting;
    FrameLayout timer;

    Button btn_korean, btn_math, btn_english, btn_science;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timer, container, false);

        countdownText = root.findViewById(R.id.countdown_text);
        startButton = root.findViewById(R.id.countdown_button);
        stopButton = root.findViewById(R.id.stop_btn);
        cancelButton = root.findViewById(R.id.cancel_btn);
        hourText = root.findViewById(R.id.hour);
        minText = root.findViewById(R.id.min);
        secondText = root.findViewById(R.id.second);
        setting = root.findViewById(R.id.setting);
        timer = root.findViewById(R.id.timer);

        btn_korean = root.findViewById(R.id.btn_korean);
        btn_math = root.findViewById(R.id.btn_math);
        btn_english = root.findViewById(R.id.btn_english);
        btn_science = root.findViewById(R.id.btn_science);

        ArrayList<String> midList = new ArrayList<String>();
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("timerNames.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String str = (new String(txt)).trim();
            if (!str.equals("")) {
                String[] timerNames = str.split(",");
                for (String timerName : timerNames) {
                    midList.add(timerName);
                }
            }
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("timerNames.txt", Context.MODE_PRIVATE);
                outFs.close();
            } catch (IOException _e) {
            }
        }

        ListView list = (ListView) root.findViewById(R.id.listView_timer);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, midList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView)view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(getActivity(), R.layout.timer_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("새 타이머");
                dlg.setIcon(R.drawable.ic_menu_timer);
                dlg.setView(dialogView);
                dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgEdt1 = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                        EditText dlgHour = (EditText) dialogView.findViewById(R.id.dlgHour);
                        EditText dlgMin = (EditText) dialogView.findViewById(R.id.dlgMin);
                        EditText dlgSec = (EditText) dialogView.findViewById(R.id.dlgSec);

                        String title = dlgEdt1.getText().toString().trim().length() == 0 ? "새 타이머" : dlgEdt1.getText().toString().trim();
                        String hour = dlgHour.getText().toString().trim().length() == 0 ? "0" : dlgHour.getText().toString().trim();
                        String min = dlgMin.getText().toString().trim().length() == 0 ? "0" : dlgMin.getText().toString().trim();
                        String sec = dlgSec.getText().toString().trim().length() == 0 ? "0" : dlgSec.getText().toString().trim();

                        makeTimer(midList, title, hour, min, sec);

                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.show();
                ViewGroup.LayoutParams params = dialogView.getLayoutParams();
                params.width = 1050;
                params.height = 300;
                dialogView.setLayoutParams(params);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    FileInputStream inFs = getActivity().getApplicationContext().openFileInput(midList.get(arg2) + ".txt");
                    byte[] txt = new byte[100];
                    inFs.read(txt);
                    String str = (new String(txt)).trim();
                    String[] times = str.split(":");
                    hourText.setText(times[0]);
                    minText.setText(times[1]);
                    secondText.setText(times[2]);
                    inFs.close();
                } catch (IOException e) {
                    System.out.println(e);
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                View dialogView = (View) View.inflate(getActivity(), R.layout.timer_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("타이머 수정하기");
                dlg.setIcon(R.drawable.ic_menu_timer);
                dlg.setView(dialogView);

                EditText dlgEdt1 = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                EditText dlgHour = (EditText) dialogView.findViewById(R.id.dlgHour);
                EditText dlgMin = (EditText) dialogView.findViewById(R.id.dlgMin);
                EditText dlgSec = (EditText) dialogView.findViewById(R.id.dlgSec);

                dlgEdt1.setText(midList.get(arg2));
                try {
                    FileInputStream inFs = getActivity().getApplicationContext().openFileInput(midList.get(arg2) + ".txt");
                    byte[] txt = new byte[1000];
                    inFs.read(txt);
                    String str = (new String(txt)).trim();
                    String[] times = str.split(":");
                    dlgHour.setText(times[0]);
                    dlgMin.setText(times[1]);
                    dlgSec.setText(times[2]);
                    inFs.close();
                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }

                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = dlgEdt1.getText().toString().trim().length() == 0 ? "새 타이머" : dlgEdt1.getText().toString().trim();
                        String hour = dlgHour.getText().toString().trim().length() == 0 ? "0" : dlgHour.getText().toString().trim();
                        String min = dlgMin.getText().toString().trim().length() == 0 ? "0" : dlgMin.getText().toString().trim();
                        String sec = dlgSec.getText().toString().trim().length() == 0 ? "0" : dlgSec.getText().toString().trim();

                        removeTimer(midList, arg2);
                        makeTimer(midList, title, hour, min, sec);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTimer(midList, arg2);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.show();
                ViewGroup.LayoutParams params = dialogView.getLayoutParams();
                params.width = 1050;
                params.height = 300;
                dialogView.setLayoutParams(params);
                return false;
            }
        });

        btn_korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourText.setText("01");
                minText.setText("20");
                secondText.setText("00");
            }
        });

        btn_math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourText.setText("01");
                minText.setText("40");
                secondText.setText("00");
            }
        });

        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourText.setText("01");
                minText.setText("10");
                secondText.setText("00");
            }
        });

        btn_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourText.setText("00");
                minText.setText("30");
                secondText.setText("00");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setBackgroundColor(Color.parseColor("#ffffff"));
                firstState = true;
                setting.setVisibility(setting.GONE);
                timer.setVisibility(timer.VISIBLE);
                startStop();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setVisibility(setting.VISIBLE);
                timer.setVisibility(timer.GONE);
                firstState = true;
                stopTimer();
            }
        });

        updateTimer();
        return root;
    }

    private void startStop() {
        if(timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if(firstState) {
            String sHour = hourText.getText().toString().length() == 0 ? "0" : hourText.getText().toString();
            String sMin = minText.getText().toString().length() == 0 ? "0" : minText.getText().toString();
            String sSecond = secondText.getText().toString().length() == 0 ? "0" : secondText.getText().toString();
            time = (Long.parseLong(sHour) * 3600000) + (Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000) + 1000;
        } else {
            time = tempTime;
        }

        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTime = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timer.setBackgroundColor(Color.RED);
                countdownText.setText("0:00:00");
            }
        }.start();

        stopButton.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("계속");
    }

    private void updateTimer() {
        int hour = (int) tempTime / 3600000;
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;

        String timeLeftText = "";
        timeLeftText = "" + hour + ":";

        if(minutes < 10) timeLeftText += "0";
        timeLeftText += minutes + ":";

        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    void makeTimer(ArrayList<String> midList, String title, String hour, String min, String sec) {
        if (midList.contains(title)) {
            int i = 1;
            while (true) {
                String title_changed = title + "(" + i + ")";
                if (midList.contains(title_changed)) {
                    i++;
                } else {
                    title = title_changed;
                    break;
                }
            }
        }

        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput(title + ".txt", Context.MODE_PRIVATE);
            String time = String.format("%02d", Integer.parseInt(hour)) + ":" + String.format("%02d", Integer.parseInt(min)) + ":" + String.format("%02d", Integer.parseInt(sec));
            outFs.write(time.getBytes());
            outFs.close();
        } catch (IOException e) {
        }

        String str = "";
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("timerNames.txt");
            byte[] txt = new byte[100]; //조정 가능
            inFs.read(txt);
            String raw_str = (new String(txt)).trim();
            if (raw_str.equals("")) {
                str = title;
            } else {
                str = raw_str + "," + title;
            }
            inFs.close();
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("timerNames.txt", Context.MODE_PRIVATE);
            outFs.write(str.getBytes());
            outFs.close();
        } catch (IOException e) {
        }

        midList.add(title);
        hourText.setText(String.format("%02d", Integer.parseInt(hour)));
        minText.setText(String.format("%02d", Integer.parseInt(min)));
        secondText.setText(String.format("%02d", Integer.parseInt(sec)));
    }

    void removeTimer(ArrayList<String> midList, int i) {
        String title = midList.get(i);
        midList.remove(i);
        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("timerNames.txt", Context.MODE_PRIVATE);
            if (midList.size() == 0) {
                outFs.write("".getBytes());
            }
            if (midList.size() == 1) {
                outFs.write((midList.get(0)).getBytes());
            }
            if (midList.size() > 1) {
                String str;
                str = midList.get(0);
                for (int index = 1; index < midList.size(); index++) {
                    str = str + "," + midList.get(index);
                }
                outFs.write(str.getBytes());
            }
            File f = new File(title);
            f.delete();
            outFs.close();
        } catch (IOException e) {
        }
    }
}