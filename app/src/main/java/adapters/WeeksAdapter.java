package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
public class WeeksAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<WeeksInYear> weeksList = new ArrayList<>();

    private Context context;

    public WeeksAdapter(Activity activity, Context context) {
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
        int layoutRes = R.layout.row_weeks;
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = inflater.inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.itemGroup = (RelativeLayout)row.findViewById(R.id.rowWeeks_linear_itemGroup);

            viewHolder.year = (TextView)row.findViewById(R.id.rowWeeks_text_year);
            viewHolder.month = (TextView)row.findViewById(R.id.rowWeeks_text_month);
            viewHolder.weekNum = (TextView)row.findViewById(R.id.rowWeeks_text_weekNum);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        WeeksInYear week = getItem(position);

        viewHolder.year.setText(String.valueOf(week.getYear()));
        viewHolder.month.setText(week.getMonth().substring(0, 3));
        viewHolder.weekNum.setText(DateCalcs.addZeroToNum(week.getWeekNum()));

		//need to explicitly declare if/else statements because the view is recycled so properties
		//set to previous views will randomly be recycled into future views
		//Changed from setting view to gone/visible to changing text content
        if(DateCalcs.isCurrentYearWeek(week)) {
            int whiteTextColor = ContextCompat.getColor(context, R.color.whiteText);

            viewHolder.itemGroup.setBackgroundResource(R.color.colorPrimary);
            viewHolder.year.setTextColor(whiteTextColor);
            viewHolder.month.setTextColor(whiteTextColor);
            viewHolder.weekNum.setTextColor(whiteTextColor);
        } else {
            int blackTextColor = ContextCompat.getColor(context, R.color.darkText);

            viewHolder.itemGroup.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.year.setTextColor(blackTextColor);
            viewHolder.month.setTextColor(blackTextColor);
            viewHolder.weekNum.setTextColor(blackTextColor);
        }

        return row;
    }

    public void buildWeeksInYear(int displayYear) {
        weeksList.clear();

        final int daysInWeek = 7;
        int weekOfYear = 1;
        int currentYear = 0;

        Calendar endOfLastYear = DateCalcs.endOfLastYear(displayYear);

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

            week.setYear(displayYear);
            week.setWeekNum(weekOfYear);
            week.setMonth(DateCalcs.formatDate(Calendar.MONTH, thisYear.getTimeInMillis()));

            weeksList.add(week);
            notifyDataSetChanged();

            weekOfYear++;
            thisYear.add(Calendar.DAY_OF_YEAR, daysInWeek);

            currentYear = thisYear.get(Calendar.YEAR);
        }
    }

    private static class ViewHolder {
        RelativeLayout itemGroup;

        TextView year;
        TextView month;
        TextView weekNum;
    }
}
