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

public class productAdapeter extends ArrayAdapter<product>{

    private int resourceId;

    public productAdapeter(Context context, int textViewResourceId,
                           List<product> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        product myproduct = getItem(position); // 获取当前项的product实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.product_name = (TextView) view.findViewById (R.id.Name);
            viewHolder.product_price = (TextView) view.findViewById (R.id.Price);
            viewHolder.product_detail = (TextView) view.findViewById (R.id.Detail);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.product_name.setText(myproduct.getProduct_name());
        viewHolder.product_price.setText(myproduct.getProduct_price());
        viewHolder.product_detail.setText(myproduct.getProduct_detail());
        return view;
    }

    class ViewHolder {
        TextView product_name;
        TextView product_price;
        TextView product_detail;
    }
}
