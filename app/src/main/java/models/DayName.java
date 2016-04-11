package models;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public enum DayName {
	MON("Monday"),
	TUE("Tuesday"),
	WED("Wednesday"),
	THU("Thursday"),
	FRI("Friday"),
	SAT("Saturday"),
	SUN("Sunday");

	String dayName;
	DayName(String dayName) {this.dayName = dayName;}

	@Override
	public String toString() {
		return this.dayName;
	}
}
