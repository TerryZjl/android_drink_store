package com.example.asus.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ray on 2019/1/2.
 */

public class StatAdapeter extends ArrayAdapter<StatStruct>{

    private int resourceId;

    public StatAdapeter(Context context, int textViewResourceId,
                           List<StatStruct> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatStruct myproduct = getItem(position); // 获取当前项的product实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Date = (TextView) view.findViewById (R.id.Date);
            viewHolder.product_count = (TextView) view.findViewById (R.id.count);
            viewHolder.product_total = (TextView) view.findViewById (R.id.total);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.Date.setText(myproduct.getProduct_Date());
        viewHolder.product_count.setText(myproduct.getProduct_count());
        viewHolder.product_total.setText(myproduct.getProduct_total());
        return view;
    }

    class ViewHolder {
        TextView Date;

        TextView product_count;

        TextView product_total;

    }
}
