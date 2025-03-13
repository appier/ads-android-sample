package com.appier.android.sample.activity.mediation.admob;

import android.os.Bundle;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.admob.AdMobBannerBasicFragment;

public class AdMobBannerBasicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new AdMobBannerBasicFragment());
    }
}
