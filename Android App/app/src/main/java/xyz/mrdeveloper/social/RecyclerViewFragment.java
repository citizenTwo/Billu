package xyz.mrdeveloper.social;

import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewFragment extends Fragment {

    public boolean GRID_LAYOUT = true;
    public static final int ITEM_COUNT = 10;
    public int CURRENT_TAB = 0;
    public int OFFSET = 0;
    public int SKIP = 2;
    public int filled = 0;
    int i = 0;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    //    DocumentReference db_inventory = FirebaseFirestore.getInstance().document("inventory/item 1");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    private ArrayList<Product> items;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        items = new ArrayList<>();

        for (int i = 0; i < ITEM_COUNT; ++i) {
            Product tempProduct = new Product();
            tempProduct.brand = "Syncing";
            tempProduct.salePrice = "Stuff";
            tempProduct.name = "with cloud";
            items.add(tempProduct);
        }

//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireSTORE Method
//        //////////////////////////////////////////////////////////////////////////////////////////
//        db_inventory.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (documentSnapshot.exists()) {

//                }
//            }
//        });
//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireBASE Method
//        //////////////////////////////////////////////////////////////////////////////////////////
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference = databaseReference.child("inventory_2");
//
//        valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    int itemStartPosition = CURRENT_TAB * ITEM_COUNT * SKIP + OFFSET;
//
//                    //Getting the data from snapshot
//                    Product tempProduct = new Product();
//                    tempProduct.brand = (String) childSnapshot.child("brand").getValue();
//
//                    if (!Objects.equals(tempProduct.brand, "brand")) {
//                        tempProduct.brand = "Pantaloons";
//                    }
//
//                    if (i % SKIP == 0 && i > itemStartPosition + 1) {
//                        tempProduct.imageUrl = (String) childSnapshot.child("image").getValue();
//                        if (tempProduct.imageUrl != null) {
//                            tempProduct.imageUrl = tempProduct.imageUrl.substring(2, tempProduct.imageUrl.length() - 1);
//                        }
//                        tempProduct.name = (String) childSnapshot.child("product_name").getValue();
//                        tempProduct.salePrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("discounted_price").getValue();
//                        tempProduct.realPrice = getString(R.string.rupee_symbol) + (String) childSnapshot.child("retail_price").getValue();
//
//                        tempProduct.details = (String) childSnapshot.child("description").getValue();
//                        tempProduct.pid = (String) childSnapshot.child("pid").getValue();
//
//                        items.remove(filled);
//                        items.add(filled, tempProduct);
//
//                        filled++;
//                        mRecyclerView.getAdapter().notifyDataSetChanged();
//                    }
//                    i++;
//                    if (filled >= ITEM_COUNT)
//                        break;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        };
//        databaseReference.addValueEventListener(valueEventListener);
//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireSTORE Method
//        //////////////////////////////////////////////////////////////////////////////////////////

//        DocumentReference db = FirebaseFirestore.getInstance().document("inventory");

        //setup materialviewpager

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new RecyclerViewAdapter(items, getContext()));

        FillInventory();

//        mRecyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("debug", "CLICKED!!!!");
//                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
//                Product clickedProduct = items.get(mRecyclerView.getChildLayoutPosition(view));
//                intent.putExtra("IMAGE_URL", clickedProduct.imageUrl);
//                intent.putExtra("NAME", clickedProduct.name);
//                intent.putExtra("BRAND", clickedProduct.brand);
//                intent.putExtra("PRICE", clickedProduct.salePrice);
//                intent.putExtra("DETAILS", clickedProduct.details);
//                intent.putExtra("PID", clickedProduct.pid);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseReference != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    public void FillInventory() {

        for (int i = 19; i <= 28; i++) {

            final int value = i;
            db.collection("inventory").document("item " + i)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Product tempProduct = new Product();
                            tempProduct.brand = documentSnapshot.getString("brand");

                            if (!Objects.equals(tempProduct.brand, "brand")) {
                                tempProduct.brand = "Pantaloons";
                            }

                            tempProduct.imageUrl = documentSnapshot.getString("image");
                            if (tempProduct.imageUrl != null) {
                                tempProduct.imageUrl = tempProduct.imageUrl.substring(2, tempProduct.imageUrl.length() - 1);
                            }
                            tempProduct.name = documentSnapshot.getString("product_name");
                            tempProduct.salePrice = getString(R.string.rupee_symbol) + documentSnapshot.getString("discounted_price");
                            tempProduct.realPrice = getString(R.string.rupee_symbol) + documentSnapshot.getString("retail_price");

                            tempProduct.details = documentSnapshot.getString("description");
                            tempProduct.pid = documentSnapshot.getString("pid");

                            items.remove(value - 19);
                            items.add(value - 19, tempProduct);

//                            Log.d("Situation", "" + items.get(value - 19).name + " " + items.get(value - 19).pid + " " + items.size());

                            mRecyclerView.getAdapter().notifyDataSetChanged();

                            Log.d("FireStore", "Size " + items.size());
                        }
                    });
        }
    }


}
