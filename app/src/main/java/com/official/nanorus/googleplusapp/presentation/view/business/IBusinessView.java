package com.official.nanorus.googleplusapp.presentation.view.business;

import android.app.Activity;

import com.official.nanorus.googleplusapp.entity.auth.Account;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;

import java.util.List;

public interface IBusinessView {
    Activity getView();

    void clearBusinessmenList();

    void updateBusinessmenList(List<Businessman> businessmen);

    void updateBusinessmenList(Businessman businessman);

    void showProgress(boolean b);

    void setupNavigationDrawer(Account account);
}
