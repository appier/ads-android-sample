package com.appier.android.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appier.ads.Appier;
import com.appier.android.sample.R;

public abstract class BaseInterstitialFragment extends BaseFragment {

    protected final int STATE_UNLOADED = 0;
    protected final int STATE_LOADING = 1;
    protected final int STATE_LOADED = 2;

    private Context context;

    private int currentState = STATE_UNLOADED;

    private ProgressBar progressLoading, progressStepsArt;
    private Button buttonLoad;
    private ImageView imageStepsArt, imageSteps;
    private TextView textIndicator;

    public BaseInterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_common_interstitial, container, false);
        context = getActivity();

        progressLoading = view.findViewById(R.id.progress_loading);
        progressStepsArt = view.findViewById(R.id.progress_steps_art);
        imageStepsArt = view.findViewById(R.id.img_steps_art);
        imageSteps = view.findViewById(R.id.img_step_progress);
        buttonLoad = view.findViewById(R.id.button_load);
        textIndicator = view.findViewById(R.id.text_step_indicator);

        currentState = STATE_UNLOADED;
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Load/Show interstitial ads by current state
                 */
                if (currentState == STATE_UNLOADED) {
                    Appier.log("[Sample App]", "====== load Appier Interstitial ======");

                    createInterstitial(context);
                    loadInterstitial();

                    currentState = getNextLoadingState(currentState);
                    updateLayoutByState(currentState);

                } else if (currentState == STATE_LOADED) {
                    Appier.log("[Sample App]", "====== show Appier Interstitial ======");

                    showInterstitial();
                }

            }
        });

        return view;
    }

    protected void updateLayoutByState(int state) {
        if (state == STATE_UNLOADED) {
            progressLoading.setVisibility(View.INVISIBLE);
            progressStepsArt.setVisibility(View.INVISIBLE);
            textIndicator.setText("Step 1: Click load and load the ad.");
            textIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
            buttonLoad.setText("LOAD");
            imageStepsArt.setImageResource(R.drawable.illustration_interstitial_step0_art);
            imageSteps.setImageResource(R.drawable.illustration_interstitial_step0);
        } else if (state == STATE_LOADING) {
            progressLoading.setVisibility(View.VISIBLE);
            progressStepsArt.setVisibility(View.VISIBLE);
            textIndicator.setText("Step 1: Click load and load the ad.");
            textIndicator.setTextColor(getResources().getColor(R.color.colorTextFaded));
            buttonLoad.setText("");
            imageStepsArt.setImageResource(R.drawable.illustration_interstitial_step1_art);
            imageSteps.setImageResource(R.drawable.illustration_interstitial_step1);
        } else if (state == STATE_LOADED) {
            progressLoading.setVisibility(View.INVISIBLE);
            progressStepsArt.setVisibility(View.INVISIBLE);
            textIndicator.setText("Step 2: Click show to see the ad.");
            textIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
            buttonLoad.setText("SHOW");
            imageStepsArt.setImageResource(R.drawable.illustration_interstitial_step2_art);
            imageSteps.setImageResource(R.drawable.illustration_interstitial_step2);
        }
    }

    protected int getCurrentState() {
        return currentState;
    }

    protected void setCurrentState(int mCurrentState) {
        this.currentState = mCurrentState;
    }

    protected int getNextLoadingState(int currentState) {
        switch (currentState) {
            case STATE_UNLOADED:
                return STATE_LOADING;
            case STATE_LOADING:
                return STATE_LOADED;
            default:
                return STATE_UNLOADED;
        }
    }

    protected abstract void createInterstitial(Context context);

    protected abstract void loadInterstitial();

    protected abstract void showInterstitial();

}
