package adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zyuki.daylies.R;

import java.util.ArrayList;
import java.util.List;

import models.ToDo;
import models.WeeksInYear;
import utils.DateCalcs;

/**
 * Created by zyuki on 2/26/2016.
 */
public class PickerAdapter extends ArrayAdapter<WeeksInYear> {
    private Activity activity;
    private int layoutRes;
    private List<WeeksInYear> weeksList;

    public PickerAdapter(Activity activity, int layoutRes, List<WeeksInYear> weeksList) {
        super(activity, layoutRes, weeksList);

        this.activity = activity;
        this.layoutRes = layoutRes;
        this.weeksList = weeksList;
    }

    @Override
    public int getCount() {
        return weeksList.size();
    }

    @Override
    public WeeksInYear getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(WeeksInYear item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = LayoutInflater.from(activity).inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.weekNum = (TextView)row.findViewById(R.id.pickerRow_text_weekNum);
            viewHolder.date = (TextView)row.findViewById(R.id.pickerRow_text_date);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        WeeksInYear week = getItem(position);

        String weekNumString = "";

        if(week.getWeekNum() < 10) {
            weekNumString = "0";
        }

        weekNumString += String.valueOf(week.getWeekNum());

        viewHolder.weekNum.setText(weekNumString);
        viewHolder.date.setText(DateCalcs.formatDate(week.getDate()));

        return row;
    }

    class ViewHolder {
        TextView weekNum;
        TextView date;
    }
}
