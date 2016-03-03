package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Day;
import models.DayName;
import models.WeeksInYear;

/**
 * Created by zyuki on 3/2/2016.
 */
public class ListBuilder {

	public static List<Day> finishBuildingWeek(int year, int weekNum) {
		List<Day> week = new ArrayList<>();

		Calendar firstDateOfWeek;
        firstDateOfWeek = DateCalcs.getDateOfWeek(year, weekNum);

        final int daysInWeek = 7;
        for(int i = 0; i < daysInWeek; i++) {
            Day day = new Day();

            day.setDate(firstDateOfWeek.getTimeInMillis());
            day.setYear(year);
            day.setWeekNum(weekNum);
            day.setDay(DayName.values()[i]);
//            day.setToDoList(dataAccess.getToDoList(year, weekNum, i));

            firstDateOfWeek.add(Calendar.DAY_OF_YEAR, 1);
            week.add(day);
        }

		return week;
	}

    public static List<WeeksInYear> buildWeeksInYear(int year) {
        final int weekNumInYear = 52;

        List<WeeksInYear> weeksInYear = new ArrayList<>();

        for(int i = 1; i <= weekNumInYear; i++) {
            WeeksInYear week = new WeeksInYear();
            Calendar cal = DateCalcs.getDateOfWeek(year, i);

            week.setWeekNum(i);
            week.setDate(cal.getTimeInMillis());

            weeksInYear.add(week);
        }

        return weeksInYear;
    }
}
