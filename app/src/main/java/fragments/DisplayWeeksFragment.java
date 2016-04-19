package fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import utils.Constant;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DisplayWeeksFragment extends Fragment implements AdapterView.OnItemClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /****/
    private WeeksAdapter weeksAdapter;

    private DataFromWeeks dataPass;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public DisplayWeeksFragment() {}

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (DataFromWeeks)context;
    }

    /****/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weeksAdapter = new WeeksAdapter(getActivity());
        buildYear();
    }

    /****/
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

    /****/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeeksInYear weeksInYear = weeksAdapter.getItem(position);

        SharedPreferences.Editor prefEdit = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        prefEdit.putInt(Constant.Prefs.PREF_KEY_WEEK, weeksInYear.getWeekNum());
        prefEdit.apply();

        dataPass.dataFromWeeks();

        ((MainActivity)getActivity()).changePage(R.string.daysFragTitle);
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public void notifyDataSetChanged(boolean hasTodos) {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        int position = weeksAdapter.getPosByWeek(
                prefs.getInt(Constant.Prefs.PREF_KEY_WEEK, Constant.ERROR)
        );

        if(position != Constant.ERROR) {
            weeksAdapter.getItem(position).setHasTodos(hasTodos);
            weeksAdapter.notifyDataSetChanged();
        }
    }

    /****/
    public void buildYear() {
        weeksAdapter.getWeeksInYear();
        weeksAdapter.notifyDataSetChanged();
    }

    /***********************************************************************************************
     * INNER CLASSES & INTERFACES
     **********************************************************************************************/
    /****/
    public interface DataFromWeeks {
        void dataFromWeeks();
    }
}
