package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zyuki.daylies.ApplicationDatabase;
import com.example.zyuki.daylies.R;

import java.util.ArrayList;
import java.util.List;

import data.DataAccessObject;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

/**
 * Created by zyuki on 3/10/2016.
 */
public class DisplayAdapter extends BaseAdapter {
    public static final int TYPE_COUNT = 2;
    public static final int TYPE_DIVIDER = 0;
    public static final int TYPE_CONTENT = 1;

    private List<ToDo> toDoList = new ArrayList<>();
    private LayoutInflater inflater;

    private DataAccessObject dataAccess;

    public DisplayAdapter(Activity activity, Context context) {
        inflater = LayoutInflater.from(activity);
        dataAccess = ((ApplicationDatabase)context).dataAccess;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType() == ToDo.TYPE_HEADER ? TYPE_DIVIDER : TYPE_CONTENT;
    }

    @Override
    public int getViewTypeCount() {return TYPE_COUNT;}

    @Override
    public int getCount() {return toDoList.size();}

    @Override
    public ToDo getItem(int position) {return toDoList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        int headerLayoutRes = R.layout.display_header_row;
        int contentLayoutRes = R.layout.display_content_row;

        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            viewHolder = new ViewHolder();

            switch(getItemViewType(position)) {
                case TYPE_DIVIDER:
                    row = inflater.inflate(headerLayoutRes, parent, false);
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_header);
                    break;
                case TYPE_CONTENT:
                    row = inflater.inflate(contentLayoutRes, parent, false);
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_content);
                    break;
            }

            assert row != null;
            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        ToDo item = getItem(position);

        viewHolder.textItem.setText(item.getItem());

        return row;
    }

    public void buildToDoList(int year, int weekNum, DayName day) {
        toDoList.clear();

        switch(day) {
            case MON: case TUE: case WED: case THU: case FRI:
                buildWeekDayList(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
                break;
            case SAT: case SUN:
                buildWeekEndList(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
                break;
        }
    }

    private void buildWeekDayList(List<ToDo> toDosFromDb) {
        List<ToDo> lunchToDos = new ArrayList<>();
        List<ToDo> dailyToDos = new ArrayList<>();

        for(ToDo item : toDosFromDb) {
            switch(item.getType()) {
                case ToDo.TYPE_LUNCH:
                    lunchToDos.add(item);
                    break;
                case ToDo.TYPE_TODO:
                    dailyToDos.add(item);
                    break;
            }
        }

        if(!lunchToDos.isEmpty()) {
            toDoList.add(buildHeader("Lunchtime"));
            toDoList.addAll(lunchToDos);
        }
        if(!dailyToDos.isEmpty()) {
            toDoList.add(buildHeader("After Work"));
            toDoList.addAll(dailyToDos);
        }
    }

    private void buildWeekEndList(List<ToDo> toDosFromDb) {
        List<ToDo> dailyToDos = new ArrayList<>();

        for(ToDo item : toDosFromDb) {
            dailyToDos.add(item);
        }

        if(!dailyToDos.isEmpty()) {
            toDoList.add(buildHeader("Daily To Dos"));
            toDoList.addAll(dailyToDos);
        }
    }

    private ToDo buildHeader(String headerText) {
        return new ToDo(ToDo.TYPE_HEADER, headerText);
    }

    private static class ViewHolder {
        TextView textItem;
    }
}
