package com.example.zyuki.daylies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import adapters.PickerAdapter;
import models.WeeksInYear;

public class WeekPickerActivity extends AppCompatActivity {
    private int year;
    private List<WeeksInYear> weeksInYear;

    private ListView weekList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);

        weekList = (ListView)findViewById(R.id.picker_list_week);
        PickerAdapter adapter = new PickerAdapter(WeekPickerActivity.this, R.layout.picker_row, weeksInYear);
        weekList.setAdapter(adapter);


    }
}
