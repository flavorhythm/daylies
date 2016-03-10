package utils;

import com.example.zyuki.daylies.ApplicationDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.DataAccessObject;
import models.Day;
import models.DayName;
import models.WeeksInYear;

/**
 * Created by zyuki on 3/2/2016.
 */
public class ListBuilder {

	public static List<Day> finishBuildingWeek(ApplicationDatabase context, int year, int weekNum) {
        final int daysInWeek = 7;

        DataAccessObject dataAccess = context.dataAccess;
		List<Day> week = new ArrayList<>();

		Calendar firstDateOfWeek;
        firstDateOfWeek = DateCalcs.getDateOfWeek(year, weekNum);

        for(int i = 0; i < daysInWeek; i++) {
            Day day = new Day();

            day.setDate(firstDateOfWeek.getTimeInMillis());
            day.setYear(year);
            day.setWeekNum(weekNum);
            day.setDay(DayName.values()[i]);
            day.setToDoList(dataAccess.getToDoList(year, weekNum, i));

            firstDateOfWeek.add(Calendar.DAY_OF_YEAR, 1);
            week.add(day);
        }

		return week;
	}
}
