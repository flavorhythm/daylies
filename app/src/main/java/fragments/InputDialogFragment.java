package fragments;

import android.app.Dialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
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
        int layoutRes = R.layout.todo_input_dialog;
        customLayout = inflater.inflate(layoutRes, container, false);

        findViewByIds();
        setListeners();
        showHideButtons();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return customLayout;
    }

    @Override
    public void onClick(View v) {
        String itemText;

        switch(v.getId()) {
            case R.id.inputDialog_btn_lunch:
                if(!TextUtils.isEmpty(newToDoItem.getText())) {
                    itemText = newToDoItem.getText().toString();
                    ((MainActivity)getActivity()).putLunchItem(itemText);

                    getDialog().dismiss();
                } else {newItemWrapper.setError("cannot be blank");}
                break;
            case R.id.inputDialog_btn_daily:
                if(!TextUtils.isEmpty(newToDoItem.getText())) {
                    itemText = newToDoItem.getText().toString();
                    ((MainActivity)getActivity()).putDailyItem(itemText);

                    getDialog().dismiss();
                } else {newItemWrapper.setError("cannot be blank");}
                break;
            case R.id. inputDialog_btn_cancel:
                getDialog().dismiss();
                break;
        }
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

    private void showHideButtons() {
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
