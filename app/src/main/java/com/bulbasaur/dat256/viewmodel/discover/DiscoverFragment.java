package com.bulbasaur.dat256.viewmodel.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.bringToFront();
        ViewPager pager = view.findViewById(R.id.pager);
        List<TabFragment> fragments = new ArrayList<>(5);

        tabs.addTab(tabs.newTab().setText("Featured"));
        TabFragment featured = new TabFragment();
        fragments.add(featured);

        tabs.addTab(tabs.newTab().setText("Recommended"));
        TabFragment recommended = new TabFragment();
        fragments.add(recommended);

        tabs.addTab(tabs.newTab().setText("Upcoming"));
        TabFragment users = new TabFragment();
        fragments.add(users);

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        pager.setAdapter(new TabsAdapter(getFragmentManager(), fragments));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private class TabsAdapter extends FragmentPagerAdapter {
        private List<TabFragment> fragments;

        private TabsAdapter(FragmentManager fm, @NonNull List<TabFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return (0 <= i && i < fragments.size()) ? fragments.get(i) : null;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
