package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zyuki.daylies.ApplicationDatabase;
import com.example.zyuki.daylies.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.DataAccessObject;
import models.WeeksInYear;
import utils.Constant;
import utils.DateCalcs;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Adapter class extending from BaseAdapter that is set to a GridView in DisplayWeeksFragment
 * Also stores the current list of weeks within the year that is displayed
 **************************************************************************************************/
public class WeeksAdapter extends BaseAdapter {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private LayoutInflater inflater;
    private List<WeeksInYear> weeksList;

    private Activity activity;
    private DataAccessObject dataAccess;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /**One and only constructor**/
    public WeeksAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
        dataAccess = ((ApplicationDatabase)activity.getApplicationContext()).dataAccess;
        weeksList = new ArrayList<>();

        this.activity = activity;
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that gets the number of elements within the list**/
    @Override
    public int getCount() {return weeksList.size();}

    /**Override method that returns the item at the given position**/
    @Override
    public WeeksInYear getItem(int position) {return weeksList.get(position);}

    /**Override method that returns the item ID of the item at the given position**/
    /**Necessary to override. Not used within the app-specific classes of this app**/
    @Override
    public long getItemId(int position) {return position;}

    /**Override method that returns a populated view to inject into the gridview**/
    @Override
    public View getView(int position, View row, ViewGroup parent) {
        int layoutRes = R.layout.row_weeks;
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = inflater.inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.itemGroup = (RelativeLayout)row.findViewById(R.id.rowWeeks_linear_itemGroup);

            viewHolder.hasTodos = (ImageView)row.findViewById(R.id.rowWeeks_image_hasTodos);
            viewHolder.year = (TextView)row.findViewById(R.id.rowWeeks_text_year);
            viewHolder.month = (TextView)row.findViewById(R.id.rowWeeks_text_month);
            viewHolder.weekNum = (TextView)row.findViewById(R.id.rowWeeks_text_weekNum);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        WeeksInYear week = getItem(position);

        viewHolder.year.setText(String.valueOf(week.getYear()));
        viewHolder.month.setText(week.getMonth().substring(0, 3));
        viewHolder.weekNum.setText(DateCalcs.addZeroToNum(week.getWeekNum()));

        if(week.getHasTodos()) {
            viewHolder.hasTodos.setVisibility(View.VISIBLE);
        } else {viewHolder.hasTodos.setVisibility(View.INVISIBLE);}

		//need to explicitly declare if/else statements because the view is recycled so properties
		//set to previous views will randomly be recycled into future views
		//Changed from setting view to gone/visible to changing text content
        if(DateCalcs.isCurrentYearWeek(week)) {
            int whiteTextColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.whiteText);

            viewHolder.itemGroup.setBackgroundResource(R.color.colorPrimary);
            viewHolder.year.setTextColor(whiteTextColor);
            viewHolder.month.setTextColor(whiteTextColor);
            viewHolder.weekNum.setTextColor(whiteTextColor);
        } else {
            int blackTextColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.darkText);

            viewHolder.itemGroup.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.year.setTextColor(blackTextColor);
            viewHolder.month.setTextColor(blackTextColor);
            viewHolder.weekNum.setTextColor(blackTextColor);
        }

        return row;
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /**Public method that finds the position of the item given the week**/
    /**Used in public method fragments.DisplayWeeksFragment.notifyDataSetChanged**/
    public int getPosByWeek(int week) {
        if(week != Constant.ERROR) {
            for(int pos = 0; pos < getCount(); pos++) {
                WeeksInYear thisWeek = getItem(pos);
                if(thisWeek != null && week == thisWeek.getWeekNum()) {
                    return pos;
                }
            }
        }

        return Constant.ERROR;
    }

    /**Public method that builds the list of weeks within the selected year**/
    /**Used in public method fragments.DisplayWeeksFragment.buildYear**/
    public void getWeeksInYear() {
        weeksList.clear();

        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        int displayYear = prefs.getInt(Constant.Prefs.PREF_KEY_YEAR, Constant.ERROR);

        final int addOneWeek = 1;
        int weekOfYear = 1;
        int currentYear = 0;

        Calendar endOfLastYear = DateCalcs.endOfLastYear(displayYear);

        Calendar thisYear = Calendar.getInstance();
        thisYear.set(Calendar.YEAR, displayYear);
        thisYear.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisYear.set(Calendar.WEEK_OF_YEAR, weekOfYear);

        if(thisYear.before(endOfLastYear)) {
            thisYear.add(Calendar.WEEK_OF_YEAR, addOneWeek);
        }

        while(currentYear <= displayYear) {
            WeeksInYear week = new WeeksInYear();

            week.setYear(thisYear.get(Calendar.YEAR));
            week.setWeekNum(weekOfYear);
            week.setMonth(DateCalcs.formatDate(Constant.Util.FORMAT_MONTH, thisYear.getTimeInMillis()));

            week.setHasTodos(dataAccess.weekHasTodos(thisYear.get(Calendar.YEAR), weekOfYear));

            weeksList.add(week);

            weekOfYear++;
            thisYear.add(Calendar.WEEK_OF_YEAR, addOneWeek);

            currentYear = thisYear.get(Calendar.YEAR);
        }
    }

    /***********************************************************************************************
     * INNER CLASSES & INTERFACES
     **********************************************************************************************/
    /**ViewHolder class that acts like a View Recycler**/
    /**Used in override method getView**/
    private static class ViewHolder {
        RelativeLayout itemGroup;

        ImageView hasTodos;
        TextView year;
        TextView month;
        TextView weekNum;
    }
}
