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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.DisplayAdapter;
import adapters.MainAdapter;
import data.DataAccessObject;
import models.Day;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int PICK_WEEK_REQUEST = 1;

    private int currentYear;
    private int currentWeek;
    private DayName currentDay;

    private MainAdapter mainAdapter;
    private DisplayAdapter displayAdapter;

    private RelativeLayout toDoNew;
    private TextView yearText, weekNumText, emptyList;
    private EditText toDoNewItem;
    private ListView dayList, toDoList;
    private Button toDoAdd;
    private SlidingUpPanelLayout slider;

    private DataAccessObject dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        dataAccess = ((ApplicationDatabase)getApplicationContext()).dataAccess;

        findViewsById();

        setUpAdapters();

        buildWeek(DateCalcs.getCurrentYear(), DateCalcs.getCurrentWeek());

        dayList.setOnItemClickListener(this);

        toDoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newToDo = toDoNewItem.getText().toString();
                String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

                //TODO: need a way to allow the user to select the type of todo
                //Use a snackbar?
                dataAccess.putToDoItem(new ToDo(yearWeekNum, ToDo.TYPE_TODO, newToDo));
                displayAdapter.buildToDoList(currentYear, currentWeek, currentDay);
                displayAdapter.notifyDataSetChanged();

                toDoNewItem.setText("");
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
        if (id == R.id.go_to_picker) {
            startActivityForResult(
                    new Intent(MainActivity.this, WeekPickerActivity.class),
                    PICK_WEEK_REQUEST
            );

            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toDoNew.setVisibility(View.VISIBLE);
        Day day = mainAdapter.getItem(position);

        currentDay = day.getDay();

        displayAdapter.buildToDoList(currentYear, currentWeek, currentDay);
        displayAdapter.notifyDataSetChanged();

        slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void findViewsById() {
        yearText = (TextView)findViewById(R.id.main_text_year);
        weekNumText = (TextView)findViewById(R.id.main_text_weekNum);
        emptyList = (TextView)findViewById(R.id.slider_text_emptyList);

        toDoNew = (RelativeLayout)findViewById(R.id.slider_rel_toDoNew);

        toDoAdd = (Button)findViewById(R.id.slider_butn_toDoAdd);

        toDoNewItem = (EditText)findViewById(R.id.slider_edit_toDoNewItem);

        slider = (SlidingUpPanelLayout)findViewById(R.id.main_sliding_layout);

        dayList = (ListView)findViewById(R.id.main_list_daily);
        toDoList = (ListView)findViewById(R.id.slider_list_toDoList);
    }

    private void buildWeek(int year, int week) {
        yearText.setText(String.valueOf(year));
        weekNumText.setText(DateCalcs.addZeroToNum(week));

		mainAdapter.finishBuildingWeek(year, week);
    }

    private void setUpAdapters() {
        mainAdapter = new MainAdapter(MainActivity.this);
        dayList.setAdapter(mainAdapter);

        displayAdapter = new DisplayAdapter(MainActivity.this, getApplicationContext());
        toDoList.setAdapter(displayAdapter);
        toDoList.setEmptyView(emptyList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_WEEK_REQUEST) {
            if(resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                currentYear = extras.getInt(DateCalcs.YEAR_KEY);
                currentWeek = extras.getInt(DateCalcs.WEEK_NUM_KEY);
            } else {
                currentYear = DateCalcs.getCurrentYear();
                currentWeek = DateCalcs.getCurrentWeek();
            }
        }

        buildWeek(currentYear, currentWeek);
    }
}
