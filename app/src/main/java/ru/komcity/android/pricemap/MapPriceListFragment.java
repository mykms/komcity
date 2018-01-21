package ru.komcity.android.pricemap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class MapPriceListFragment extends Fragment {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser dbUser = null;
    private DatabaseReference dbRef = null;
    private FirebaseRecyclerAdapter<Message, PriceViewHolder> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mapprice_list_fragment, container, false);
        ButterKnife.bind(this, view);

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
        mDBAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
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
    }

    private void getPriceList(DatabaseReference mDBRef) {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(mDBRef.child("messages"), Message.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Message, PriceViewHolder>(options) {
            @Override
            public PriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new PriceViewHolder(inflater.inflate(R.layout.price_list_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(PriceViewHolder holder, int position, Message model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
            }
        };

        mDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message msg = dataSnapshot.getValue(Message.class);
                int x = 0;
                x++;
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
}
