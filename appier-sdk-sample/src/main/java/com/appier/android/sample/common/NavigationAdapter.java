package com.appier.android.sample.common;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appier.android.sample.R;

public class NavigationAdapter extends ArrayAdapter<Pair<String, Class<?>>> {
    private Context mContext;

    public NavigationAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.template_secondary_navigation_row, null);
        }
        Pair<String, Class<?>> data = getItem(position);
        TextView txtView = convertView.findViewById(R.id.text_title);
        txtView.setText(data.first);
        return convertView;
    }
}
