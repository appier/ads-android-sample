package com.appier.android.sample.fragment.mediation.applovin;

import android.graphics.Color;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;

public class AppLovinBannerFloatingWindowFragment extends BaseFloatingWindowFragment implements MaxAdViewAdListener {

    private MaxAdView MRECView;
    private LinearLayout container;

    public AppLovinBannerFloatingWindowFragment() {
        // Required empty public constructor
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin MREC Floating Window] " + message);
    }

    @Override
    protected void loadAdInContainer(LinearLayout adContainer) {
        container = adContainer;

        MRECView = new MaxAdView("ae2b0183bc89bdb7", MaxAdFormat.MREC, requireContext());
        MRECView.setListener(this);
        // MREC width and height are 300 and 250 respectively, on phones and tablets
        int recWidthPx = AppLovinSdkUtils.dpToPx(requireContext(), 300);
        int recHeightPx = AppLovinSdkUtils.dpToPx(requireContext(), 250);
        MRECView.setLayoutParams(new FrameLayout.LayoutParams(recWidthPx, recHeightPx));
        MRECView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
        MRECView.stopAutoRefresh();
        MRECView.loadAd();
    }

    @Override
    protected void destroyAdView() {
        if (MRECView != null) {
            MRECView.destroy();
        }
    }

    @Override
    public void onAdExpanded(@NonNull MaxAd maxAd) {
        log("onAdExpanded");
    }

    @Override
    public void onAdCollapsed(@NonNull MaxAd maxAd) {
        log("onAdCollapsed");
    }

    @Override
    public void onAdLoaded(@NonNull MaxAd maxAd) {
        log("onAdLoaded");
        if (container != null) {
            mDemoFlowController.notifyAdBid();

            container.removeAllViews();
            container.addView(MRECView);
        }
    }

    @Override
    public void onAdDisplayed(@NonNull MaxAd maxAd) {
        log("onAdDisplayed");
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
        log("onAdLoadFailed: " + maxError.getMessage());
        if (mFloatViewManager != null) {
            mFloatViewManager.close();
        }
    }

    @Override
    public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
        log("onAdDisplayFailed");
    }
}