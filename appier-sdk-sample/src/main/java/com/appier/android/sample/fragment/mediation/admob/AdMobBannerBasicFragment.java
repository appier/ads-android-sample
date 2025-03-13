package com.appier.android.sample.fragment.mediation.admob;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.ads.AppierBanner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdMobBannerBasicFragment extends BaseFragment {

    private AdView mAdView;

    public AdMobBannerBasicFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mediation_admob_banner_basic, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    protected void onViewVisible(View view) {
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

        mAdView = getView().findViewById(R.id.banner_container_300_250);

        // Load Ad!
        mAdView.setAdListener(new AdMobBannerAdListener());
        mAdView.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(AppierBanner.class, localExtras)
                .build());
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    public class AdMobBannerAdListener extends AdListener {
        @Override
        public void onAdLoaded() {
            Appier.log("[Sample App]", "onAdLoaded()");
            mDemoFlowController.notifyAdBid();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Appier.log("[Sample App]", "onAdFailedToLoad():");
            if (loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                mDemoFlowController.notifyAdNoBid();
            } else {
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }
        }

        @Override
        public void onAdImpression() {
            Appier.log("[Sample App]", "onAdImpression()");
        }

        @Override
        public void onAdClicked() {
            Appier.log("[Sample App]", "onAdClicked()");
        }

        @Override
        public void onAdOpened() {
            Appier.log("[Sample App]", "onAdOpened()");
        }

        @Override
        public void onAdClosed() {
            Appier.log("[Sample App]", "onAdClosed()");
        }
    }
}
