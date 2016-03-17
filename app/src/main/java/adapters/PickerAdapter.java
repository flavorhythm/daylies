package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private LayoutInflater inflater;
    private List<WeeksInYear> weeksList = new ArrayList<>();

    private Context context;

    public PickerAdapter(Activity activity, Context context) {
        inflater = LayoutInflater.from(activity);
        this.context = context;
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

            viewHolder.weekText = (TextView)row.findViewById(R.id.pickerRow_text_weekText);
            viewHolder.weekNum = (TextView)row.findViewById(R.id.pickerRow_text_weekNum);
            viewHolder.date = (TextView)row.findViewById(R.id.pickerRow_text_date);
            viewHolder.entireRow = (LinearLayout)row.findViewById(R.id.pickerRow_linear_entireRow);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        WeeksInYear week = getItem(position);

        viewHolder.weekNum.setText(DateCalcs.addZeroToNum(week.getWeekNum()));
        viewHolder.date.setText(DateCalcs.formatDate(DateCalcs.FULL_DATE, week.getDate()));

		//need to explicitly declare if/else statements because the view is recycled so properties
		//set to previous views will randomly be recycled into future views
		//Changed from setting view to gone/visible to changing text content
        if(DateCalcs.isCurrentYearWeek(week.getDate())) {
            int whiteTextColor = ContextCompat.getColor(context, R.color.whiteText);

            viewHolder.entireRow.setBackgroundResource(R.color.colorPrimary);
            viewHolder.weekText.setTextColor(whiteTextColor);
            viewHolder.weekNum.setTextColor(whiteTextColor);
            viewHolder.date.setTextColor(whiteTextColor);
        } else {
            int blackTextColor = ContextCompat.getColor(context, R.color.darkText);

            viewHolder.entireRow.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.weekText.setTextColor(blackTextColor);
            viewHolder.weekNum.setTextColor(blackTextColor);
            viewHolder.date.setTextColor(blackTextColor);
        }

        return row;
    }

    //TODO: Need to prevent Week 53 from showing for some years
    public void buildWeeksInYear(Calendar endOfLastYear, int displayYear) {
        weeksList.clear();

        final int daysInWeek = 7;
        int weekOfYear = 1;
        int currentYear = 0;

        Calendar thisYear = Calendar.getInstance();
        thisYear.set(Calendar.YEAR, displayYear);
        thisYear.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisYear.set(Calendar.WEEK_OF_YEAR, weekOfYear);

        if(thisYear.before(endOfLastYear)) {
            final int addOneWeek = 1;
            thisYear.add(Calendar.WEEK_OF_YEAR, addOneWeek);
        }

        while(currentYear <= displayYear) {
            WeeksInYear week = new WeeksInYear();

            week.setWeekNum(weekOfYear);
            week.setDate(thisYear.getTimeInMillis());

            weeksList.add(week);
            notifyDataSetChanged();

            weekOfYear++;
            thisYear.add(Calendar.DAY_OF_YEAR, daysInWeek);

            currentYear = thisYear.get(Calendar.YEAR);
        }
    }

    private static class ViewHolder {
        TextView weekText;
        TextView weekNum;
        TextView date;
        LinearLayout entireRow;
    }
}
