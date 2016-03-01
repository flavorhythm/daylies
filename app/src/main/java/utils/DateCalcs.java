package utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.util.Calendar.*;

/**
 * Created by zyuki on 2/29/2016.
 */
public class DateCalcs {
    public static final int getCurrentWeek(int year) {
        final int firstMonth = 0;
        final int firstDay = 1;
        final int dayOffset = 2;
        final int daysInWeek = 7;

        Calendar firstOfYear = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        firstOfYear.set(year, firstMonth, firstDay);

        int startOfWeek = firstOfYear.get(DAY_OF_WEEK) - dayOffset;
        firstOfYear.add(DAY_OF_MONTH, -1 * startOfWeek);

        int weekNum = 0;
        while(current.compareTo(firstOfYear) != 1) {
            weekNum++;
            firstOfYear.add(DAY_OF_MONTH, daysInWeek);
        }

        return weekNum;
    }

    public static final Map<String, Integer> getThisWeek(long dateInMillis) {
        Map<String, Integer> yearWeek = new HashMap<>();
        
    }
}
