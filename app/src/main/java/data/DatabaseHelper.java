package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DatabaseHelper extends SQLiteOpenHelper {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Private variables**/
    private static DatabaseHelper thisInstance;

    private static final String DATABASE_NAME = "daily_db";
    private static final int DATABASE_VERSION = 4;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(thisInstance == null) {thisInstance = new DatabaseHelper(context);}

        return thisInstance;
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    public void onCreate(SQLiteDatabase db) {
        TblToDo.onCreate(db);
    }

    /****/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TblToDo.onUpgrade(db);
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
