package com.appier.android.sample.fragment.mediation.applovin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppLovinBannerRecyclerViewFragment extends BaseFragment implements MaxAdViewAdListener {

    private final int AD_VIEW_COUNT = 1;
    private final ArrayList<String> sampleData = new ArrayList<>(Arrays.asList("ABCDE".split("")));
    private final List<MaxAdView> adViews = new ArrayList<>(AD_VIEW_COUNT);
    private CustomRecyclerAdapter adapter;

    public AppLovinBannerRecyclerViewFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Banner Recycler] " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_lovin_banner_recycler_view, container, false);
    }

    @Override
    protected void onViewVisible(View view) {
        adapter = new CustomRecyclerAdapter(requireActivity(), sampleData);

        // Configure recycler view
        RecyclerView recyclerView = view.findViewById(R.id.apploviin_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        configureAdViews();
    }

    private void configureAdViews() {
        sampleData.add(1, "");
        adapter.notifyItemInserted(1);

        for (int i = 0; i < AD_VIEW_COUNT; i++) {
            MaxAdView adView = new MaxAdView("ae2b0183bc89bdb7", MaxAdFormat.MREC, requireContext());
            adView.setListener(this);

            // Set this extra parameter to work around SDK bug that ignores calls to stopAutoRefresh()
            adView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
            adView.stopAutoRefresh();

            // Load the ad
            adView.loadAd();
            adViews.add(adView);
        }
    }

    //region MAX Ad Listener
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
    // endregion

    private enum ViewHolderType {
        AD_VIEW,
        CUSTOM_VIEW
    }

    public class CustomRecyclerAdapter
            extends RecyclerView.Adapter {
        private final LayoutInflater inflater;
        private final List<String> data;

        public CustomRecyclerAdapter(Activity activity, List<String> data) {
            this.inflater = activity.getLayoutInflater();
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            ViewHolderType viewHolderType = ViewHolderType.values()[viewType];

            switch (viewHolderType) {
                case AD_VIEW:
                    return new AdViewHolder(inflater.inflate(R.layout.applovin_ad_view_holder, parent, false));
                case CUSTOM_VIEW:
                    return new CustomViewHolder(inflater.inflate(R.layout.applovin_text_recycler_view_holder, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof AdViewHolder) {
                // Select an ad view to display
                MaxAdView adView = adViews.get(position % adViews.size());

                // Configure view holder with an ad
                ((AdViewHolder) holder).configure(adView);
            } else if (holder instanceof CustomViewHolder) {
//                ((CustomViewHolder) holder).textView.setText(data.get(position));
            }
        }

        @Override
        public void onViewRecycled(@NonNull final RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);

            if (holder instanceof AdViewHolder) {
                AdViewHolder adViewHolder = (AdViewHolder) holder;
                ((ViewGroup) adViewHolder.itemView).removeView(adViewHolder.adView);
                adViewHolder.adView = null;
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull final RecyclerView.ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);

            if (holder instanceof AdViewHolder) {
                ((AdViewHolder) holder).stopAutoRefresh();
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(final int position) {
            return (position == 1) ? ViewHolderType.AD_VIEW.ordinal() : ViewHolderType.CUSTOM_VIEW.ordinal();
        }

        public class AdViewHolder
                extends RecyclerView.ViewHolder {
            MaxAdView adView;

            public AdViewHolder(View itemView) {
                super(itemView);
            }

            public void configure(MaxAdView adView) {
                this.adView = adView;
                this.adView.startAutoRefresh();

                // MREC width and height are 300 and 250 respectively, on phones and tablets
                int widthPx = AppLovinSdkUtils.dpToPx(requireContext(), 300);
                int heightPx = AppLovinSdkUtils.dpToPx(requireContext(), 250);

                // Set background or background color for MRECs to be fully functional
                this.adView.setBackgroundColor(Color.BLACK);
                this.adView.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx, Gravity.CENTER));
                ((ViewGroup) itemView).addView(this.adView);
            }

            public void stopAutoRefresh() {
                adView.setExtraParameter("allow_pause_auto_refresh_immediately", "true");
                adView.stopAutoRefresh();
            }
        }

        public class CustomViewHolder
                extends RecyclerView.ViewHolder {

            public CustomViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}