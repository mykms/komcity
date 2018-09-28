package ru.komcity.mobile.base;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.komcity.mobile.pricemap.IProductDBListener;
import ru.komcity.mobile.pricemap.PriceListModel;

public class ProductBase {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Utils utils = new Utils();
    private DatabaseReference dbRef = null;
    private DatabaseReference productListRef = null;
    private DatabaseReference productTypeListRef = null;
    private IProductDBListener productDBListener = null;
    private HashMap<String, ArrayList<String>> productTypesListItems = new HashMap<>();
    private final static String PROD_LIST = "productList";
    private final static String PROD_TYPE = "productTypes";

    public ProductBase() {
        initDB();
    }

    public ProductBase(IProductDBListener productDBListener) {
        this.productDBListener = productDBListener;
        initDB();
    }

    private void initDB() {
        this.dbRef = db.getReference();
        if (dbRef != null) {
            productListRef = dbRef.child(PROD_LIST);
            productTypeListRef = dbRef.child(PROD_TYPE);
        }
    }

    public void setProductDBListener(IProductDBListener productDBListener) {
        this.productDBListener = productDBListener;
    }

    /**
     * Получает список всех цен и продуктов
     */
    public void getPriceList() {
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
                    if (productDBListener != null) {
                        productDBListener.onProductLoadComplete(items);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
        }
    }

    /**
     * Получает список всех Типов продуктов
     */
    public void getProductTypesList() {
        if (productTypeListRef != null) {
            productTypeListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        productTypesListItems = (HashMap<String, ArrayList<String>>)dataSnapshot.getValue();
                    } catch (Exception ex) {
                        utils.getException(ex);
                    }
                    if (productDBListener != null) {
                        productDBListener.onProductTypesLoadComplete(productTypesListItems);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
        }
    }

    /**
     * Фактически добавляет объект типа PriceListModel в Базу
     * @param productItem Объект для добавления
     */
    public void addPriceToDB(final PriceListModel productItem) {
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
                if (productDBListener != null) {
                    if (databaseError != null) {
                        productDBListener.onSaveProductResult(false);
                    } else {
                        productDBListener.onSaveProductResult(true);
                    }
                }
            }
        });
    }
}
