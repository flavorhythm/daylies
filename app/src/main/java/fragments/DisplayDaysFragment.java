package fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import adapters.DaysAdapter;
import models.DayName;
import utils.Constant;

import static utils.Constant.Fragment.*;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DisplayDaysFragment extends Fragment implements AdapterView.OnItemClickListener {
    private DaysAdapter daysAdapter;

    public DisplayDaysFragment() {}

    //Runs before onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysAdapter = new DaysAdapter(getActivity(), getContext());

        if(!getArguments().isEmpty()) {
            buildWeek(
                    getArguments().getInt(BUNDLE_KEY_YEAR),
                    getArguments().getInt(BUNDLE_KEY_WEEK)
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.fragment_days;
        View customView = inflater.inflate(layoutRes, container, false);

        GridView dayGridView = (GridView)customView.findViewById(R.id.fragDays_grid);
        dayGridView.setAdapter(daysAdapter);

        dayGridView.setOnItemClickListener(this);

        return customView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int dayType = daysAdapter.getItemViewType(position);

        if(dayType != Constant.Adapter.TYPE_DIVIDER) {
            ((MainActivity)getActivity()).showToDoList(daysAdapter.getItem(position));
        }
    }

    public void notifyDataSetChanged(DayName dayName, boolean hasTodos) {
        final int error = -1;
        int position = daysAdapter.getPosByDay(dayName);

        if(position != error) {
            daysAdapter.getItem(position).setHasTodos(hasTodos);
            daysAdapter.notifyDataSetChanged();
        }
    }

    public void buildWeek(int currentYear, int currentWeek) {
        daysAdapter.finishBuildingWeek(currentYear, currentWeek);
        daysAdapter.notifyDataSetChanged();
    }
}
