package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zyuki.daylies.R;

import java.util.ArrayList;

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
public class DisplayTodosFragment extends Fragment implements View.OnClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private FloatingActionButton fab;
    private ListView todoList;
    private ToDoAdapter todoAdapter;
    private TextView todoDate;

    private Callback dataPass;

    private View customView;
    private SharedPreferences prefs;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public DisplayTodosFragment() {}

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that initializes an inner interface object**/
    /**This method runs as soon as the fragment is attached to a ViewGroup**/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (Callback)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

    }

    /****/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.fragment_todos;
        customView = inflater.inflate(layoutRes, container, false);
        findViewByIds();

        todoAdapter = new ToDoAdapter(getActivity());
        todoList.setAdapter(todoAdapter);
        todoList.setEmptyView(customView.findViewById(R.id.fragTodos_text_emptyList));

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (todoAdapter.getItemViewType(position) != Constant.Adapter.TYPE_DIVIDER) {
                    DialogRouter.instantiateDeleteDialog((AppCompatActivity)getActivity(), position);
                }
                return true;
            }
        });

        fab.setOnClickListener(this);

        return customView;
    }

    /****/
    @Override
    public void onClick(View v) {
        DayName currentDay = DayName.values()[getDay()];

        if(currentDay != null) {
            switch(currentDay) {
                case MON: case TUE: case WED: case THU: case FRI:
                    DialogRouter.instantiateInputDialog(
                            (AppCompatActivity)getActivity(),
                            Constant.Fragment.DAY_TYPE_WEEKDAY
                    );
                    break;
                case SAT: case SUN:
                    DialogRouter.instantiateInputDialog(
                            (AppCompatActivity)getActivity(),
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
        String date = day.getDay().toString() + ", ";
        date += DateCalcs.formatDate(Constant.Util.FORMAT_FULL_DATE, day.getDate());

        todoDate.setText(date);

        todoAdapter.buildList(getYear(), getWeek(), DayName.values()[getDay()]);
        todoAdapter.notifyDataSetChanged();
    }

    /****/
    public void addLunchItem(String newToDo) {
        String yearWeekNum = getYearWeekNum();

        todoAdapter.add(new ToDo(yearWeekNum, Constant.Model.CONTENT_LUNCH, newToDo));
        todoAdapter.notifyDataSetChanged();

        boolean hasTodos = todoAdapter.getCount() > 0;
        dataPass.updateFrags(hasTodos);
    }

    /****/
    public void addDailyItem(String newToDo, int itemType) {
        String yearWeekNum = getYearWeekNum();

        todoAdapter.add(new ToDo(yearWeekNum, itemType, newToDo));
        todoAdapter.notifyDataSetChanged();

        boolean hasTodos = todoAdapter.getCount() > 0;
        dataPass.updateFrags(hasTodos);
    }

    /****/
    public void removeItem(int position) {
        todoAdapter.removeItem(position);
        todoAdapter.notifyDataSetChanged();

        boolean hasTodos = todoAdapter.getCount() > 0;
        dataPass.updateFrags(hasTodos);
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private void findViewByIds() {
        fab = (FloatingActionButton)customView.findViewById(R.id.fragTodos_fab_addNewItem);
        todoList = (ListView)customView.findViewById(R.id.fragTodos_list_toDoList);
        todoDate = (TextView)customView.findViewById(R.id.fragTodos_text_date);
    }

    /****/
    private String getYearWeekNum() {
        return DateCalcs.buildDateString(
                getYear(),
                getWeek(),
                DayName.values()[getDay()]
        );
    }

    /****/
    private int getYear() {return prefs.getInt(Constant.Prefs.PREF_KEY_YEAR, Constant.ERROR);}

    /****/
    private int getWeek() {return prefs.getInt(Constant.Prefs.PREF_KEY_WEEK, Constant.ERROR);}

    /****/
    private int getDay() {return prefs.getInt(Constant.Prefs.PREF_KEY_DAY, Constant.ERROR);}

    /***********************************************************************************************
     * INNER CLASSES & INTERFACES
     **********************************************************************************************/
    /****/
    public interface Callback {
        void updateFrags(boolean hasTodos);
    }
}
