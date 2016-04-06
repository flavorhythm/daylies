package utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import models.DayName;
import models.WeeksInYear;

import static java.util.Calendar.*;

/**
 * Created by zyuki on 2/29/2016.
 */
public class DateCalcs {
    public static final String KEY_YEAR = "year";
    public static final String KEY_WEEK = "weekNum";

    public static final int FULL_DATE = 0;

    private DateCalcs() {}

    public static final String addZeroToNum(int num) {
        String numStr = String.valueOf(num);

        return (num < 10) ? ("0" + numStr) : numStr;
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
                return addZeroToNum(cal.get(DAY_OF_MONTH));
            default:
                return "Corrupt data";
        }
    }

    private static final String getMonthName(int monthNum) {
        String month = "month";
        DateFormatSymbols formatSymbols = new DateFormatSymbols();
        String[] months = formatSymbols.getMonths();

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

    public static final boolean isCurrentYearWeek(WeeksInYear thisWeek) {
        int currentYear = getCurrentYear();
        int currentWeek = getCurrentWeek();

        int checkingYear = thisWeek.getYear();
        int checkingWeek = thisWeek.getWeekNum();

        return (currentYear == checkingYear) && (currentWeek == checkingWeek);
    }

    public static final boolean isCurrentDay(long dateInMillis) {
        Calendar current = Calendar.getInstance();

        Calendar checkDate = Calendar.getInstance();
        checkDate.setTimeInMillis(dateInMillis);

        if(checkDate.get(YEAR) == current.get(YEAR)) {
            return checkDate.get(DAY_OF_YEAR) == current.get(DAY_OF_YEAR);
        } else {return false;}
    }

    public static final String buildDateString(int year, int weekNum, DayName day) {
        return String.valueOf(year) + addZeroToNum(weekNum) + String.valueOf(day.ordinal());
    }

    public static final Calendar endOfLastYear(final int currentYear) {
        final int daysInWeek = 7;
        int previousYear = currentYear - 1;
        int weekOfYear = 1;

        Calendar counterCal = Calendar.getInstance();
        counterCal.set(YEAR, previousYear);
        counterCal.set(DAY_OF_WEEK, MONDAY);
        counterCal.set(WEEK_OF_YEAR, weekOfYear);

        while (currentYear != previousYear) {
            counterCal.add(DAY_OF_YEAR, daysInWeek);
            previousYear = counterCal.get(YEAR);
        }

        final int oneWeekOffset = -1;
        counterCal.add(WEEK_OF_YEAR, oneWeekOffset);

        return counterCal;
    }
}