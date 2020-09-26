package com.arslan6015.clubherofitv2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.arslan6015.clubherofitv2.ui.ClassesFragment;
import com.arslan6015.clubherofitv2.ui.HitFragment;
import com.arslan6015.clubherofitv2.ui.HpFragment;
import com.arslan6015.clubherofitv2.ui.MovFragment;
import com.arslan6015.clubherofitv2.ui.StrFragment;
import com.arslan6015.clubherofitv2.ui.SuggestionFragment;
import com.arslan6015.clubherofitv2.ui.UpcomingWorkoutFragment;

public class TabsAdapterClasses extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapterClasses(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                ClassesFragment classesFragment = new ClassesFragment();
                return classesFragment;
            case 1:
                SuggestionFragment suggestionFragment = new SuggestionFragment();
                return suggestionFragment;
            case 2:
                UpcomingWorkoutFragment upcomingWorkoutFragment = new UpcomingWorkoutFragment();
                return upcomingWorkoutFragment;

            default:
                return null;
        }
    }
}