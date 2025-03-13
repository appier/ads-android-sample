package com.appier.android.sample.fragment.mediation.applovin;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;

public class AppLovinBannerBasicFragment extends BaseFragment implements MaxAdViewAdListener {

    // for zone id: 6241
    private MaxAdView bannerView;
    // for zone id: 5933
    private MaxAdView MRECView;

    public AppLovinBannerBasicFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin BannerAd] " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_lovin_banner_basic, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bannerView != null) {
            bannerView.destroy();
        }
        if (MRECView != null) {
            MRECView.destroy();
        }
    }

    @Override
    protected void onViewVisible(View view) {

        ViewGroup container = view.findViewById(R.id.applovin_banner_ad_container);

        // banner view in Applovin is always (full width * 50)
        bannerView = new MaxAdView("f5ea1afb4c0b5c5a", MaxAdFormat.BANNER, requireContext());
        bannerView.setListener(this);
        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = getResources().getDimensionPixelSize(R.dimen.banner_height);
        int widthPx = getResources().getDimensionPixelSize(R.dimen.banner_width);
        bannerView.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx));
        bannerView.setBackgroundColor(Color.TRANSPARENT);
        bannerView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
        bannerView.stopAutoRefresh();

        // MREC view is a kind of banner view with size (300 * 250)
        MRECView = new MaxAdView("ae2b0183bc89bdb7", MaxAdFormat.MREC, requireContext());
        MRECView.setListener(this);
        // MREC width and height are 300 and 250 respectively, on phones and tablets
        int recWidthPx = AppLovinSdkUtils.dpToPx(requireContext(), 300);
        int recHeightPx = AppLovinSdkUtils.dpToPx(requireContext(), 250);
        MRECView.setLayoutParams(new FrameLayout.LayoutParams(recWidthPx, recHeightPx));
        MRECView.setBackgroundColor(Color.TRANSPARENT);
        MRECView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
        MRECView.stopAutoRefresh();

        container.removeAllViews();

        container.addView(bannerView);
        container.addView(MRECView);

        // add margin
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bannerView.getLayoutParams();
        params.setMargins(0, 100, 0, 100);
        bannerView.setLayoutParams(params);

        bannerView.loadAd();
        MRECView.loadAd();
    }

    /**
     * MaxAdViewAdListener implements methods
     */
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
        log("network name:" + maxAd.getNetworkName());
        log("unitId:" + maxAd.getAdUnitId());
        log("dsp name:" + maxAd.getDspName());
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
        log("onAdLoadFailed:" + maxError.getMessage());
    }

    @Override
    public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
        log("onAdDisplayFailed:" + maxError.getMessage());
    }
}