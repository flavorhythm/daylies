package data;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zyuki on 2/26/2016.
 */
public enum TblDaily {
	TABLE_NAME("daily_table"),
	KEY_ID("_id"),
	DATE("date"),
	YEAR("year"),
	WEEK_NUM("week_number"),
	DAY("day");

	String colName;
	TblDaily(String colName) {this.colName = colName;}

	@Override
	public String toString() {return this.colName;}

	private static final String CREATE_DAILY_TBL = "CREATE TABLE " + TABLE_NAME.toString() + "(" +
			KEY_ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DATE.toString() + " INTEGER NOT NULL, " +
			YEAR.toString() + " INTEGER NOT NULL, " +
			WEEK_NUM.toString() + " INTEGER NOT NULL, " +
			DAY.toString() + " INTEGER NOT NULL);";

	public static void onCreate(SQLiteDatabase db) {db.execSQL(CREATE_DAILY_TBL);}

	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
