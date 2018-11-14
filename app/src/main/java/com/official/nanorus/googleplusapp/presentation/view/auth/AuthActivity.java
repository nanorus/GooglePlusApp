package com.official.nanorus.googleplusapp.presentation.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.presentation.presenter.AuthPresenter;
import com.official.nanorus.googleplusapp.presentation.ui.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements IAuthView {
    private String TAG = this.getClass().getName();

    public static final int RC_SIGN_IN = 1;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;

    private AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        setGoogleSignInButtonTitle(getString(R.string.sign_in_google));
        presenter = new AuthPresenter();
        presenter.bindView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewStart();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.onSigningIn(account);
            } catch (ApiException e) {
                Toaster.shortToast(e.getMessage());
            }
        }
    }

    private void setGoogleSignInButtonTitle(String title) {
        TextView signInButtonTextView = (TextView) signInButton.getChildAt(0);
        signInButtonTextView.setText(title);
        signInButtonTextView.setTextSize(18);
        signInButtonTextView.setTypeface(Typeface.DEFAULT);
        signInButtonTextView.setTextColor(Color.BLACK);
        signInButton.setOnClickListener(view -> presenter.onSignInClicked());
    }

    @Override
    public Activity getView() {
        return this;
    }

}
