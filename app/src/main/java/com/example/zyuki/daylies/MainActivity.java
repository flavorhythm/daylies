package com.example.zyuki.daylies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import data.DataAccessObject;
import fragments.DisplayDaysFragment;
import fragments.DialogRouter;
import fragments.DisplayTodosFragment;
import fragments.DisplayWeeksFragment;
import models.DayName;
import utils.Constant;
import utils.DateCalcs;
/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
//TODO: need to prevent slider from sliding when clicked
//TODO: move slider elements into it's own fragment?
public class MainActivity extends AppCompatActivity implements DisplayWeeksFragment.DataFromWeeks,
        TabLayout.OnTabSelectedListener, DisplayTodosFragment.DataFromTodos {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private SlidingUpPanelLayout slider;

    private DisplayDaysFragment daysFrag;
    private DisplayWeeksFragment weeksFrag;

    private SharedPreferences prefs;

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that runs once this Activity's lifecycle begins**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putInt(Constant.Prefs.PREF_KEY_YEAR, DateCalcs.getCurrentYear());
        prefEdit.putInt(Constant.Prefs.PREF_KEY_WEEK, DateCalcs.getCurrentWeek());
        prefEdit.putInt(Constant.Prefs.PREF_KEY_DAY, DateCalcs.getCurrentDay());
        prefEdit.apply();

        findViewByIds();

        setSupportActionBar(toolbar);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);

        //TODO: add code here to prevent slider dragging?
    }

    /**Override method that creates the menu from layout >> menu_main.xml**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
    }

    /**Override method that routes the selected item menu to its correct corresponding action**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // A switch to route the selected items to the correct action
        switch(id) {
            case R.id.select_year:
                DialogRouter.instantiatePickerDialog(MainActivity.this, getYear(), getWeek());
                break;
            default:
                break;
        }

        // Returns the Super call of this method
        return super.onOptionsItemSelected(item);
    }

    /**Override method that is implemented from fragment.DisplayTodosFragment.DataFromTodos**/
    /**This method rebuilds the week/year lists whenever there's a change in the display year**/
    /**Used in override method fragments.DialogYearPickFragment.onClick**/
    @Override
    public void dataFromWeeks() {
        daysFrag.buildWeek();
        weeksFrag.buildYear();
    }

    /**Override method for when the back button is pressed**/
    @Override
    public void onBackPressed() {
        //If the slider wasn't retracted, collapse when the back button is pressed
        if(!retractSlider()) {super.onBackPressed();}
    }

    /**Override method that is implemented from TabLayout.OnTabSelectedListener**/
    /**Used to retract slider when tab is selected**/
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Retracts slider
        retractSlider();
        //Sets the viewPager's page (fragment) to the corresponding selected tab
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**Override method that is implemented from TabLayout.OnTabSelectedListener**/
    /**Required to override but not used here**/
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    /**Override method that is implemented from TabLayout.OnTabSelectedListener**/
    /**Used to retract slider when tab is reselected**/
    @Override
    public void onTabReselected(TabLayout.Tab tab) {retractSlider();}

    /**Override method that is implemented from fragments.DisplayTodosFragment.DataFromTodos**/
    /**Used in fragments.DisplayTodosFragment in the add/delete item methods**/
    /**Used to update the state of the days (within the week display) and weeks (within the
     * year display) with a mark that shows the user said day/week has todos**/
    @Override
    public void updateFrags(boolean daysHasTodos) {
        updateDaysFrag(daysHasTodos);
        updateWeeksFrag();
    }

    /**Override method that is implemented from fragments.DisplayTodosFragment.DataFromTodos**/
    /**Automatically expands the panel whenever a day is selected**/
    @Override
    public void expandPanel() {slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);}

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public void changePage(int goToThisTitle) {
        int position = pagerAdapter.getPosition(goToThisTitle);
        TabLayout.Tab goToTab = tabLayout.getTabAt(position);

        if(goToTab != null) {goToTab.select();}
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
    }

    /****/
    private void setupViewPager() {
        daysFrag = new DisplayDaysFragment();
        weeksFrag = new DisplayWeeksFragment();

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(R.string.daysFragTitle, daysFrag);
        pagerAdapter.addFragment(R.string.weeksFragTitle, weeksFrag);

        viewPager.setAdapter(pagerAdapter);
    }

    /****/
    private void updateDaysFrag(boolean hasTodos) {
        int dayPos = prefs.getInt(Constant.Prefs.PREF_KEY_DAY, Constant.ERROR);
        DayName day = DayName.values()[dayPos];
        daysFrag.notifyDataSetChanged(day, hasTodos);
    }

    /****/
    private void updateWeeksFrag() {
        DataAccessObject dataAccess = ((ApplicationDatabase)getApplicationContext()).dataAccess;
        boolean hasTodos = dataAccess.weekHasTodos(
                getYear(),
                getWeek()
        );
        weeksFrag.notifyDataSetChanged(hasTodos);
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

    private int getYear() {return prefs.getInt(Constant.Prefs.PREF_KEY_YEAR, Constant.ERROR);}

    private int getWeek() {return prefs.getInt(Constant.Prefs.PREF_KEY_WEEK, Constant.ERROR);}

    /***********************************************************************************************
     * INNER CLASSES & INTERFACES
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

//    class CustomSlidingPanel extends SlidingUpPanelLayout {
//        public CustomSlidingPanel() {}
//
//
//    }
}