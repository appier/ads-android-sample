package com.appier.android.sample.fragment.mediation.applovin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;


public class AppLovinNativeMediumFragment extends BaseFragment {

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;

    public AppLovinNativeMediumFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin NativeAd Medium] " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_lovin_native_medium, container, false);
    }

    @Override
    public void onDestroy() {
        if (nativeAd != null) {
            nativeAdLoader.destroy(nativeAd);
        }
        if (nativeAdLoader != null) {
            nativeAdLoader.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onViewVisible(View view) {
        FrameLayout nativeAdContainer = view.findViewById(R.id.applovin_native_ad_container);
        nativeAdContainer.removeAllViews();

        nativeAdLoader = new MaxNativeAdLoader("75c5b52276242ea6", requireContext());
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, @NonNull MaxAd maxAd) {
                log("onNativeAdLoaded");
                log("network name:" + maxAd.getNetworkName());
                log("unitId:" + maxAd.getAdUnitId());
                log("dsp name:" + maxAd.getDspName());

                // Clean up any pre-existing native ad to prevent memory leaks
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }

                // Save ad for cleanup
                nativeAd = maxAd;

                // Add ad view to view
                if (maxNativeAdView != null) {
                    nativeAdContainer.addView(maxNativeAdView);
                }
            }

            @Override
            public void onNativeAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                log("onNativeAdLoadFailed:" + maxError.getMessage());
            }

            @Override
            public void onNativeAdClicked(@NonNull MaxAd maxAd) {
                log("onNativeAdClicked");
            }
        });

        nativeAdLoader.loadAd();
    }
}