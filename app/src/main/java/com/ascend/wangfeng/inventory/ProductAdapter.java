package com.ascend.wangfeng.inventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    public static final String KEY_PRODUCT = "product";
    private int mResource;

    public ProductAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        holder.mSoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(getContext());
                if (dbHelper.sold(getItem(position), 1)) {
                    Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(KEY_PRODUCT, (Serializable) getItem(position));
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView mTitleView;
        TextView mPriceView;
        TextView mSoldCountView;
        TextView mInventoryCountView;
        Button mSoldBtn;

        public ViewHolder(View view) {
            mTitleView = view.findViewById(R.id.title);
            mPriceView = view.findViewById(R.id.price);
            mSoldCountView = view.findViewById(R.id.count_sold);
            mInventoryCountView = view.findViewById(R.id.count_inventory);
            mSoldBtn = view.findViewById(R.id.btn_sold);
        }
    }
}
