package ru.komcity.android.pricemap;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
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
import ru.komcity.android.base.IUserAuth;
import ru.komcity.android.base.UserAuth;
import ru.komcity.android.base.Utils;

public class MapPriceListFragment extends Fragment implements IUserAuth {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = null;
    private DatabaseReference productListRef = null;
    private DatabaseReference productTypeListRef = null;
    private HashMap<String, ArrayList<String>> productTypesListItems = new HashMap<>();
    private Utils utils = new Utils();
    private UserAuth userAuth = null;
    private final String PROD_LIST = "productList";
    private IPriceSaveCompleteListener saveCompleteListener = new IPriceSaveCompleteListener() {
        @Override
        public void onAddToDB(final PriceListModel item) {
            addPriceToDB(item);
        }
    };

    @BindView(R.id.prod_list) public RecyclerView productList;
    @OnClick(R.id.btnAddFloat)
    public void onAddProduct_Click(View view) {
        // Авторизация при добавлении
        userAuth = new UserAuth(getActivity());
        userAuth.setAuthListener(this);
        Intent uSignIntent = userAuth.getGoogleSignInIntent();
        startActivityForResult(uSignIntent, userAuth.getGoogleReguestCode());
    }

    /**
     * Фактически добавляет объект типа PriceListModel в Базу
     * @param productItem Объект для добавления
     */
    private void addPriceToDB(final PriceListModel productItem) {
        dbRef.child(PROD_LIST).runTransaction(new Transaction.Handler() {
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
                dbRef.child(PROD_LIST + "/" + nextNumber).setValue(productItem);
                dbRef.child(PROD_LIST + "/" + nextNumber).push();

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    (new Utils(getActivity().getApplicationContext())).showMessage(databaseError.getMessage(), true);
                } else {
                    (new Utils(getActivity().getApplicationContext())).showMessage("Добавлен 1 товар", false);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mapprice_list_fragment, container, false);
        ButterKnife.bind(this, view);

        productList.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbRef = db.getReference();
        if (dbRef != null) {
            productListRef = dbRef.child(PROD_LIST);
            productTypeListRef = dbRef.child("productTypes");
        }
        getPriceList();
        getProductTypesList();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == userAuth.getGoogleReguestCode()) {
            GoogleSignInResult result = userAuth.getGoogleSignInResult(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                userAuth.signInEmail("developkms@gmail.com", "483866007");
                // Google Sign In failed
                //FirebaseAccount.signInAccount(this, etEmail.getText().toString(), etPassword.getText().toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        this.isSuccess(true);
        /*
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        userAuth.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //
                        } else {
                            //startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            //finish();
                        }
                    }
                });
        */
    }

    private void getPriceListCurrentMonth(DatabaseReference mDBRef) {
        if (mDBRef != null) {
            final Query query = productListRef
                    .startAt("2134")
                    .endAt("2134");
            //mDBRef.child("productList")
        }
    }

    /**
     * Получает список всех цен и продуктов
     */
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

    /**
     * Отображает список на экране
     * @param items список для отображения
     */
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

    /**
     * Получает список всех Типов продуктов
     */
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

    @Override
    public void isSuccess(boolean authResult) {
        if (authResult) {
            PriceAddDialog addDialog = new PriceAddDialog(getActivity());
            addDialog.setProductTypes(productTypesListItems);
            addDialog.setUserInfo("user Ivanov I.D.");
            addDialog.setPriceSaveComleteListener(saveCompleteListener);
            addDialog.show();
        } else {
            (new Utils(getActivity().getApplicationContext())).showMessage("Не удалось пройти авторизацию. Добавление невозможно", true);
        }
    }
}
