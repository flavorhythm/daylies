package com.example.zyuki.daylies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.PickerAdapter;
import models.WeeksInYear;
import utils.DateCalcs;
import utils.ListBuilder;

public class WeekPickerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<WeeksInYear> weeksInYear;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);

        Bundle extras = getIntent().getExtras();

        if(extras == null || extras.isEmpty()) {
            year = DateCalcs.getCurrentYear();
        } else {
            year = extras.getInt(DateCalcs.YEAR_KEY);
        }

        weeksInYear = ListBuilder.buildWeeksInYear(year);

        ListView weekList = (ListView)findViewById(R.id.picker_list_week);
        PickerAdapter adapter = new PickerAdapter(WeekPickerActivity.this, R.layout.picker_row, weeksInYear);
        weekList.setAdapter(adapter);

        weekList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeeksInYear week = weeksInYear.get(position);

        Intent intent = new Intent(WeekPickerActivity.this, MainActivity.class);
        intent.putExtra(DateCalcs.YEAR_KEY, year);
        intent.putExtra(DateCalcs.WEEK_NUM_KEY, week.getWeekNum());

        startActivity(intent);
        finish();
    }
}