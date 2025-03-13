package com.appier.android.sample.fragment.sdk;

import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class BannerFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierBannerAd mAppierBannerAd;

    public BannerFloatingWindowFragment() {
    }

    protected void loadAdInContainer(LinearLayout adContainer) {

        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_300x250);
        final String APPIER_ZONE_ID = getString(R.string.zone_300x250);

        /*
         * Load Ad in ad container layout
         */

        mAppierBannerAd = new AppierBannerAd(getActivity(), new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(adContainer));
        mAppierBannerAd.setAdDimension(300, 250);
        mAppierBannerAd.setZoneId(APPIER_ZONE_ID);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(mAppierBannerAd);

        mAppierBannerAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierBannerAd != null) {
            mAppierBannerAd.destroy();
        }
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
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onViewClick(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onViewClick()");
        }

    }

}
