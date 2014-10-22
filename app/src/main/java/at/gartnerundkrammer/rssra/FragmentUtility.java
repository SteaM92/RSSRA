package at.gartnerundkrammer.rssra;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.List;

public class FragmentUtility {

    public static void changeFragment(Activity activity, Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_place, fragment);
        transaction.addToBackStack(((Object) fragment).getClass().getName());
        transaction.commit();
    }

    public static void changeToLastFragment(Activity activity) {
        activity.getFragmentManager().popBackStack();
    }
}
