package com.appier.android.sample.fragment.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appier.ads.Appier;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinBannerBasicActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinBannerFloatingWindowActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinBannerListActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinInterstitialActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinNativeBasicActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinNativeFloatingWindowActivity;
import com.appier.android.sample.activity.mediation.applovin.AppLovinNativeListActivity;
import com.appier.android.sample.common.NavigationAdapter;
import com.appier.android.sample.fragment.BaseFragment;

public class AppLovinMediationNavigationFragment extends BaseFragment {
    public static AppLovinMediationNavigationFragment newInstance(String title) {
        AppLovinMediationNavigationFragment fragment = new AppLovinMediationNavigationFragment();
        fragment.setTitleArgs(title);
        return fragment;
    }

    @Override
    protected void onViewVisible(View view) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_primary_navigation, container, false);

        // remove predict
        RelativeLayout buttonLayout = layout.findViewById(R.id.button_group_layout);
        buttonLayout.setVisibility(View.GONE);

        // remove video
        layout.findViewById(R.id.secondary_linear_video).setVisibility(View.GONE);
        layout.findViewById(R.id.secondary_nav_video).setVisibility(View.GONE);
        layout.findViewById(R.id.secondary_table_row_video).setVisibility(View.GONE);

        initializeSDKView(layout);
        return layout;
    }

    private void initializeSDKView(View layout) {
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_interstitial),
                new Pair[]{
                        new Pair<>("Interstitial", AppLovinInterstitialActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[]{
                        new Pair<>("Banner - basic format", AppLovinBannerBasicActivity.class),
                        new Pair<>("Banner - in a listview", AppLovinBannerListActivity.class),
                        new Pair<>("Banner - in a floating window", AppLovinBannerFloatingWindowActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[]{
                        new Pair<>("Native - Basic format", AppLovinNativeBasicActivity.class),
                        new Pair<>("Native - In a ListView", AppLovinNativeListActivity.class),
                        new Pair<>("Native - In a floating window", AppLovinNativeFloatingWindowActivity.class),
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        textVersion.setText("Appier SDK version : " + Appier.getVersionName());
    }

    private void initializeNavigationList(View view, Pair<String, Class<?>>[] navigations) {
        ListView listView = (ListView) view;
        final String title = this.getTitle();
        final NavigationAdapter navigationAdapter = new NavigationAdapter(getContext());
        for (Pair<String, Class<?>> navigation : navigations) {
            navigationAdapter.add(navigation);
        }
        listView.setAdapter(navigationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                Pair<String, Class<?>> item = navigationAdapter.getItem(position);
                Intent intent = new Intent(getContext(), item.second);
                intent.putExtra(BaseActivity.EXTRA_TITLE, item.first);
                intent.putExtra(BaseActivity.EXTRA_SUB_TITLE, title);
                startActivity(intent);
            }
        });
    }
}
