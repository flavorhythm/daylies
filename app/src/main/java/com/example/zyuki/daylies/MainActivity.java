package com.example.zyuki.daylies;

import android.os.Bundle;
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
import utils.Constant;
import utils.DateCalcs;
/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
//TODO: need to prevent slider from sliding when clicked
//TODO: move slider elements into it's own fragment?
public class MainActivity extends AppCompatActivity
        implements DisplayWeeksFragment.DataFromWeeks, View.OnClickListener, TabLayout.OnTabSelectedListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private SlidingUpPanelLayout slider;
    private FloatingActionButton fab;
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

        todoAdapter = new ToDoAdapter(MainActivity.this);
        todoList.setAdapter(todoAdapter);
        todoList.setEmptyView(findViewById(R.id.slider_text_emptyList));

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (todoAdapter.getItemViewType(position) != Constant.Adapter.TYPE_DIVIDER) {
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
                    DialogRouter.instantiateInputDialog(MainActivity.this, Constant.Fragment.DAY_TYPE_WEEKDAY);
                    break;
                case SAT: case SUN:
                    DialogRouter.instantiateInputDialog(MainActivity.this, Constant.Fragment.DAY_TYPE_WEEKEND);
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
        date += DateCalcs.formatDate(Constant.Util.FORMAT_FULL_DATE, day.getDate());

        todoDate.setText(date);

        todoAdapter.buildList(currentYear, currentWeek, currentDay);
        todoAdapter.notifyDataSetChanged();

        slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    /****/
    public void addLunchItem(String newToDo) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        todoAdapter.add(new ToDo(yearWeekNum, Constant.Model.CONTENT_LUNCH, newToDo));
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

        args.putInt(Constant.Fragment.BUNDLE_KEY_YEAR, currentYear);
        args.putInt(Constant.Fragment.BUNDLE_KEY_WEEK, currentWeek);

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