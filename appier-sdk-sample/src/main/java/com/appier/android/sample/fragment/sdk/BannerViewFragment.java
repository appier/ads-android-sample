package com.appier.android.sample.fragment.sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appier.ads.AppierBannerView;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class BannerViewFragment extends BaseFragment {

    private AppierBannerView mAppierBannerView1;
    private AppierBannerView mAppierBannerView2;
    private AppierBannerView mAppierBannerView3;

    public BannerViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * AppierBannerView doesn't support lifecycle control,
         * so enableErrorHandling() only invokes initial render and doesn't handle any error for this sample.
         */
        enableErrorHandling(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_banner_view, container, false);
    }

    @Override
    protected void onViewVisible(View view) {
        final String APPIER_320_50_AD_UNIT_ID = getString(R.string.ad_unit_320x50);
        final String APPIER_300_250_AD_UNIT_ID = getString(R.string.ad_unit_300x250);
        final String APPIER_320_480_AD_UNIT_ID = getString(R.string.ad_unit_320x480);

        final String APPIER_320_50_ZONE_ID = getString(R.string.zone_320x50);
        final String APPIER_300_250_ZONE_ID = getString(R.string.zone_300x250);
        final String APPIER_320_480_ZONE_ID = getString(R.string.zone_320x480);

        /*
         * Create AppierBannerViews and load the Ads
         */
        mAppierBannerView1 = view.findViewById(R.id.appier_banner_view_320_50);
        mAppierBannerView1.setAdUnitId(APPIER_320_50_AD_UNIT_ID);
        mAppierBannerView1.setAdDimension(320, 50);
        mAppierBannerView1.setZoneId(APPIER_320_50_ZONE_ID);
        AppierAdHelper.setTargeting(mAppierBannerView1);  // Set targeting should be done before loadAd()
        mAppierBannerView1.loadAd();

        mAppierBannerView2 = view.findViewById(R.id.appier_banner_view_300_250);
        mAppierBannerView2.setAdUnitId(APPIER_300_250_AD_UNIT_ID);
        mAppierBannerView2.setAdDimension(300, 250);
        mAppierBannerView2.setZoneId(APPIER_300_250_ZONE_ID);
        AppierAdHelper.setTargeting(mAppierBannerView2);  // Set targeting should be done before loadAd()
        mAppierBannerView2.loadAd();

        mAppierBannerView3 = view.findViewById(R.id.appier_banner_view_320_480);
        mAppierBannerView3.setAdUnitId(APPIER_320_480_AD_UNIT_ID);
        mAppierBannerView3.setAdDimension(320, 480);
        mAppierBannerView3.setZoneId(APPIER_320_480_ZONE_ID);
        AppierAdHelper.setTargeting(mAppierBannerView3);  // Set targeting should be done before loadAd()
        mAppierBannerView3.loadAd();
    }

    @Override
    public void onDestroy() {
        if (mAppierBannerView1 != null) {
            mAppierBannerView1.destroy();
        }
        if (mAppierBannerView2 != null) {
            mAppierBannerView2.destroy();
        }
        if (mAppierBannerView3 != null) {
            mAppierBannerView3.destroy();
        }
        super.onDestroy();
    }

}
