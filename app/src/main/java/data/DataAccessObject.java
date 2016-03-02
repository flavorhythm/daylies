package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Day;
import models.DayName;
import models.ToDo;

/**
 * Created by zyuki on 2/26/2016.
 */
public class DataAccessObject {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DataAccessObject(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public List<Day> getWeek(int year, int week) {
        final int daysInWeek = 7;

        List<Day> dayList = new ArrayList<>();

        Cursor cursor = db.query(
                TblDaily.TABLE_NAME.toString(),
                TblDaily.ALL_COLUMNS,
                TblDaily.YEAR + " =? AND " + TblDaily.WEEK_NUM + " =?",
                new String[] {String.valueOf(year), String.valueOf(week)},
                null,
                null,
                TblDaily.DATE + " ASC"
        );

        if(cursor.moveToFirst()) {
            do {
                Day day = new Day();

                day.setId(cursor.getInt(cursor.getColumnIndex(TblDaily.KEY_ID.toString())));
                day.setDate(cursor.getLong(cursor.getColumnIndex(TblDaily.DATE.toString())));
                day.setYear(cursor.getInt(cursor.getColumnIndex(TblDaily.YEAR.toString())));
                day.setWeekNum(cursor.getInt(cursor.getColumnIndex(TblDaily.WEEK_NUM.toString())));
                day.setDay(DayName.values()[cursor.getInt(cursor.getColumnIndex(TblDaily.DAY.toString()))]);

                dayList.add(day);
            } while(cursor.moveToNext());
        }

        cursor.close();

        if(dayList.size() != daysInWeek) {

        }

        return dayList;
    }

    public List<ToDo> getToDoList(int year, int weekNum, int day) {
        final String yearWeekDay = String.valueOf(year) + String.valueOf(weekNum) + String.valueOf(day);

        List<ToDo> toDoList = new ArrayList<>();

        Cursor cursor = db.query(
                TblToDo.TABLE_NAME.toString(),
                TblToDo.ALL_COLUMNS,
                TblToDo.YEAR_WEEK_DAY + " =?",
                new String[] {yearWeekDay},
                null,
                null,
                TblToDo.KEY_ID + " DESC"
        );

        if(cursor.moveToFirst()) {
            do {
                ToDo item = new ToDo();

                item.setYearWeekDay(cursor.getString(cursor.getColumnIndex(TblToDo.YEAR_WEEK_DAY.toString())));
                item.setType(cursor.getInt(cursor.getColumnIndex(TblToDo.TODO_TYPE.toString())));
                item.setItem(cursor.getString(cursor.getColumnIndex(TblToDo.TODO_ITEM.toString())));

                toDoList.add(item);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return toDoList;
    }

    public long putDay(Day day) {
        ContentValues values = new ContentValues();
        values.put(TblDaily.DATE.toString(), day.getDate());
        values.put(TblDaily.YEAR.toString(), day.getYear());
        values.put(TblDaily.WEEK_NUM.toString(), day.getWeekNum());
        values.put(TblDaily.DAY.toString(), day.getDay().ordinal());

        return db.replace(TblDaily.TABLE_NAME.toString(), null, values);
    }

    public long putToDoItem(ToDo item) {
        ContentValues values = new ContentValues();
        values.put(TblToDo.YEAR_WEEK_DAY.toString(), item.getYearWeekDay());
        values.put(TblToDo.TODO_TYPE.toString(), item.getType());
        values.put(TblToDo.TODO_ITEM.toString(), item.getItem());

        return db.insert(TblToDo.TABLE_NAME.toString(), null, values);
    }
}
