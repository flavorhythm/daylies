package adapters;

import android.app.Activity;
import android.content.Context;
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
import models.Day;
import models.DayName;
import utils.DateCalcs;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Adapter class extending from BaseAdapter that is set to a GridView in DisplayDaysFragment
 * Also stores the current list of days within the week that is displayed
 **************************************************************************************************/
public class DaysAdapter extends BaseAdapter {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Public variables**/
    /**Used for differentiating content data and monthly divider data**/
    //TODO: aggregate constants
    public static final int TYPE_COUNT = 2;
    public static final int TYPE_DIVIDER = 0;
    public static final int TYPE_CONTENT = 1;

    /**Private variables**/
    private LayoutInflater inflater;
    private List<Day> dayList;

    private Context context;
    private DataAccessObject dataAccess;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /**One and only constructor**/
    public DaysAdapter(Activity activity, Context context) {
        //Inflates the layout from the passed activity variable
        inflater = LayoutInflater.from(activity);
        //Saves context to a global variable to be used later (used in getView)
        this.context = context;
        //Instantiates an arraylist to the todoList variable
        dayList = new ArrayList<>();
        //Points the global variable to the open dataAccess variable in ApplicationDatabase
        dataAccess = ((ApplicationDatabase)activity.getApplicationContext()).dataAccess;
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that gets view type**/
    @Override
    public int getItemViewType(int position) {return getItem(position).getType();}

    /**Override method that gets view type count**/
    @Override
    public int getViewTypeCount() {return TYPE_COUNT;}

    /**Override method that gets count of the list**/
    @Override
    public int getCount() {return dayList.size();}

    /**Override method that gets the item at position in the list**/
    @Override
    public Day getItem(int position) {return dayList.get(position);}

    /**Override method that gets ID (in this case position) of the item residing in position**/
    @Override
    public long getItemId(int position) {return position;}

    /**Override method that builds a view for each item**/
    @Override
    public View getView(int position, View row, ViewGroup parent) {
        //Local variables that are set to the appropriate layouts (better readability)
        int dividerLayout = R.layout.row_days_divider;
        int contentLayout = R.layout.row_days_content;

        //Declares the recyclable view (defined here as an inner class)
        ViewHolder viewHolder;

        //Sets the appropriate view to the row if it's empty
        if(row == null || row.getTag() == null) {
            //Initializes the recyclable view
            viewHolder = new ViewHolder();

            //Switch block to inflate the appropriate view to the row variable
            switch(getItemViewType(position)) {
                //Case when the view is a divider type
                case TYPE_DIVIDER:
                    //Inflates the appropriate layout
                    row = inflater.inflate(dividerLayout, parent, false);

                    //Sets the recyclable view's TextView to the appropriate ID
                    viewHolder.month = (TextView)row.findViewById(R.id.rowDays_text_month);
                    //Sets the tag of row to the recyclable view
                    row.setTag(viewHolder);
                    break;
                //Case when the view is a content type
                case TYPE_CONTENT:
                    //Inflates the appropriate layout
                    row = inflater.inflate(contentLayout, parent, false);

                    //Sets the recyclable view's TextViews and ViewGroups to the appropriate IDs
                    viewHolder.itemGroup = (RelativeLayout)row.findViewById(R.id.rowDays_relative_itemGroup);
                    viewHolder.hasTodos = (ImageView)row.findViewById(R.id.rowDays_image_hasTodos);
                    viewHolder.dayOfMonth = (TextView)row.findViewById(R.id.rowDays_text_dayOfMonth);
                    viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.rowDays_text_dayOfWeek);

                    //Sets the tag of row to the recyclable view
                    row.setTag(viewHolder);
                    break;
            }
        //If the view is being recycled, sets the recyclable view to the row's tag
        } else {viewHolder = (ViewHolder)row.getTag();}

        //Gets the item stored in the list, located within this class, at position
        Day day = getItem(position);

        //Switch block to set the appropriate views to the corresponding data
        switch(getItemViewType(position)) {
            //Case when this position's row type is a divider
            case TYPE_DIVIDER:
                //Local variable to store the month's name (first three chars only)
                String month = DateCalcs.formatDate(
                        Calendar.MONTH, day.getDate()
                ).substring(0, 3);
                //Sets the view's text to the month
                viewHolder.month.setText(month);
                break;
            //Case when this position's row type is content
            case TYPE_CONTENT:
                //Sets the day of the month
                viewHolder.dayOfMonth.setText(
                        DateCalcs.formatDate(Calendar.DAY_OF_MONTH, day.getDate())
                );
                //Sets the day of the week (first three cars only)
                viewHolder.dayOfWeek.setText(
                        day.getDay().toString().substring(0, 3)
                );

                //Sets the "has todos" icon to visible when todos exist for this day
                //Invisible when not
                if(day.getHasTodos()) {
                    viewHolder.hasTodos.setVisibility(View.VISIBLE);
                } else {viewHolder.hasTodos.setVisibility(View.INVISIBLE);}

                //Checks to see what day it is
                if(DateCalcs.isCurrentDay(day.getDate())) {
                    //Highlights the item if it's the current day
                    int whiteTextColor = ContextCompat.getColor(context, R.color.whiteText);

                    viewHolder.itemGroup.setBackgroundResource(R.color.colorPrimary);
                    viewHolder.dayOfMonth.setTextColor(whiteTextColor);
                    viewHolder.dayOfWeek.setTextColor(whiteTextColor);
                } else {
                    //Reverts the item back to normal
                    //This is required since views are recycled
                    int blackTextColor = ContextCompat.getColor(context, R.color.darkText);

                    viewHolder.itemGroup.setBackgroundColor(Color.TRANSPARENT);
                    viewHolder.dayOfMonth.setTextColor(blackTextColor);
                    viewHolder.dayOfWeek.setTextColor(blackTextColor);
                }
                break;
        }

        //Returns inflated and customized row view
        return row;
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /**Gets the position of the item corresponding to the day of dayName**/
    /**Used in public method DisplayDaysFragment.notifyDataSetChanged**/
    public int getPosByDay(DayName searchThisDay) {
        //Error variable (better readability)
        final int error = -1;

        //For loop that goes through all the days to check for todos
        for(int pos = 0; pos < getCount(); pos++) {
            //Gets day at every position
            DayName thisDay = getItem(pos).getDay();

            //Checks to see if the day exists and if it matches the search day
            if(thisDay != null && thisDay == searchThisDay) {
                //Returns position of this day
                return pos;
            }
        }

        //Returns an error if the above return statement is skipped
        return error;
    }

    /**Sets up the entire list of days for this week**/
    public void finishBuildingWeek(int year, int weekNum) {
        //Clears the current list to make room for a new set of days
        dayList.clear();

        //Final variables that won't need to change
        final int daysInWeek = 7;

        //Sets a Calendar variable to the year and week that was passed as input
        Calendar firstDateOfWeek = Calendar.getInstance();
        firstDateOfWeek.clear();
        firstDateOfWeek.set(Calendar.YEAR, year);
        firstDateOfWeek.set(Calendar.WEEK_OF_YEAR, weekNum);
        firstDateOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        addDivider(firstDateOfWeek);

        //For loop that adds every day of the week (including additional dividers when necessary)
        //to the list
        for(int i = 0; i < daysInWeek; i++) {
            //Initializes a new day
            Day day = new Day();

            //Sets the private variable of day to pass onto the list
            day.setType(TYPE_CONTENT);
            day.setDate(firstDateOfWeek.getTimeInMillis());
            day.setYear(year);
            day.setDay(DayName.values()[i]);

            //Determines whether or not this day has todos
            String yearWeekDay = DateCalcs.buildDateString(year, weekNum, DayName.values()[i]);
            day.setHasTodos(dataAccess.dayHasTodos(yearWeekDay));

            //Adds the day to the list and notifies the adapter
            dayList.add(day);

            //Determines the months before and after a day is added to the calendar object
            int monthBefore = firstDateOfWeek.get(Calendar.MONTH);
            firstDateOfWeek.add(Calendar.DAY_OF_YEAR, 1);
            int monthAfter = firstDateOfWeek.get(Calendar.MONTH);

            //Adds a month divider if the months change after adding another day
            //Divider will not be added if the day is the last day of the week
            if(monthBefore != monthAfter && i != (daysInWeek - 1)) {addDivider(firstDateOfWeek);}
        }
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /**Adds a month divider in between the days when needed**/
    /**Used in public method finishBuildingWeek**/
    private void addDivider(Calendar theMonth) {
        //Initializes a new month object
        Month month = new Month();

        //Sets the type to divider and sets the date
		month.setType(TYPE_DIVIDER);
		month.setDate(theMonth.getTimeInMillis());

        //Adds the month divider into the list
        dayList.add(month);
    }

    /***********************************************************************************************
     * INNER CLASSES
     **********************************************************************************************/
    /**ViewHolder class that acts like a View Recycler**/
    /**Used in override method getView**/
    private static class ViewHolder {
        //Content views
        RelativeLayout itemGroup; //Used to make changes to the entire element
        ImageView hasTodos; //Used to inform the user whether or not to do items exist for this day
        TextView dayOfMonth; //Displays the day of month
        TextView dayOfWeek; //Displays the day of week

        //Divider views
        TextView month; //Displays the month
    }

    /**Month extends Day. Nothing to declare here**/
    /**Used in the private method addDivider**/
	private static class Month extends Day {}
}
