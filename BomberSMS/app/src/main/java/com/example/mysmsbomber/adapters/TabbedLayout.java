package com.example.mysmsbomber.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mysmsbomber.views.Settings;
import com.example.mysmsbomber.views.SMS;
import com.example.mysmsbomber.views.Contact;

public class TabbedLayout extends FragmentPagerAdapter {

    Context Context;
    int count_tab;

    public TabbedLayout(Context context , FragmentManager fragmentManager , int tabs) {
        super(fragmentManager);
        count_tab = tabs;
        Context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Contact();
            case 1:
                return new SMS();
            case 2:
                return new Settings();
            default:
                return new Contact();

        }
    }

    @Override
    public int getCount() {
        return count_tab;
    }
}
