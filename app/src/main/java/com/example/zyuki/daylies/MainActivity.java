package com.example.zyuki.daylies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.MainAdapter;
import models.Day;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;
import utils.ListBuilder;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Day> week;
    private List<String> lunchToDoStr, dailyToDoStr;
    private ArrayAdapter<String> lunchAdapter, toDoAdapter;

    private TextView yearText, weekNumText;
    private ListView dayList, lunchList, toDoList;
    private SlidingUpPanelLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findViewsById();

        buildWeek();

        yearText.setText(String.valueOf(week.get(0).getYear()));
        weekNumText.setText(DateCalcs.addZeroToNum(week.get(0).getWeekNum()));

        setUpAdapters();

        dayList.setOnItemClickListener(this);
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
        if (id == R.id.go_to_picker) {
            startActivity(new Intent(MainActivity.this, WeekPickerActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dailyToDoStr.clear();
        DayName day = week.get(position).getDay();
        List<ToDo> dailyToDo = week.get(position).getToDoList();

        String noValStr = "Nothing to do on " + day.toString();

        if(!dailyToDo.isEmpty()) {
            for(ToDo item : dailyToDo) {
                switch(item.getType()) {
                    case ToDo.LUNCH_TYPE:
                        lunchToDoStr.add(item.getItem());
                        break;
                    case ToDo.TODO_TYPE:
                        dailyToDoStr.add(item.getItem());
                        break;
                }
            }
        } else {
            switch(day) {
                case MON: case TUE: case WED: case THU: case FRI:
                    lunchToDoStr.add(noValStr);
                    break;
                case SAT: case SUN:
                    lunchList.setVisibility(View.GONE);
                    toDoAdapter.add(noValStr);
                    break;
            }
        }

        lunchAdapter.notifyDataSetChanged();
        toDoAdapter.notifyDataSetChanged();
        slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void findViewsById() {
        yearText = (TextView)findViewById(R.id.main_text_year);
        weekNumText = (TextView)findViewById(R.id.main_text_weekNum);
        dayList = (ListView)findViewById(R.id.main_list_daily);
        slider = (SlidingUpPanelLayout)findViewById(R.id.main_sliding_layout);
        toDoList = (ListView)findViewById(R.id.main_list_todo);
        lunchList = (ListView)findViewById(R.id.main_list_lunch);
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

        ApplicationDatabase context = ((ApplicationDatabase)getApplicationContext());
		week = ListBuilder.finishBuildingWeek(context , year, weekNum);
    }

    private void setUpAdapters() {
        MainAdapter mainAdapter = new MainAdapter(MainActivity.this, R.layout.main_row, week);
        dayList.setAdapter(mainAdapter);

        lunchToDoStr = new ArrayList<>();
        lunchAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lunchToDoStr);
        lunchList.setAdapter(lunchAdapter);

        dailyToDoStr = new ArrayList<>();
        toDoAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dailyToDoStr);
        toDoList.setAdapter(toDoAdapter);
    }
}
