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

                day.setToDoList(getToDoList(day.getId()));

                dayList.add(day);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return dayList;
    }

    private List<ToDo> getToDoList(int dayId) {
        List<ToDo> toDoList = new ArrayList<>();

        Cursor cursor = db.query(
                TblToDo.TABLE_NAME.toString(),
                TblToDo.ALL_COLUMNS,
                TblToDo.DAY_KEY + " =?",
                new String[] {String.valueOf(dayId)},
                null,
                null,
                TblToDo.KEY_ID + " DESC"
        );

        if(cursor.moveToFirst()) {
            do {
                ToDo item = new ToDo();

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

    public long putToDoItem(int dayId, ToDo item) {
        ContentValues values = new ContentValues();
        values.put(TblToDo.DAY_KEY.toString(), dayId);
        values.put(TblToDo.TODO_TYPE.toString(), item.getType());
        values.put(TblToDo.TODO_ITEM.toString(), item.getItem());

        return db.insert(TblToDo.TABLE_NAME.toString(), null, values);
    }
}
