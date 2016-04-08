package com.example.zyuki.daylies;

import android.os.Bundle;
//import com.melnykov.fab.FloatingActionButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.ToDoAdapter;
import data.DataAccessObject;
import fragments.DisplayDaysFragment;
import fragments.DialogRouter;
import fragments.DisplayWeeksFragment;
import models.Day;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

public class MainActivity extends AppCompatActivity
        implements DisplayWeeksFragment.DataFromWeeks, View.OnClickListener, TabLayout.OnTabSelectedListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private SlidingUpPanelLayout slider;
    private FloatingActionButton fab; //TODO: fab gets "stuck", stops clicking
    private ListView todoList;
    private ToDoAdapter todoAdapter;
    private TextView todoDate;

    private DisplayDaysFragment daysFrag;
    private DisplayWeeksFragment weeksFrag;

    private int currentYear, currentWeek;
    private DayName currentDay;

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentYear = DateCalcs.getCurrentYear();
        currentWeek = DateCalcs.getCurrentWeek();

        findViewByIds();

        setSupportActionBar(toolbar);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

        todoAdapter = new ToDoAdapter(MainActivity.this, getApplicationContext());
        todoList.setAdapter(todoAdapter);
        todoList.setEmptyView(findViewById(R.id.slider_text_emptyList));

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (todoAdapter.getItem(position).getType() != ToDo.TYPE_HEADER) {
                    DialogRouter.instantiateDeleteDialog(MainActivity.this, position);
                }
                return true;
            }
        });

        fab.setOnClickListener(this);
        tabLayout.setOnTabSelectedListener(this);
    }

    /**Creates the menu from layout >> menu_main.xml**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
    }

    /**Routes the selected item menu to its correct corresponding action**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // A switch to route the selected items to the correct action
        switch(id) {
            case R.id.select_year:
                DialogRouter.instantiatePickerDialog(MainActivity.this, currentYear, currentWeek);
                break;
            default:
                break;
        }

        // Returns the Super call of this method
        return super.onOptionsItemSelected(item);
    }

    /****/
    @Override
    public void onClick(View v) {
        if(currentDay != null) {
            switch(currentDay) {
                case MON: case TUE: case WED: case THU: case FRI:
                    DialogRouter.instantiateInputDialog(MainActivity.this, DialogRouter.TYPE_WEEKDAY);
                    break;
                case SAT: case SUN:
                    DialogRouter.instantiateInputDialog(MainActivity.this, DialogRouter.TYPE_WEEKEND);
                    break;
            }
        }
    }

    /****/
    @Override
    public void dataFromWeeks(int year, int week) {
        currentYear = year;
        currentWeek = week;

        daysFrag.buildWeek(year, week);
        weeksFrag.buildYear(year);
    }

    /**Override method for when the back button is pressed**/
    @Override
    public void onBackPressed() {
        //If the slider wasn't retracted, collapse when the back button is pressed
        if(!retractSlider()) {super.onBackPressed();}
    }

    /**Override method to retract slider when tab is selected**/
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Retracts slider
        retractSlider();
        //Sets the viewPager's page (fragment) to the corresponding selected tab
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**Override method required but not used here**/
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    /**Override method to retract slider when tab is reselected**/
    @Override
    public void onTabReselected(TabLayout.Tab tab) {retractSlider();}

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public void changePage(int goToThisTitle) {
        int position = pagerAdapter.getPosition(goToThisTitle);
        TabLayout.Tab goToTab = tabLayout.getTabAt(position);

        if(goToTab != null) {goToTab.select();}
    }

    /****/
    public void showToDoList(Day day) {
        this.currentDay = day.getDay();
        String date = day.getDay().toString() + ", ";
        date += DateCalcs.formatDate(DateCalcs.FULL_DATE, day.getDate());

        todoDate.setText(date);

        todoAdapter.buildList(currentYear, currentWeek, currentDay);
        todoAdapter.notifyDataSetChanged();

        slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    /****/
    public void addLunchItem(String newToDo) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        todoAdapter.add(new ToDo(yearWeekNum, ToDo.CONTENT_LUNCH, newToDo));
        todoAdapter.notifyDataSetChanged();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /****/
    public void addDailyItem(String newToDo, int itemType) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        todoAdapter.add(new ToDo(yearWeekNum, itemType, newToDo));
        todoAdapter.notifyDataSetChanged();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /****/
    public void removeItem(int position) {
        todoAdapter.removeItem(position);
        todoAdapter.notifyDataSetChanged();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private void findViewByIds() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        viewPager = (ViewPager)findViewById(R.id.main_viewPager);
        tabLayout = (TabLayout)findViewById(R.id.main_tabLayout);

        slider = (SlidingUpPanelLayout)findViewById(R.id.main_sliding);
        fab = (FloatingActionButton)findViewById(R.id.slider_fab_addNewItem);
        todoList = (ListView)findViewById(R.id.slider_list_toDoList);
        todoDate = (TextView)findViewById(R.id.slider_text_date);
    }

    /****/
    private void setupViewPager() {
        Bundle args = new Bundle();
        daysFrag = new DisplayDaysFragment();
        weeksFrag = new DisplayWeeksFragment();

        args.putInt(DateCalcs.KEY_YEAR, currentYear);
        args.putInt(DateCalcs.KEY_WEEK, currentWeek);

        daysFrag.setArguments(args);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(R.string.daysFragTitle, daysFrag);
        pagerAdapter.addFragment(R.string.weeksFragTitle, weeksFrag);

        viewPager.setAdapter(pagerAdapter);
    }

    /****/
    private void notifyDaysFrag() {
        boolean hasTodos = todoAdapter.getCount() > 0;
        daysFrag.notifyDataSetChanged(currentDay, hasTodos);
    }

    /****/
    private void notifyWeeksFrag() {
        DataAccessObject dataAccess = ((ApplicationDatabase)getApplicationContext()).dataAccess;
        boolean hasTodos = dataAccess.weekHasTodos(currentYear, currentWeek);
        weeksFrag.notifyDataSetChanged(currentWeek, hasTodos);
    }

    /**Private method that retracts slider if expanded.**/
    /**Returns true if collapsed, false otherwise**/
    /**Used in override methods: onBackPressed, onTabSelected, and onTabReselected**/
    private boolean retractSlider() {
        boolean expanded = slider.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
        boolean dragging = slider.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING;
        //If the slider is expanded, collapse when the back button is pressed
        if(expanded || dragging) {
            //Sets the slider state to collapsed
            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
            //If the panel is in any other state, run this method's Super
        } else {return false;}
    }

    /***********************************************************************************************
     * INNER CLASSES
     **********************************************************************************************/
    /****/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        /*******************************************************************************************
         * GLOBAL VARIABLES
         ******************************************************************************************/
        /****/
        private final List<Fragment> fragList = new ArrayList<>();
        private final List<String> fragTitleList = new ArrayList<>();

        /*******************************************************************************************
         * CONSTRUCTORS
         ******************************************************************************************/
        /****/
        public ViewPagerAdapter(FragmentManager fragManager) {super(fragManager);}

        /*******************************************************************************************
         * OVERRIDE METHODS
         ******************************************************************************************/
        /****/
        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        /****/
        @Override
        public int getCount() {
            return fragList.size();
        }

        /****/
        @Override
        public CharSequence getPageTitle(int position) {
            return fragTitleList.get(position);
        }

        /*******************************************************************************************
         * PUBLIC METHODS
         ******************************************************************************************/
        /****/
        public void addFragment(int stringResId, Fragment fragment) {
            String title = getResources().getString(stringResId);

            fragTitleList.add(title);
            fragList.add(fragment);
        }

        /****/
        public int getPosition(int fragTitleStrRes) {
            String title = getResources().getString(fragTitleStrRes);

            return fragTitleList.indexOf(title);
        }
    }
}

//    public static final int PICK_WEEK_REQUEST = 1;
//
//    private int currentYear;
//    private int currentWeek;
//    private DayName currentDay;
//
//    private DaysAdapter mainAdapter;
//    private ToDoAdapter displayAdapter;
//
//    private RelativeLayout sliderHeader;
//    private TextView yearText, weekNumText, emptyList, toDoDate;
//    private ListView dayListView, toDoListView;
//    private Button toDoAdd;
//    private SlidingUpPanelLayout slider;

//        findViewsById();
//        setUpAdapters();
//
//        sliderHeader.getViewTreeObserver().addOnGlobalLayoutListener(this);
//
//        onActivityResult(PICK_WEEK_REQUEST, RESULT_FIRST_RUN, null);
//
//        dayListView.setOnItemClickListener(this);
//
//        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

//        });
//
//        toDoAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch(currentDay) {
//                    case MON: case TUE: case WED: case THU: case FRI:
//                        DialogRouter.instantiateInputDialog(getActivity(), DialogRouter.TYPE_WEEKDAY);
//                        break;
//                    case SAT: case SUN:
//                        DialogRouter.instantiateInputDialog(getActivity(), DialogRouter.TYPE_WEEKEND);
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onGlobalLayout() {
//        int width = sliderHeader.getWidth();
//        int widthAspect = 16;
//        int heightAspect = 9;
//
//        int orientation = getResources().getConfiguration().orientation;
//        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
//            sliderHeader.setMinimumHeight(width * heightAspect / widthAspect);
//        }
//
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            sliderHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//        } else {
//            sliderHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        switch(parent.getId()) {
//            case R.id.main_list_daily:
//                getToDoList(position);
//                break;
//        }
//    }
//
//    private void getToDoList(int position) {
//        Day day = mainAdapter.getItem(position);
//
//        if(day.getType() != DaysAdapter.TYPE_DIVIDER) {
//            toDoAdd.setVisibility(View.VISIBLE);
//
//            toDoDate.setText(DateCalcs.formatDate(DateCalcs.FULL_DATE, day.getDate()));
//
//            currentDay = day.getDay();
//
//            displayAdapter.buildList(currentYear, currentWeek, currentDay);
//            displayAdapter.notifyDataSetChanged();
//
//            slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//        }
//    }
//
//    private void findViewsById() {
//        yearText = (TextView)findViewById(R.id.main_text_year);
//        weekNumText = (TextView)findViewById(R.id.main_text_weekNum);
//        emptyList = (TextView)findViewById(R.id.slider_text_emptyList);
//        toDoDate = (TextView)findViewById(R.id.slider_text_date);
//
//        toDoAdd = (Button)findViewById(R.id.slider_butn_toDoAdd);
//
//        slider = (SlidingUpPanelLayout)findViewById(R.id.main_sliding_layout);
//
//        sliderHeader = (RelativeLayout)findViewById(R.id.slider_rel_header);
//
//        dayListView = (ListView)findViewById(R.id.main_list_daily);
//        toDoListView = (ListView)findViewById(R.id.slider_list_toDoList);
//    }
//
//    private void buildWeek(int year, int week) {
//        yearText.setText(String.valueOf(year).substring(2, 4));
//        weekNumText.setText(DateCalcs.addZeroToNum(week));
//
//        mainAdapter.finishBuildingWeek(year, week);
//    }
//
//    private void setUpAdapters() {
//        mainAdapter = new DaysAdapter(getActivity(), getContext());
//        dayListView.setAdapter(mainAdapter);
//
//        displayAdapter = new ToDoAdapter(getActivity(), getContext());
//        toDoListView.setAdapter(displayAdapter);
//        toDoListView.setEmptyView(emptyList);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == PICK_WEEK_REQUEST) {
//            if(resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//
//                currentYear = extras.getInt(DateCalcs.KEY_YEAR);
//                currentWeek = extras.getInt(DateCalcs.KEY_WEEK);
//            } else {
//                currentYear = DateCalcs.getCurrentYear();
//                currentWeek = DateCalcs.getCurrentWeek();
//            }
//        }
//
//        buildWeek(currentYear, currentWeek);
//    }
