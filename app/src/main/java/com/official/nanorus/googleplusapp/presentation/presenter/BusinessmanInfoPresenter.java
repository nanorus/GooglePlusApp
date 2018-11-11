package com.official.nanorus.googleplusapp.presentation.presenter;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.entity.business.api.Geo;
import com.official.nanorus.googleplusapp.model.domain.BusinessInteractor;
import com.official.nanorus.googleplusapp.model.repository.business.BusinessRepository;
import com.official.nanorus.googleplusapp.navigation.Router;
import com.official.nanorus.googleplusapp.presentation.view.business.IBusinessView;
import com.official.nanorus.googleplusapp.presentation.view.businessman_info.IBusinessmanInfoView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class BusinessmanInfoPresenter {

    private IBusinessmanInfoView view;
    private BusinessInteractor interactor;
    private Router router;
    private Disposable businessmanDisposable;
    private Businessman businessman;
    private boolean isMapReady = false;

    public BusinessmanInfoPresenter() {
        interactor = new BusinessInteractor();
        router = Router.getInstance();
    }

    public void bindView(IBusinessmanInfoView view) {
        this.view = view;
    }

    public void onMapReady() {
        this.isMapReady = true;
        if (this.businessman != null) {
            view.setMarker(this.businessman);
            view.checkLocationPermissions();
        }
    }

    public void onLocationPermissionsChecked(boolean allowed) {
        if (!allowed)
            view.requestLocationPermissions();
        view.showMyLocationButton(allowed);
    }

    public void start(int businessmanId) {
        if (businessmanId==-1){

        }
        businessmanDisposable = interactor.getBusinessman(businessmanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(businessman -> {
                            this.businessman = businessman;
                            view.fillInfo(businessman);
                            if (isMapReady)
                                onMapReady();
                        },
                        throwable -> Log.d(TAG, throwable.getMessage()));
    }

    public void releasePresenter() {
        if (businessmanDisposable != null && !businessmanDisposable.isDisposed())
            businessmanDisposable.dispose();
        businessmanDisposable = null;
        view = null;
        interactor = null;
        businessman = null;
    }

    public void onLocationPermissionsRequestResult(boolean granted) {
        if (granted)
            onLocationPermissionsChecked(true);
    }

    public void onBackPressed() {
        router.finishActivity(view.getView());
    }
}
