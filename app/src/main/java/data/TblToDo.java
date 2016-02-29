package data;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zyuki on 2/26/2016.
 */
public enum TblToDo {
	TABLE_NAME("todo_table"),
	KEY_ID("_id"),
	DAY_KEY("day_foreign_id"),
	TODO_TYPE("todo_type"),
	TODO_ITEM("todo_item");

	String colName;
	TblToDo(String colName) {this.colName = colName;}

	@Override
	public String toString() {return this.colName;}

	public static final String[] ALL_COLUMNS = new String[] {
			KEY_ID.toString(),
			DAY_KEY.toString(),
			TODO_TYPE.toString(),
			TODO_ITEM.toString()
	};

	private static final String CREATE_TODO_TBL = "CREATE TABLE " + TABLE_NAME.toString() + "(" +
			KEY_ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DAY_KEY.toString() + " INTEGER NOT NULL, " +
			TODO_TYPE.toString() + " INTEGER NOT NULL, " +
			TODO_ITEM.toString() + " TEXT NOT NULL);";

	public static void onCreate(SQLiteDatabase db) {db.execSQL(CREATE_TODO_TBL);}

	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME.toString());
		onCreate(db);
	}
}
