package com.appier.android.sample.activity.mediation.applovin;

import android.os.Bundle;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.applovin.AppLovinInterstitialFragment;

public class AppLovinInterstitialActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new AppLovinInterstitialFragment());
    }
}
