package com.appier.android.sample.fragment.mediation.applovin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppLovinNativeRecyclerViewFragment extends BaseFragment {
    private final int AD_VIEW_COUNT = 1;
    private final ArrayList<String> sampleData = new ArrayList<>(Arrays.asList("ABCDE".split("")));
    private final List<MaxNativeAdView> adViews = new ArrayList<>(AD_VIEW_COUNT);
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd loadedNativeAd;
    private CustomRecyclerAdapter adapter;

    public AppLovinNativeRecyclerViewFragment() {
    }

    private void log(String message) {
        Log.d("[Sample App]", "[AppLovin Native RecycleView] " + message);
    }

    private MaxNativeAdView createNativeAdViewByBinder() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.applovin_native_recycler_item_template)
                .setTitleTextViewId(R.id.native_title)
                .setBodyTextViewId(R.id.native_text)
                .setMediaContentViewGroupId(R.id.native_main_image)
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
        return inflater.inflate(R.layout.fragment_app_lovin_banner_recycler_view, container, false);
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

                adViews.add(maxNativeAdView);
                adapter = new CustomRecyclerAdapter(requireActivity(), sampleData);

                // Configure recycler view
                RecyclerView recyclerView = view.findViewById(R.id.apploviin_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(adapter);

                configureAdViews();

                loadedNativeAd = maxAd;
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

    private void configureAdViews() {
        sampleData.add(1, "");
        adapter.notifyItemInserted(1);
    }

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
                    return new CustomRecyclerAdapter.AdViewHolder(inflater.inflate(R.layout.applovin_ad_view_holder, parent, false));
                case CUSTOM_VIEW:
                    return new CustomRecyclerAdapter.CustomViewHolder(inflater.inflate(R.layout.applovin_text_recycler_view_holder, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof CustomRecyclerAdapter.AdViewHolder) {
                // Select an ad view to display
                MaxNativeAdView adView = adViews.get(position % adViews.size());

                // Configure view holder with an ad
                ((CustomRecyclerAdapter.AdViewHolder) holder).configure(adView);
            } else if (holder instanceof CustomRecyclerAdapter.CustomViewHolder) {
//                ((CustomViewHolder) holder).textView.setText(data.get(position));
            }
        }

        @Override
        public void onViewRecycled(@NonNull final RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);

            if (holder instanceof CustomRecyclerAdapter.AdViewHolder) {
                CustomRecyclerAdapter.AdViewHolder adViewHolder = (CustomRecyclerAdapter.AdViewHolder) holder;
                ((ViewGroup) adViewHolder.itemView).removeView(adViewHolder.adView);
                adViewHolder.adView = null;
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull final RecyclerView.ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
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
            MaxNativeAdView adView;

            public AdViewHolder(View itemView) {
                super(itemView);
            }

            public void configure(MaxNativeAdView adView) {
                this.adView = adView;
                ((ViewGroup) itemView).addView(this.adView);
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