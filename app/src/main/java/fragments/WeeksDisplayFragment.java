package fragments;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import adapters.WeeksAdapter;
import models.WeeksInYear;

/**
 * Created by zyuki on 3/29/2016.
 */
public class WeeksDisplayFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView weeksList;
    private WeeksAdapter weeksAdapter;

    private DataFromWeeks dataPass;

    public WeeksDisplayFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (DataFromWeeks)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weeksAdapter = new WeeksAdapter(getActivity(), getContext());
        buildYear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.fragment_weeks;
        View customView = inflater.inflate(layoutRes, container, false);

        weeksList = (ListView)customView.findViewById(R.id.fragWeeks_list);
        weeksList.setAdapter(weeksAdapter);

        weeksList.setOnItemClickListener(this);

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

    private void buildYear() {
        weeksAdapter.buildWeeksInYear(2016);
    }

    public interface DataFromWeeks {
        void dataFromWeeks(int year, int week);
    }
}
