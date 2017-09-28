package com.ascend.wangfeng.inventory;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private int mResource;

    public ProductAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleView.setText(getItem(position).getTitle());
        holder.mPriceView.setText(Convert.convertMoney(getItem(position).getPrice()));
        holder.mSoldCountView.setText(getContext().getString(R.string.solded) + getItem(position).getSoldCount());
        holder.mInventoryCountView.setText(getContext().getString(R.string.inventory) + getItem(position).getInventoryCount());
        return convertView;
    }

    class ViewHolder {
        TextView mTitleView;
        TextView mPriceView;
        TextView mSoldCountView;
        TextView mInventoryCountView;

        public ViewHolder(View view) {
            mTitleView = view.findViewById(R.id.title);
            mPriceView = view.findViewById(R.id.price);
            mSoldCountView = view.findViewById(R.id.count_sold);
            mInventoryCountView = view.findViewById(R.id.count_inventory);
        }
    }
}
