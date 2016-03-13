package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.DayName;

import static java.util.Calendar.*;

/**
 * Created by zyuki on 2/29/2016.
 */
public class DateCalcs {
    public static final String YEAR_KEY = "year";
    public static final String WEEK_NUM_KEY = "weekNum";

    private static final int DAYS_IN_WEEK = 7;

    private DateCalcs() {}

    public static final String addZeroToNum(int weekNum) {
        String weekNumStr = "";

        if(weekNum < 10) {
            weekNumStr = "0";
        }

        return weekNumStr + String.valueOf(weekNum);
    }

    public static final String formatDate(long dateInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        return dateFormat.format(cal.getTime());
    }

    public static final int getCurrentYear() {
        Calendar current = Calendar.getInstance();

        return current.get(YEAR);
    }

    public static final int getCurrentWeek(int year) {
        Calendar current = Calendar.getInstance();
        current.set(YEAR, year);

        return getWeek(current);
    }

    public static final Map<String, Integer> getThisWeek(long dateInMillis) {
        Map<String, Integer> yearWeek = new HashMap<>();

        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(dateInMillis);

        yearWeek.put(YEAR_KEY, current.get(YEAR));
        yearWeek.put(WEEK_NUM_KEY, getWeek(current));

        return yearWeek;
    }

    public static final Calendar getDateOfWeek(int year, int week) {
        Calendar current = Calendar.getInstance();
        current.set(YEAR, year);

        Calendar firstOfYear = findFirstDate(current);

        firstOfYear.add(DAY_OF_YEAR, (week - 1) * DAYS_IN_WEEK);

        return firstOfYear;
    }

    public static final String buildDateString(int year, int weekNum, DayName day) {
        return String.valueOf(year) + String.valueOf(weekNum) + String.valueOf(day.ordinal());
    }

    private static final int getWeek(Calendar cal) {
        Calendar firstOfYear = findFirstDate(cal);

        int weekNum = 0;
        while(cal.compareTo(firstOfYear) == 1) {
            weekNum++;
            firstOfYear.add(DAY_OF_MONTH, DAYS_IN_WEEK);
        }

        return weekNum;
    }

    private static final Calendar findFirstDate(Calendar cal) {
        final int firstMonth = 0;
        final int firstDay = 1;
        final int dayOffset = 2;

        Calendar firstOfYear = Calendar.getInstance();
        firstOfYear.set(cal.get(YEAR), firstMonth, firstDay);

        int startOfWeek = firstOfYear.get(DAY_OF_WEEK) - dayOffset;
        firstOfYear.add(DAY_OF_MONTH, -1 * startOfWeek);

        return firstOfYear;
    }
}
