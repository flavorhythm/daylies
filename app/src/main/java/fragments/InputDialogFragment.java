package fragments;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

import static fragments.DialogRouter.*;

/**
 * Created by zyuki on 3/8/2016.
 */
public class InputDialogFragment extends DialogFragment implements View.OnClickListener {
    //TODO: finish inputdialogfragment with onCreateView
    //TODO: create another dialog to edit/delete item
    private View customLayout;

    private Button lunchToDoBtn, dailyToDoBtn, cancelBtn;
    private EditText newToDoItem;
    private TextInputLayout newItemWrapper;

    public static InputDialogFragment newInstance(int dayType) {
        InputDialogFragment dialogFragment = new InputDialogFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_DAYTYPE, dayType);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.todo_input_dialog;
        customLayout = inflater.inflate(layoutRes, container, false);

        findViewByIds();
        setListeners();

        adjustButtons();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return customLayout;
    }

    @Override
    public void onClick(View v) {
        String itemText;

        switch(v.getId()) {
            case R.id.inputDialog_btn_lunch:
                itemText = newToDoItem.getText().toString();

                ((MainActivity)getActivity()).putLunchItem(itemText);
                break;
            case R.id.inputDialog_btn_daily:
                itemText = newToDoItem.getText().toString();

                ((MainActivity)getActivity()).putDailyItem(itemText);
                break;
            case R.id. inputDialog_btn_cancel:
                break;
        }

        getDialog().dismiss();
    }

    private void findViewByIds() {
        lunchToDoBtn = (Button)customLayout.findViewById(R.id.inputDialog_btn_lunch);
        dailyToDoBtn = (Button)customLayout.findViewById(R.id.inputDialog_btn_daily);
        cancelBtn = (Button)customLayout.findViewById(R.id.inputDialog_btn_cancel);

        newToDoItem = (EditText)customLayout.findViewById(R.id.inputDialog_edit_newItem);
        newItemWrapper = (TextInputLayout)customLayout.findViewById(R.id.inputDialog_textWrap_newItem);
    }

    private void setListeners() {
        lunchToDoBtn.setOnClickListener(this);
        dailyToDoBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void adjustButtons() {
        int type = getArguments().getInt(KEY_DAYTYPE);

        if(type == TYPE_WEEKEND) {
            lunchToDoBtn.setVisibility(View.GONE);
            dailyToDoBtn.setText(getResources().getText(R.string.dailyToDo));
        } else {
            lunchToDoBtn.setVisibility(View.VISIBLE);
            dailyToDoBtn.setText(getResources().getText(R.string.afterWorkToDo));
        }
    }
}
