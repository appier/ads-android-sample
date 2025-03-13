package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.VideoAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseVideoFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class VideoFragment extends BaseVideoFragment implements VideoAd.EventListener {

    private VideoAd videoAd;

    public VideoFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The functions to handle Appier Video lifecycle
     */
    @Override
    protected void loadVideo(Context context) {
        if (videoAd != null) {
            videoAd.destroy();
        }

        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_video);
        final String APPIER_ZONE_ID = getString(R.string.zone_video);

        /*
         * Create VideoAd, waiting for load and show
         */
        videoAd = new VideoAd(context, new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), this);

        // Set Appier video ad orientation
        videoAd.setOrientation(Configuration.ORIENTATION_PORTRAIT);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(videoAd);

        videoAd.setZoneId(APPIER_ZONE_ID);

        // Request for Ad content
        videoAd.loadAd();
    }

    @Override
    protected void showVideo() {
        // Check the ad is loaded
        if (videoAd.isLoaded()) {
            // Show the full screen video activity
            videoAd.showAd();
        }
    }

    /*
     * Override VideoAd.EventListener functions for event callbacks
     */

    @Override
    public void onAdLoaded(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video loaded");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onAdNoBid(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video ad returns no bid");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdNoBid();
    }

    @Override
    public void onAdLoadFail(AppierError appierError, VideoAd videoAd) {
        Appier.log("[Sample App]", "Video load failed");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onViewClick(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video is clicked");
    }

    @Override
    public void onViewClickFail(AppierError appierError, VideoAd videoAd) {
        Appier.log("[Sample App]", "Video click fail");
    }

    @Override
    public void onShown(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video shown");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onAdVideoComplete(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video complete");
    }

    @Override
    public void onShowFail(AppierError appierError, VideoAd videoAd) {
        Appier.log("[Sample App]", "Video show failed with error: " + appierError);
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onDismiss(VideoAd videoAd) {
        Appier.log("[Sample App]", "Video dismissed");
        setCurrentState(STATE_UNLOADED);
        updateLayoutByState(getCurrentState());

        // Destroy Video properly to prevent from memory leak
        videoAd.destroy();
    }
}
