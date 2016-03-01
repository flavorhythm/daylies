package adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import models.Day;

/**
 * Created by zyuki on 2/26/2016.
 */
public class MainAdapter extends ArrayAdapter<Day> {
    public MainAdapter(Activity activity, int layoutRes, List<Day> dayList) {
        super(activity, layoutRes, dayList);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Day getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Day item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        return row;
    }
}
