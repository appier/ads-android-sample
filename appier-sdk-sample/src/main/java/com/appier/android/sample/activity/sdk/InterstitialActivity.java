package com.appier.android.sample.activity.sdk;

import android.os.Bundle;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.sdk.InterstitialFragment;


public class InterstitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load interstitial fragment
        addFragment(new InterstitialFragment());
    }

}
