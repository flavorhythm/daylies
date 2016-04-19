package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zyuki.daylies.R;

import adapters.DaysAdapter;
import models.Day;
import models.DayName;
import utils.Constant;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DisplayDaysFragment extends Fragment implements AdapterView.OnItemClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    private DaysAdapter daysAdapter;

    private Callback dataPass;

    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public DisplayDaysFragment() {}

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (Callback)context; //Might not work because it is being implemented in another fragment
    }

    /****/

    //Runs before onCreateView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysAdapter = new DaysAdapter(getActivity());

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean emptyYear = prefs.contains(Constant.Prefs.PREF_KEY_YEAR);
        boolean emptyWeek = prefs.contains(Constant.Prefs.PREF_KEY_WEEK);
        if(emptyYear && emptyWeek) {buildWeek();}
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
            int dayNum = daysAdapter.getItem(position).getDay().ordinal();

            SharedPreferences.Editor prefEdit = getActivity()
                    .getPreferences(Context.MODE_PRIVATE).edit();
            prefEdit.putInt(Constant.Prefs.PREF_KEY_DAY, dayNum);
            prefEdit.apply();

            dataPass.showToDoList(daysAdapter.getItem(position));
        }
    }

    /***********************************************************************************************
     * PUBLIC METHODS
     **********************************************************************************************/
    /****/
    public void notifyDataSetChanged(DayName dayName, boolean hasTodos) {
        int position = daysAdapter.getPosByDay(dayName);

        if(position != Constant.ERROR) {
            daysAdapter.getItem(position).setHasTodos(hasTodos);
            daysAdapter.notifyDataSetChanged();
        }
    }

    /****/
    public void buildWeek() {
        daysAdapter.finishBuildingWeek();
        daysAdapter.notifyDataSetChanged();
    }

    /****/
    public interface Callback {
        void showToDoList(Day day);
    }
}
