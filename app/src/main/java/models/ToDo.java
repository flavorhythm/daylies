package models;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class ToDo {
    private int id;
    private String yearWeekDay;
    private int type;
    private String item;

    public ToDo() {}

    public ToDo(String yearWeekDay, int type, String item) {
        this.yearWeekDay = yearWeekDay;
        this.type = type;
        this.item = item;
    }

    public ToDo(int type, String item) {
        this.yearWeekDay = null;
        this.type = type;
        this.item = item;
    }

    public int getId() {return id;}
    public String getYearWeekDay() {return yearWeekDay;}
    public int getType() {return type;}
    public String getItem() {return item;}

    public void setId(int id) {this.id = id;}
    public void setYearWeekDay(String yearWeekDay) {this.yearWeekDay = yearWeekDay;}
    public void setType(int type) {this.type = type;}
    public void setItem(String item) {this.item = item;}
}
