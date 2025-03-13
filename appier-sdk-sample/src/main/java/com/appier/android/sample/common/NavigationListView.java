/*
 * Put ListView with android:layout_height="wrap_content" inside a ScrollView
 * will make the ListView get height of only one row.
 *
 * To fix this issue, we use custom extended ListView.
 *
 * Reference: https://stackoverflow.com/a/24186596
 */
package com.appier.android.sample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NavigationListView extends ListView {
    public NavigationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationListView(Context context) {
        super(context);
    }

    public NavigationListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
