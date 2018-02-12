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
import java.util.HashMap;
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
    private DatabaseReference productListRef = null;
    private DatabaseReference productTypeListRef = null;
    private HashMap<String, ArrayList<String>> productTypesListItems = new HashMap<>();
    private Utils utils = new Utils();
    private IPriceSaveComleteListener saveComleteListener = new IPriceSaveComleteListener() {
        @Override
        public void onAddToDB(final PriceListModel item) {
            addPriceToDB(item);
        }
    };

    @BindView(R.id.prod_list) public RecyclerView productList;
    @OnClick(R.id.btnAddFloat)
    public void OnAddProduct(View view) {
        PriceAddDialog addDialog = new PriceAddDialog(getActivity());
        addDialog.setProductTypes(productTypesListItems);
        addDialog.setPriceSaveComleteListener(saveComleteListener);
        addDialog.show();
    }

    private void addPriceToDB(final PriceListModel productItem) {
        dbRef.child("productList").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long nextNumber = mutableData.getChildrenCount();
                try {
                    long maxVal = 0;
                    for (MutableData childrenItem : mutableData.getChildren()) {
                        try {
                            long item = Long.parseLong(childrenItem.getKey());
                            maxVal = maxVal >= item ? maxVal : item;
                        } catch (Exception ex) {
                            utils.getException(ex);
                        }
                    }
                    nextNumber = nextNumber >= maxVal ? nextNumber : maxVal;
                    nextNumber++;
                } catch (Exception ex) {
                    utils.getException(ex);
                }
                dbRef.child("productList/" + nextNumber).setValue(productItem);
                dbRef.child("productList/" + nextNumber).push();

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    (new Utils(getActivity().getApplicationContext())).showMessage(databaseError.getMessage(), true);
                } else {
                    (new Utils(getActivity().getApplicationContext())).showMessage("Добавлен 1 товар", false);
                    //utils.showMessage("Добавлен 1 товар", false);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mapprice_list_fragment, container, false);
        ButterKnife.bind(this, view);

        productList.setLayoutManager(new LinearLayoutManager(getActivity()));

        signInAnonymous(dbAuth);
        dbRef = db.getReference();
        if (dbRef != null) {
            productListRef = dbRef.child("productList");
            productTypeListRef = dbRef.child("productTypes");
        }
        getPriceList();
        getProductTypesList();

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

    private void getPriceListCurrentMonth(DatabaseReference mDBRef) {
        if (mDBRef != null) {
            final Query query = productListRef
                    .startAt("2134")
                    .endAt("2134");
            //mDBRef.child("productList")
        }
    }

    private void getPriceList() {
        if (productListRef != null) {
            productListRef.addValueEventListener(new ValueEventListener() {
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
                    //
                }
            });
        }
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


    private void getProductTypesList() {
        if (productTypeListRef != null) {
            productTypeListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        productTypesListItems = (HashMap<String, ArrayList<String>>)dataSnapshot.getValue();
                    } catch (Exception ex) {
                        utils.getException(ex);
                    }
                    if (productTypesListItems == null)
                        productTypesListItems = new HashMap<>();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
        }
    }
}
