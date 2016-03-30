package com.example.zyuki.daylies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import fragments.DaysDisplayFragment;
import fragments.WeeksDisplayFragment;

public class MainActivity extends AppCompatActivity implements WeeksDisplayFragment.OnDataPass {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    //TODO: need to implement drag-up panel
    //TODO: increase header size
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIds();

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void findViewByIds() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        viewPager = (ViewPager)findViewById(R.id.main_viewPager);
        tabLayout = (TabLayout)findViewById(R.id.main_tabLayout);
    }

    private void setupViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(R.string.daysFragTitle, new DaysDisplayFragment());
        pagerAdapter.addFragment(R.string.weeksFragTitle, new WeeksDisplayFragment());

        viewPager.setAdapter(pagerAdapter);
    }

    //TODO: tab text highlight doesn't change
    public void changePage(int goToThisTitle) {
        int position = pagerAdapter.getPosition(goToThisTitle);
        viewPager.setCurrentItem(position);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragList = new ArrayList<>();
        private final List<String> fragTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragManager) {
            super(fragManager);
        }

        public void addFragment(int stringResId, Fragment fragment) {
            String title = getResources().getString(stringResId);

            fragTitleList.add(title);
            fragList.add(fragment);
        }

        public int getPosition(int fragTitleStrRes) {
            String title = getResources().getString(fragTitleStrRes);

            return fragTitleList.indexOf(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragTitleList.get(position);
        }
    }

    @Override
    public void onDataPass(int year, int week) {
        ((DaysDisplayFragment)pagerAdapter.getItem(0)).buildWeek(year, week);
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
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if(displayAdapter.getItem(position).getType() != ToDo.TYPE_HEADER) {
//                    DialogRouter.instantiateDeleteDialog(getActivity(), position);
//                }
//                return true;
//            }
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

//    public void notifyDisplayAdapter() {
//        displayAdapter.notifyDataSetChanged();
//    }
//
//    public void putLunchItem(String newToDo) {
//        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);
//
//        displayAdapter.add(new ToDo(yearWeekNum, ToDo.TYPE_LUNCH, newToDo));
//    }
//
//    public void putDailyItem(String newToDo) {
//        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);
//
//        displayAdapter.add(new ToDo(yearWeekNum, ToDo.TYPE_TODO, newToDo));
//    }
//
//    public void deleteItem(int position) {
//        displayAdapter.removeItem(position);
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
//                currentYear = extras.getInt(DateCalcs.YEAR_KEY);
//                currentWeek = extras.getInt(DateCalcs.WEEK_NUM_KEY);
//            } else {
//                currentYear = DateCalcs.getCurrentYear();
//                currentWeek = DateCalcs.getCurrentWeek();
//            }
//        }
//
//        buildWeek(currentYear, currentWeek);
//    }
