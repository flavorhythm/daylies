package fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import adapters.DaysAdapter;
import models.Day;
import utils.DateCalcs;

/**
 * Created by zyuki on 3/29/2016.
 */
public class DisplayDaysFragment extends Fragment implements AdapterView.OnItemClickListener/*AdapterView.OnItemClickListener, ViewTreeObserver.OnGlobalLayoutListener*/ {
//    public static final int PICK_WEEK_REQUEST = 1;
//
//    private int currentYear;
//    private int currentWeek;
//    private DayName currentDay;
//
    private DaysAdapter daysAdapter;
//    private ToDoAdapter displayAdapter;
//
//    private RelativeLayout sliderHeader;
//    private TextView yearText, weekNumText, emptyList, toDoDate;
    private GridView dayGridView;
//    private Button toDoAdd;
//    private SlidingUpPanelLayout slider;

    public DisplayDaysFragment() {}

    //Runs before onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysAdapter = new DaysAdapter(getActivity(), getContext());

        if(!getArguments().isEmpty()) {
            buildWeek(
                    getArguments().getInt(DateCalcs.KEY_YEAR),
                    getArguments().getInt(DateCalcs.KEY_WEEK)
            );
        }

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.fragment_days;
        View customView = inflater.inflate(layoutRes, container, false);

        dayGridView = (GridView)customView.findViewById(R.id.fragDays_grid);
        dayGridView.setAdapter(daysAdapter);

        dayGridView.setOnItemClickListener(this);

        return customView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Day day = daysAdapter.getItem(position);

        if(day.getType() != DaysAdapter.TYPE_DIVIDER) {
            ((MainActivity)getActivity()).showToDoList(day);
        }
    }


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

    public void buildWeek(int currentYear, int currentWeek) {
//        yearText.setText(String.valueOf(year).substring(2, 4));
//        weekNumText.setText(DateCalcs.addZeroToNum(week));

        daysAdapter.finishBuildingWeek(currentYear, currentWeek);
    }
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

}
