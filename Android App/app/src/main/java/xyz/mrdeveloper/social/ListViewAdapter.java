package xyz.mrdeveloper.social;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 17-02-2018.
 */

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Product> products;

    public ListViewAdapter(ArrayList<Product> contents, Context context) {
        this.mContext = context;
        this.products = contents;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.list_item_card_small, null);
        }

        TextView text_product_name = v.findViewById(R.id.text_product_name);
        TextView text_product_price = v.findViewById(R.id.text_product_price);
        text_product_name.setText(products.get(position).name);
        text_product_price.setText(products.get(position).salePrice);

        return v;
    }
}