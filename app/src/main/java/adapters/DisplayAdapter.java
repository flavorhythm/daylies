package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zyuki.daylies.ApplicationDatabase;
import com.example.zyuki.daylies.MainActivity;
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

    private List<ToDo> toDoList;
    private LayoutInflater inflater;
    private DataAccessObject dataAccess;

    private Activity activity;

    private int currentYear, currentWeek;
    private DayName currentDay;

    public DisplayAdapter(Activity activity, Context context) {
        this.activity = activity;
        dataAccess = ((ApplicationDatabase)context).dataAccess;
        inflater = LayoutInflater.from(activity);

        toDoList = new ArrayList<>();
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
    public View getView(final int position, View row, ViewGroup parent) {
        int headerLayoutRes = R.layout.display_header_row;
        int contentLayoutRes = R.layout.display_content_row;

        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            viewHolder = new ViewHolder();

            switch(getItemViewType(position)) {
                case TYPE_DIVIDER:
                    row = inflater.inflate(headerLayoutRes, parent, false);
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_header);
                    viewHolder.textItem.setClickable(false);
                    break;
                case TYPE_CONTENT:
                    row = inflater.inflate(contentLayoutRes, parent, false);
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_content);
                    viewHolder.deleteBtn = (Button)row.findViewById(R.id.display_butn_delete);
                    break;
            }

            assert row != null;
            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        ToDo item = getItem(position);

        viewHolder.textItem.setText(item.getItem());

        final int itemId = item.getId();
        if(getItemViewType(position) == TYPE_CONTENT) {
            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(itemId, position);
                }
            });
        }

        return row;
    }

    public void add(ToDo item) {
        dataAccess.putToDoItem(item);
        buildList(currentYear, currentWeek, currentDay);
    }

    public void removeItem(final int dbId, final int listPos) {
        dataAccess.deleteToDoItem(dbId);
        toDoList.remove(listPos);

        final int maxHeaderCount = 2;
        int headerCount = 0;
        if(toDoList.size() <= maxHeaderCount) {
            for(ToDo item : toDoList) {
                if(item.getType() == ToDo.TYPE_HEADER) {
                    headerCount++;
                }
            }
        }

        if(headerCount == toDoList.size()) {
            toDoList.clear();
        }

        ((MainActivity)activity).notifyDisplayAdapter();
    }

    public void buildList(int year, int weekNum, DayName day) {
        this.currentYear = year;
        this.currentWeek = weekNum;
        this.currentDay = day;
        toDoList.clear();

        switch(day) {
            case MON: case TUE: case WED: case THU: case FRI:
                buildWeekDayList(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
                break;
            case SAT: case SUN:
                buildWeekEndList(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
                break;
        }

        ((MainActivity)activity).notifyDisplayAdapter();
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
        TextView deleteBtn;
    }
}
