package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewItemDecoration;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

import java.util.Arrays;

public class NativeRecyclerViewFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    public NativeRecyclerViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler);

        // add space between items
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(Dimension.dipsToIntPixels(12, getContext())));

        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items = new String[]{"", "", ""};

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        MyRecyclerViewAdapter recyclerAdapter = new MyRecyclerViewAdapter(context, Arrays.asList(items));
        AppierRecyclerAdapter mAppierRecyclerAdapter = new AppierRecyclerAdapter(recyclerAdapter);
        mRecyclerView.setAdapter(mAppierRecyclerAdapter);

        createNativeAdAndInsertInRecyclerView(mAppierRecyclerAdapter, 1);

    }

    private void createNativeAdAndInsertInRecyclerView(AppierRecyclerAdapter appierRecyclerAdapter, int insertPosition){
        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_native);
        final String APPIER_ZONE_ID = getString(R.string.zone_native);

        /*
         * Create AppierNativeAd and insert into RecyclerView using ViewBinder
         */
        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(R.layout.template_native_ad_compact_2)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAd appierNativeAd = new AppierNativeAd(getActivity(), new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(appierRecyclerAdapter, insertPosition));

        appierNativeAd.setViewBinder(appierNativeViewBinder);
        appierNativeAd.setZoneId(APPIER_ZONE_ID);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(appierNativeAd);

        appierNativeAd.loadAd();

    }

    /*
     * AppierNativeAd EventListener to handle event callbacks
     */
    private class EventListener implements AppierNativeAd.EventListener {

        private AppierRecyclerAdapter appierRecyclerAdapter;
        private int insertPosition;

        private EventListener(AppierRecyclerAdapter adapter, int position) {
            appierRecyclerAdapter = adapter;
            insertPosition = position;
        }

        @Override
        public void onAdLoaded(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoaded()");
            View adView = appierNativeAd.getAdView();

            try {
                mDemoFlowController.notifyAdBid();

                // Insert AppierNativeAd
                appierRecyclerAdapter.insertAd(insertPosition, appierNativeAd);

            } catch (Exception e) {
                Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }
        }

        @Override
        public void onAdNoBid(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
        }

        @Override
        public void onAdShown(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdShown()");
        }

        @Override
        public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onImpressionRecorded()");
        }

        @Override
        public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onImpressionRecordFail()", appierError.toString());
        }

        @Override
        public void onAdClick(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdClick()");
        }

        @Override
        public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdClickFail()");
        }

    }

}
