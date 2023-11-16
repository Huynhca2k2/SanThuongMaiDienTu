package com.chh.shoponline.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.chh.shoponline.Fragment.FragmentStatis30day;
import com.chh.shoponline.Fragment.OrderFragment1;
import com.chh.shoponline.Fragment.OrderFragment2;

public class OrderAdapter extends FragmentStatePagerAdapter {

    public OrderAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OrderFragment1();
            case 1:
                return new OrderFragment2();
            default:
                return new OrderFragment1();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Waiting";
                break;
            case 1:
                title = "Accomplished";
                break;
        }
        return title;
    }
}
