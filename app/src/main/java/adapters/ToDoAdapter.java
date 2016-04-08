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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.DataAccessObject;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Adapter class extending from BaseAdapter that is set to a ListView in MainActivity
 * Also stores the current list of to dos associated with the day that is displayed
 **************************************************************************************************/
public class ToDoAdapter extends BaseAdapter {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    //TODO: aggregate constants
    private static final int TYPE_COUNT = 2;

    private static final int ERROR = -1;
    private static final int TYPE_DIVIDER = 0;
    private static final int TYPE_CONTENT = 1;

    private List<ToDo> toDoList;
    private LayoutInflater inflater;
    private DataAccessObject dataAccess;
    private Activity activity;

    private Map<Integer, Integer> dividerPos;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /**One and only constructor**/
    public ToDoAdapter(Activity activity, Context context) {
        //Inflates the layout from the passed activity variable
        inflater = LayoutInflater.from(activity);
        //Points the global variable to the open dataAccess variable in ApplicationDatabase
        dataAccess = ((ApplicationDatabase)context).dataAccess;

        //Sets activity param to the global variable activity
        this.activity = activity;
        //Instantiates an arraylist to the todoList variable
        toDoList = new ArrayList<>();
        dividerPos = new HashMap<>();
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that gets view type**/
    @Override
    public int getItemViewType(int position) {
        //Gets item type from position
        int itemType = getItem(position).getType();
        //Switch statement that converts the To Do constants into two possible types:
        //content and divider
        switch(itemType) {
            //Converts all dividers into one divider type
            case ToDo.DIVIDER_LUNCH: case ToDo.DIVIDER_WORK: case ToDo.DIVIDER_DAILY:
                //Returns single divider type
                return TYPE_DIVIDER;
            //Converts all contents into one content type
            case ToDo.CONTENT_LUNCH:case ToDo.CONTENT_WORK:case ToDo.CONTENT_DAILY:
                //Returns content type
                return TYPE_CONTENT;
            //Default returns error (item type was not found)
            default:
                //Returns error
                return ERROR;
        }
    }

    /**Override method that gets view type count**/
    @Override
    public int getViewTypeCount() {return TYPE_COUNT;}

    /**Override method that gets count of the list**/
    @Override
    public int getCount() {return toDoList.size();}

    /**Override method that gets the item at position in the list**/
    @Override
    public ToDo getItem(int position) {return toDoList.get(position);}

    /**Override method that gets ID (in this case position) of the item residing in position**/
    @Override
    public long getItemId(int position) {return position;}

    /**Override method that builds a view for each item**/
    /**This method reuses a ViewHolder so that less memory is used**/
    @Override
    public View getView(final int position, View row, ViewGroup parent) {
        //Local variables that are set to the appropriate layouts (better readability)
        int dividerLayout = R.layout.row_todos_divider;
        int contentLayout = R.layout.row_todos_content;

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
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_header);

                    //Sets the tag of row to the recyclable view
                    row.setTag(viewHolder);
                    break;
                //Case when the view is a content type
                case TYPE_CONTENT:
                    //Inflates the appropriate layout
                    row = inflater.inflate(contentLayout, parent, false);
                    //Sets the recyclable view's TextView to the appropriate ID
                    viewHolder.textItem = (TextView)row.findViewById(R.id.display_text_content);

                    //Sets the tag of row to the recyclable view
                    row.setTag(viewHolder);
                    break;
                case ERROR:
                    return row;
            }
        //If the view is being recycled, sets the recyclable view to the row's tag
        } else {viewHolder = (ViewHolder)row.getTag();}

        //Gets the item stored in the list, located within this class, at position
        ToDo item = getItem(position);

        //Sets the view's TextView to the to do item
        viewHolder.textItem.setText(item.getItem());

        //Sets section length for each divider that is displayed
        if(getItemViewType(position) == TYPE_DIVIDER) {
            //Gets type of item at current position and declare variable for ease of readability
            int dividerType = getItem(position).getType();
            //Appends section length with parentheses to the TextView
            viewHolder.textItem.append(" " + "(" + String.valueOf(dividerPos.get(dividerType)) + ")");
        }

        //Returns inflated and customized row view
        return row;
    }

    /**Override method that updates divider positions once the list has updated**/
    @Override
    public void notifyDataSetChanged() {
        //Calls this method's Super
        super.notifyDataSetChanged();
        //Relocates all dividers within the list
        locateDividers();
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /**Public method that adds a to do item to the current year, week, day**/
    /**Used in public method MainActivity.addLunchItem**/
    public void add(ToDo item) {
        //Accesses database and puts new to do item into db
        dataAccess.addToDoItem(item);
        //Adds item to appropriate position in global list
        addToList(item);
    }

    /**Public method that removes a to do item from the database and from the list**/
    /**Used in public method MainActivity.removeItem**/
    public void removeItem(final int listPos) {
        //Gets database ID of item from the position in the list
        int dbId = getItem(listPos).getId();
//        int dividerType = contentToDivider(getItem(listPos).getType());

        //Deletes item from db using dbID
        dataAccess.deleteToDoItem(dbId);
        //Removes item from to do list
        toDoList.remove(listPos);

        //Final local variable that stores the position of the previous item
        //(for ease of readability)
        final int previousPos = listPos - 1;
        //Checks whether deleted item was at the end of the list
        if(listPos == getCount()) {
            //Checks whether the previous position's type is a header
            //After deleting the the last item on the list, the header will remain
            //and needs to be removed
            if(getItemViewType(previousPos) == TYPE_DIVIDER) {
                //Remove the previous item (the empty divider, section)
                toDoList.remove(previousPos);
            }
        }

        //Weekdays can have two dividers. If the uppermost divider is empty but the other is not
        //then removes the first divider
        if(!toDoList.isEmpty()) {
            //Final local variables that stores positions of the first and second item
            //(better readability)
            final int firstItem = 0;
            final int secondItem = 1;
            //If the second item is a divider, the first item is also a divider
            if(getItemViewType(secondItem) == TYPE_DIVIDER) {
                //Removes the first empty divider
                toDoList.remove(firstItem);
            }
        }
    }

    /**Public method that builds to do list for the day**/
    /**Used in public method MainActivity.todoAdapter & public method removeItem**/
    public void buildList(int year, int weekNum, DayName day) {
        //Clears to do list of previous items
        toDoList.clear();
        //Clears the hashmap for every day
        dividerPos.clear();
        //Build list from database
        buildFromDb(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /**Private method that builds a to do list with dividers for the week**/
    /**Used in public method buildList**/
    private void buildFromDb(List<ToDo> toDosFromDb) {
        //Instantiates local variable lists for each section
        List<ToDo> lunchToDos = new ArrayList<>();
        List<ToDo> afterWork = new ArrayList<>();
        List<ToDo> dailyToDos = new ArrayList<>();

        //Loops through each item from the database and adds the item to the appropriate local list
        for(ToDo item : toDosFromDb) {
            //Switch statement that builds the appropriate list depending on the type of to do
            switch(item.getType()) {
                //Case when the to do type is lunch
                case ToDo.CONTENT_LUNCH:
                    //Adds the to do item to the corresponding local list
                    lunchToDos.add(item);
                    break; //Breaks switch statement
                //Case when the to do type is after work
                case ToDo.CONTENT_WORK:
                    //Adds the to do item to the corresponding local list
                    afterWork.add(item);
                    break; //Breaks switch statement
                //Case when the to do type is daily
                case ToDo.CONTENT_DAILY:
                    //Adds the to do item to the corresponding local list
                    dailyToDos.add(item);
                    break; //Breaks switch statement
            }
        }

        //Builds the list with dividers using the set of lists created above
        buildFromDbWithDividers(
                buildListOfList(lunchToDos, afterWork, dailyToDos)
        );
    }

    /**Private method that builds the item list (for the first time) and injects dividers**/
    /**Parameter is built with private method buildListOfList when invoking this method**/
    private void buildFromDbWithDividers(List<List<ToDo>> itemLists) {
        //Builds divider list using buildDivderList method
        List<Integer> dividerList = buildDividerList();

        //Initializes a position counter for the "foreach" loop below
        //Used to pull the position of the corresponding divider type
        int position = 0;
        //For loop that goes through every item and injects a divider when needed
        for(List<ToDo> itemList : itemLists) {
            //If statement that adds a divider and the item if the item is the first one of its kind
            //Kind being lunch, after work, and daily to do
            //Accessed only if itemList is empty
            if(!itemList.isEmpty()) {
                //Adds divider into the global to do list
                toDoList.add(buildDivider(dividerList.get(position)));
                //Adds all the items in the list below the divider
                toDoList.addAll(itemList);
            }
            //Adds one to the position variable
            //This is so the next loop can look up the next divider position
            position++;
        }
    }

    /**Private method that builds the dividers for the to do lists**/
    /**Used in private methods buildWeekDayList & buildWeekEndList**/
    private ToDo buildDivider(int dividerType) {
        //Initialize a string variable
        //This is to store the divider text corresponding to the divider type param
        String dividerText = "";

        //Switch statement that converts divider type to divider text
        switch(dividerType) {
            //Case when divider type is for lunch to dos
            case ToDo.DIVIDER_LUNCH:
                //Sets divider text to appropriate text for section
                dividerText = activity.getResources().getString(R.string.divider_lunch);
                break; //Breaks switch statement
            //Case when divider type is for after work to dos
            case ToDo.DIVIDER_WORK:
                //Sets divider text to appropriate text for section
                dividerText = activity.getResources().getString(R.string.divider_afterWork);
                break; //Breaks switch statement
            //Case when divider type is for the entire day (weekend)
            case ToDo.DIVIDER_DAILY:
                //Sets divider text to appropriate text for section
                dividerText = activity.getResources().getString(R.string.divider_daily);
                break; //Breaks switch statement
        }

        //Returns a divider To Do
        return new ToDo(dividerType, dividerText);
    }

    /**Private method that adds the item to the global list**/
    /**Used in public method add**/
    private void addToList(ToDo item) {
        //Declare final variable set to zero for ease of readability
        final int first = 0;
        //Pulls content type of the to do item
        int contentType = item.getType();
        //Converts the content type to the corresponding divider type
        int dividerType = contentToDivider(contentType);

        //Finds the position at the end of the section
        int sectionEndsAt = dividerPos.get(dividerType);
        //Builds divider according to the divider type
        ToDo divider = buildDivider(dividerType);
        //Checks whether the section exists by finding length of section
        //Compare it to the first position in the list
        //Adds the item to the list and a divider if necessary
        if(sectionEndsAt == first) {
            //Checks whether the divider type is for lunch
            if(dividerType == ToDo.DIVIDER_LUNCH) {
                //Adds divider to the front of the list
                toDoList.add(first, divider);
                //Adds item to the front of the list
                toDoList.add(first + 1, item);
            //All other dividers/content
            } else {
                //Adds divider to the end of the list
                toDoList.add(divider);
                //Adds content to the end of the list
                toDoList.add(item);
            }
        //Section already exists
        } else {
            //Checks whether the divider type is for lunch
            if(dividerType == ToDo.DIVIDER_LUNCH) {
                //adds item at the end of the lunch section
                toDoList.add(sectionEndsAt + 1, item);
            //All other dividers/content
            } else {
                //Adds item at the end of the list
                toDoList.add(item);
            }
        }
    }

    /**Private method that converts the content type parameter to its divider counterpart**/
    /**Used in private method addToList**/
    private int contentToDivider(int contentType) {
        //Declares final error variable for ease of readability
        final int error = -1;

        //Switch statement that converts the content type to its divider counterpart
        switch(contentType) {
            //Case when content type is lunch
            case ToDo.CONTENT_LUNCH:
                //Returns lunch divider
                return ToDo.DIVIDER_LUNCH;
            //Case when content type is after work
            case ToDo.CONTENT_WORK:
                //Returns after work divider
                return ToDo.DIVIDER_WORK;
            //Case when content type is daily (weekend)
            case ToDo.CONTENT_DAILY:
                //Returns daily divider
                return ToDo.DIVIDER_DAILY;
            //Case when content type is unknown
            default:
                //Returns error
                return error;
        }
    }

    /**Private method that locates dividers and stores their section lengths into dividerPos Map**/
    /**Used in override method notifyDataSetChanged**/
    private void locateDividers() {
        //Checks whether global items list is not empty
        if(!toDoList.isEmpty()) {
            //Sets all divider positions in dividerPos to zero
            clearDividerPos();

            //Looks through every item in the global to do list
            for(int listPos = 0; listPos < toDoList.size(); listPos++) {
                //Checks whether the item at this position is a divider type
                if(getItemViewType(listPos) == TYPE_DIVIDER) {
                    //Finds exact divider type of divider
                    int dividerType = getItem(listPos).getType();

                    //Offsets position of item to compensate for the current divider
                    int dividerOffset = listPos + 1;
                    //Sets listSize to dividerOffset to continue counting up listSize
                    int listSize = dividerOffset;
                    //Counts up listSize until end of list or the next divider
                    while((listSize < toDoList.size()) &&
                            (getItemViewType(listSize) != TYPE_DIVIDER)) {
                        //Counts up listSize
                        listSize++;
                    }

                    //Sets this divider type's size to the listSize minus dividerOffset
                    dividerPos.put(dividerType, listSize - dividerOffset);
                }
            }
        //If global items list is empty
        } else {
            //Sets all divider positions in dividerPos to zero
            clearDividerPos();
        }
    }

    /**Private method that sets all divider positions in dividerPos to zero **/
    /**Used in private method locateDividers**/
    private void clearDividerPos() {
        //Looks up every divider type that returns from buildDividerList
        for(Integer dividerType : buildDividerList()) {
            //Declares final variable for the int value zero for ease of readability
            final int clear = 0;
            //Clears this divider type's length/position
            dividerPos.put(dividerType, clear);
        }
    }

    /**Private method that builds a list from the different divider types**/
    /**Used in private methods buildFromDbWithDividers & clearDividerPos**/
    private List<Integer> buildDividerList() {
        //Instantiates an integer list
        List<Integer> dividerList = new ArrayList<>();

        //Adds the three types of dividers to the list
        dividerList.add(ToDo.DIVIDER_LUNCH);
        dividerList.add(ToDo.DIVIDER_WORK);
        dividerList.add(ToDo.DIVIDER_DAILY);

        //Returns divider type list
        return dividerList;
    }

    /**Private method that builds a list of item lists from db, separated by content type**/
    /**Used in private method buildFromDb and a direct input for buildFromDbWithDividers**/
    /**Is final for SafeVarargs annotation**/
    @SafeVarargs
    final private List<List<ToDo>> buildListOfList(List<ToDo>... lists) {
        //Declares final int variable number of lists to three for ease of readability
        final int numOfLists = 3;
        //Instantiates a list of lists of to do items
        List<List<ToDo>> itemLists = new ArrayList<>();

        //Checks whether there are truly three input parameters
        if(lists.length == numOfLists) {
            //Sets the list of lists to each list from the input parameters
            for(List<ToDo> list : lists) {itemLists.add(list);}
        //If there are more than three input parameters
        } else {
            //Sets the list of lists to three blank lists
            for(int i = 0; i < numOfLists; i++) {itemLists.add(new ArrayList<ToDo>());}
        }

        //Returns the list of lists
        return itemLists;
    }

    /***********************************************************************************************
     * INNER CLASSES
     **********************************************************************************************/
    /**ViewHolder class that acts like a View Recycler**/
    /**Used in override method getView**/
    private static class ViewHolder {
        //Stores the divider text or the to do item
        TextView textItem;
    }
}