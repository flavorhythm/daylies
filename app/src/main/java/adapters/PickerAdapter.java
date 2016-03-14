package adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zyuki.daylies.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.WeeksInYear;
import utils.DateCalcs;

/**
 * Created by zyuki on 2/26/2016.
 */
public class PickerAdapter extends BaseAdapter {
    private static final int WEEKS_IN_YEAR = 52;

    private LayoutInflater inflater;
    private List<WeeksInYear> weeksList = new ArrayList<>();

    public PickerAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {return weeksList.size();}

    @Override
    public WeeksInYear getItem(int position) {return weeksList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        int layoutRes = R.layout.picker_row;
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = inflater.inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.weekNum = (TextView)row.findViewById(R.id.pickerRow_text_weekNum);
            viewHolder.date = (TextView)row.findViewById(R.id.pickerRow_text_date);
            viewHolder.current = (TextView)row.findViewById(R.id.pickerRow_text_current);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        WeeksInYear week = getItem(position);

        viewHolder.weekNum.setText(DateCalcs.addZeroToNum(week.getWeekNum()));
        viewHolder.date.setText(DateCalcs.formatDate(week.getDate()));

		//need to explicitly declare if/else statements because the view is recycled so properties
		//set to previous views will randomly be recycled into future views
		//Changed from setting view to gone/visible to changing text content
        if(DateCalcs.isCurrentYearWeek(week.getDate())) {
            viewHolder.current.setText("(current)");
        } else {
            viewHolder.current.setText("");
        }

        return row;
    }

    public void buildWeeksInYear(int displayYear) {
        weeksList.clear();

        Calendar thisYear = Calendar.getInstance();
        thisYear.set(Calendar.YEAR, displayYear);

        for(int weekOfYear = 1; weekOfYear <= WEEKS_IN_YEAR; weekOfYear++) {
            WeeksInYear week = new WeeksInYear();
            thisYear.set(Calendar.WEEK_OF_YEAR, weekOfYear);

            week.setWeekNum(weekOfYear);
            week.setDate(thisYear.getTimeInMillis());

            if(thisYear.get(Calendar.YEAR) != displayYear) {
                thisYear.set(Calendar.YEAR, displayYear);
            }

            weeksList.add(week);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        TextView weekNum;
        TextView date;
        TextView current;
    }
}
