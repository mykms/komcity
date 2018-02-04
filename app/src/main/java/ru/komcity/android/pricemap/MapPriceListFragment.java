package ru.komcity.android.pricemap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.android.R;
import ru.komcity.android.base.Utils;

public class MapPriceListFragment extends Fragment {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser dbUser = null;
    private DatabaseReference dbRef = null;
    private Utils utils = new Utils();

    @BindView(R.id.prod_list) public RecyclerView productList;
    @OnClick(R.id.btnAddFloat)
    public void OnAddProduct(View view) {
        ArrayList<Object> geo = new ArrayList<Object>();

        PriceListModel prod1 = new PriceListModel(geo,
                "Москва, ул.Вавилова, д.3",
                "Ашан-24",
                89.99,
                "Ананас",
                "Продовольственные",
                "Фрукты",
                "user2");

        final PriceListModel prod2 = new PriceListModel(geo,
                "Москва, ул.Вавилова, д.3",
                "Ашан-14",
                102.5,
                "Яблоко",
                "Продовольственные",
                "Фрукты",
                "user2");

        dbRef.child("productList/1").setValue(prod1);
        dbRef.child("productList/2").setValue(prod1);
        dbRef.child("productList/3").setValue(prod1);
        dbRef.child("productList/4").setValue(prod1);
        dbRef.child("productList/5").setValue(prod1);
        dbRef.child("productList/6").setValue(prod1);
        dbRef.child("productList/7").setValue(prod1);
        dbRef.child("productList/8").setValue(prod1);
        dbRef.child("productList/9").setValue(prod1);
        dbRef.child("productList/10").setValue(prod1);
        dbRef.child("productList").push();

        dbRef.child("productList/11").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                PriceListModel priceListModel = mutableData.getValue(PriceListModel.class);
                mutableData.setValue(prod2);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

        dbRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                PriceListModel priceListModel = mutableData.getValue(PriceListModel.class);
                mutableData.setValue(priceListModel);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //
            }
        });

        utils.showMessageSnackbar("Добавлено", view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mapprice_list_fragment, container, false);
        ButterKnife.bind(this, view);

        productList.setHasFixedSize(true);    // Не будем динамически изменять размеры
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        productList.setLayoutManager(layoutManager);

        signInAnonymous(dbAuth);
        dbRef = db.getReference();
        getPriceList(dbRef);

        return view;
    }

    private void signInAnonymous(final FirebaseAuth mDBAuth) {
        /*
        writeNewUser(user.getUid(), username, user.getEmail());
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
         */
        if (mDBAuth != null) {
            try {
                mDBAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dbUser = mDBAuth.getCurrentUser();
                        } else {
                            AuthCredential credential = null;//GoogleAuthProvider.getCredential(null, null);
                    /*
                                    mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "linkWithCredential:success");
                                    FirebaseUser user = task.getResult().getUser();
                                    updateUI(user);
                                } else {
                                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                                    Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
                        AnonymousAuthActivity.java
                    */
                            //Странно, но мы не смогли определить текущего пользователя
                            int x = 0;
                            x++;
                        }
                    }
                });
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    private void getPriceList(DatabaseReference mDBRef) {
        mDBRef.child("productList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Object> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PriceListModel item = null;
                    try {
                        item = snapshot.getValue(PriceListModel.class);
                    } catch (Exception ex) {
                        utils.getException(ex);
                    }
                    if (item != null)
                        items.add(item);
                }
                showPriceList(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String err = databaseError.getDetails() + "\n";
                err += databaseError.getMessage();
                int x = 0;
                x++;
            }
        });
    }

    private void showPriceList(List<Object> items) {
        PriceListAdapter adapter = new PriceListAdapter(getActivity(), items);
        adapter.setOnItemClickListener(new IPriceListClickListener() {
            @Override
            public void onItemClick(PriceListModel item) {
                //
            }
        });
        productList.setAdapter(adapter);
    }
}
