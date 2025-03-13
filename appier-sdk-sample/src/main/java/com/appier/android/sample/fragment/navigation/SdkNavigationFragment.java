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
import com.appier.android.sample.activity.sdk.BannerBasicActivity;
import com.appier.android.sample.activity.sdk.BannerFloatingWindowActivity;
import com.appier.android.sample.activity.sdk.BannerListActivity;
import com.appier.android.sample.activity.sdk.InterstitialActivity;
import com.appier.android.sample.activity.sdk.NativeBasicActivity;
import com.appier.android.sample.activity.sdk.NativeFloatingWindowActivity;
import com.appier.android.sample.activity.sdk.NativeListActivity;
import com.appier.android.sample.activity.sdk.VideoActivity;
import com.appier.android.sample.common.NavigationAdapter;
import com.appier.android.sample.fragment.BaseFragment;


public class SdkNavigationFragment extends BaseFragment {

    public SdkNavigationFragment() {}

    @Override
    protected void onViewVisible(View view) {

    }

    public static SdkNavigationFragment newInstance(String title) {
        SdkNavigationFragment fragment = new SdkNavigationFragment();
        fragment.setTitleArgs(title);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_primary_navigation, container, false);
        RelativeLayout buttonLayout = layout.findViewById(R.id.button_group_layout);
        buttonLayout.setVisibility(View.GONE);

        initializeSDKView(layout);

        return layout;
    }

    private void initializeSDKView(View layout) {
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_video),
                new Pair[] {
                        new Pair<>("Video", VideoActivity.class)
                }
        );
        initializeNavigationList(
            layout.findViewById(R.id.secondary_nav_interstitial),
            new Pair[] {
                new Pair<>("Interstitial", InterstitialActivity.class)
            }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[] {
                        new Pair<>("Banner - basic format", BannerBasicActivity.class),
                        new Pair<>("Banner - in a listview", BannerListActivity.class),
                        new Pair<>("Banner - in a floating window", BannerFloatingWindowActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[] {
                        new Pair<>("Native - basic format", NativeBasicActivity.class),
                        new Pair<>("Native - in a listview", NativeListActivity.class),
                        new Pair<>("Native - in a floating window", NativeFloatingWindowActivity.class)
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        textVersion.setText("Appier SDK version : " + Appier.getVersionName());
    }

    // TODO: extract me
    private void initializeNavigationList(View view, Pair<String, Class<?>>[] navigations) {
        ListView listView = (ListView) view;
        final String title = this.getTitle();
        final NavigationAdapter navigationAdapter = new NavigationAdapter(getContext());
        for (Pair<String, Class<?>> navigation: navigations) {
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
