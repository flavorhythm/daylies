package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<ToDo> getToDoList(final String yearWeekDay) {
        List<ToDo> toDoList = new ArrayList<>();

        Cursor cursor = db.query(
                TblToDo.TABLE_NAME.toString(),
                TblToDo.ALL_COLUMNS,
                TblToDo.YEAR_WEEK_DAY + " =?",
                new String[] {yearWeekDay},
                null,
                null,
                TblToDo.KEY_ID + " ASC"
        );

        if(cursor.moveToFirst()) {
            do {
                ToDo item = new ToDo();

                item.setId(cursor.getInt(cursor.getColumnIndex(TblToDo.KEY_ID.toString())));
                item.setYearWeekDay(cursor.getString(cursor.getColumnIndex(TblToDo.YEAR_WEEK_DAY.toString())));
                item.setType(cursor.getInt(cursor.getColumnIndex(TblToDo.TODO_TYPE.toString())));
                item.setItem(cursor.getString(cursor.getColumnIndex(TblToDo.TODO_ITEM.toString())));

                toDoList.add(item);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return toDoList;
    }

    public long putToDoItem(ToDo item) {
        ContentValues values = new ContentValues();
        values.put(TblToDo.YEAR_WEEK_DAY.toString(), item.getYearWeekDay());
        values.put(TblToDo.TODO_TYPE.toString(), item.getType());
        values.put(TblToDo.TODO_ITEM.toString(), item.getItem());

        return db.insert(TblToDo.TABLE_NAME.toString(), null, values);
    }

    public int deleteToDoItem(int id) {
        return db.delete(TblToDo.TABLE_NAME.toString(),
                TblToDo.KEY_ID + " =?",
                new String[] {String.valueOf(id)}
        );
    }
}
