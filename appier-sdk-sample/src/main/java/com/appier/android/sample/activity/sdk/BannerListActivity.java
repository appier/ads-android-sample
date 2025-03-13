package com.appier.android.sample.activity.sdk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.sdk.BannerListViewFragment;
import com.appier.android.sample.fragment.sdk.BannerRecyclerViewFragment;

public class BannerListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"ListView", "RecyclerView"}) {
            @Override
            @NonNull
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new BannerListViewFragment();
                    case 1:
                        return new BannerRecyclerViewFragment();
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
