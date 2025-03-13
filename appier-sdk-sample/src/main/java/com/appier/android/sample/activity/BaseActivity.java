package com.appier.android.sample.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.appier.android.sample.R;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class BaseActivity extends FragmentActivity {
    public static final String EXTRA_SUB_TITLE = "SUB_TITLE";
    public static final String EXTRA_TITLE = "TITLE";

    public static int getThemeAccentColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme ().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TextView textTitle = findViewById(R.id.text_title);
        TextView textSubTitle = findViewById(R.id.text_sub_title);
        ImageButton buttonBack = findViewById(R.id.button_back);

        Bundle bundle = getIntent().getExtras();
        textTitle.setText(bundle.getString(EXTRA_TITLE));
        textSubTitle.setText(bundle.getString(EXTRA_SUB_TITLE));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void addTabbedViewPager(SectionsPagerAdapter sectionsPagerAdapter) {
        addTabbedViewPager(sectionsPagerAdapter, false);
    }

    protected void addTabbedViewPager(SectionsPagerAdapter sectionsPagerAdapter, boolean scrollableTabTitle) {
        AppBarLayout appBar = findViewById(R.id.appBar);
        TabLayout tabs = (TabLayout) LayoutInflater
            .from(this)
            .inflate(R.layout.template_tab, null,false);
        appBar.addView(tabs);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        if (scrollableTabTitle) {
             tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.parseColor("#b9c6f7"), getThemeAccentColor(BaseActivity.this));
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
}
