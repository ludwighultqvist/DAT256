package com.bulbasaur.dat256.viewmodel.discover;

import android.icu.lang.UScript;
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
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.QueryFilter;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
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
        featured.setOnUpdateListener(() -> populateFeaturedTab(featured));
        fragments.add(featured);

        tabs.addTab(tabs.newTab().setText("Recommended"));
        TabFragment recommended = new TabFragment();
        recommended.setOnUpdateListener(() -> populateRecommendedTab(recommended));
        fragments.add(recommended);

        tabs.addTab(tabs.newTab().setText("Upcoming"));
        TabFragment users = new TabFragment();
        users.setOnUpdateListener(() -> populateUserTab(users));
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

    private void populateFeaturedTab(TabFragment fragment) {
        Database database = Database.getInstance();
        QueryFilter filter = new QueryFilter("featured", "=", true);

        database.meetups().search(filter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> documents) {
                super.onSuccess(documents);

                List<String> ids = new ArrayList<>(documents.size());

                for (DBDocument document : documents) {
                    ids.add(document.id());
                }

                meetUpSectionFactory(fragment).createSection("Featured Meetups", ids);
            }
        });

        database.users().search(filter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> documents) {
                super.onSuccess(documents);
                List<String> ids = new ArrayList<>(documents.size());

                for (DBDocument document : documents) {
                    ids.add(document.id());
                }

                userSectionFactory(fragment).createSection("Featured Users", ids);
            }
        });
    }

    private void populateRecommendedTab(Fragment fragment) {
        Database database = Database.getInstance();
    }

    private void populateUserTab(TabFragment fragment) {
        Database database = Database.getInstance();

        if (database.hasUser()) {
            database.user(new RequestListener<DBDocument>() {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);

                    userSectionFactory(fragment).createSection("Friends", (List<String>) object.get("friends"));

                    SectionFactory<MeetUp> factory = meetUpSectionFactory(fragment);
                    factory.createSection("Joined Meetups", (List<String>) object.get("joined meetups"));
                    factory.createSection("Created Meetups", (List<String>) object.get("created meetups"));
                }
            });
        }
    }

    private abstract class SectionFactory<E> {

        private DBCollection collection;
        private TabFragment fragment;

        private SectionFactory(DBCollection collection, TabFragment fragment) {
            this.collection = collection;
            this.fragment = fragment;
        }

        void createSection(String title, List<String> ids) {
            if (title == null || ids == null) {
                return;
            }

            List<E> items = new ArrayList<>(ids.size());

            for (String id: ids) {
                collection.get(id, new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);
                        E item = convert(document);

                        if (item != null) {
                            items.add(item);
                        }
                        else {
                            ids.remove(id);
                        }

                        if (items.isEmpty()) {
                            // show error or do nothing
                        }
                        else if (items.size() == ids.size()) {
                            addSection(fragment, title, items);
                            fragment.refresh();
                        }
                    }
                });
            }
        }

        abstract E convert(DBDocument document);

        abstract void addSection(TabFragment fragment, String title, List<E> items);
    }

    private SectionFactory<User> userSectionFactory(TabFragment fragment) {
        return new SectionFactory<User>(Database.getInstance().users(), fragment) {
            @Override
            User convert(DBDocument document) {
                return Helpers.convertDocToUser(document);
            }

            @Override
            void addSection(TabFragment fragment, String title, List<User> items) {
                fragment.userSection(title, items);
            }
        };
    }

    private SectionFactory<MeetUp> meetUpSectionFactory(TabFragment fragment) {
        return new SectionFactory<MeetUp>(Database.getInstance().meetups(), fragment) {
            @Override
            MeetUp convert(DBDocument document) {
                return Helpers.convertDocToMeetUp(document);
            }

            @Override
            void addSection(TabFragment fragment, String title, List<MeetUp> items) {
                fragment.meetUpSection(title, items);
            }
        };
    }


}
