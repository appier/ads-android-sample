package com.appier.android.sample;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appier.ads.Appier;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.navigation.AdMobMediationNavigationFragment;
import com.appier.android.sample.fragment.navigation.AppLovinMediationNavigationFragment;
import com.appier.android.sample.fragment.navigation.SdkNavigationFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkInitializationConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] titles = new String[]{"Appier SDK", "AdMob Mediation", "AppLovin Mediation"};

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return SdkNavigationFragment.newInstance(titles[position]);
                    case 1:
                        return AdMobMediationNavigationFragment.newInstance(titles[position]);
                    case 2:
                        return AppLovinMediationNavigationFragment.newInstance(titles[position]);
                    default:
                        throw new IndexOutOfBoundsException();
                }

            }
        };

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        // Lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Appier.log("AdMob", "SDK initialized");
            }
        });

        // AppLovin
        // Create the initialization configuration
        final String AppLovinSDKKey = "5PRz32n_vi41LTITQC4JcXSXUSnkUMxWXVotDbWPTGCDKPuN3zSU7JCa17XDiSJ6RacLhIVyG5JU25SMz_hyOW";
        AppLovinSdkInitializationConfiguration initConfig = AppLovinSdkInitializationConfiguration.builder(AppLovinSDKKey, this)
                .setMediationProvider("Appier")
//                .setMediationProvider(AppLovinMediationProvider.MAX)
//                .setTestDeviceAdvertisingIds(Arrays.asList("b7f26681-e95f-434f-8a25-10ca1ad1abe1"))
                .build();

        AppLovinSdk.getInstance(this).initialize(initConfig, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                Appier.log("AppLovinSdk", "AppLovinSdk SDK initialized");
            }
        });
    }
}
