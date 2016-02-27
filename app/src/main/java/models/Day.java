package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyuki on 2/26/2016.
 */
public class Day {
	protected static final int TODO_TYPE = 0;
	protected static final int LUNCH_TYPE = 1;

	protected long date;
	protected int year;
	protected int weekNum;
	protected DayName day;

	public long getDate() {return date;}
	public int getYear() {return year;}
	public int getWeekNum() {return weekNum;}
	public DayName getDay() {return day;}

	public void setDate(long date) {this.date = date;}
	public void setYear(int year) {this.year = year;}
	public void setWeekNum(int weekNum) {this.weekNum = weekNum;}
	public void setDay(DayName day) {this.day = day;}

	protected List<ToDo> toDoList = new ArrayList<>();

	public List<ToDo> getToDoList() {return toDoList;}
	public void addToDo(String item) {toDoList.add(new ToDo(TODO_TYPE, item));}
}

class ToDo {
	private int type;
	private String item;

	public ToDo(int type, String item) {
		this.type = type;
		this.item = item;
	}

	public int getType() {return type;}
	public String getItem() {return item;}

	public void setType(int type) {this.type = type;}
	public void setItem(String item) {this.item = item;}
}
