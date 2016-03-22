package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyuki.daylies.ApplicationDatabase;
import com.example.zyuki.daylies.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import data.DataAccessObject;
import models.Day;
import models.DayName;
import utils.DateCalcs;

/**
 * Created by zyuki on 2/26/2016.
 */
public class MainAdapter extends BaseAdapter {
    public static final int TYPE_COUNT = 2;
    public static final int TYPE_DIVIDER = 0;
    public static final int TYPE_CONTENT = 1;

    private LayoutInflater inflater;
    private List<Day> dayList = new ArrayList<>();

    private Context context;

    public MainAdapter(Activity activity, Context context) {
        inflater = LayoutInflater.from(activity);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {return getItem(position).getType();}

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {return dayList.size();}

    @Override
    public Day getItem(int position) {return dayList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        int dividerLayout = R.layout.main_divider_row;
        int contentLayout = R.layout.main_content_row;

        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            viewHolder = new ViewHolder();

            switch(getItemViewType(position)) {
                case TYPE_DIVIDER:
                    row = inflater.inflate(dividerLayout, parent, false);

                    viewHolder.month = (TextView)row.findViewById(R.id.mainRow_text_month);
                    break;
                case TYPE_CONTENT:
                    row = inflater.inflate(contentLayout, parent, false);

                    viewHolder.entireRow = (LinearLayout)row.findViewById(R.id.mainRow_linear_entireRow);
                    viewHolder.dayOfMonth = (TextView)row.findViewById(R.id.mainRow_text_dayOfMonth);
                    viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.mainRow_text_dayOfWeek);

                    row.setTag(viewHolder);
                    break;
            }
        } else {viewHolder = (ViewHolder)row.getTag();}

        Day day = getItem(position);

        switch(getItemViewType(position)) {
            case TYPE_DIVIDER:
                viewHolder.month.setText(DateCalcs.formatDate(Calendar.MONTH, day.getDate()));
                break;
            case TYPE_CONTENT:
                viewHolder.dayOfMonth.setText(DateCalcs.formatDate(Calendar.DAY_OF_MONTH, day.getDate()));
                viewHolder.dayOfWeek.setText(day.getDay().toString());

                if(DateCalcs.isCurrentDay(day.getDate())) {
                    int whiteTextColor = ContextCompat.getColor(context, R.color.whiteText);

                    viewHolder.entireRow.setBackgroundResource(R.color.colorPrimary);
                    viewHolder.dayOfMonth.setTextColor(whiteTextColor);
                    viewHolder.dayOfWeek.setTextColor(whiteTextColor);
                } else {
                    int blackTextColor = ContextCompat.getColor(context, R.color.darkText);

                    viewHolder.entireRow.setBackgroundColor(Color.TRANSPARENT);
                    viewHolder.dayOfMonth.setTextColor(blackTextColor);
                    viewHolder.dayOfWeek.setTextColor(blackTextColor);
                }
                break;
        }

        return row;
    }

    public void finishBuildingWeek(int year, int weekNum) {
        dayList.clear();

        final int daysInWeek = 7;

        Calendar firstDateOfWeek = Calendar.getInstance();
        firstDateOfWeek.clear();
        firstDateOfWeek.set(Calendar.YEAR, year);
        firstDateOfWeek.set(Calendar.WEEK_OF_YEAR, weekNum);

        addMonth(firstDateOfWeek);

        for(int i = 0; i < daysInWeek; i++) {
            Day day = new Day();

            int monthBefore = firstDateOfWeek.get(Calendar.MONTH);
            firstDateOfWeek.add(Calendar.DAY_OF_YEAR, 1);
            int monthAfter = firstDateOfWeek.get(Calendar.MONTH);

            day.setType(TYPE_CONTENT);
            day.setDate(firstDateOfWeek.getTimeInMillis());
            day.setYear(year);
            day.setDay(DayName.values()[i]);

            if(monthBefore != monthAfter) {addMonth(firstDateOfWeek);}

            dayList.add(day);
            notifyDataSetChanged();
        }
    }

    //TODO: find a way to only show the month once
    private void addMonth(Calendar theMonth) {
        Calendar nextDayCheck = Calendar.getInstance();
        nextDayCheck.add(Calendar.DAY_OF_YEAR, 1);

        Day day = new Day();

        day.setType(TYPE_DIVIDER);
        day.setDate(theMonth.getTimeInMillis());

        dayList.add(day);
    }

    private static class ViewHolder {
        LinearLayout entireRow;
        TextView dayOfMonth;
        TextView dayOfWeek;

        TextView month;
    }
}
