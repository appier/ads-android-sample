package com.appier.android.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.appier.ads.AppierError;
import com.appier.android.sample.common.DemoFlowController;

public abstract class BaseFragment extends Fragment {

    private static final String ARG_TITLE = "title";

    private String mTitle;

    protected DemoFlowController mDemoFlowController;

    public BaseFragment() {}

    public String getTitle() {
        return mTitle;
    }

    public void setTitleArgs(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDemoFlowController != null) {
            return mDemoFlowController.createView(inflater, container, savedInstanceState);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDemoFlowController != null && mDemoFlowController.isDemoAvailable()) {
            onViewVisible(getView());
        }
    }

    protected void enableErrorHandling() {
        enableErrorHandling(1);
    }

    protected void enableErrorHandling(final int adCount) {
        if (adCount <= 1) {
            mDemoFlowController = new DemoFlowController(this, getContext()) {
                @Override
                public View createDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                    return BaseFragment.this.onCreateDemoView(inflater, container, savedInstanceState);
                }
            };
        } else {
            mDemoFlowController = new DemoFlowController(this, getContext()) {
                int mTotalCount = 0;
                int mBidCount = 0;

                @Override
                public void notifyAdBid() {
                    mTotalCount += 1;
                    mBidCount += 1;
                    if (mTotalCount == adCount) {
                        if (mBidCount > 0) {
                            super.notifyAdBid();
                        }
                    }
                }

                @Override
                public void notifyAdNoBid() {
                    mTotalCount += 1;
                    if (mTotalCount == adCount) {
                        if (mBidCount > 0) {
                            super.notifyAdBid();
                        } else {
                            super.notifyAdNoBid();
                        }
                    }
                }

                @Override
                public void notifyAdError(AppierError appierError) {
                    mTotalCount = 0;
                    mBidCount = 0;
                    super.notifyAdError(appierError);
                }

                @Override
                public void refresh() {
                    super.refresh();
                    mTotalCount = 0;
                    mBidCount = 0;
                }

                @Override
                public View createDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                    return BaseFragment.this.onCreateDemoView(inflater, container, savedInstanceState);
                }
            };
        }
    }

    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new View(getContext());
    }

    protected abstract void onViewVisible(View view);
}
