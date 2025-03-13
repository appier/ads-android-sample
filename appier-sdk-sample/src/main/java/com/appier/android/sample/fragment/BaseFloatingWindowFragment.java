package com.appier.android.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.android.sample.R;
import com.appier.android.sample.common.FloatViewManager;


public abstract class BaseFloatingWindowFragment extends BaseFragment {

    private Button mLoadButton;
    private FrameLayout mOverlayFrame;
    private LinearLayout mAdContainer;

    protected FloatViewManager mFloatViewManager;

    public BaseFloatingWindowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
        mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
            @Override
            public void onOpen(LinearLayout contentContainer) {
                mAdContainer = contentContainer;
                View view = getView();
                if (view != null) {
                    mLoadButton.setVisibility(View.GONE);
                    mOverlayFrame.setVisibility(View.VISIBLE);
                }

                /*
                 * Load Ad in ad container layout
                 */
                loadAdInContainer(mAdContainer);
            }

            @Override
            public void onDrawOverlayPermissionResult(boolean isPermissionGranted) {
                if (isPermissionGranted) {
                    mFloatViewManager.open();
                }
            }

            @Override
            public void onClose(LinearLayout contentContainer) {
                destroyAdView();
                mAdContainer.removeAllViews();
                View view = getView();
                if (view != null) {
                    mLoadButton.setVisibility(View.VISIBLE);
                    mOverlayFrame.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_floating_window, container, false);

        mLoadButton = view.findViewById(R.id.button_load);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatViewManager.openWithPermissionCheck();
            }
        });

        mOverlayFrame = view.findViewById(R.id.overlay_frame);
        mOverlayFrame.setVisibility(View.GONE);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mDemoFlowController.createView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAdView();
    }

    @Override
    protected void onViewVisible(View view) {}

    public FloatViewManager getFloatViewManager() {
        return mFloatViewManager;
    }

    protected abstract void loadAdInContainer(LinearLayout adContainer);

    protected abstract void destroyAdView();
}
