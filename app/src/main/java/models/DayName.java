package models;

/**
 * Created by Flavorhythm on 2/27/2016.
 */
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
