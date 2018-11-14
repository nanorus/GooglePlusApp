package com.official.nanorus.googleplusapp.presentation.presenter;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.model.data.GoogleAuth;
import com.official.nanorus.googleplusapp.navigation.Router;
import com.official.nanorus.googleplusapp.presentation.ui.Toaster;
import com.official.nanorus.googleplusapp.presentation.view.auth.IAuthView;

public class AuthPresenter {
    private String TAG = this.getClass().getName();

    private IAuthView view;
    private Router router;
    private GoogleAuth googleAuth;

    public AuthPresenter() {
        router = Router.getInstance();
        googleAuth = GoogleAuth.getInstance();
    }

    public void bindView(IAuthView view) {
        this.view = view;
    }

    public void onViewStart() {
        checkAuth();
    }

    public void onSignInClicked() {
        googleAuth.signIn(view.getView());
    }

    private void checkAuth() {
        onAuthChecked(googleAuth.isSignedIn());
    }

    private void onAuthChecked(boolean signedIn) {
        if (signedIn) {
            router.startMainActivityWithFinish(view.getView());
        }
    }

    public void onSigningIn(GoogleSignInAccount account) {
        googleAuth.firebaseAuthWithGoogle(account, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                checkAuth();
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                Toaster.shortToast(R.string.auth_failed);
            }
        });
    }
}
