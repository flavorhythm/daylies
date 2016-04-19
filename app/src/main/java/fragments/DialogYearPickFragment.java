package fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import utils.Constant;
import utils.DateCalcs;

import static utils.Constant.Fragment.*;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DialogYearPickFragment extends DialogFragment implements View.OnClickListener {
	/***********************************************************************************************
	 * GLOBAL VARIABLES
	 **********************************************************************************************/
	private NumberPicker yearPicker;

	/***********************************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************************/
	/****/
	//TODO: remove text on todosfrag when year changes
	//TODO: develop custom picker and replace numberpicker
	public static DialogYearPickFragment newInstance(int year, int week) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_YEAR, year);
		args.putInt(BUNDLE_KEY_WEEK, week);

        DialogYearPickFragment fragment = new DialogYearPickFragment();
        fragment.setArguments(args);

        return fragment;
    }

	/***********************************************************************************************
	 * OVERRIDE METHODS
	 **********************************************************************************************/
	@Override
	public void onResume() {
		super.onResume();

		Dialog dialog = getDialog();

		if(dialog != null) {
			dialog.getWindow().setLayout(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final int dateRange = 5;

		int layoutRes = R.layout.year_picker_dialog;
		View customLayout = inflater.inflate(layoutRes, container, false);

		yearPicker = (NumberPicker)customLayout.findViewById(R.id.dialog_year_picker);
		Button cancelBtn = (Button)customLayout.findViewById(R.id.dialog_butn_cancel);
		Button submitBtn = (Button)customLayout.findViewById(R.id.dialog_butn_submit);

		yearPicker.setMinValue(DateCalcs.getCurrentYear());
		yearPicker.setMaxValue(DateCalcs.getCurrentYear() + dateRange);
        yearPicker.setValue(getArguments().getInt(BUNDLE_KEY_YEAR));

		cancelBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return customLayout;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.dialog_butn_cancel:
				getDialog().dismiss();
				break;
			case R.id.dialog_butn_submit:
				SharedPreferences.Editor prefEdit = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
				prefEdit.putInt(Constant.Prefs.PREF_KEY_YEAR, yearPicker.getValue());
				prefEdit.apply();

				((MainActivity)getActivity()).dataFromWeeks();

				getDialog().dismiss();
				break;
		}
	}
}
