package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyuki on 2/26/2016.
 */
public class Day {
	private int id;
	private long date;
	private int year;
	private int weekNum;
	private DayName day;

	public Day() {}

	public int getId() {return id;}
	public long getDate() {return date;}
	public int getYear() {return year;}
	public int getWeekNum() {return weekNum;}
	public DayName getDay() {return day;}

	public void setId(int id) {this.id = id;}
	public void setDate(long date) {this.date = date;}
	public void setYear(int year) {this.year = year;}
	public void setWeekNum(int weekNum) {this.weekNum = weekNum;}
	public void setDay(DayName day) {this.day = day;}

	private List<ToDo> toDoList = new ArrayList<>();

	public List<ToDo> getToDoList() {return toDoList;}
	public void setToDoList(List<ToDo> toDoList) {this.toDoList = toDoList;}
	public void addToDo(int type, String item) {toDoList.add(new ToDo(type, item));}
}
