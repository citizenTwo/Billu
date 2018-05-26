package xyz.mrdeveloper.social;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;

public class CheckoutActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView mListView;

    ListViewAdapter listAdapter;
    ArrayList<Product> items;



    Button buttonCancel;
    Button buttonCheckout;
    TextView textTotalAmount;

    float totalAmount = 0;

    //    int i = 0;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference db_bill = FirebaseFirestore.getInstance().document("bill");
    DocumentReference db_checkout = FirebaseFirestore.getInstance().document("process/checkout");

//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireSTORE Method
//        //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();

        items = new ArrayList<>();
        textTotalAmount = findViewById(R.id.text_total_amount);

        listAdapter = new ListViewAdapter(items, this);

        mListView = findViewById(R.id.listView);
        mListView.setAdapter(listAdapter);

        db_checkout.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    String step = documentSnapshot.getString("step");
                    if (step.equals("2")) {
                        FillData(documentSnapshot.getString("count"));
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

//        //////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        buttonCheckout = findViewById(R.id.button_checkout);
        buttonCancel = findViewById(R.id.button_cancel);

        buttonCancel.setVisibility(View.GONE);
        buttonCheckout.setVisibility(View.GONE);

//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireBASE Method
//        //////////////////////////////////////////////////////////////////////////////////////////

//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference = databaseReference.child("customer_bill");
//
//        valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    //Getting the data from snapshot
//                    Product tempProduct = new Product();
//                    tempProduct.name = (String) childSnapshot.child("product_name").getValue();
//                    tempProduct.salePrice = (String) childSnapshot.child("product_price").getValue();
//                    tempProduct.pid = (String) childSnapshot.child("product_id").getValue();
//
//                    int i;
//                    for (i = 0; i < items.size(); i++) {
//                        if (Objects.equals(tempProduct.pid, items.get(i).pid)) {
//                            break;
//                        }
//                    }
//                    if (i >= items.size()) {
//                        totalAmount += Float.parseFloat(tempProduct.salePrice);
//                        textTotalAmount.setText(getString(R.string.rupee_symbol) + totalAmount);
//
//                        tempProduct.salePrice = getString(R.string.rupee_symbol) + tempProduct.salePrice;
//                        items.add(tempProduct);
//
//                        buttonCancel.setVisibility(View.VISIBLE);
//                        buttonCheckout.setVisibility(View.VISIBLE);
//
//                        Log.i("debug", "Product:" + tempProduct.name);
//                    }
//
//                    listAdapter.notifyDataSetChanged();
//
//                    childSnapshot.getRef().removeValue();
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
    }


//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireSTORE Method
//        //////////////////////////////////////////////////////////////////////////////////////////


    public void FillData(String count) {

        items = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            Product tempProduct = new Product();
            tempProduct.brand = "";
            tempProduct.salePrice = "";
            tempProduct.name = "";
            items.add(tempProduct);
        }

        textTotalAmount = findViewById(R.id.text_total_amount);

        listAdapter = new ListViewAdapter(items, this);

        mListView = findViewById(R.id.listView);
        mListView.setAdapter(listAdapter);

        totalAmount = Float.valueOf(0);
        final int total = Integer.parseInt(count);

        for (int i = 0; i < total; i++) {
            final int value = i;
            db.collection("bill").document("product " + i)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Product tempProduct = new Product();

                            tempProduct.name =documentSnapshot.getString("product_name");
                            tempProduct.salePrice = documentSnapshot.getString("discounted_price");
                            tempProduct.pid = documentSnapshot.getString("pid");

                            totalAmount += Float.parseFloat(tempProduct.salePrice);
                            textTotalAmount.setText(getString(R.string.rupee_symbol) + totalAmount);
//                            textTotalAmount.setText("1000");

                            tempProduct.salePrice = getString(R.string.rupee_symbol) + tempProduct.salePrice;
                            items.add(value, tempProduct);

                            Log.d("Situation","Item Added on Bill" + value + items.size());

                            buttonCancel.setVisibility(View.VISIBLE);
                            buttonCheckout.setVisibility(View.VISIBLE);

                            listAdapter.notifyDataSetChanged();

                            Log.i("FireStore", "Product Added on Bill:" + tempProduct.name);
                        }
                    });
//            textTotalAmount.setText(getString(R.string.rupee_symbol) + totalAmount);
//            textTotalAmount.setText("1000");
        }

    }


//        //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseReference != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    public void CancelCheckout(View view) {
        items.clear();
        listAdapter.notifyDataSetChanged();

        TextView textMessage = findViewById(R.id.text_message);
        textMessage.setText("Your order has been canceled\nMove into the checkout zone");

//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireBASE Method
//        //////////////////////////////////////////////////////////////////////////////////////////

//        FirebaseDatabase.getInstance().getReference().child("checkout_step").setValue("4");
//        FirebaseDatabase.getInstance().getReference().child("payment_status").setValue("Cancelled");

//        //////////////////////////////////////////////////////////////////////////////////////////


//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireSTORE Method
//        //////////////////////////////////////////////////////////////////////////////////////////

        HashMap<String, String> status = new HashMap<>();
        status.put("step","4");
        status.put("payment","Canceled");

        DocumentReference database = FirebaseFirestore.getInstance().document("process/checkout");

        database.set(status,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d("FireStore","Checkout Step 4 SUCCESSFUL");
                else
                    Log.d("FireStore","Checkout Step 4 FAILED " + task.getException());
            }
        });

        finish();
    }

    public void DoCheckout(View view) {
        items.clear();
        listAdapter.notifyDataSetChanged();
        TextView textMessage = findViewById(R.id.text_message);
        textMessage.setText("We took all of you money\nThank you for shopping at Social.");


//        //////////////////////////////////////////////////////////////////////////////////////////

//        FireBASE Method
//        //////////////////////////////////////////////////////////////////////////////////////////
//        FirebaseDatabase.getInstance().getReference().child("checkout_step").setValue("3");
//        FirebaseDatabase.getInstance().getReference().child("payment_status").setValue("Completed");


        HashMap<String, String> status = new HashMap<>();
        status.put("step","3");
        status.put("payment","Completed");

        DocumentReference database = FirebaseFirestore.getInstance().document("process/checkout");
        database.set(status,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d("FireStore","Checkout Step 3 SUCCESSFUL");
                else
                    Log.d("FireStore","Checkout Step 3 FAILED " + task.getException());
            }
        });

        buttonCancel.setVisibility(View.GONE);
        buttonCheckout.setVisibility(View.GONE);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
