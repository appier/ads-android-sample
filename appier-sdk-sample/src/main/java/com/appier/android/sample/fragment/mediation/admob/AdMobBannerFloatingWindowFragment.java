package com.appier.android.sample.fragment.mediation.admob;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.ads.AppierBanner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdMobBannerFloatingWindowFragment extends BaseFloatingWindowFragment {

    private LinearLayout mAdContainer;
    private AdView mAdView;

    public AdMobBannerFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {

        Context context = getActivity();
        mAdContainer = adContainer;

        final String ADMOB_AD_UNIT_ID = getString(R.string.admob_adunit_banner_300x250);
        final int AD_WIDTH = 300;
        final int AD_HEIGHT = 250;

        /*
         * Initialize AdView and load banner
         *
         * To enable Appier AdMob Mediation, the AdUnit requires at least one "Custom Event",
         * with the following settings:
         *
         *   "Class Name": "com.appier.mediation.admob.ads.AppierBanner".
         *   "Parameter":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Bundle localExtras = new Bundle();
        localExtras.putInt(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        localExtras.putInt(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);
        localExtras.putString(AppierDataKeys.AD_UNIT_ID_LOCAL, ADMOB_AD_UNIT_ID);

        mAdView = new AdView(context);

        // set layout parameter to remove white margin
        mAdView.setLayoutParams(new LinearLayout.LayoutParams(
                Dimension.dipsToIntPixels(AD_WIDTH, context),
                Dimension.dipsToIntPixels(AD_HEIGHT, context)
        ));

        // Load Ad!
        mAdView.setAdUnitId(ADMOB_AD_UNIT_ID);
        mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mAdView.setAdListener(new AdMobBannerAdListener());
        mAdView.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(AppierBanner.class, localExtras)
                .build());

    }

    @Override
    protected void destroyAdView() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    public class AdMobBannerAdListener extends AdListener {
        @Override
        public void onAdLoaded() {
            Appier.log("[Sample App]", "onAdLoaded()");
            if (mAdContainer != null) {
                mDemoFlowController.notifyAdBid();

                // Display banner in parent container
                mAdContainer.removeAllViews();
                mAdContainer.addView(mAdView);
            }
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Appier.log("[Sample App]", "onAdFailedToLoad():");
            if (loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                mDemoFlowController.notifyAdNoBid();
            } else {
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }

            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onAdClicked() {
            Appier.log("[Sample App]", "onAdClicked()");
        }
    }
}
