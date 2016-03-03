package com.example.zyuki.daylies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.PickerAdapter;
import models.WeeksInYear;
import utils.DateCalcs;
import utils.ListBuilder;

public class WeekPickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);

        Bundle extras = getIntent().getExtras();

        int year;
        if(extras == null || extras.isEmpty()) {
            year = DateCalcs.getCurrentYear();
        } else {
            year = extras.getInt(DateCalcs.YEAR_KEY);
        }

        List<WeeksInYear> weeksInYear = ListBuilder.buildWeeksInYear(year);

        ListView weekList = (ListView)findViewById(R.id.picker_list_week);
        PickerAdapter adapter = new PickerAdapter(WeekPickerActivity.this, R.layout.picker_row, weeksInYear);
        weekList.setAdapter(adapter);
    }
}
