package utils;

import android.util.Log;

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

	private static final int WEEKS_IN_YEAR = 52;
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

    public static final boolean isCurrentYearWeek(int weekNum, long dateInMillis) {

        int currentYear = getCurrentYear();
        int currentWeek = getCurrentWeek(currentYear);

        Calendar compareDate = Calendar.getInstance();
        compareDate.setTimeInMillis(dateInMillis);

        int compareYear = compareDate.get(YEAR);
        int compareWeek = getWeekTest(compareDate);
		//int compareWeek = weekNum;

        if(currentYear == compareYear) {
            return currentWeek == compareWeek;
        } else {return false;}
    }

    public static final Calendar getDateOfWeek(int year, int week) {
		final int weekOffset = 1;

        Calendar current = Calendar.getInstance();
        current.set(YEAR, year);

		//TODO:change the code so it doedsn't run 52 times
        Calendar firstOfYear = findFirstDate(current);

		if(week > weekOffset) {
			firstOfYear.add(DAY_OF_YEAR, (week - weekOffset) * DAYS_IN_WEEK);
		}

        return firstOfYear;
    }

    public static final String buildDateString(int year, int weekNum, DayName day) {
        return String.valueOf(year) + String.valueOf(weekNum) + String.valueOf(day.ordinal());
    }

    private static final int getWeek(Calendar cal) {
		//TODO: when the very first date is passed into here,
		//the one that starts on the year prior to the displayed year
		//it is counted as the last date.
        Calendar firstOfYear = findFirstDate(cal);

        int weekNum = 0;

        while(cal.compareTo(firstOfYear) == 1 || cal.compareTo(firstOfYear) == 0) {
            weekNum++;
			int daysInMillis = DAYS_IN_WEEK * 24 * 60 * 60 * 1000;
            firstOfYear.add(MILLISECOND, daysInMillis);
        }

        return weekNum;
    }

	//TODO: need to accept year instead o calendar
	private static final int getWeekTest(Calendar cal) {
		Calendar firstOfYear = findFirstDate(cal.get(YEAR));

		int weekNum = 0;

		while(cal.compareTo(firstOfYear) == 1 || cal.compareTo(firstOfYear) == 0) {
			Log.v("Date", "firstofyear: " + formatDate(firstOfYear.getTimeInMillis()));
			Log.v("Date", "current: " + formatDate(cal.getTimeInMillis()));
			weekNum++;
			int daysInMillis = DAYS_IN_WEEK * 24 * 60 * 60 * 1000;
			firstOfYear.add(MILLISECOND, daysInMillis);
		}

		return weekNum;
	}

    private static final Calendar findFirstDate(int year) {
        final int firstMonth = 0;
        final int firstDay = 1;
        final int dayOffset = 2; //

        Calendar firstOfYear = Calendar.getInstance();
        firstOfYear.set(year, firstMonth, firstDay);

        int startOfWeek = firstOfYear.get(DAY_OF_WEEK) > 2 ?
				firstOfYear.get(DAY_OF_WEEK) - dayOffset :
				DAYS_IN_WEEK - firstOfYear.get(DAY_OF_WEEK) - dayOffset; //Monday = 2

        firstOfYear.add(DAY_OF_YEAR, -1 * startOfWeek);

        return firstOfYear;
    }
}
