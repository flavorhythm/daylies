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
import java.util.Set;

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
    private static final int TYPE_COUNT = 2;

    private static final int TYPE_DIVIDER = 0;
    private static final int TYPE_CONTENT = 1;

    private List<ToDo> toDoList;
    private LayoutInflater inflater;
    private DataAccessObject dataAccess;

    private Map<Integer, Integer> dividerPos;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /**One and only constructor**/
    public ToDoAdapter(Activity activity, Context context) {
        //Inflates the layout from the passed activity variable
        inflater = LayoutInflater.from(activity);
        //Instantiates an arraylist to the todoList variable
        toDoList = new ArrayList<>();
        //Points the global variable to the open dataAccess variable in ApplicationDatabase
        dataAccess = ((ApplicationDatabase)context).dataAccess;
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that gets view type**/
    @Override
    public int getItemViewType(int position) {
        int itemType = getItem(position).getType();
        switch(itemType) {
            case ToDo.DIVIDER_LUNCH: case ToDo.DIVIDER_WORK: case ToDo.DIVIDER_DAILY:
                return TYPE_DIVIDER;
            case ToDo.CONTENT_LUNCH:case ToDo.CONTENT_WORK:case ToDo.CONTENT_DAILY:
                return TYPE_CONTENT;
            default:
                return TYPE_CONTENT;
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
            }
        //If the view is being recycled, sets the recyclable view to the row's tag
        } else {viewHolder = (ViewHolder)row.getTag();}

        //Gets the item stored in the list, located within this class, at position
        ToDo item = getItem(position);

        //Sets the view's TextView to the to do item
        viewHolder.textItem.setText(item.getItem());

        //Returns inflated and customized row view
        return row;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

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
        int dividerType = contentToDivider(getItem(listPos).getType());

        //Deletes item from db using dbID
        dataAccess.deleteToDoItem(dbId);
        //Removes item from to do list
        toDoList.remove(listPos);
        removeDividerCount(dividerType);

        //Final local variable that stores the position of the previous item (
        final int previousPos = listPos - 1;
        //Checks whether deleted item was at the end of the list
        if(listPos == getCount()) {
            //Checks whether the previous position's type is a header
            //After deleting the the last item on the list, the header will remain
            //and needs to be removed
            if(getItemViewType(previousPos) == TYPE_DIVIDER) {
                //Remove the previous item (the empty divider, section)
                toDoList.remove(previousPos);
                removeDividerCount(dividerType);
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
                removeDividerCount(dividerType);
            }
        }
    }

    //TODO: check this
    private void removeDividerCount(int dividerType) {
        int newPos = dividerPos.get(dividerType) - 1;
        dividerPos.put(dividerType, newPos);
    }

    /**Public method that builds to do list for the day**/
    /**Used in public method MainActivity.todoAdapter & public method removeItem**/
    public void buildList(int year, int weekNum, DayName day) {
        //Clears to do list of previous items
        toDoList.clear();

        //Clears the hashmap for every day
        dividerPos = new HashMap<>();

        buildWeekList(dataAccess.getToDoList(DateCalcs.buildDateString(year, weekNum, day)));
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /**Private method that builds a to do list with dividers for the weekdays**/
    /**Used in public method buildList**/
    private void buildWeekList(List<ToDo> toDosFromDb) {
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
                    break;
                //Case when the to do type is after work/daily
                case ToDo.CONTENT_WORK:
                    //Adds the to do item to the corresponding local list
                    afterWork.add(item);
                    break;
                case ToDo.CONTENT_DAILY:
                    dailyToDos.add(item);
                    break;
            }
        }

        List<List<ToDo>> itemLists = new ArrayList<>();
        itemLists.add(lunchToDos);
        itemLists.add(afterWork);
        itemLists.add(dailyToDos);

        buildWithDividers(itemLists);
    }

    private void buildWithDividers(List<List<ToDo>> itemLists) {
        List<Integer> dividerList = new ArrayList<>();
        dividerList.add(ToDo.DIVIDER_LUNCH);
        dividerList.add(ToDo.DIVIDER_WORK);
        dividerList.add(ToDo.DIVIDER_DAILY);

        int position = 0;
        for(List<ToDo> itemList : itemLists) {
            int divider = dividerList.get(position);

            if(!itemList.isEmpty()) {
                toDoList.add(buildDivider(divider));
                toDoList.addAll(itemList);

                dividerPos.put(divider, toDoList.size());
            } else {
                final int empty = 0;
                dividerPos.put(divider, empty);
            }
            position++;
        }
    }

    /**Private method that builds the dividers for the to do lists**/
    /**Used in private methods buildWeekDayList & buildWeekEndList**/
    private ToDo buildDivider(int dividerType) {
        String dividerText = "";

        switch(dividerType) {
            case ToDo.DIVIDER_LUNCH:
                dividerText = "Lunchtime";
                break;
            case ToDo.DIVIDER_WORK:
                dividerText = "After Work";
                break;
            case ToDo.DIVIDER_DAILY:
                dividerText = "Daily To Dos";
                break;
        }

        return new ToDo(dividerType, dividerText);
    }

    private void addToList(ToDo item) {
        final int first = 0;

        int contentType = item.getType();
        int dividerType = contentToDivider(contentType);

        //if dividerAt = 0, put divider in front for lunch and at the back for the rest
        //Then,
        int dividerAt = dividerPos.get(dividerType);
        ToDo divider = buildDivider(dividerType);
        if(dividerAt == 0) {
            if(dividerType == ToDo.DIVIDER_LUNCH) {
                toDoList.add(first, divider);
                toDoList.add(first + 1, item);
            } else {
                toDoList.add(divider);
                toDoList.add(item);
            }

//            int newPos = dividerPos.get(dividerType) + 2;
//            dividerPos.put(dividerType, newPos);
        } else {
            toDoList.add(dividerPos.get(dividerType), item);

//            int newPos = dividerPos.get(dividerType) + 1;
//            dividerPos.put(dividerType, newPos);
        }
    }

    private int contentToDivider(int contentType) {
        final int error = -1;

        switch(contentType) {
            case ToDo.CONTENT_LUNCH:
                return ToDo.DIVIDER_LUNCH;
            case ToDo.CONTENT_WORK:
                return ToDo.DIVIDER_WORK;
            case ToDo.CONTENT_DAILY:
                return ToDo.DIVIDER_DAILY;
            default:
                return error;
        }


    }

    //TODO: still not working
    private void locateDividers() {
        if(!toDoList.isEmpty()) {
            for(int listPos = 0; listPos < toDoList.size(); listPos++) {
                if(getItemViewType(listPos) == TYPE_DIVIDER) {
                    int dividerType = getItem(listPos).getType();

                    int listSize = listPos + 1;
                    while((listSize < toDoList.size()) |
                            (getItemViewType(listSize) != TYPE_DIVIDER)) {
                        listSize++;
                    }

                    dividerPos.put(dividerType, listSize);
                }
            }
        }
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