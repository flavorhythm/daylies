package fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.zyuki.daylies.R;
import com.example.zyuki.daylies.WeekPickerActivity;

import utils.DateCalcs;

/**
 * Created by Flavorhythm on 3/3/2016.
 */
public class PickerDialogFragment extends DialogFragment implements View.OnClickListener {
	private NumberPicker yearPicker;

	public static PickerDialogFragment newInstance() {
	        return new PickerDialogFragment();
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

		cancelBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);

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
