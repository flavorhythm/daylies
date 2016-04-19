package data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Day;
import models.DayName;
import models.ToDo;
import utils.DateCalcs;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DataAccessObject {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /****/
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public DataAccessObject(Context context) {dbHelper = DatabaseHelper.getInstance(context);}

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public void open() throws SQLException {db = dbHelper.getWritableDatabase();}

    /****/
    public void close() {db.close();}

    /****/
    public List<ToDo> getToDoList(final String yearWeekDay) {
        List<ToDo> toDoList = new ArrayList<>();

        Cursor cursor = getCursor(yearWeekDay);

        if(cursor.moveToFirst()) {
            do {
                ToDo item = new ToDo();

                //
                item.setId(
                        cursor.getInt(cursor.getColumnIndex(TblToDo.KEY_ID.toString()))
                );
                //
                item.setYearWeekDay(
                        cursor.getString(cursor.getColumnIndex(TblToDo.YEAR_WEEK_DAY.toString()))
                );
                //
                item.setType(
                        cursor.getInt(cursor.getColumnIndex(TblToDo.TODO_TYPE.toString()))
                );
                //
                item.setItem(
                        cursor.getString(cursor.getColumnIndex(TblToDo.TODO_ITEM.toString()))
                );

                toDoList.add(item);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return toDoList;
    }

    /****/
    public boolean dayHasTodos(final String yearWeekDay) {
        Cursor cursor = getCursor(yearWeekDay);

        boolean hasTodos = cursor.moveToFirst();
        cursor.close();

        return hasTodos;
    }

    /****/
    public boolean weekHasTodos(int year, int week) {
        final int daysInWeek = 7;

        for(int i = 0; i < daysInWeek; i++) {
            String yearWeekDay = DateCalcs.buildDateString(year, week, DayName.values()[i]);
            if(dayHasTodos(yearWeekDay)) {
                return true;
            }
        }

        return false;
    }

    /****/
    public long addToDoItem(ToDo item) {
        ContentValues values = new ContentValues();
        values.put(TblToDo.YEAR_WEEK_DAY.toString(), item.getYearWeekDay());
        values.put(TblToDo.TODO_TYPE.toString(), item.getType());
        values.put(TblToDo.TODO_ITEM.toString(), item.getItem());

        return db.insert(TblToDo.TABLE_NAME.toString(), null, values);
    }

    /****/
    public int deleteToDoItem(int id) {
        return db.delete(TblToDo.TABLE_NAME.toString(),
                TblToDo.KEY_ID + " =?",
                new String[] {String.valueOf(id)}
        );
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private Cursor getCursor(final String yearWeekDay) {
        return db.query(
                TblToDo.TABLE_NAME.toString(),
                TblToDo.ALL_COLUMNS,
                TblToDo.YEAR_WEEK_DAY + " =?",
                new String[] {yearWeekDay},
                null,
                null,
                TblToDo.KEY_ID + " ASC"
        );
    }
}
