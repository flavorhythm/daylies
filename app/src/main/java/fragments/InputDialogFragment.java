package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

/**
 * Created by zyuki on 3/8/2016.
 */
public class InputDialogFragment extends DialogFragment {
    //TODO: finish inputdialogfragment with onCreateView
    //TODO: create another dialog to edit/delete items
    private

    public static InputDialogFragment newInstance() {
        return new InputDialogFragment();
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final EditText toDoItem = new EditText(getContext());

//        return new AlertDialog.Builder(getContext()).setTitle("What do you want to do?")
//                .setView(toDoItem)
//                .setMessage("When will you do it?")
//                .setPositiveButton("at lunch", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity)getActivity()).putLunchItem(toDoItem.getText().toString());
//                        dismiss();
//                    }
//                })
//                .setNegativeButton("after work", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity)getActivity()).putDailyItem(toDoItem.getText().toString());
//                        dismiss();
//                    }
//                })
//                .create();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.todo_input_dialog;
        View customLayout = inflater.inflate(layoutRes, container, false);

        getDialog().setTitle("When will you do it?");

        return customLayout;
    }
}
