package ru.komcity.android.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth {
    private FirebaseAuth uAuth = null;
    private FirebaseAuth.AuthStateListener uAuthListener = null;
    private FirebaseUser uCurUser = null;
    private GoogleSignInOptions gOptions = null;
    private GoogleSignInClient signInClient = null;
    private Utils utils = null;
    private final int GOOGLE_CODE_REQUEST = 900001;
    private Context context;

    public UserAuth(Context mContext) {
        this.context = mContext;
        utils = new Utils();
        initObjects();
    }

    private void initObjects() {
        uAuth = FirebaseAuth.getInstance();
        uCurUser = uAuth.getCurrentUser();
        uAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        uAuth.addAuthStateListener(uAuthListener);
        initGoogleOptions();
        initGoogleClient();
    }

    /**
     * Configure Google Sign In
     */
    private void initGoogleOptions() {
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    private void initGoogleClient() {
        signInClient = GoogleSignIn.getClient(context, gOptions);
    }

    public Intent getGoogleSignInIntent() {
        return signInClient.getSignInIntent();
    }

    public GoogleSignInResult getGoogleSignInResult(Intent intentData) {
        return Auth.GoogleSignInApi.getSignInResultFromIntent(intentData);
    }

    public int getGoogleReguestCode() {
        return GOOGLE_CODE_REQUEST;
    }

    public FirebaseAuth getFirebaseAuth() {
        return uAuth;
    }

    public void signInEmail(String email, String password) {
        /*
        // [START sign_in_with_email]
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful()) {
                        mStatusTextView.setText(R.string.auth_failed);
                    }
                    hideProgressDialog();
                    // [END_EXCLUDE]
                }
            });
    // [END sign_in_with_email]
         */
    }

    private void createUserWitEmail() {
        /*
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                }
            });
         */
    }

    private void signInAnonymous() {
        if (uAuth != null) {
            try {
                uAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String x = uCurUser.getDisplayName();
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
}
