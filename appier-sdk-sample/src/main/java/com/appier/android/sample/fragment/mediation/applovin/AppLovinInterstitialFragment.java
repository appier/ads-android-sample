package com.appier.android.sample.fragment.mediation.applovin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;

public class AppLovinInterstitialFragment extends BaseInterstitialFragment implements MaxAdListener, MaxAdRevenueListener {

    private MaxInterstitialAd interstitialAd;

    public AppLovinInterstitialFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Interstitial] " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createInterstitial(Context context) {
        interstitialAd = new MaxInterstitialAd("b5e1f433157052e8", requireContext());
        interstitialAd.setListener(this);
        interstitialAd.setRevenueListener(this);
    }

    @Override
    protected void loadInterstitial() {
        log("loadInterstitial click");
        interstitialAd.loadAd();
    }

    @Override
    protected void showInterstitial() {
        if (interstitialAd.isReady()) {
            log("showInterstitial click");
            interstitialAd.showAd(getActivity());
        }
    }

    /*
     *  AppLovin MaxAdListener
     */
    @Override
    public void onAdLoaded(@NonNull MaxAd maxAd) {
        log("onAdLoaded");
        log("network name:" + maxAd.getNetworkName());
        log("unitId:" + maxAd.getAdUnitId());
        log("dsp name:" + maxAd.getDspName());

        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onAdDisplayed(@NonNull MaxAd maxAd) {
        log("onAdDisplayed");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onAdHidden(@NonNull MaxAd maxAd) {
        log("onAdHidden");
    }

    @Override
    public void onAdClicked(@NonNull MaxAd maxAd) {
        log("onAdClicked");
    }

    @Override
    public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
        log("onAdLoadFailed:" + maxError.getMessage());
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
        log("onAdDisplayFailed:" + maxError.getMessage());
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onDestroy() {
        if (interstitialAd != null) {
            log("onDestroy");
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAdRevenuePaid(@NonNull MaxAd maxAd) {
        log("onAdRevenuePaid");
        log("getRevenue:" + maxAd.getRevenue());
        log("getRevenuePrecision:" + maxAd.getRevenuePrecision());
    }
}