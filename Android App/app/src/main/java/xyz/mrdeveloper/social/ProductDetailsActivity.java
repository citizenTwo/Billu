package xyz.mrdeveloper.social;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        final ArrayList<Product> suggestedItems = new ArrayList<>();
//        final String pids[] = new String[4];
//        pids[0] = "TOPEBABFMZ2GSC2G";
//        pids[1] = "SWTEDFVGSHZBRB3Z";
//        pids[2] = "TOPEEERRTBB77BXA";
//        pids[3] = "TKPDS6ZEQBRWSM8U";

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://lit-river-44429.herokuapp.com/?pid=" + getIntent().getStringExtra("PID");
        Log.i("debug", url);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("debug", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        for (int i = 0; i < 4; i++) {
//                            Product tempProduct = new Product();
//                            tempProduct.pid = pids[i];
//                            suggestedItems.add(tempProduct);
//                        }
//                        Log.d("debug", error.getMessage());
                        Log.d("debug", "ERRROOROORR!!!");
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);


        ImageView image_product = findViewById(R.id.image_product);
        TextView text_product_name = findViewById(R.id.text_product_name);
        TextView text_product_details = findViewById(R.id.text_product_details);
        TextView text_product_price = findViewById(R.id.text_product_price);

//        final ImageView suggestImage[] = new ImageView[4];
//        final TextView suggestName[] = new TextView[4];
//        final TextView suggestBrand[] = new TextView[4];
//        final TextView suggestPrice[] = new TextView[4];

        Glide.with(this)
                .load(getIntent().getStringExtra("IMAGE_URL"))
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.social_logo)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(image_product);

        text_product_name.setText(getIntent().getStringExtra("NAME"));
        text_product_price.setText(getIntent().getStringExtra("PRICE"));
        text_product_details.setText(getIntent().getStringExtra("DETAILS"));

//        View itemcard[] = new View[4];
//
//        itemcard[0] = findViewById(R.id.item_card1);
//        itemcard[1] = findViewById(R.id.item_card2);
//        itemcard[2] = findViewById(R.id.item_card3);
//        itemcard[3] = findViewById(R.id.item_card4);

//        for (int i = 0; i < 4; i++) {
//            suggestImage[i] = itemcard[i].findViewById(R.id.image_product);
//            suggestName[i] = itemcard[i].findViewById(R.id.text_product_name);
//            suggestBrand[i] = itemcard[i].findViewById(R.id.text_product_brand);
//            suggestPrice[i] = itemcard[i].findViewById(R.id.text_product_price);
//        }

//        final MyViewHolder myViewHolders[] = new MyViewHolder[4];
//        for (int i = 0; i < 4; i++) {
//            myViewHolders[i] = new MyViewHolder(itemcard[i]);
//        }

//        final String[] pids = new String[4];
//        final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference();
////        final FirebaseFirestore db1 = FirebaseFirestore.getInstance();
//
//        Query query = db1.child("inventory_2").orderByChild("pid").equalTo(getIntent().getStringExtra("PID"));
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                    pids[0] = (String) childSnapshot.child("similar").child("pid1").getValue();
//                    pids[1] = (String) childSnapshot.child("similar").child("pid2").getValue();
//                    pids[2] = (String) childSnapshot.child("similar").child("pid3").getValue();
//                    pids[3] = (String) childSnapshot.child("similar").child("pid4").getValue();
////                    Log.i("debug", "PID1:" + pids[0] + "\nPID2:" + pids[1] + "\nPID3:" + pids[2] + "\nPID4:" + pids[3]);
//
//                    for (int k = 0; k < 4; k++) {
//                        Query query1 = db1.child("inventory_2").orderByChild("pid").equalTo(pids[k]);
//
//                        final int finalK = k;
//                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
////                                Log.i("debug", "SNAP:" + dataSnapshot.getValue());
//                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                    Product tempProduct = new Product();
//                                    tempProduct.brand = (String) childSnapshot.child("brand").getValue();
//                                    if (!Objects.equals(tempProduct.brand, "brand")) {
//                                        tempProduct.brand = "Pantaloons";
//                                    }
//
//                                    tempProduct.imageUrl = (String) childSnapshot.child("image").getValue();
//                                    if (tempProduct.imageUrl != null) {
//                                        tempProduct.imageUrl = tempProduct.imageUrl.substring(2, tempProduct.imageUrl.length() - 1);
//                                    }
//                                    tempProduct.name = (String) childSnapshot.child("product_name").getValue();
//                                    tempProduct.salePrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("discounted_price").getValue();
////                                    tempProduct.realPrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("retail_price").getValue();
//                                    tempProduct.details = (String) childSnapshot.child("description").getValue();
//
//                                    tempProduct.pid = (String) childSnapshot.child("pid").getValue();
//
//                                    Log.i("debug", "BRAND:" + tempProduct.salePrice);
//
//                                    Glide.with(getApplicationContext())
//                                            .load(tempProduct.imageUrl)
//                                            .thumbnail(0.5f)
//                                            .apply(new RequestOptions()
//                                                    .placeholder(R.drawable.social_logo)
//                                                    .centerCrop()
//                                                    .skipMemoryCache(true)
//                                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
//                                            .into(myViewHolders[finalK].image_product);
//
//                                    myViewHolders[finalK].brand.setText(tempProduct.brand);
//                                    myViewHolders[finalK].name.setText(tempProduct.name);
//                                    myViewHolders[finalK].price.setText(tempProduct.salePrice);
//                                    myViewHolders[finalK].currentProduct = tempProduct;
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        db1.child("trends_v").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//
//                    //Getting the data from snapshot
//                    Product tempProduct = new Product();
//                    tempProduct.brand = (String) childSnapshot.child("brand").getValue();
//                    tempProduct.imageUrl = (String) childSnapshot.child("image").getValue();
//                    if (tempProduct.imageUrl != null) {
//                        tempProduct.imageUrl = tempProduct.imageUrl.substring(2, tempProduct.imageUrl.length() - 1);
//                    }
//                    tempProduct.name = (String) childSnapshot.child("name").getValue();
//                    tempProduct.salePrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("price").getValue();
////                      tempProduct.realPrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("retail_price").getValue();
////                      tempProduct.details = (String) childSnapshot.child("description").getValue();
//
//                    tempProduct.pid = (String) childSnapshot.child("pid").getValue();
//
////                        suggestedItems.add(tempProduct);
//
//                    int i;
//                    for (i = 0; i < suggestedItems.size(); i++) {
//                        if (Objects.equals(tempProduct.pid, suggestedItems.get(i).pid)) {
//                            break;
//                        }
//                    }
//                    if (i >= suggestedItems.size() && i < 4 && suggestedItems.size() < 4) {
//                        suggestedItems.add(tempProduct);
//
//                        Glide.with(getApplicationContext())
//                                .load(tempProduct.imageUrl)
//                                .thumbnail(0.5f)
//                                .apply(new RequestOptions()
//                                        .placeholder(R.drawable.social_logo)
//                                        .centerCrop()
//                                        .skipMemoryCache(true)
//                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
//                                .into(myViewHolders[suggestedItems.size() - 1].image_product);
//
//                        myViewHolders[suggestedItems.size() - 1].brand.setText(tempProduct.brand);
//                        myViewHolders[suggestedItems.size() - 1].name.setText(tempProduct.name);
//                        myViewHolders[suggestedItems.size() - 1].price.setText(tempProduct.salePrice);
//                        myViewHolders[suggestedItems.size() - 1].currentProduct = tempProduct;
//
////                        Log.i("debug", "SUGGESTED IDs:" + tempProduct.pid + "  SIZE:" + suggestedItems.size());
//                    }
////                    childSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });
    }


//    public class MyViewHolder {
//        TextView brand, name, price;
//        ImageView image_product;
//        public View view;
//        public Product currentProduct;
//
//        MyViewHolder(View view) {
//            image_product = (ImageView) view.findViewById(R.id.image_product);
//            brand = (TextView) view.findViewById(R.id.text_product_brand);
//            name = (TextView) view.findViewById(R.id.text_product_name);
//            price = (TextView) view.findViewById(R.id.text_product_price);
//            currentProduct = new Product();
//
//            this.view = view;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.i("debug", "CLICKED!!!!");
//                    Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
//                    intent.putExtra("IMAGE_URL", currentProduct.imageUrl);
//                    intent.putExtra("NAME", currentProduct.name);
//                    intent.putExtra("BRAND", currentProduct.brand);
//                    intent.putExtra("PRICE", currentProduct.salePrice);
//                    intent.putExtra("DETAILS", currentProduct.details);
//                    intent.putExtra("PID", currentProduct.pid);
//                    startActivity(intent);
//                }
//            });
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//    }
}
