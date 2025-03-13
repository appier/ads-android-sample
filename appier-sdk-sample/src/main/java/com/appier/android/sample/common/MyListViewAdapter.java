package com.appier.android.sample.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.appier.android.sample.R;

import java.util.List;

public class MyListViewAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mData;

    public MyListViewAdapter(Context context, List<String> data) {
        super(context, 0);
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.template_demo_list_row, null);
        }
        return convertView;
    }
}
