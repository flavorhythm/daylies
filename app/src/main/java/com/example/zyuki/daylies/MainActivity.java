package com.example.zyuki.daylies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.MainAdapter;
import data.DataAccessObject;
import models.Day;
import models.DayName;
import utils.DateCalcs;
import utils.ListBuilder;

public class MainActivity extends AppCompatActivity {
    private List<Day> week;

    private TextView yearText, weekNumText;
    private ListView dayList;

    private DataAccessObject dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataAccess = ((ApplicationDatabase)getApplicationContext()).dataAccess;

        yearText = (TextView)findViewById(R.id.main_text_year);
        weekNumText = (TextView)findViewById(R.id.main_text_weekNum);
        dayList = (ListView)findViewById(R.id.main_list_daily);

        buildWeek();

        yearText.setText(String.valueOf(week.get(0).getYear()));
        weekNumText.setText(String.valueOf(week.get(0).getWeekNum()));
        MainAdapter adapter = new MainAdapter(MainActivity.this, R.layout.main_row, week);
        dayList.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, WeekPickerActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildWeek() {
        int year;
        int weekNum;

        week = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        if(extras == null || extras.isEmpty()) {
            year = DateCalcs.getCurrentYear();
            weekNum = DateCalcs.getCurrentWeek(year);
        } else {
            year = extras.getInt(DateCalcs.YEAR_KEY);
            weekNum = extras.getInt(DateCalcs.WEEK_NUM_KEY);
        }

		week = ListBuilder.finishBuildingWeek(year, weekNum);
    }
}
