package com.appier.android.sample.activity.mediation.applovin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.applovin.AppLovinNativeFloatingWindowFragment;

public class AppLovinNativeFloatingWindowActivity extends BaseActivity {
    AppLovinNativeFloatingWindowFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new AppLovinNativeFloatingWindowFragment();
        addFragment(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.getFloatViewManager().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fragment.getFloatViewManager().close();
    }
}