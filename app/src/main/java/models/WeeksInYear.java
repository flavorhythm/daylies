package models;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class WeeksInYear {
    private int year;
    private int weekNum;
    private String month;
    private boolean hasTodos;

    public int getYear() {return year;}
    public int getWeekNum() {return weekNum;}
    public String getMonth() {return month;}
    public boolean getHasTodos() {return hasTodos;}

    public void setYear(int year) {this.year = year;}
    public void setWeekNum(int weekNum) {this.weekNum = weekNum;}
    public void setMonth(String month) {this.month = month;}
    public void setHasTodos(boolean hasTodos) {this.hasTodos = hasTodos;}
}
