package com.askhmer.chat.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.fragments.FourFragment;
import com.askhmer.chat.fragments.OneFragment;
import com.askhmer.chat.fragments.ThreeFragment;
import com.askhmer.chat.fragments.TwoFragment;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.MutiLanguage;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

//import com.askhmer.chat.fragments.TwoFragment;

public class MainActivityTab extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int badgeCount;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MutiLanguage(getApplicationContext(),this).StartUpCheckLanguage();
        setContentView(R.layout.activity_main_activity_tab);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


        /**
         * begin ShortcutBadger
         */
        getCountFriendAdd();
        /**
         * end ShortcutBadger
         */


        initUI();


/*
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        setupTabIcons();
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorLine);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
*/
    }

    private void initUI() {



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_person),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_person_sel))
                        .title("Friends")
                        .badgeTitle(badgeCount + "")
                        .build()

        );
        //Toast.makeText(MainActivityTab.this, "badgeCount :" + badgeCount, Toast.LENGTH_SHORT).show();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_mychat),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_mychat_sel))
                        .title("Chat")
                        .badgeTitle("3")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_add_friend),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_add_friend_sel))
                        .title("Add Friends")
                        .badgeTitle("2")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_more),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_more_sel))
                        .title("Setting")
                        .badgeTitle("1")
                        .build()
        );



        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment());
        adapter.addFrag(new TwoFragment());
        adapter.addFrag(new ThreeFragment());
        adapter.addFrag(new FourFragment());
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_action_friend,
                R.drawable.ic_action_chat_btn,
                R.drawable.ic_action_addfri,
                R.drawable.ic_more
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }


    /**
     * count number of friend add me
     */
    public void getCountFriendAdd() {
        String url ="http://chat.askhmer.com/api/friend/countFriendAdd/"+user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        if (response.has("DATA")) {
                            badgeCount = response.getInt("DATA");
                            ShortcutBadger.applyCount(getApplicationContext(), badgeCount);
                          //  Toast.makeText(MainActivityTab.this, badgeCount+"", Toast.LENGTH_SHORT).show();
                            Log.d("BAD",badgeCount+"");
                    } else {
                        Toast.makeText(MainActivityTab.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityTab.this, "There is Something Wrong !!", Toast.LENGTH_LONG).show();
                Log.d("ravyerror",error.toString());
            }
        });
        // Add request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }

}
