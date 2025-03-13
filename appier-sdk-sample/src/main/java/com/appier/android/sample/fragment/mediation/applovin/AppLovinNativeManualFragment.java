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
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;

public class AppLovinNativeManualFragment extends BaseFragment {

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd loadedNativeAd;

    public AppLovinNativeManualFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin NativeAd Manual] " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_lovin_native_manual, container, false);
    }

    @Override
    public void onDestroy() {
        if (loadedNativeAd != null) {
            nativeAdLoader.destroy(loadedNativeAd);
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

        // create loader
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

                nativeAdContainer.removeAllViews();
                nativeAdContainer.addView(maxNativeAdView);
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

    private MaxNativeAdView createNativeAdViewByBinder() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.applovin_native_manual_template)
                .setTitleTextViewId(R.id.native_title)
                .setBodyTextViewId(R.id.native_text)
//                .setStarRatingContentViewGroupId(R.id.star_rating_view)
//                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.native_icon_image)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.option_view)
                .setCallToActionButtonId(R.id.native_cta)
                .build();
        return new MaxNativeAdView(binder, requireContext());
    }
}