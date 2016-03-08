package fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

	public static PickerDialogFragment newInstance(int year) {
        Bundle args = new Bundle();
        args.putInt(DateCalcs.YEAR_KEY, year);

        PickerDialogFragment fragment = new PickerDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int layoutRes = R.layout.year_picker_dialog;
		View customLayout = inflater.inflate(layoutRes, container, false);

		yearPicker = (NumberPicker)customLayout.findViewById(R.id.dialog_year_picker);
		Button cancelBtn = (Button)customLayout.findViewById(R.id.dialog_butn_cancel);
		Button submitBtn = (Button)customLayout.findViewById(R.id.dialog_butn_submit);

		yearPicker.setMinValue(DateCalcs.getCurrentYear());
		yearPicker.setMaxValue(DateCalcs.getCurrentYear() + 30);
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
				activity.selectedYear(yearPicker.getValue());
				getDialog().dismiss();
				break;
		}
	}
}
