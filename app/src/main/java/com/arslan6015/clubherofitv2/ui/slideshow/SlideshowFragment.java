package com.arslan6015.clubherofitv2.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.arslan6015.clubherofitv2.R;
import com.arslan6015.clubherofitv2.TabsAdapter;
import com.arslan6015.clubherofitv2.TabsAdapterClasses;
import com.google.android.material.tabs.TabLayout;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        TabLayout tabLayoutclasses = (TabLayout) root.findViewById(R.id.tab_layout_classes);
        tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("Classes"));
        tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("Suggestion"));
            tabLayoutclasses.addTab(tabLayoutclasses.newTab().setText("Upcoming workout"));

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