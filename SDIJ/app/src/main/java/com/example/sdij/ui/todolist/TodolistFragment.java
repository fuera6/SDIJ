package com.example.sdij.ui.todolist;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sdij.R;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TodolistFragment extends Fragment {

    static final int TEACHERS_NUM = 13;

    SQLiteDatabase sqlDB;

    dailyDBHelper dailyHelper;
    Integer[] dateDailyIDs = {R.id.dateMyDaily, R.id.dateDaily1, R.id.dateDaily2, R.id.dateDaily3, R.id.dateDaily4, R.id.dateDaily5, R.id.dateDaily6, R.id.dateDaily7, R.id.dateDaily8, R.id.dateDaily9, R.id.dateDaily10, R.id.dateDaily11, R.id.dateDaily12};
    Integer[] textDailyIDs = {R.id.textMyDaily, R.id.textDaily1, R.id.textDaily2, R.id.textDaily3, R.id.textDaily4, R.id.textDaily5, R.id.textDaily6, R.id.textDaily7, R.id.textDaily8, R.id.textDaily9, R.id.textDaily10, R.id.textDaily11, R.id.textDaily12};
    Integer[] btnDailyIDs = {R.id.btnMyDaily, R.id.btnDaily1, R.id.btnDaily2, R.id.btnDaily3, R.id.btnDaily4, R.id.btnDaily5, R.id.btnDaily6, R.id.btnDaily7, R.id.btnDaily8, R.id.btnDaily9, R.id.btnDaily10, R.id.btnDaily11, R.id.btnDaily12};
    Integer[] edtDailyIDs = {R.id.edtMyDaily, R.id.edtDaily1, R.id.edtDaily2, R.id.edtDaily3, R.id.edtDaily4, R.id.edtDaily5, R.id.edtDaily6, R.id.edtDaily7, R.id.edtDaily8, R.id.edtDaily9, R.id.edtDaily10, R.id.edtDaily11, R.id.edtDaily12};
    Integer[] listDailyIDs = {R.id.listMyDaily, R.id.listDaily1, R.id.listDaily2, R.id.listDaily3, R.id.listDaily4, R.id.listDaily5, R.id.listDaily6, R.id.listDaily7, R.id.listDaily8, R.id.listDaily9, R.id.listDaily10, R.id.listDaily11, R.id.listDaily12};
    TextView[] dateDailys = new TextView[TEACHERS_NUM];
    TextView[] textDailys = new TextView[TEACHERS_NUM];
    Button[] btnDailys = new Button[TEACHERS_NUM];
    EditText[] edtDailys = new EditText[TEACHERS_NUM];
    ListView[] listDailys = new ListView[TEACHERS_NUM];
    ArrayList<String>[] dailyArrayLists = new ArrayList[TEACHERS_NUM];
    ArrayAdapter<String>[] dailyArrayAdapters = new ArrayAdapter[TEACHERS_NUM];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todolist, container, false);

        dailyHelper = new dailyDBHelper(getContext());

        for(int i=0; i<TEACHERS_NUM; i++) {
            final int index = i;
            dateDailys[index] = (TextView) root.findViewById(dateDailyIDs[index]);
            textDailys[index] = (TextView) root.findViewById(textDailyIDs[index]);
            btnDailys[index] = (Button) root.findViewById(btnDailyIDs[index]);
            edtDailys[index] = (EditText) root.findViewById(edtDailyIDs[index]);
            listDailys[index] = (ListView) root.findViewById(listDailyIDs[index]);

            try {
                FileInputStream inFs = getActivity().getApplicationContext().openFileInput("dateDailys" + (index+1) + ".txt");
                byte[] txt = new byte[30];
                inFs.read(txt);
                String str = (new String(txt)).trim();
                inFs.close();
                String[] Dday = str.split(":");
                setTaskDate(dateDailys[index], Integer.parseInt(Dday[0]), Integer.parseInt(Dday[1]), Integer.parseInt(Dday[2]));

            } catch (IOException e) {
                try {
                    FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("dateDailys" + (index+1) + ".txt", Context.MODE_PRIVATE);
                    outFs.write("2022:1:1".getBytes());
                    outFs.close();
                } catch (IOException _e) {
                }
            }

            try {
                FileInputStream inFs = getActivity().getApplicationContext().openFileInput("textDailys" + (index+1) + ".txt");
                byte[] txt = new byte[3000];
                inFs.read(txt);
                String str = (new String(txt)).trim();
                inFs.close();
                textDailys[index].setText(str);
            } catch (IOException e) {
                try {
                    FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("textDailys" + (index+1) + ".txt", Context.MODE_PRIVATE);
                    outFs.write("과제 없음".getBytes());
                    outFs.close();
                } catch (IOException _e) {
                }
            }

            dailyArrayLists[index] = new ArrayList<String>();
            initArrayList(dailyArrayLists[index], "teacher" + (index+1));

            dailyArrayAdapters[index] = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, dailyArrayLists[index]){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView)view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.BLACK);
                    return view;
                }
            };

            listDailys[index].setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listDailys[index].setAdapter(dailyArrayAdapters[index]);

            initList(listDailys[index],"teacher" + (index+1));
        }


        for(int i=0; i<TEACHERS_NUM; i++) {
            int index = i;

            textDailys[index].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    View dialogView = (View) View.inflate(getActivity(), R.layout.task_dialog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    dlg.setTitle("과제 입력하기");
                    dlg.setIcon(R.drawable.ic_menu_todolist);
                    dlg.setView(dialogView);
                    DatePicker dateDlgEdt = (DatePicker) dialogView.findViewById(R.id.dateDlgEdt);
                    EditText textDlgEdt = (EditText) dialogView.findViewById(R.id.textDlgEdt);

                    String task = "";
                    try {
                        FileInputStream inFs = getActivity().getApplicationContext().openFileInput("textDailys" + (index+1) + ".txt");
                        byte[] txt = new byte[3000];
                        inFs.read(txt);
                        task = (new String(txt)).trim();
                        inFs.close();
                    } catch (IOException e) {
                        Toast.makeText(getActivity().getApplicationContext(), index + "번 text에서 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                    textDlgEdt.setText(task);

                    dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int year = dateDlgEdt.getYear();
                            int month = dateDlgEdt.getMonth();
                            int day = dateDlgEdt.getDayOfMonth();
                            setTaskDate(dateDailys[index], year, month, day);
                            textDailys[index].setText(textDlgEdt.getText().toString().trim().length() == 0 ? "과제 없음" : textDlgEdt.getText().toString().trim());
                            try {
                                FileOutputStream outFs1 = getActivity().getApplicationContext().openFileOutput("dateDailys" + (index+1) + ".txt", Context.MODE_PRIVATE);
                                String date = String.valueOf(year) + ":" + String.valueOf(month) + ":" + String.valueOf(day);
                                outFs1.write(date.getBytes());
                                outFs1.close();
                                FileOutputStream outFs2 = getActivity().getApplicationContext().openFileOutput("textDailys" + (index+1) + ".txt", Context.MODE_PRIVATE);
                                outFs2.write(textDailys[index].getText().toString().trim().getBytes());
                                outFs2.close();
                            } catch (IOException _e) {
                            }
                        }
                    });

                    dlg.show();
                    return true;
                }
            });

            btnDailys[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String edtDaily = edtDailys[index].getText().toString().trim().length() == 0 ? "새 계획" : edtDailys[index].getText().toString().trim();
                    addData("teacher" + (index+1), edtDaily, dailyArrayLists[index]);
                    dailyArrayAdapters[index].notifyDataSetChanged();
                    Toast.makeText(getActivity().getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                }
            });

            listDailys[index].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String homework = dailyArrayLists[index].get(arg2);
                    updateDataCompletion("teacher" + (index+1), homework, listDailys[index].isItemChecked(arg2) ? 1 : 0);
                    dailyArrayAdapters[index].notifyDataSetChanged();
                }
            });

            listDailys[index].setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    View dialogView = (View) View.inflate(getActivity(), R.layout.todolist_dialog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    dlg.setTitle("수정하기");
                    dlg.setIcon(R.drawable.ic_menu_todolist);
                    dlg.setView(dialogView);
                    EditText dlgEdt = (EditText) dialogView.findViewById(R.id.dlgEdt);
                    dlgEdt.setText(dailyArrayLists[index].get(arg2));

                    String pre_homework = dailyArrayLists[index].get(arg2);
                    dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String homework = dlgEdt.getText().toString().trim().length() == 0 ? "새 계획" : dlgEdt.getText().toString().trim();
                            deleteData("teacher" + (index+1), pre_homework, dailyArrayLists[index], listDailys[index], arg2);
                            addData("teacher" + (index+1), homework, dailyArrayLists[index]);
                            dailyArrayAdapters[index].notifyDataSetChanged();
                            Toast.makeText(getActivity().getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData("teacher" + (index+1), pre_homework, dailyArrayLists[index], listDailys[index], arg2);
                            dailyArrayAdapters[index].notifyDataSetChanged();
                            Toast.makeText(getActivity().getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dlg.show();
                    return true;
                }
            });
        }

        return root;
    }

    public class dailyDBHelper extends SQLiteOpenHelper {

        public dailyDBHelper(Context context) {
            super(context, "dailyDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE dailyTBL ( teacher CHAR(20), homework CHAR(100), completion INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void initArrayList(ArrayList<String> arrayList, String teacher) {
        sqlDB = dailyHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM dailyTBL WHERE teacher = '" + teacher + "';", null);

        String homework;
        while (cursor.moveToNext()) {
            homework = cursor.getString(1);
            arrayList.add(homework);
        }
    }

    public void initList(ListView listView, String teacher) {
        sqlDB = dailyHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM dailyTBL WHERE teacher = '" + teacher + "';", null);

        int index = 0;
        while (cursor.moveToNext()) {
            int completion = Integer.parseInt(cursor.getString(2));
            if(completion == 1) {
                listView.setItemChecked(index, true);
            }
            index++;
        }
    }

    public void addData(String teacher, String homework, ArrayList<String> arrayList) {
        String homework_changed = homework;
        if(arrayList.contains(homework_changed)) {
            int i = 1;
            while (true) {
                homework_changed = homework + "(" + i + ")";
                if(arrayList.contains(homework_changed)) {
                    i++;
                } else {
                    break;
                }
            }
        }

        sqlDB = dailyHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO dailyTBL VALUES ( '" + teacher + "', '" + homework_changed + "', 0 );");
        sqlDB.close();
        arrayList.add(homework_changed);
    }

    public void deleteData(String teacher, String homework, ArrayList<String> arrayList, ListView listView, int index) {
        arrayList.remove(index);
        sqlDB = dailyHelper.getWritableDatabase();
        sqlDB.execSQL("DELETE FROM dailyTBL WHERE teacher = '" + teacher + "' AND homework = '" + homework + "';");
        sqlDB.close();
        for(int j = index; j < listView.getCount() - 1; j++) {
            listView.setItemChecked(j, listView.isItemChecked(j+1));
        }
        listView.setItemChecked(listView.getCount() - 1, false);
    }

    public void updateDataCompletion(String teacher, String homework, int completion) {
        sqlDB = dailyHelper.getWritableDatabase();
        sqlDB.execSQL("UPDATE dailyTBL SET completion = " + completion + " WHERE teacher = '" + teacher + "' AND homework = '" + homework + "';");
        sqlDB.close();
    }

    public void setTaskDate(TextView textView, int d_year, int d_month, int d_day) {
        Calendar tday = Calendar.getInstance();
        Calendar dday = Calendar.getInstance();
        dday.set(d_year, d_month, d_day);

        long diffSec = (dday.getTimeInMillis() - tday.getTimeInMillis()) / 1000;
        long diffDays = diffSec / (24*60*60);
        int count = (int) diffDays;

        String str = count > 0 ? " D-" + count : " D-Day";
        String colorString;
        if (count > 2) {
            colorString="#22b14c";
        } else if (count == 2) {
            colorString="#ff8000";
        } else if (count == 1) {
            colorString="#d20000";
        } else {
            colorString="#6c0000";
        }
        textView.setText(str);
        textView.setTextColor(Color.parseColor(colorString));
    }
}