package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
    private LayoutInflater inflater;
    private List<Day> dayList = new ArrayList<>();

    public MainAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {return dayList.size();}

    @Override
    public Day getItem(int position) {return dayList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        int layoutRes = R.layout.main_row;
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = inflater.inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.month = (TextView)row.findViewById(R.id.mainRow_text_month);
            viewHolder.dayOfMonth = (TextView)row.findViewById(R.id.mainRow_text_dayOfMonth);
            viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.mainRow_text_dayOfWeek);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        Day day = getItem(position);

        viewHolder.month.setText(DateCalcs.formatDate(DateCalcs.MONTH, day.getDate()));
        viewHolder.dayOfMonth.setText(DateCalcs.formatDate(DateCalcs.DAY_OF_MONTH, day.getDate()));
        viewHolder.dayOfWeek.setText(day.getDay().toString());

        return row;
    }

    public void finishBuildingWeek(int year, int weekNum) {
        dayList.clear();

        final int daysInWeek = 7;

        Calendar firstDateOfWeek = Calendar.getInstance();
        firstDateOfWeek.clear();
        firstDateOfWeek.set(Calendar.YEAR, year);
        firstDateOfWeek.set(Calendar.WEEK_OF_YEAR, weekNum);

        for(int i = 0; i < daysInWeek; i++) {
            Day day = new Day();

            firstDateOfWeek.add(Calendar.DAY_OF_YEAR, 1);

            day.setDate(firstDateOfWeek.getTimeInMillis());
            day.setYear(year);
            day.setWeekNum(weekNum);
            day.setDay(DayName.values()[i]);

            dayList.add(day);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        TextView month;
        TextView dayOfMonth;
        TextView dayOfWeek;
    }
}
