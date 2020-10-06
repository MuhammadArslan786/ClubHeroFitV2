package com.arslan6015.clubherofitv2.ui.Performance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arslan6015.clubherofitv2.R;
import com.arslan6015.clubherofitv2.TabsAdapter;
import com.google.android.material.tabs.TabLayout;

public class PerformanceFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_performance, container, false);

//        Toolbar toolbar = (Toolbar)root.findViewById(R.id.tool_bar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("HP"));
        tabLayout.addTab(tabLayout.newTab().setText("STR"));
        tabLayout.addTab(tabLayout.newTab().setText("HIT"));
        tabLayout.addTab(tabLayout.newTab().setText("MOV"));

//        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)root.findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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