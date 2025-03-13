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
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

import java.util.Arrays;

public class BannerListViewFragment extends BaseFragment {

    private ListView mListView;
    private AppierAdAdapter mAppierAdAdapter;

    public BannerListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        mListView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items;

        items = new String[] {"", "", "", "", "", "", "", "", "", ""};
        ArrayAdapter<String> arrayAdapter = new MyListViewAdapter(context, Arrays.asList(items));
        mAppierAdAdapter = new AppierAdAdapter(arrayAdapter);
        mListView.setAdapter(mAppierAdAdapter);

        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_320x50);
        final String APPIER_ZONE_ID = getString(R.string.zone_320x50);

        /*
         * Create new AppierBannerAd and insert into ListView
         */
        AppierBannerAd appierBannerAd = new AppierBannerAd(getActivity(), new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(mAppierAdAdapter, 2));
        appierBannerAd.setAdDimension(320, 50);
        appierBannerAd.setZoneId(APPIER_ZONE_ID);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(appierBannerAd);

        appierBannerAd.loadAd();

    }

    /*
     * AppierBannerAd EventListener to handle event callbacks
     */
    private class EventListener implements AppierBannerAd.EventListener {

        private AppierAdAdapter appierAdAdapter;
        private int insertPosition;

        private EventListener(AppierAdAdapter adapter, int position) {
            appierAdAdapter = adapter;
            insertPosition = position;
        }

        @Override
        public void onAdLoaded(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");

            try {
                // Insert AppierBannerAd
                appierAdAdapter.insertAd(insertPosition, appierBannerAd);

                mDemoFlowController.notifyAdBid();

            } catch (Exception e) {
                Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }

        }

        @Override
        public void onAdNoBid(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
        }

        @Override
        public void onViewClick(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onViewClick()");
        }

    }

}
