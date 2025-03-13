package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierInterstitialAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class InterstitialFragment extends BaseInterstitialFragment implements AppierInterstitialAd.EventListener {

    private AppierInterstitialAd interstitialAd;

    public InterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The functions to handle Appier Interstitial lifecycle
     */

    protected void createInterstitial(Context context) {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }

        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_320x480);
        final String APPIER_ZONE_ID = getString(R.string.zone_interstitial);

        /*
         * Create AppierInterstitialAd, waiting for load and show
         */
        interstitialAd = new AppierInterstitialAd(context, new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), this);
        interstitialAd.setAdDimension(320, 480);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(interstitialAd);

        interstitialAd.setZoneId(APPIER_ZONE_ID);
    }

    protected void loadInterstitial() {
        // Request for Ad content
        interstitialAd.loadAd();
    }

    protected void showInterstitial() {
        // Check the ad is loaded
        if (interstitialAd.isLoaded()) {
            // Show the full screen interstitial activity
            interstitialAd.showAd();
        }
    }

    /*
     * Override AppierInterstitialAd.EventListener functions for event callbacks
     */

    @Override
    public void onAdLoaded(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial loaded");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onAdNoBid(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial ad returns no bid");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdNoBid();
    }

    @Override
    public void onAdLoadFail(AppierError appierError, AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial load failed");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onViewClick(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial is clicked");
    }

    @Override
    public void onShown(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial shown");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onShowFail(AppierError appierError, AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial show failed with error: " + appierError);
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onDismiss(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial dismissed");
        setCurrentState(STATE_UNLOADED);
        updateLayoutByState(getCurrentState());

        // Destroy Interstitial properly to prevent from memory leak
        interstitialAd.destroy();
    }

}
