package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import adapters.ToDoAdapter;
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
public class DisplayTodosFragment extends FragmentActivity implements View.OnClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private FloatingActionButton fab;
    private ListView todoList;
    private ToDoAdapter todoAdapter;
    private TextView todoDate;

    private int currentYear, currentWeek;
    private DayName currentDay;

    private DataFromTodos dataPass;

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_main);

        currentYear = DateCalcs.getCurrentYear();
        currentWeek = DateCalcs.getCurrentWeek();

        findViewByIds();

        todoAdapter = new ToDoAdapter(DisplayTodosFragment.this);
        todoList.setAdapter(todoAdapter);
        todoList.setEmptyView(findViewById(R.id.slider_text_emptyList));

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (todoAdapter.getItemViewType(position) != Constant.Adapter.TYPE_DIVIDER) {
                    DialogRouter.instantiateDeleteDialog(DisplayTodosFragment.this, position);
                }
                return true;
            }
        });

        fab.setOnClickListener(this);
    }

    /****/
    @Override
    public void onClick(View v) {
        if(currentDay != null) {
            switch(currentDay) {
                case MON: case TUE: case WED: case THU: case FRI:
                    DialogRouter.instantiateInputDialog(
                            DisplayTodosFragment.this,
                            Constant.Fragment.DAY_TYPE_WEEKDAY
                    );
                    break;
                case SAT: case SUN:
                    DialogRouter.instantiateInputDialog(
                            DisplayTodosFragment.this,
                            Constant.Fragment.DAY_TYPE_WEEKEND
                    );
                    break;
            }
        }
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
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

        dataPass.notifyDaysFrag();
        dataPass.notifyWeeksFrag();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /****/
    public void addDailyItem(String newToDo, int itemType) {
        String yearWeekNum = DateCalcs.buildDateString(currentYear, currentWeek, currentDay);

        todoAdapter.add(new ToDo(yearWeekNum, itemType, newToDo));
        todoAdapter.notifyDataSetChanged();

        dataPass.notifyDaysFrag();
        dataPass.notifyWeeksFrag();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /****/
    public void removeItem(int position) {
        todoAdapter.removeItem(position);
        todoAdapter.notifyDataSetChanged();

        dataPass.notifyDaysFrag();
        dataPass.notifyWeeksFrag();

        notifyDaysFrag();
        notifyWeeksFrag();
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private void findViewByIds() {
        fab = (FloatingActionButton)findViewById(R.id.slider_fab_addNewItem);
        todoList = (ListView)findViewById(R.id.slider_list_toDoList);
        todoDate = (TextView)findViewById(R.id.slider_text_date);
    }

    //TODO: implement interface to MainActivity so data can be passed from here to the main
    public interface DataFromTodos {
        void notifyDaysFrag();
        void notifyWeeksFrag();
        void expandPanel();
    }
}
