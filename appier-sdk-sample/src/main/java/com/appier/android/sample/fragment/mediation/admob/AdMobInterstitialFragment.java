package com.appier.android.sample.fragment.mediation.admob;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.ads.AppierInterstitial;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdMobInterstitialFragment extends BaseInterstitialFragment {

    private InterstitialAd mInterstitialAd;
    private Bundle mLocalExtras;
    public AdMobInterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The function handle AdMob Interstitial life cycle
     */
    protected void createInterstitial(Context context) {
        final int AD_WIDTH = 320;
        final int AD_HEIGHT = 480;

        /*
         * Initialize AdMob Interstitial
         *
         * To enable Appier AdMob Mediation, the AdUnit requires at least one "Custom Event",
         * with the following settings:
         *
         *   "Class Name": "com.appier.mediation.admob.ads.AppierInterstitial".
         *   "Parameter":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        mLocalExtras = new Bundle();
        mLocalExtras.putInt(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        mLocalExtras.putInt(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

        Appier.log("[Sample App]", "====== make request ======");
    }

    @Override
    protected void loadInterstitial() {
        // Load interstitial
        final String ADMOB_AD_UNIT_ID = getString(R.string.admob_adunit_interstitial);
        mLocalExtras.putString(AppierDataKeys.AD_UNIT_ID_LOCAL, ADMOB_AD_UNIT_ID);
        AdRequest request = new AdRequest.Builder().addCustomEventExtrasBundle(AppierInterstitial.class, mLocalExtras)
                .build();
        InterstitialAd.load(requireContext(), ADMOB_AD_UNIT_ID, request, new AdMobInterstitialAdLoadCallback());
    }

    @Override
    protected void showInterstitial() {
        // Pop up full-screen activity to show interstitial
        Appier.log("[Sample App]", "====== InterstitialAd showInterstitial ======");
        mInterstitialAd.show(requireActivity());
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    private class AdMobInterstitialAdLoadCallback extends InterstitialAdLoadCallback {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            Appier.log("[Sample App]", "onAdLoaded()");
            mInterstitialAd = interstitialAd;
            mInterstitialAd.setFullScreenContentCallback(new AdmobInterstitialAdFullContentCallback());
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
            mDemoFlowController.notifyAdBid();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Appier.log("[Sample App]", "onAdFailedToLoad():");
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
            if (loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                mDemoFlowController.notifyAdNoBid();
            } else {
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }
        }
    }

    private class AdmobInterstitialAdFullContentCallback extends FullScreenContentCallback {
        @Override
        public void onAdDismissedFullScreenContent() {
            Appier.log("[Sample App]", "onAdDismissedFullScreenContent()");
            setCurrentState(STATE_UNLOADED);
            updateLayoutByState(getCurrentState());
            mInterstitialAd = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            Appier.log("[Sample App]", "onAdFailedToShowFullScreenContent()");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            Appier.log("[Sample App]", "onAdShowedFullScreenContent()");
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
        }
    }

}
