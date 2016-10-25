package com.askhmer.chat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.fragments.FourFragment;
import com.askhmer.chat.fragments.OneFragment;
import com.askhmer.chat.fragments.ThreeFragment;
import com.askhmer.chat.fragments.TwoFragment;
import com.askhmer.chat.listener.HideToolBarListener;
import com.askhmer.chat.util.MutiLanguage;
import com.askhmer.chat.util.MyAppp;
import com.askhmer.chat.util.MyService;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.askhmer.chat.util.ToolBarUtils;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;
import java.util.List;

//import com.askhmer.chat.fragments.TwoFragment;

public class MainActivityTab extends AppCompatActivity implements HideToolBarListener{

    private static int RESULT_LOAD_IMAGE_PROFILE = 1;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String badgeCount;
    private String user_id;
    private SharedPreferencesFile mSharedPrefer;
    private LinearLayout toolbarLayout;

    private LinearLayout mToolbarContainer;
    private int mToolbarHeight;
    private TwoFragment twoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MutiLanguage(getApplicationContext(),this).StartUpCheckLanguage();
        setContentView(R.layout.activity_main_activity_tab);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
        badgeCount = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.FRIEND_ADD);

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
        /*btn_buttom*/
        ImageView marketBtn = (ImageView)findViewById(R.id.market_btn);
        marketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityTab.this, WebViewMaket.class));
            }
        });

        ImageView newsBtn = (ImageView)findViewById(R.id.timeline_btn);
        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityTab.this, WebViewTimeLine.class));
            }
        });

        Button btnCamera = (Button) findViewById(R.id.cemara_btn);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityTab.this, CameraActivity.class));
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, RESULT_LOAD_IMAGE_PROFILE);
            }
        });


    }

    public void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToolbarHeight = ToolBarUtils.getToolbarHeight(this);
        mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);

        twoFragment = new TwoFragment();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        toolbarLayout = (LinearLayout) findViewById(R.id.layout_toolbar);


        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_person),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_person_sel))
                        .title("Friends")
                        .badgeTitle(badgeCount)
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_mychat),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_mychat_sel))
                        .title("Chat")
                        .badgeTitle("0")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_add_friend),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_add_friend_sel))
                        .title("Add Friends")
                        .badgeTitle("0")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.more),
                        Color.parseColor("#ffffff"))
                        .selectedIcon(getResources().getDrawable(R.drawable.more))
                        .title("Setting")
                        .badgeTitle("0")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                if (position != 1) {
                    twoFragment.stopHandler();
                }
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
        adapter.addFrag(twoFragment);
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

    //Enable backward
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK: onBackPressed();
            }
        } catch (NullPointerException e) {

        } catch (Exception e1) {

        }
        return false;
    }

    public void onBackPressed() {
//        TwoFragment twoFragment = (TwoFragment) adapter.getItem(2);
//        if (viewPager.getCurrentItem() == 2 && twoFragment.canGoOrNot()) {
//            return;
//        }
//
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(MyAppp.getWebSocketClient()==null){
            try{
                stopService(new Intent(this, MyService.class));
            }catch (Exception e){

            }finally {
                startService(new Intent(this, MyService.class));
            }
        }
    }

    @Override
    protected void onStart() {
//        mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        super.onStart();
    }

    @Override
    public void callHideToolBar(int distance) {
        mToolbarContainer.setTranslationY(-distance);
    }

    @Override
    public void callOnHide() {
        mToolbarContainer.animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public void callOnShow() {
        mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

}
