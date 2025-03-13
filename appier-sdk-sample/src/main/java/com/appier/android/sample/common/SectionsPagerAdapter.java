package com.appier.android.sample.common;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public abstract class SectionsPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public SectionsPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mTitles = titles;
    }

    @Override
    abstract public Fragment getItem(int position);

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
