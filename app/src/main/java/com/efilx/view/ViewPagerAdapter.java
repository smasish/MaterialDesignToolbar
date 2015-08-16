package com.efilx.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.efilx.fragment.Fragment_JustAdded;
import com.efilx.fragment.Fragment_MusicVideo;
import com.efilx.fragment.Fragment_Popular;
import com.efilx.fragment.Fragment_Promos;
import com.efilx.fragment.Fragment_TvShow;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =5;
    private String titles[] ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }




    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return Fragment_JustAdded.newInstance(position);
            case 1:
                return Fragment_Popular.newInstance(position);
            case 2:
                return Fragment_TvShow.newInstance(position);
            case 3:
                return Fragment_Promos.newInstance(position);
            case 4:
                return Fragment_MusicVideo.newInstance(position);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}