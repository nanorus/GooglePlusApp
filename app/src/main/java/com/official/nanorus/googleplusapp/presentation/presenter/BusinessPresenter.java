package com.official.nanorus.googleplusapp.presentation.presenter;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.entity.auth.Account;
import com.official.nanorus.googleplusapp.model.data.GoogleAuth;
import com.official.nanorus.googleplusapp.model.data.Utils;
import com.official.nanorus.googleplusapp.model.domain.BusinessInteractor;
import com.official.nanorus.googleplusapp.navigation.Router;
import com.official.nanorus.googleplusapp.presentation.ui.Toaster;
import com.official.nanorus.googleplusapp.presentation.view.business.IBusinessView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BusinessPresenter {
    private String TAG = this.getClass().getName();

    private IBusinessView view;
    private Router router;
    private BusinessInteractor interactor;
    private Disposable businessmenDisposable;
    private GoogleAuth googleAuth;

    public BusinessPresenter() {
        googleAuth = GoogleAuth.getInstance();
        router = Router.getInstance();
        interactor = new BusinessInteractor();
    }

    public void bindView(IBusinessView view) {
        this.view = view;
        Account account = googleAuth.getCurrentUser();
        view.setupNavigationDrawer(account);
    }

    public void onViewStart() {
        onAuthChecked(googleAuth.isSignedIn());
    }

    public void onAuthChecked(boolean signedIn) {
        if (signedIn) {
            startPresenter();
        } else
            router.startAuthActivityWithFinish(view.getView());
    }


    public void onRefresh() {
        disposeBusinessmenDisposable();
        view.showProgress(true);
        businessmenDisposable = interactor.getRefreshedBusinessmen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(businessmenList -> {
                            view.showProgress(false);
                            view.clearBusinessmenList();
                            view.updateBusinessmenList(businessmenList);
                        },
                        throwable -> {
                            Log.e(TAG, throwable.getMessage());
                            view.showProgress(false);
                            if (Utils.checkNetWorkError(throwable))
                                Toaster.shortToast(R.string.networkError);
                            else
                                Toaster.shortToast(throwable.getMessage());
                        },

                        () -> {
                            Log.d(TAG, "Api loading completed");
                            view.showProgress(false);
                        });
    }

    private void loadBusinessmen() {
        disposeBusinessmenDisposable();
        view.showProgress(true);
        businessmenDisposable = interactor.getBusinessmen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(businessmen -> {
                            view.showProgress(false);
                            if (businessmen != null && !businessmen.isEmpty()) {
                                view.clearBusinessmenList();
                                view.updateBusinessmenList(businessmen);
                            } else
                                onRefresh();
                        },
                        throwable -> {
                            Log.e(TAG, throwable.getMessage());
                            view.showProgress(false);
                            onRefresh();
                        },
                        () -> {
                            view.showProgress(false);
                            Log.e(TAG, "DB loading completed");
                        });
    }

    private void startPresenter() {
        loadBusinessmen();
    }

    public void onLogOutClicked() {
        FirebaseAuth.getInstance().signOut();
        router.signOut(view.getView());
        router.finishActivity(view.getView());
    }

    public void releasePresenter() {
        view = null;
        disposeBusinessmenDisposable();
        businessmenDisposable = null;
        interactor = null;
        router = null;
    }

    private void disposeBusinessmenDisposable() {
        if (businessmenDisposable != null && !businessmenDisposable.isDisposed())
            businessmenDisposable.dispose();
    }
}
