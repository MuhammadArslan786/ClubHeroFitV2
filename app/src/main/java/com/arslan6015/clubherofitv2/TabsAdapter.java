package com.arslan6015.clubherofitv2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.arslan6015.clubherofitv2.ui.HitFragment;
import com.arslan6015.clubherofitv2.ui.HpFragment;
import com.arslan6015.clubherofitv2.ui.MovFragment;
import com.arslan6015.clubherofitv2.ui.StrFragment;
import com.arslan6015.clubherofitv2.ui.home.HomeFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
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
                HpFragment hp = new HpFragment();
                return hp;
            case 1:
                StrFragment str = new StrFragment();
                return str;
            case 2:
                HitFragment hit = new HitFragment();
                return hit;
            case 3:
                MovFragment mov = new MovFragment();
                return mov;
            default:
                return null;
        }
    }
}