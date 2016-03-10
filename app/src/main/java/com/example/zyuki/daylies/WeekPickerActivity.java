package com.example.zyuki.daylies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.PickerAdapter;
import fragments.DialogRouter;
import models.WeeksInYear;
import utils.DateCalcs;
import utils.ListBuilder;

public class WeekPickerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private PickerAdapter adapter;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);

        ListView weekList = (ListView)findViewById(R.id.picker_list_week);

        year = DateCalcs.getCurrentYear();

        adapter = new PickerAdapter(WeekPickerActivity.this);
        displaySelectedYear(year);
        weekList.setAdapter(adapter);

        weekList.setOnItemClickListener(this);
    }

    public void displaySelectedYear(int year) {
        this.year = year;

        adapter.buildWeeksInYear(year);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeeksInYear week = adapter.getItem(position);

        Intent intent = new Intent(WeekPickerActivity.this, MainActivity.class);
        intent.putExtra(DateCalcs.YEAR_KEY, year);
        intent.putExtra(DateCalcs.WEEK_NUM_KEY, week.getWeekNum());

        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.select_year) {
            DialogRouter.instantiatePickerDialog(WeekPickerActivity.this, year);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}