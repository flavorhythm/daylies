package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyuki on 2/26/2016.
 */
public class Weekday extends Day {
	protected List<ToDo> lunchList = new ArrayList<>();

	public List<ToDo> getToDoList() {return lunchList;}
	public void addToDo(String item) {lunchList.add(new ToDo(TODO_TYPE, item));}
}
