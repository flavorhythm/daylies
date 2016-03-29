package com.example.zyuki.daylies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import adapters.DisplayAdapter;
import adapters.MainAdapter;
import data.DataAccessObject;
import fragments.DialogRouter;
import models.Day;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    public static final int PICK_WEEK_REQUEST = 1;

    private static final int RESULT_FIRST_RUN = RESULT_FIRST_USER;

    private int currentYear;
    private int currentWeek;
    private DayName currentDay;

    private MainAdapter mainAdapter;
    private DisplayAdapter displayAdapter;

    private RelativeLayout sliderHeader;
    private TextView yearText, weekNumText, emptyList, toDoDate;
    private ListView dayListView, toDoListView;
    private Button toDoAdd;
    private SlidingUpPanelLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findViewsById();
        setUpAdapters();

        sliderHeader.getViewTreeObserver().addOnGlobalLayoutListener(this);

        onActivityResult(PICK_WEEK_REQUEST, RESULT_FIRST_RUN, null);

        dayListView.setOnItemClickListener(this);

        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(displayAdapter.getItem(position).getType() != ToDo.TYPE_HEADER) {
                    DialogRouter.instantiateDeleteDialog(MainActivity.this, position);
                }
                return true;
            }
        });

        toDoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(currentDay) {
                    case MON: case TUE: case WED: case THU: case FRI:
                        DialogRouter.instantiateInputDialog(MainActivity.this, DialogRouter.TYPE_WEEKDAY);
                        break;
                    case SAT: case SUN:
                        DialogRouter.instantiateInputDialog(MainActivity.this, DialogRouter.TYPE_WEEKEND);
                        break;
                }
            }
        });
    }

    public void notifyDisplayAdapter() {
        displayAdapter.notifyDataSetChanged();
    }

    public void putLunchItem(String newToDo) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        displayAdapter.add(new ToDo(yearWeekNum, ToDo.TYPE_LUNCH, newToDo));
    }

    public void putDailyItem(String newToDo) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        displayAdapter.add(new ToDo(yearWeekNum, ToDo.TYPE_TODO, newToDo));
    }

    public void deleteItem(int position) {
        displayAdapter.removeItem(position);
    }

    @Override
    public void onGlobalLayout() {
        int width = sliderHeader.getWidth();
        int widthAspect = 16;
        int heightAspect = 9;

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            sliderHeader.setMinimumHeight(width * heightAspect / widthAspect);
        }

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            sliderHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            sliderHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        if(slider.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {super.onBackPressed();}
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
            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            Intent intent = new Intent(MainActivity.this, WeekPickerActivity.class);
            intent.putExtra(DateCalcs.YEAR_KEY, currentYear);

            startActivityForResult(
                    intent,
                    PICK_WEEK_REQUEST
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.main_list_daily:
                getToDoList(position);
                break;
        }
    }

    private void getToDoList(int position) {
        Day day = mainAdapter.getItem(position);

        if(day.getType() != MainAdapter.TYPE_DIVIDER) {
            toDoAdd.setVisibility(View.VISIBLE);

            toDoDate.setText(DateCalcs.formatDate(DateCalcs.FULL_DATE, day.getDate()));

            currentDay = day.getDay();

            displayAdapter.buildList(currentYear, currentWeek, currentDay);
            displayAdapter.notifyDataSetChanged();

            slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }

    private void findViewsById() {
        yearText = (TextView)findViewById(R.id.main_text_year);
        weekNumText = (TextView)findViewById(R.id.main_text_weekNum);
        emptyList = (TextView)findViewById(R.id.slider_text_emptyList);
        toDoDate = (TextView)findViewById(R.id.slider_text_date);

        toDoAdd = (Button)findViewById(R.id.slider_butn_toDoAdd);

        slider = (SlidingUpPanelLayout)findViewById(R.id.main_sliding_layout);

        sliderHeader = (RelativeLayout)findViewById(R.id.slider_rel_header);

        dayListView = (ListView)findViewById(R.id.main_list_daily);
        toDoListView = (ListView)findViewById(R.id.slider_list_toDoList);
    }

    private void buildWeek(int year, int week) {
        yearText.setText(String.valueOf(year).substring(2, 4));
        weekNumText.setText(DateCalcs.addZeroToNum(week));

		mainAdapter.finishBuildingWeek(year, week);
    }

    private void setUpAdapters() {
        mainAdapter = new MainAdapter(MainActivity.this, getApplicationContext());
        dayListView.setAdapter(mainAdapter);

        displayAdapter = new DisplayAdapter(MainActivity.this, getApplicationContext());
        toDoListView.setAdapter(displayAdapter);
        toDoListView.setEmptyView(emptyList);
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
