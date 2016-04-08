package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import adapters.WeeksAdapter;
import models.WeeksInYear;
import utils.DateCalcs;

/**
 * Created by zyuki on 3/29/2016.
 */
public class DisplayWeeksFragment extends Fragment implements AdapterView.OnItemClickListener {
    private WeeksAdapter weeksAdapter;

    private DataFromWeeks dataPass;

    public DisplayWeeksFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (DataFromWeeks)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weeksAdapter = new WeeksAdapter(getActivity(), getContext());
        buildYear(DateCalcs.getCurrentYear());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.fragment_weeks;
        View customView = inflater.inflate(layoutRes, container, false);

        GridView weeksGrid = (GridView)customView.findViewById(R.id.fragWeeks_grid);
        weeksGrid.setAdapter(weeksAdapter);

        weeksGrid.setOnItemClickListener(this);

        return customView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeeksInYear weeksInYear = weeksAdapter.getItem(position);
        int year = weeksInYear.getYear();
        int week = weeksInYear.getWeekNum();

        dataPass.dataFromWeeks(year, week);

        ((MainActivity)getActivity()).changePage(R.string.daysFragTitle);
    }

    public void notifyDataSetChanged(int week, boolean hasTodos) {
        final int error = -1;
        int position = weeksAdapter.getPosByWeek(week);

        if(position != error) {
            weeksAdapter.getItem(position).setHasTodos(hasTodos);
            weeksAdapter.notifyDataSetChanged();
        }
    }

    public void buildYear(int year) {
        weeksAdapter.buildWeeksInYear(year);
        weeksAdapter.notifyDataSetChanged();
    }

    public interface DataFromWeeks {
        void dataFromWeeks(int year, int week);
    }
}
