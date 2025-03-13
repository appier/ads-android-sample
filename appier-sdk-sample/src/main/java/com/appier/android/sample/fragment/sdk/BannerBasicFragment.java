package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class BannerBasicFragment extends BaseFragment {

    private Context mContext;

    public BannerBasicFragment() {
    }

    public static BannerBasicFragment newInstance(Context context) {
        return new BannerBasicFragment(context);
    }

    private BannerBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling(3);
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_banner_basic, container, false);
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
         * Create Appier Banner Ads and load them into containers manually
         */

        // Load AppierBannerAd 1: 320x50
        LinearLayout appierBannerAdContainer1 = view.findViewById(R.id.banner_container_320_50);
        appierBannerAdContainer1.removeAllViews();
        AppierBannerAd appierBannerAd1 = new AppierBannerAd(mContext, new AppierAdUnitIdentifier(APPIER_320_50_AD_UNIT_ID), new EventListener(appierBannerAdContainer1));
        appierBannerAd1.setAdDimension(320, 50);
        appierBannerAd1.setZoneId(APPIER_320_50_ZONE_ID);
        AppierAdHelper.setTargeting(appierBannerAd1);  // Set targeting should be done before loadAd()
        appierBannerAd1.loadAd();

        // Load AppierBannerAd 2: 320x50
        LinearLayout appierBannerAdContainer2 = view.findViewById(R.id.banner_container_300_250);
        appierBannerAdContainer2.removeAllViews();
        AppierBannerAd appierBannerAd2 = new AppierBannerAd(mContext, new AppierAdUnitIdentifier(APPIER_300_250_AD_UNIT_ID), new EventListener(appierBannerAdContainer2));
        appierBannerAd2.setAdDimension(300, 250);
        appierBannerAd2.setZoneId(APPIER_300_250_ZONE_ID);
        AppierAdHelper.setTargeting(appierBannerAd2);  // Set targeting should be done before loadAd()
        appierBannerAd2.loadAd();

        // Load AppierBannerAd 3: 320x480
        LinearLayout appierBannerAdContainer3 = view.findViewById(R.id.banner_container_320_480);
        appierBannerAdContainer3.removeAllViews();
        AppierBannerAd appierBannerAd3 = new AppierBannerAd(mContext, new AppierAdUnitIdentifier(APPIER_320_480_AD_UNIT_ID), new EventListener(appierBannerAdContainer3));
        appierBannerAd3.setAdDimension(320, 480);
        appierBannerAd3.setZoneId(APPIER_320_480_ZONE_ID);
        AppierAdHelper.setTargeting(appierBannerAd3);  // Set targeting should be done before loadAd()
        appierBannerAd3.loadAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * AppierBannerAd EventListener to handle event callbacks
     */
    private class EventListener implements AppierBannerAd.EventListener {

        private LinearLayout adContainer;

        private EventListener(LinearLayout container) {
            adContainer = container;
        }

        @Override
        public void onAdLoaded(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
            mDemoFlowController.notifyAdBid();

            // Add banner view to container when AdLoaded
            adContainer.addView(appierBannerAd.getView());
        }

        @Override
        public void onAdNoBid(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
        }

        @Override
        public void onViewClick(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onViewClick()");
        }

    }

}