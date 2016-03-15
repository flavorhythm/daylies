package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

/**
 * Created by zyuki on 3/8/2016.
 */
public class InputDialogFragment extends DialogFragment {
    public static final String ITEM_TYPE = "itemType";

    public static InputDialogFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putInt(ITEM_TYPE, itemType);

//        InputDialogFragment fragment = new InputDialogFragment();
//        fragment.setArguments(args);

//        return fragment;
        return new InputDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext()).setTitle("Which one?")
                .setMessage("When will you do it?")
                .setPositiveButton("at lunch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).putLunchItem();
                        dismiss();
                    }
                })
                .setNegativeButton("after work", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).putDailyItem();
                        dismiss();
                    }
                })
                .create();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        int layoutRes = R.layout.todo_input_dialog;
//        int itemType = getArguments().getInt(ITEM_TYPE);
//        View customLayout = inflater.inflate(layoutRes, container, false);
//
//
//
//        return customLayout;
//    }
}
