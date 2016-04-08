package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by Flavorhythm on 3/3/2016.
 */
public class DialogRouter {
	//TODO:aggregate constants
	public static final String DIALOG = "dialog";

	public static final String KEY_DAYTYPE = "dayType";
	public static final int TYPE_WEEKDAY = 0;
	public static final int TYPE_WEEKEND = 1;

	public static final String KEY_ITEMPOS = "itemPos";

	private DialogRouter() {}

	public static void instantiatePickerDialog(Activity activity, int year, int week) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogYearPickFragment pickerDialog = DialogYearPickFragment.newInstance(year, week);
		pickerDialog.show(fragTransaction, DIALOG);
	}

	public static void instantiateInputDialog(Activity activity, int dayType) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogInputFragment inputDialog = DialogInputFragment.newInstance(dayType);
		inputDialog.show(fragTransaction, DIALOG);
	}

	public static void instantiateDeleteDialog(Activity activity, int itemPos) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogDeleteFragment deleteDialog = DialogDeleteFragment.newInstance(itemPos);
		deleteDialog.show(fragTransaction, DIALOG);
	}

	private static FragmentTransaction clearFragments(Activity activity) {
		FragmentManager fragManager = activity.getFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();

		Fragment previousFrag = fragManager.findFragmentByTag(DIALOG);
		if(previousFrag != null) {fragTransaction.remove(previousFrag);}
		fragTransaction.addToBackStack(null);

		return fragTransaction;
	}
}