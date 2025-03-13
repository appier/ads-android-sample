package com.appier.android.sample.fragment.mediation.applovin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

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

import java.util.ArrayList;

public class AppLovinNativeListFragment extends BaseFragment {
    private ListView listView;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd loadedNativeAd;

    public AppLovinNativeListFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Native List] " + message);
    }

    private MaxNativeAdView createNativeAdViewByBinder() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.applovin_native_list_item_template)
                .setTitleTextViewId(R.id.native_title)
                .setBodyTextViewId(R.id.native_text)
                .setIconImageViewId(R.id.native_icon_image)
                .build();
        return new MaxNativeAdView(binder, requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        listView = view.findViewById(R.id.list);

        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            items.add("");
        }

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

                SimpleAdAdapter adapter = new SimpleAdAdapter(requireContext(), items, maxNativeAdView, 2);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNativeAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                log("onNativeAdLoadFailed:" + maxError.getMessage());
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
    public void onDestroy() {
        if (loadedNativeAd != null) {
            nativeAdLoader.destroy(loadedNativeAd);
        }

        if (nativeAdLoader != null) {
            nativeAdLoader.destroy();
        }

        super.onDestroy();
    }

    public static class SimpleAdAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_AD = 1;

        private final Context context;
        private final ArrayList<String> items;
        private final MaxNativeAdView adView;
        private final int adPosition; // Position where ad should appear

        public SimpleAdAdapter(Context context, ArrayList<String> items, MaxNativeAdView adView, int adPosition) {
            this.context = context;
            this.items = items;
            this.adView = adView;
            this.adPosition = adPosition;
        }

        @Override
        public int getCount() {
            return items.size() + 1; // Total items plus one ad
        }

        @Override
        public Object getItem(int position) {
            if (position == adPosition) {
                return null;
            }
            return position > adPosition ? items.get(position - 1) : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position == adPosition ? TYPE_AD : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == adPosition) {
                return adView;
            } else {
                if (convertView == null || convertView instanceof FrameLayout) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.template_demo_list_row,
                            parent,
                            false
                    );
                }
                return convertView;
            }
        }
    }
}