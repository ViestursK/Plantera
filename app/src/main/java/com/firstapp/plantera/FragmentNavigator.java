package com.firstapp.plantera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentNavigator {

    public static Fragment selectFragment(int itemId, boolean isSignedIn) {
        if (itemId == R.id.navigation_home && isSignedIn) {
            return new HomeFragment();
        } else if (itemId == R.id.navigation_search) {
            return new SearchFragment();
        } else if (itemId == R.id.navigation_calendar) {
            return new CalendarFragment();
        } else if (itemId == R.id.navigation_settings) {
            return new SettingsFragment();
        } else {
            return null;
        }
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }
}
