package models;

/**
 * Created by zyuki on 2/29/2016.
 */
public class ToDo {
    public static final int TODO_TYPE = 0;
    public static final int LUNCH_TYPE = 1;

    private int type;
    private String item;

    public ToDo() {}

    public ToDo(int type, String item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {return type;}
    public String getItem() {return item;}

    public void setType(int type) {this.type = type;}
    public void setItem(String item) {this.item = item;}
}
