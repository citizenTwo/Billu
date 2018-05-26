package xyz.mrdeveloper.social;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Product> products;
    Context mContext;

    static final int TYPE_CARDS = 0;
    static final int TYPE_LIST = 1;

    boolean BROWSING = true;

    public RecyclerViewAdapter(ArrayList<Product> contents, Context context) {
        this.products = contents;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (BROWSING) {
            return TYPE_CARDS;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_CARDS: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);

                return new MyViewHolder(view) {
                };
            }
            case TYPE_LIST: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new MyViewHolder(view) {
                };
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = products.get(position);
        holder.currentProduct = product;

//        Log.i("debug", "URL " + product.imageUrl);

        Glide.with(mContext)
                .load(product.imageUrl)
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.social_logo)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.image_product);

        holder.brand.setText(product.brand);
        holder.name.setText(product.name);
        holder.price.setText(product.salePrice);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView brand, name, price;
        public ImageView image_product;
        public View view;
        public Product currentProduct;

        public MyViewHolder(View view) {
            super(view);
            image_product = (ImageView) view.findViewById(R.id.image_product);
            brand = (TextView) view.findViewById(R.id.text_product_brand);
            name = (TextView) view.findViewById(R.id.text_product_name);
            price = (TextView) view.findViewById(R.id.text_product_price);

            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("debug", "CLICKED!!!!");

                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("IMAGE_URL", currentProduct.imageUrl);
                    intent.putExtra("NAME", currentProduct.name);
                    intent.putExtra("BRAND", currentProduct.brand);
                    intent.putExtra("PRICE", currentProduct.salePrice);
                    intent.putExtra("DETAILS", currentProduct.details);
                    intent.putExtra("PID", currentProduct.pid);

                    mContext.startActivity(intent);
                }
            });
        }
    }
}