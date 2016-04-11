package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import static utils.Constant.Fragment.*;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class DialogRouter {
	/***********************************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************************/
	/**Private Variables**/
	private DialogRouter() {}

	/***********************************************************************************************
	 * PUBLIC METHODS
	 **********************************************************************************************/
	/****/
	public static void instantiatePickerDialog(Activity activity, int year, int week) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogYearPickFragment pickerDialog = DialogYearPickFragment.newInstance(year, week);
		pickerDialog.show(fragTransaction, TAG_DIALOG);
	}

	/****/
	public static void instantiateInputDialog(Activity activity, int dayType) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogInputFragment inputDialog = DialogInputFragment.newInstance(dayType);
		inputDialog.show(fragTransaction, TAG_DIALOG);
	}

	/****/
	public static void instantiateDeleteDialog(Activity activity, int itemPos) {
		FragmentTransaction fragTransaction = clearFragments(activity);

		DialogDeleteFragment deleteDialog = DialogDeleteFragment.newInstance(itemPos);
		deleteDialog.show(fragTransaction, TAG_DIALOG);
	}

	/***********************************************************************************************
	 * PRIVATE METHODS
	 **********************************************************************************************/
	/****/
	private static FragmentTransaction clearFragments(Activity activity) {
		FragmentManager fragManager = activity.getFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();

		Fragment previousFrag = fragManager.findFragmentByTag(TAG_DIALOG);
		if(previousFrag != null) {fragTransaction.remove(previousFrag);}
		fragTransaction.addToBackStack(null);

		return fragTransaction;
	}
}