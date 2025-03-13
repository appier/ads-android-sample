package com.appier.android.sample.activity.mediation.admob;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appier.ads.Appier;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyService;
import com.appier.android.sample.common.MyServiceController;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.mediation.admob.AdMobNativeBasicFragment;

public class AdMobNativeBasicActivity extends BaseActivity {
    private MyServiceController mMyServiceController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyServiceController = new MyServiceController(this);
        mMyServiceController.startMyService();

        mContext = this;

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"Activity", "Application", "Service"}) {
            @Override
            @NonNull
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        /*
                         * Basic integration from Activity Context
                         */
                        return AdMobNativeBasicFragment.newInstance(mContext);
                    case 1:
                        /*
                         * Integration from Application Context
                         */
                        return AdMobNativeBasicFragment.newInstance(mContext.getApplicationContext());
                    case 2:
                        /*
                         * Integration from Service Context
                         */
                        MyService myService = mMyServiceController.getService();
                        if (myService != null) {
                            return AdMobNativeBasicFragment.newInstance(myService);
                        } else {
                            Appier.log("[Sample App]", "Please make sure the service is added into manifest");
                        }
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyServiceController.stopMyService();
    }
}
