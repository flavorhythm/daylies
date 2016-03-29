package fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.zyuki.daylies.R;
import com.example.zyuki.daylies.WeekPickerActivity;

import utils.DateCalcs;

/**
 * Created by Flavorhythm on 3/3/2016.
 */
public class PickerDialogFragment extends DialogFragment implements View.OnClickListener {
	private NumberPicker yearPicker;

	//TODO: develop custom picker and replace numberpicker
	public static PickerDialogFragment newInstance(int year) {
        Bundle args = new Bundle();
        args.putInt(DateCalcs.YEAR_KEY, year);

        PickerDialogFragment fragment = new PickerDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

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
        yearPicker.setValue(getArguments().getInt(DateCalcs.YEAR_KEY));

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
				WeekPickerActivity activity = (WeekPickerActivity)getActivity();
				activity.displaySelectedYear(yearPicker.getValue());

				getDialog().dismiss();
				break;
		}
	}
}
