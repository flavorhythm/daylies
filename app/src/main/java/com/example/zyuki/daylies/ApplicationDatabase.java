package com.example.zyuki.daylies;

import android.app.Application;

import java.sql.SQLException;

import data.DataAccessObject;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to open and close the database connection once each, on creation of the app and on
 * termination of the app.
 * This class is used in the Manifest file to populate the "name" attribute of the application tag
 **************************************************************************************************/
public class ApplicationDatabase extends Application {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /**Public variables**/
    public DataAccessObject dataAccess;

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /**Override method that runs when application starts (from extending Application, required)**/
    /**Database access point is opened once for the lifecycle of the app**/
    @Override
    public void onCreate() {
        //Calls this method's Super
        super.onCreate();

        //Instantiates the data access point class
        dataAccess = new DataAccessObject(getApplicationContext());
        //Opens the database
        openAndTry();
    }

    /**Override method that terminates the database access point**/
    /**(from extending Application, optional)**/
    /**This occurs once the lifecycle of the app goes to terminate**/
    @Override
    public void onTerminate() {
        //Calls this method's Super
        super.onTerminate();

        //Closes the access point
        dataAccess.close();
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /**Private method that catches an SQL exception if opening the database goes wrong**/
    /**Used in override method onCreate**/
    private void openAndTry() {
        //Try block to catch an SQL exception
        try {
            //Opens the database
            dataAccess.open();
        //Catch block to catch an SQL exception
        } catch(SQLException e) {
            //Prints the stack trace of the exception
            e.printStackTrace();
        }
    }
}
