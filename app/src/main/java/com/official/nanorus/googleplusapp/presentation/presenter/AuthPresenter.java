package com.official.nanorus.googleplusapp.presentation.presenter;

import com.official.nanorus.googleplusapp.navigation.Router;
import com.official.nanorus.googleplusapp.presentation.view.auth.IAuthView;

public class AuthPresenter {
    private String TAG = this.getClass().getName();

    IAuthView view;
    Router router;

    public AuthPresenter() {
        router = Router.getInstance();
    }

    public void onSignInClicked() {
        view.signIn();
    }

    public void onViewStart(){
        view.checkAuth();
    }

    public void bindView(IAuthView view) {
        this.view = view;
    }

    public void onAuthChecked(boolean signedIn) {
        if (signedIn) {
            router.startMainActivityWithFinish(view.getView());
        }
    }

    public void onSigningIn() {

    }
}
