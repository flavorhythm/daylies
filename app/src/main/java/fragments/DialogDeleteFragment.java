package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;

import com.example.zyuki.daylies.R;

import static utils.Constant.Fragment.*;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DialogDeleteFragment extends DialogFragment implements DialogInterface.OnClickListener {
    /***********************************************************************************************
     * GLOBAL VARIABLES
     **********************************************************************************************/
    /****/
    private DelMethod dataPass;
    /***********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/
    /****/
    public static DialogDeleteFragment newInstance(int itemPos) {
        DialogDeleteFragment dialogFragment = new DialogDeleteFragment();
        Bundle args = new Bundle();

        args.putInt(BUNDLE_KEY_ITEM_POS, itemPos);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    /***********************************************************************************************
     * OVERRIDE METHODS
     **********************************************************************************************/
    /****/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataPass = (DelMethod)context;
    }

    /****/
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

    /****/
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_deleteDialog)
                .setPositiveButton(R.string.button_delete, this)
                .setNegativeButton(R.string.button_cancel, this)
                .create();
    }

    /****/
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case Dialog.BUTTON_POSITIVE:
                int position = getArguments().getInt(BUNDLE_KEY_ITEM_POS);

                dataPass.removeItem(position);

                dialog.dismiss();
                break;
            case Dialog.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }

    public interface DelMethod {
        void removeItem(int position);
    }
}
