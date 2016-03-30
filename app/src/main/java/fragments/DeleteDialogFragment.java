package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.view.ViewGroup;

import com.example.zyuki.daylies.MainActivity;
import com.example.zyuki.daylies.R;

/**
 * Created by zyuki on 3/28/2016.
 */
public class DeleteDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    public static DeleteDialogFragment newInstance(int itemPos) {
        DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
        Bundle args = new Bundle();

        args.putInt(DialogRouter.KEY_ITEMPOS, itemPos);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_deleteDialog)
                .setPositiveButton(R.string.button_delete, this)
                .setNegativeButton(R.string.button_cancel, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case Dialog.BUTTON_POSITIVE:
                int position = getArguments().getInt(DialogRouter.KEY_ITEMPOS);
//                ((MainActivity)getActivity()).deleteItem(position);

                dialog.dismiss();
                break;
            case Dialog.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }
}
