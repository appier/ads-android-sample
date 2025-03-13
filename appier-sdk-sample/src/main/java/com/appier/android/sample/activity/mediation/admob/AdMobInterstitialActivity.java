package com.appier.android.sample.activity.mediation.admob;

import android.os.Bundle;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.admob.AdMobInterstitialFragment;

public class AdMobInterstitialActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new AdMobInterstitialFragment());
    }
}
