package fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Flavorhythm on 3/3/2016.
 */
public class DialogRouter {
	public static final String DIALOG = "dialog";
	private DialogRouter() {}

	public static void instantiatePickerDialog(Activity activity, int year) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		PickerDialogFragment pickerDialog = PickerDialogFragment.newInstance(year);
		pickerDialog.show(fragTransaction, DIALOG);
	}

	public static void instantiateInputDialog(Activity activity, int toDoType) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		InputDialogFragment inputDialog = InputDialogFragment.newInstance(toDoType);
		inputDialog.show(fragTransaction, DIALOG);
	}

	public static FragmentTransaction clearFragments(Activity activity) {
		FragmentManager fragManager = ((FragmentActivity)activity).getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();

		Fragment previousFrag = fragManager.findFragmentByTag(DIALOG);
		if(previousFrag != null) {fragTransaction.remove(previousFrag);}
		fragTransaction.addToBackStack(null);

		return fragTransaction;
	}
}