package com.official.nanorus.googleplusapp.presentation.view.businessman_info;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;

public interface IBusinessmanInfoView {
    Activity getView();

    void checkLocationPermissions();

    void setMarker(Businessman businessman);

    void fillInfo(Businessman businessman);

    void requestLocationPermissions();

    void showMyLocationButton(boolean show);
}
