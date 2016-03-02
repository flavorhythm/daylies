package adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zyuki.daylies.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import models.Day;
import utils.DateCalcs;

/**
 * Created by zyuki on 2/26/2016.
 */
public class MainAdapter extends ArrayAdapter<Day> {
    private Activity activity;
    private int layoutRes;
    private List<Day> dayList;

    public MainAdapter(Activity activity, int layoutRes, List<Day> dayList) {
        super(activity, layoutRes, dayList);

        this.activity = activity;
        this.layoutRes = layoutRes;
        this.dayList = dayList;
    }

    @Override
    public int getCount() {
        return dayList.size();
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
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = LayoutInflater.from(activity).inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.date = (TextView)row.findViewById(R.id.mainRow_text_date);
            viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.mainRow_text_dayOfWeek);

            row.setTag(viewHolder);
        } else {viewHolder = (ViewHolder)row.getTag();}

        Day day = getItem(position);

        viewHolder.date.setText(DateCalcs.formatDate(day.getDate()));
        viewHolder.dayOfWeek.setText(day.getDay().toString());

        return row;
    }

    class ViewHolder {
        TextView date;
        TextView dayOfWeek;
    }
}
