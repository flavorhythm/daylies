package com.example.zyuki.daylies;

import android.app.Application;

import java.sql.SQLException;

import data.DataAccessObject;

/**
 * Created by zyuki on 3/1/2016.
 */
public class ApplicationDatabase extends Application {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Public variables**/
    public DataAccessObject dataAccess;

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    public void onCreate() {
        super.onCreate();

        dataAccess = new DataAccessObject(getApplicationContext());
        openAndTry();
    }

    /****/
    @Override
    public void onTerminate() {
        super.onTerminate();

        dataAccess.close();
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private void openAndTry() {
        try {
            dataAccess.open();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
