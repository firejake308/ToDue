package com.insertcoolnamehere.todue;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * An adapter that displays one of two task lists
 */

public class TaskListsPagerAdapter extends FragmentPagerAdapter {

    public TaskListsPagerAdapter(FragmentManager fm) {super(fm);}

    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new DailyTaskListFragment();
            default:
                return null;
        }
    }

    public int getCount() {
        return 1;
    }
}
