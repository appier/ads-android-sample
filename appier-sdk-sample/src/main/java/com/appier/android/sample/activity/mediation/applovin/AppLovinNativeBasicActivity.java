package com.appier.android.sample.activity.mediation.applovin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.mediation.applovin.AppLovinNativeManualFragment;
import com.appier.android.sample.fragment.mediation.applovin.AppLovinNativeMediumFragment;
import com.appier.android.sample.fragment.mediation.applovin.AppLovinNativeSmallFragment;

public class AppLovinNativeBasicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[]{"Template - Small", "Template - Medium", "Manual"}) {
            @Override
            @NonNull
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new AppLovinNativeSmallFragment();
                    case 1:
                        return new AppLovinNativeMediumFragment();
                    case 2:
                        return new AppLovinNativeManualFragment();
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
