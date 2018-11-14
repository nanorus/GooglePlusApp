package com.official.nanorus.googleplusapp.model.data;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.official.nanorus.googleplusapp.entity.auth.Account;
import com.official.nanorus.googleplusapp.presentation.view.auth.AuthActivity;

public class GoogleAuth {
    private String TAG = this.getClass().getName();

    private static GoogleAuth instance;
    private ResourceManager resourceManager;
    private FirebaseAuth auth;

    public static GoogleAuth getInstance() {
        if (instance == null)
            instance = new GoogleAuth();
        return instance;
    }

    public GoogleAuth(){
        resourceManager = ResourceManager.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    public void signIn(Activity activity){

        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resourceManager.getDefaultWebClientId())
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, AuthActivity.RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, OnCompleteListener<AuthResult> onCompleteListener) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(onCompleteListener);
    }

   public boolean isSignedIn(){
        return auth.getCurrentUser() != null;
   }

    public Account getCurrentUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
            return new Account(user.getPhotoUrl().toString(), user.getDisplayName(), user.getEmail());
        else return null;
    }
}
