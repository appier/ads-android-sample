package com.appier.android.sample.fragment.mediation.admob;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.AppierAdapterConfiguration;
import com.appier.mediation.admob.ads.AppierNative;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdMobNativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private Context mContext;
    private LinearLayout mAdContainer;
    private NativeAdView mNativeAdView;

    public AdMobNativeFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {

        mContext = getActivity();
        mAdContainer = adContainer;

        final String ADMOB_AD_UNIT_ID = getString(R.string.admob_adunit_native);

        /*
         * Initialize AdMob native
         *
         * To enable Appier AdMob Mediation, the AdUnit requires at least one "Custom Event",
         * with the following settings:
         *
         *   "Class Name": "com.appier.mediation.admob.ads.AppierNative".
         *   "Parameter":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Bundle localExtras = new Bundle();
        localExtras.putString(AppierDataKeys.AD_UNIT_ID_LOCAL, ADMOB_AD_UNIT_ID);

        // Inflate the layout
        mNativeAdView = (NativeAdView) getLayoutInflater().inflate(R.layout.template_admob_native_ad_full_1, null);

        // Load ad
        AdLoader adLoader = new AdLoader.Builder(mContext, ADMOB_AD_UNIT_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        Appier.log("[Sample App]", "onUnifiedNativeAdLoaded()");

                        populateUnifiedNativeAdView(nativeAd, mNativeAdView);
                        mAdContainer.addView(mNativeAdView);
                    }
                })
                .withAdListener(new AdMobNativeListener())
                .build();
        adLoader.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(AppierNative.class, localExtras)
                .build());
    }

    @Override
    protected void destroyAdView() {
        if (mNativeAdView != null) {
            mNativeAdView.destroy();
        }
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setVisibility(View.VISIBLE);

        adView.setAdvertiserView(adView.findViewById(R.id.native_privacy_information_icon_image));
        adView.setHeadlineView(adView.findViewById(R.id.native_title));
        adView.setBodyView(adView.findViewById(R.id.native_text));
        adView.setCallToActionView(adView.findViewById(R.id.native_cta));
        adView.setImageView(adView.findViewById(R.id.native_main_image));
        adView.setIconView(adView.findViewById(R.id.native_icon_image));
        adView.setNativeAd(nativeAd);

        // Check whether the native ad is provided by Appier
        if (nativeAd.getAdvertiser() != null && nativeAd.getAdvertiser().equals(AppierAdapterConfiguration.getAdvertiserName())) {
            /*
             * We provide two way (text and image) to show appier advertiser info.
             * You can choose one to bind the AdvertiserView.
             *
             * ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
             */
            ((ImageView) adView.getAdvertiserView()).setImageDrawable(
                    // The image at index 0 would always Appier privacy info icon
                    nativeAd.getImages().get(0).getDrawable()
            );

            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            ((ImageView) adView.getImageView()).setImageDrawable(
                    // The image at index 1 would be the main image
                    nativeAd.getImages().get(1).getDrawable()
            );
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
        } else {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            if (nativeAd.getBody() != null)
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            else
                adView.getBodyView().setVisibility(View.GONE);

            if (nativeAd.getCallToAction() != null)
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            else
                adView.getCallToActionView().setVisibility(View.GONE);

            if (nativeAd.getImages() != null && nativeAd.getImages().get(0) != null)
                ((ImageView) adView.getImageView()).setImageDrawable(nativeAd.getImages().get(0).getDrawable());
            else
                adView.getImageView().setVisibility(View.GONE);

            if (nativeAd.getIcon() != null)
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            else
                adView.getIconView().setVisibility(View.GONE);
        }
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    public class AdMobNativeListener extends AdListener {
        @Override
        public void onAdLoaded() {
            Appier.log("[Sample App]", "onAdLoaded():");
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
        public void onAdClicked() {
            Appier.log("[Sample App]", "onAdClicked():");
        }
    }
}
