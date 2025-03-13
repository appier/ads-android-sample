package com.appier.android.sample.common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public MyRecyclerViewItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }
}
