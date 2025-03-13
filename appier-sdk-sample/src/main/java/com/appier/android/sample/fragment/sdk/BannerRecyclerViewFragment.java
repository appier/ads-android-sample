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
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewItemDecoration;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

import java.util.Arrays;

public class BannerRecyclerViewFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private AppierRecyclerAdapter mAppierRecyclerAdapter;

    public BannerRecyclerViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler);

        // add space between items
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(Dimension.dipsToIntPixels(12, getContext())));

        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items;

        items = new String[]{"", "", ""};
        MyRecyclerViewAdapter recyclerAdapter = new MyRecyclerViewAdapter(context, Arrays.asList(items));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAppierRecyclerAdapter = new AppierRecyclerAdapter(recyclerAdapter);
        mRecyclerView.setAdapter(mAppierRecyclerAdapter);

        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_300x250);
        final String APPIER_ZONE_ID = getString(R.string.zone_300x250);

        /*
         * Create new AppierBannerAd and insert into RecyclerView
         */
        AppierBannerAd appierBannerAd = new AppierBannerAd(getActivity(), new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(mAppierRecyclerAdapter, 1));
        appierBannerAd.setAdDimension(300, 250);
        appierBannerAd.setZoneId(APPIER_ZONE_ID);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(appierBannerAd);

        appierBannerAd.loadAd();

    }

    /*
     * AppierBannerAd EventListener to handle event callbacks
     */
    private class EventListener implements AppierBannerAd.EventListener {

        private AppierRecyclerAdapter appierRecyclerAdapter;
        private int insertPosition;

        private EventListener(AppierRecyclerAdapter adapter, int position) {
            appierRecyclerAdapter = adapter;
            insertPosition = position;
        }

        @Override
        public void onAdLoaded(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");

            try {
                // Insert AppierBannerAd
                appierRecyclerAdapter.insertAd(insertPosition, appierBannerAd);

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
