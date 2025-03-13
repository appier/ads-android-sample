package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

import java.util.Arrays;

public class NativeListViewFragment extends BaseFragment {

    private ListView mListView;

    public NativeListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        mListView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};

        ArrayAdapter<String> arrayAdapter = new MyListViewAdapter(context, Arrays.asList(items));
        AppierAdAdapter mAppierAdAdapter = new AppierAdAdapter(arrayAdapter);
        mListView.setAdapter(mAppierAdAdapter);

        createNativeAdAndInsertInListView(mAppierAdAdapter, 2);
    }

    private void createNativeAdAndInsertInListView(AppierAdAdapter appierAdAdapter, int insertPosition){
        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_native);
        final String APPIER_ZONE_ID = getString(R.string.zone_native);

        /*
         * Create AppierNativeAd and insert into ListView using ViewBinder
         */
        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(R.layout.template_native_ad_compact_1)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAd appierNativeAd = new AppierNativeAd(getActivity(), new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(appierAdAdapter, insertPosition));
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

        private AppierAdAdapter appierAdAdapter;
        private int insertPosition;

        private EventListener(AppierAdAdapter adapter, int position) {
            appierAdAdapter = adapter;
            insertPosition = position;
        }

        @Override
        public void onAdLoaded(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoaded()");
            View adView = appierNativeAd.getAdView();

            try {
                mDemoFlowController.notifyAdBid();

                // Insert AppierNativeAd
                appierAdAdapter.insertAd(insertPosition, appierNativeAd);

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
