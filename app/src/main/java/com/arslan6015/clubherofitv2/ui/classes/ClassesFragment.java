package com.arslan6015.clubherofitv2.ui.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arslan6015.clubherofitv2.R;
import com.arslan6015.clubherofitv2.TabsAdapterClasses;
import com.google.android.material.tabs.TabLayout;

public class ClassesFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_class, container, false);

        TabLayout tabLayoutclasses = (TabLayout) root.findViewById(R.id.tab_layout_classes);
        tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("Classes"));
        tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("Suggestive Articles"));
            tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("News & Events"));

//        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayoutclasses.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPagerClasses =(ViewPager)root.findViewById(R.id.view_pager_classes);
        TabsAdapterClasses tabsAdapterClasses = new TabsAdapterClasses(getFragmentManager(), tabLayoutclasses.getTabCount());
        viewPagerClasses.setAdapter(tabsAdapterClasses);
        viewPagerClasses.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutclasses));
        tabLayoutclasses.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerClasses.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}