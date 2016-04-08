package models;

/**
 * Created by zyuki on 2/29/2016.
 */
public class ToDo {
    public static final int DIVIDER_LUNCH = 1;
    public static final int DIVIDER_WORK = 2;
    public static final int DIVIDER_DAILY = 3;
    public static final int CONTENT_LUNCH = 4;
    public static final int CONTENT_WORK = 5;
    public static final int CONTENT_DAILY = 6;

    public static final int TYPE_TODO = 0;
    public static final int TYPE_LUNCH = 1;
    public static final int TYPE_HEADER = 2;

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
