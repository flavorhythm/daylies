package models;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class Day {
	private int id;
	private int type;
	private long date;
	private int year;
	private DayName day;
	private boolean hasTodos;

	public Day() {}

	public int getId() {return id;}
	public int getType() {return type;}
	public long getDate() {return date;}
	public int getYear() {return year;}
	public DayName getDay() {return day;}
	public boolean getHasTodos() {return hasTodos;}

	public void setId(int id) {this.id = id;}
	public void setType(int type) {this.type = type;}
	public void setDate(long date) {this.date = date;}
	public void setYear(int year) {this.year = year;}
	public void setDay(DayName day) {this.day = day;}
	public void setHasTodos(boolean hasTodos) {this.hasTodos = hasTodos;}
}
