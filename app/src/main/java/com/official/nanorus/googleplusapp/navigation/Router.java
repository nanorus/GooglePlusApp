package com.official.nanorus.googleplusapp.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.official.nanorus.googleplusapp.presentation.view.auth.AuthActivity;
import com.official.nanorus.googleplusapp.presentation.view.business.BusinessActivity;

public class Router {

    public static Router instance;

    public static Router getInstance() {
        if (instance == null)
            instance = new Router();
        return instance;
    }

    public void finishActivity(Activity activity) {
        activity.finish();
    }

    public void startMainActivityWithFinish(Activity activity) {
        activity.startActivity(new Intent(activity, BusinessActivity.class));
        activity.finish();
    }

    public void startAuthActivityWithFinish(Activity activity) {
        activity.startActivity(new Intent(activity, AuthActivity.class));
        activity.finish();
    }

    public void signOut(Context context) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
