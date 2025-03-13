package com.appier.android.sample.fragment.mediation.applovin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;

import java.util.ArrayList;

public class AppLovinBannerListFragment extends BaseFragment implements MaxAdViewAdListener {
    private ListView listView;
    private MaxAdView maxAdView;

    public AppLovinBannerListFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Banner List] " + message);
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
        for (int i = 0; i <= 15; i++) {
            items.add("");
        }

        maxAdView = new MaxAdView("f5ea1afb4c0b5c5a", requireContext());
        maxAdView.setListener(this);
        int heightPx = getResources().getDimensionPixelSize(R.dimen.banner_height);
        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx, Gravity.CENTER));
        maxAdView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
        maxAdView.stopAutoRefresh();
        maxAdView.loadAd();

        SimpleAdAdapter adapter = new SimpleAdAdapter(requireContext(), items, maxAdView, 2);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (maxAdView != null) {
            maxAdView.destroy();
            maxAdView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onAdExpanded(@NonNull MaxAd maxAd) {
        log("onAdExpanded");
    }

    @Override
    public void onAdCollapsed(@NonNull MaxAd maxAd) {
        log("onAdCollapsed");
    }

    @Override
    public void onAdLoaded(@NonNull MaxAd maxAd) {
        log("onAdLoaded");
    }

    @Override
    public void onAdDisplayed(@NonNull MaxAd maxAd) {
        log("onAdDisplayed");
    }

    @Override
    public void onAdHidden(@NonNull MaxAd maxAd) {
        log("onAdHidden");
    }

    @Override
    public void onAdClicked(@NonNull MaxAd maxAd) {
        log("onAdClicked");
    }

    @Override
    public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
        log("onAdLoadFailed:" + maxError.getMessage());

    }

    @Override
    public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
        log("onAdDisplayFailed:" + maxError.getMessage());
    }

    public static class SimpleAdAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_AD = 1;

        private final Context context;
        private final ArrayList<String> items;
        private final MaxAdView adView;
        private final int adPosition; // Position where ad should appear

        public SimpleAdAdapter(Context context, ArrayList<String> items, MaxAdView adView, int adPosition) {
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