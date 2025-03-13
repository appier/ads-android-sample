package com.appier.android.sample.fragment.mediation.applovin;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;

public class AppLovinNativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd loadedNativeAd;

    private MaxAdView MRECView;
    private LinearLayout container;

    public AppLovinNativeFloatingWindowFragment() {
        // Required empty public constructor
    }

    private MaxNativeAdView createNativeAdViewByBinder() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.applovin_native_float_template)
                .setTitleTextViewId(R.id.native_title)
                .setBodyTextViewId(R.id.native_text)
                .setIconImageViewId(R.id.native_icon_image)
                .setMediaContentViewGroupId(R.id.native_main_image)
                .build();
        return new MaxNativeAdView(binder, requireContext());
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Native Floating Window] " + message);
    }

    @Override
    protected void loadAdInContainer(LinearLayout adContainer) {
        container = adContainer;
        nativeAdLoader = new MaxNativeAdLoader("dd0f62c426586007", requireContext());
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, @NonNull MaxAd maxAd) {
                log("onNativeAdLoaded");
                log("network name:" + maxAd.getNetworkName());
                log("unitId:" + maxAd.getAdUnitId());
                log("dsp name:" + maxAd.getDspName());

                if (loadedNativeAd != null) {
                    nativeAdLoader.destroy(loadedNativeAd);
                }

                loadedNativeAd = maxAd;

                container.removeAllViews();
                container.addView(maxNativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                log("onNativeAdLoadFailed:" + maxError.getMessage());
                // Native ad load failed.
                // AppLovin recommends retrying with exponentially higher delays up to a maximum delay.
            }

            @Override
            public void onNativeAdClicked(@NonNull MaxAd maxAd) {
                log("onNativeAdClicked");
            }

            @Override
            public void onNativeAdExpired(@NonNull MaxAd maxAd) {
                super.onNativeAdExpired(maxAd);
            }
        });

        nativeAdLoader.loadAd(createNativeAdViewByBinder());
    }


    @Override
    protected void destroyAdView() {
        if (loadedNativeAd != null) {
            nativeAdLoader.destroy(loadedNativeAd);
        }

        if (nativeAdLoader != null) {
            nativeAdLoader.destroy();
        }
    }
}