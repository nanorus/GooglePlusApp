package com.official.nanorus.googleplusapp.presentation.view.auth;

import android.app.Activity;

public interface IAuthView {

    Activity getView();

    void checkAuth();

    void signIn();

    void auth();
}
