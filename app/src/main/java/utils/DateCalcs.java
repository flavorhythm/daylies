package utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import models.DayName;
import models.WeeksInYear;

import static utils.Constant.Util.*;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DateCalcs {
    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    private DateCalcs() {}

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public static String addZeroToNum(int num) {
        final String leadingZero = "0";
        String numStr = String.valueOf(num);

        return (num < 10) ? (leadingZero + numStr) : numStr;
    }

    /****/
    public static final String formatDate(int returnType, long dateInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateInMillis);

        switch(returnType) {
            case FORMAT_FULL_DATE:
                return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(cal.getTime());
            case FORMAT_MONTH:
                return getMonthName(cal.get(Calendar.MONTH));
            case FORMAT_DAY_OF_MONTH:
                return addZeroToNum(cal.get(Calendar.DAY_OF_MONTH));
            default:
                return "Corrupt data";
        }
    }

    /****/
    public static final int getCurrentYear() {
        Calendar current = Calendar.getInstance();

        return current.get(Calendar.YEAR);
    }

    /****/
    public static final int getCurrentWeek() {
        Calendar current = Calendar.getInstance();

        return current.get(Calendar.WEEK_OF_YEAR);
    }

    /****/
    public static final int getCurrentDay() {
        int dayOffset = 2;
        Calendar current = Calendar.getInstance();

        return current.get(Calendar.DAY_OF_WEEK) - dayOffset;
    }

    /****/
    public static final boolean isCurrentYearWeek(WeeksInYear thisWeek) {
        int currentYear = getCurrentYear();
        int currentWeek = getCurrentWeek();

        int checkingYear = thisWeek.getYear();
        int checkingWeek = thisWeek.getWeekNum();

        return (currentYear == checkingYear) && (currentWeek == checkingWeek);
    }

    /****/
    public static final boolean isCurrentDay(long dateInMillis) {
        final int getYear = Calendar.YEAR;
        final int getDayOfYear = Calendar.DAY_OF_YEAR;
        Calendar current = Calendar.getInstance();

        Calendar checkDate = Calendar.getInstance();
        checkDate.setTimeInMillis(dateInMillis);

        if(checkDate.get(getYear) == current.get(getYear)) {
            return checkDate.get(getDayOfYear) == current.get(getDayOfYear);
        } else {return false;}
    }

    /****/
    public static final String buildDateString(int year, int weekNum, DayName day) {
        return String.valueOf(year) + addZeroToNum(weekNum) + String.valueOf(day.ordinal());
    }

    /****/
    public static final Calendar endOfLastYear(final int currentYear) {
        final int daysInWeek = 7;
        int previousYear = currentYear - 1;
        int firstWeekOfYear = 1;

        Calendar counterCal = Calendar.getInstance();
        counterCal.set(Calendar.YEAR, previousYear);
        counterCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        counterCal.set(Calendar.WEEK_OF_YEAR, firstWeekOfYear);

        while (currentYear != previousYear) {
            counterCal.add(Calendar.DAY_OF_YEAR, daysInWeek);
            previousYear = counterCal.get(Calendar.YEAR);
        }

        final int oneWeekOffset = -1;
        counterCal.add(Calendar.WEEK_OF_YEAR, oneWeekOffset);

        return counterCal;
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     **********************************************************************************************/
    /****/
    private static final String getMonthName(int monthNum) {
        String month = "month";
        DateFormatSymbols formatSymbols = new DateFormatSymbols();

        String[] months = formatSymbols.getMonths();

        if(monthNum >= 0 && monthNum <= 11) {month = months[monthNum];}

        return month;
    }
}