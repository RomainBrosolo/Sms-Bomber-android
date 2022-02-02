package com.example.mysmsbomber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.mysmsbomber.adapters.TabbedLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabbedLayout;
    private ViewPager viewPager;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        tabbedLayout = findViewById(R.id.tabbedLayout);
        viewPager = findViewById(R.id.viewPager);
        tabbedLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabbedLayout adapter = new TabbedLayout(this, getSupportFragmentManager(), tabbedLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabbedLayout));

        tabbedLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }
}