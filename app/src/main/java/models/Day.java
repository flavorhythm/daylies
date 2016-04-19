package models;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class Day {
	/***********************************************************************************************
	 * GLOBAL VARIABLES
	 **********************************************************************************************/
	private int id;
	private int type;
	private long date;
	private DayName day;
	private boolean hasTodos;

	/***********************************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************************/
	public Day() {}

	/***********************************************************************************************
	 * GETTER METHODS
	 **********************************************************************************************/
	public int getId() {return id;}
	public int getType() {return type;}
	public long getDate() {return date;}
	public DayName getDay() {return day;}
	public boolean getHasTodos() {return hasTodos;}

	/***********************************************************************************************
	 * SETTER METHODS
	 **********************************************************************************************/
	public void setId(int id) {this.id = id;}
	public void setType(int type) {this.type = type;}
	public void setDate(long date) {this.date = date;}
	public void setDay(DayName day) {this.day = day;}
	public void setHasTodos(boolean hasTodos) {this.hasTodos = hasTodos;}
}
