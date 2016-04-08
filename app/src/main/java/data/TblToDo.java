package data;

import android.database.sqlite.SQLiteDatabase;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * NEEDS COMMENTS
 **************************************************************************************************/
public enum TblToDo {
	/***********************************************************************************************
	 * GLOBAL VARIABLES
	 **********************************************************************************************/
	/**Enums**/
	TABLE_NAME("todo_table"),
	KEY_ID("_id"),
	YEAR_WEEK_DAY("year_week_day"),
	TODO_TYPE("todo_type"),
	TODO_ITEM("todo_item");

	/**Public variables**/
	/**Aggregates all columns into one string array**/
	/**Used in public methods DataAccessObject.getToDoList & DataAccessObject.dayHasTodos**/
	public static final String[] ALL_COLUMNS = new String[] {
			KEY_ID.toString(),
			YEAR_WEEK_DAY.toString(),
			TODO_TYPE.toString(),
			TODO_ITEM.toString()
	};

	/**Private variables**/
	/**Aggregates a create table SQL command**/
	/**Used in public method onCreate**/
	private static final String CREATE_TODO_TBL = "CREATE TABLE " + TABLE_NAME.toString() + "(" +
			KEY_ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			YEAR_WEEK_DAY.toString() + " TEXT NOT NULL, " +
			TODO_TYPE.toString() + " INTEGER NOT NULL, " +
			TODO_ITEM.toString() + " TEXT NOT NULL);";

	/***********************************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************************/
	/**String variable that stores the name of the database columns**/
	String colName;
	/**Constructor that stores the name of the database columns**/
	TblToDo(String colName) {this.colName = colName;}

	/***********************************************************************************************
	 * OVERRIDE METHODS
	 **********************************************************************************************/
	/**Override method that returns the string value of the database column**/
	@Override
	public String toString() {return this.colName;}

	/***********************************************************************************************
	 * PUBLIC METHODS
	 **********************************************************************************************/
	/**Public method that executes the create table SQL command**/
	/**Used in override method DatabaseHelper.onCreate & onUpgrade**/
	public static void onCreate(SQLiteDatabase db) {db.execSQL(CREATE_TODO_TBL);}

	/**Public method that upgrades database if DatabaseHelper's db version changes**/
	/**Used in override method DatabaseHelper.onUpgrade**/
	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME.toString());
		onCreate(db);
	}
}
