package com.appier.android.sample.activity.sdk;

import android.content.Intent;
import android.os.Bundle;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.sdk.NativeFloatingWindowFragment;

public class NativeFloatingWindowActivity extends BaseActivity {

    NativeFloatingWindowFragment mDemoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDemoFragment = new NativeFloatingWindowFragment();
        addFragment(mDemoFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDemoFragment.getFloatViewManager().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDemoFragment.getFloatViewManager().close();
    }

}
