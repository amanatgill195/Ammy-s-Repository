package com.example.amanat.citytour.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.amanat.citytour.RestaurantFragments.DeliveryFragment;
import com.example.amanat.citytour.RestaurantFragments.DiningOutFragment;
import com.example.amanat.citytour.RestaurantFragments.LocalEatsFragment;

public class RestaurantViewPagerAdapter extends FragmentPagerAdapter {
    private String fragment1 = "Delivery", fragment2 = "Dining Out", fragment3 = "Local Eats";

    public RestaurantViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new DeliveryFragment();

            case 1:
                return new DiningOutFragment();

            case 2:
                return new LocalEatsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return fragment1;

            case 1:
                return fragment2;

            case 2:
                return fragment3;

            default:
                return null;
        }
    }
}
