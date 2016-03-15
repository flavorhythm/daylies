package utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import models.DayName;

import static java.util.Calendar.*;

/**
 * Created by zyuki on 2/29/2016.
 */
public class DateCalcs {
    public static final String YEAR_KEY = "year";
    public static final String WEEK_NUM_KEY = "weekNum";

    public static final int FULL_DATE = 0;
    public static final int MONTH = Calendar.MONTH;
    public static final int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;

    private DateCalcs() {}

    public static final String addZeroToNum(int weekNum) {
        String weekNumStr = String.valueOf(weekNum);

        return weekNum < 10 ? "0" + weekNumStr : weekNumStr;
    }

    public static final String formatDate(int returnType, long dateInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateInMillis);

        switch(returnType) {
            case FULL_DATE:
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

                return dateFormat.format(cal.getTime());
            case MONTH:
                return getMonthName(cal.get(MONTH));
            case DAY_OF_MONTH:
                return String.valueOf(cal.get(DAY_OF_MONTH));
            default:
                return "Corrupt data";
        }
    }

    private static final String getMonthName(int monthNum) {
        String month = "month";
        DateFormatSymbols formatSymbols = new DateFormatSymbols();
        String[] months = formatSymbols.getShortMonths();

        if(monthNum >= 0 && monthNum <= 11) {month = months[monthNum];}

        return month;
    }

    public static final int getCurrentYear() {
        Calendar current = Calendar.getInstance();

        return current.get(YEAR);
    }

    public static final int getCurrentWeek() {
        Calendar current = Calendar.getInstance();

        return current.get(WEEK_OF_YEAR);
    }

    public static final boolean isCurrentYearWeek(long currentDateInMillis) {
        int currentYear = getCurrentYear();
        int currentWeek = getCurrentWeek();

        Calendar compareDate = Calendar.getInstance();
        compareDate.setTimeInMillis(currentDateInMillis);

        int compareYear = compareDate.get(YEAR);
        int compareWeek = compareDate.get(WEEK_OF_YEAR);

        if(currentYear == compareYear) {
            return currentWeek == compareWeek;
        } else {return false;}
    }

    public static final String buildDateString(int year, int weekNum, DayName day) {
        return String.valueOf(year) + String.valueOf(weekNum) + String.valueOf(day.ordinal());
    }
}
