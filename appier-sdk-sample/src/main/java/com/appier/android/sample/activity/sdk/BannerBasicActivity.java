package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appier.ads.Appier;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyService;
import com.appier.android.sample.common.MyServiceController;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.sdk.BannerBasicFragment;
import com.appier.android.sample.fragment.sdk.BannerViewFragment;


public class BannerBasicActivity extends BaseActivity {

    private MyServiceController mMyServiceController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a dummy service to demonstrate the service context integration
        mMyServiceController = new MyServiceController(this);
        mMyServiceController.startMyService();

        mContext = this;

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"AppierBannerView", "Activity", "Application", "Service"}) {
            @Override
            @NonNull
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        /*
                         * Basic integration using AppierBannerView
                         */
                        return new BannerViewFragment();
                    case 1:
                        /*
                         * Basic integration from Activity Context
                         */
                        return BannerBasicFragment.newInstance(mContext);

                    case 2:
                        /*
                         * Integration from Application Context
                         */
                        return BannerBasicFragment.newInstance(mContext.getApplicationContext());

                    case 3:
                        /*
                         * Integration from Service Context
                         */
                        MyService myService = mMyServiceController.getService();
                        if (myService != null) {
                            return BannerBasicFragment.newInstance(myService);
                        } else {
                            Appier.log("[Sample App]", "Please make sure the service is added into manifest");
                        }

                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        }, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyServiceController.stopMyService();
    }

}
